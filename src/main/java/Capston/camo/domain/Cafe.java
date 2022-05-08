package Capston.camo.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
//카페는 크롤링해서 저장하니까 id를 외부에서 주입해줘야됨
public class Cafe extends BaseEntity{

    @Id
    @Column(name = "cafe_id",unique = true)
    private Long id;

    private String name;

    private String businessHours;

    private String address;

    private String phone;

    @Embedded
    private CafeInfo cafeInfo;

}
