package konkuk.chacall.domain.chat.application.room;

import konkuk.chacall.domain.chat.domain.ChatRoom;
import konkuk.chacall.domain.chat.domain.repository.ChatRoomRepository;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatOpponentResponse;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomIdResponse;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.user.domain.model.Role;
import konkuk.chacall.domain.user.domain.model.User;
import konkuk.chacall.global.common.exception.BusinessException;
import konkuk.chacall.global.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if(chatRoomRepository.existsByMemberAndFoodTruck(member, foodTruck)) {
            throw new EntityNotFoundException(CHAT_ROOM_ALREADY_EXISTS);
        }

        ChatRoom chatRoom = ChatRoom.createChatRoom(member, foodTruck);

        return ChatRoomIdResponse.of(
                chatRoomRepository.save(chatRoom)
        );
    }

    public ChatOpponentResponse getChatOpponentName(User user, Long roomId, boolean isOwner) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new EntityNotFoundException(CHAT_ROOM_NOT_FOUND));

        if(isOwner && user.getRole() != Role.OWNER) {
            throw new BusinessException(USER_FORBIDDEN);
        }

        String name = isOwner ? chatRoom.getMember().getName() : chatRoom.getFoodTruck().getFoodTruckInfo().getName();

        return ChatOpponentResponse.of(name);
    }
}
