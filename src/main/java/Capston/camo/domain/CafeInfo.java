package Capston.camo.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CafeInfo {

    private String seat;
    private String consent;
    private String parking;
    private String purpose;
}
