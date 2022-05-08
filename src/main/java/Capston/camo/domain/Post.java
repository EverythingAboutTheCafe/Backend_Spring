package Capston.camo.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "cafe_id",nullable = false)
    private Long cafeId;

    private String content; //내용

    //사진은 따로 entity 만들어야됨

    private String location; //장소

    private String visitingHours;//방문시각



}
