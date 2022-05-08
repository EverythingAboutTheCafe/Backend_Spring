package Capston.camo.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(value = {AuditingEntityListener.class}) //객체의 변화 감지
//엔티티 객체가 생성/변경되는 것을 감지하는 역할
public class BaseEntity {

    @CreatedDate //엔티티의 생성시간 처리
    @Column(name = "create_at",updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate //최중 수정 시간을 자동으로 처리하는 용도
    @Column(name = "update_at")
    private LocalDateTime updateAt;
}
