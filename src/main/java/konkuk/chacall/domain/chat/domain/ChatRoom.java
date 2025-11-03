package konkuk.chacall.domain.chat.domain;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.model.FoodTruck;
import konkuk.chacall.domain.user.domain.model.User;
import lombok.*;

@Entity
@Table(name = "chat_rooms")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    public static ChatRoom createChatRoom(User member, FoodTruck foodTruck) {
        return ChatRoom.builder()
                .member(member)
                .foodTruck(foodTruck)
                .build();
    }
}

