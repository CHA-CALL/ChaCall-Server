package konkuk.chacall.domain.region.domain.model;

import jakarta.persistence.*;
import konkuk.chacall.global.common.domain.BaseEntity;
import lombok.Getter;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "regions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Region extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long regionId;

    @Column(nullable = false)
    private Long regionCode;

    @Column(nullable = false, length = 100)
    private String name; // ex) 강남구 or 개포동

    @Column(nullable = false, length = 200)
    private String fullName; // ex) 서울특별시 강남구 개포동

    @Column(nullable = false, length = 1)
    private Integer depth; // 1: 시/도, 2: 구/군, 3: 동/읍/면

    private Long parentCode;
}
