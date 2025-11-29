package konkuk.chacall.domain.chat.application.room;

import konkuk.chacall.domain.chat.domain.ChatRoom;
import konkuk.chacall.domain.chat.domain.ChatRoomMetaData;
import konkuk.chacall.domain.chat.domain.repository.ChatRoomMetaDataRepository;
import konkuk.chacall.domain.chat.domain.repository.ChatRoomRepository;
import konkuk.chacall.domain.chat.domain.repository.dto.ChatRoomMetaDataProjection;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomMetaDataResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomIdResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomResponse;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.reservation.domain.model.Reservation;
import konkuk.chacall.domain.reservation.domain.repository.ReservationRepository;
import konkuk.chacall.domain.reservation.domain.repository.dto.ReservationConfirmedProjection;
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static konkuk.chacall.global.common.exception.code.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final FoodTruckRepository foodTruckRepository;
    private final ChatRoomMetaDataRepository chatRoomMetaDataRepository;
    private final ReservationRepository reservationRepository;

    public ChatRoomIdResponse createChatRoom(User member, Long foodTruckId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(FOOD_TRUCK_NOT_FOUND));

        // 채팅방이 이미 존재하는지 확인
        ChatRoom chatRoom = chatRoomRepository.findByMemberAndFoodTruck(member, foodTruck)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.createChatRoom(member, foodTruck)));

        // MongoDB 메타데이터 존재 여부 확인
        chatRoomMetaDataRepository.findByRoomId(chatRoom.getChatRoomId())
                .orElseGet(() -> {
                    // 없을 경우 새로 생성
                    ChatRoomMetaData metaData = ChatRoomMetaData.from(chatRoom);
                    return chatRoomMetaDataRepository.save(metaData);
                });

        return ChatRoomIdResponse.of(chatRoom);
    }

    public ChatRoomMetaDataResponse getChatRoomMetaData(User user, Long roomId, boolean isOwner) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_ROOM_NOT_FOUND));

        Long reservationId = reservationRepository.findByChatRoom(chatRoom)
                .map(Reservation::getReservationId)
                .orElse(null);             // 없으면 null

        if(isOwner && user.getRole() != Role.OWNER) {
            throw new BusinessException(USER_FORBIDDEN);
        }

        // 푸드트럭 사장일 경우 예약자 이름 반환
        if(isOwner) return ChatRoomMetaDataResponse.of(chatRoom.getMember().getName(), null, reservationId);

        // 예약자일 경우 푸드트럭 사장 이름 및 푸드트럭 이름 반환
        FoodTruck foodTruck = chatRoom.getFoodTruck();
        return ChatRoomMetaDataResponse.of(foodTruck.getOwner().getName(), foodTruck.getFoodTruckInfo().getName(), reservationId);
    }

    public CursorPagingResponse<ChatRoomResponse> getChatRooms(User member, Boolean isOwner, Long cursor, Integer size) {
        int pageSize = (size == null || size < 1) ? 20 : size;

        Long cursorSortKey = (cursor != null) ? cursor : Long.MAX_VALUE;
        int limit = pageSize + 1;

        // 1. MongoDB에서 메타데이터 + unreadCount 조회
        List<ChatRoomMetaDataProjection> metaList =
                chatRoomMetaDataRepository.findChatRoomsForUser(member.getUserId(), isOwner, cursorSortKey, limit);

        boolean hasNext = metaList.size() > pageSize;
        if (hasNext) {
            metaList = metaList.subList(0, pageSize);
        }

        // 2. roomId 리스트로 RDB에서 ChatRoom 배치 조회
        List<Long> roomIds = metaList.stream()
                .map(ChatRoomMetaDataProjection::getRoomId)
                .toList();

        List<ChatRoom> chatRooms = chatRoomRepository.findByChatRoomIdIn(roomIds);
        Map<Long, ChatRoom> chatRoomMap = chatRooms.stream()
                .collect(Collectors.toMap(ChatRoom::getChatRoomId, Function.identity()));

        // 3. 예약 확정 여부 조회
        Map<Long, Boolean> reservationConfirmedMap;
        if (roomIds.isEmpty()) {
            reservationConfirmedMap = Collections.emptyMap();
        } else {
            List<ReservationConfirmedProjection> confirmedList = reservationRepository.findReservationConfirmedByChatRoomIds(roomIds);

            reservationConfirmedMap = confirmedList.stream()
                    .collect(Collectors.toMap(
                            ReservationConfirmedProjection::getRoomId,
                            ReservationConfirmedProjection::getConfirmed
                    ));
        }

        // 4. 메타데이터 + RDB 정보 조합해서 ChatRoomResponse 생성
        List<ChatRoomResponse> responses = metaList.stream()
                .map(meta -> ChatRoomResponse.from(chatRoomMap.get(meta.getRoomId()), meta, isOwner,
                        reservationConfirmedMap.getOrDefault(meta.getRoomId(), false)))
                .toList();

        // 5. CursorPagingResponse 생성
        Long lastCursor = responses.isEmpty() ? null : metaList.get(metaList.size() - 1).getSortKey();
        return new CursorPagingResponse<>(
                responses,
                lastCursor,
                hasNext,
                null
        );
    }
}
