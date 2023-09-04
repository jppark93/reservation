package zerobase.reservation.entity.dto.reservation;


import lombok.*;

import java.time.LocalDate;

/**
 * 예약 정보 수정 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReservationDto {
    private String code;
    private LocalDate reservedDate;

}
