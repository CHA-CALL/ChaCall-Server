package konkuk.chacall.domain.foodtruck.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.domain.foodtruck.domain.value.DocumentType;
import lombok.*;

@Builder
@Entity
@Table(name = "food_truck_documents")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FoodTruckDocument {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_truck_document_id", nullable = false)
    private Long foodTruckDocumentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_truck_id", nullable = false)
    private FoodTruck foodTruck;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType type;

    @Column(nullable = false, length = 500)
    private String documentUrl;

    public static FoodTruckDocument create(FoodTruck foodTruck, DocumentType type, String documentUrl) {
        return FoodTruckDocument.builder()
                .foodTruck(foodTruck)
                .type(type)
                .documentUrl(documentUrl)
                .build();
    }
}
