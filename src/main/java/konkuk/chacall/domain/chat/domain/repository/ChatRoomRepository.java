package konkuk.chacall.domain.chat.domain.repository;

import konkuk.chacall.domain.chat.domain.ChatRoom;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    boolean existsByMemberAndFoodTruck(User member, FoodTruck foodTruck);
}
