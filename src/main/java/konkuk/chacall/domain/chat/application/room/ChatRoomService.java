package konkuk.chacall.domain.chat.application.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import konkuk.chacall.domain.chat.domain.ChatRoom;
import konkuk.chacall.domain.chat.domain.repository.ChatRoomRepository;
import konkuk.chacall.domain.chat.presentation.dto.request.ChatRoomFilter;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatOpponentResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomIdResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomResponse;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.dto.CursorPagingResponse;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static konkuk.chacall.global.common.exception.code.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final FoodTruckRepository foodTruckRepository;

    public ChatRoomIdResponse createChatRoom(User member, Long foodTruckId) {
        FoodTruck foodTruck = foodTruckRepository.findById(foodTruckId)
                .orElseThrow(() -> new EntityNotFoundException(FOOD_TRUCK_NOT_FOUND));

        // 채팅방이 이미 존재하는지 확인
        ChatRoom chatRoom = chatRoomRepository.findByMemberAndFoodTruck(member, foodTruck)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.createChatRoom(member, foodTruck)));

        return ChatRoomIdResponse.of(chatRoom);
    }

    public ChatOpponentResponse getChatOpponentName(User user, Long roomId, boolean isOwner) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_ROOM_NOT_FOUND));

        if(isOwner && user.getRole() != Role.OWNER) {
            throw new BusinessException(USER_FORBIDDEN);
        }

        // 푸드트럭 사장일 경우 예약자 이름 반환
        if(isOwner) return ChatOpponentResponse.of(chatRoom.getMember().getName(), null);

        // 예약자일 경우 푸드트럭 사장 이름 및 푸드트럭 이름 반환
        FoodTruck foodTruck = chatRoom.getFoodTruck();
        return ChatOpponentResponse.of(foodTruck.getOwner().getName(), foodTruck.getFoodTruckInfo().getName());
    }

    public CursorPagingResponse<ChatRoomResponse> getChatRooms(User member, ChatRoomFilter filter, Boolean owner, Long cursor, Integer size) {

    }
}
