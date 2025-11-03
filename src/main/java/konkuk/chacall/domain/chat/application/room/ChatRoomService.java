package konkuk.chacall.domain.chat.application.room;

import konkuk.chacall.domain.chat.domain.ChatRoom;
import konkuk.chacall.domain.chat.domain.repository.ChatRoomRepository;
import konkuk.chacall.domain.chat.presentation.dto.response.ChatRoomIdResponse;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.foodtruck.domain.repository.FoodTruckRepository;
import konkuk.chacall.domain.user.domain.model.User;
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

        ChatRoom chatRoom = ChatRoom.createChatRoom(member, foodTruck.getOwner());

        return ChatRoomIdResponse.of(
                chatRoomRepository.save(chatRoom)
        );
    }
}
