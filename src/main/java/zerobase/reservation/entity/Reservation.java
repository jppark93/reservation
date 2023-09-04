package zerobase.reservation.entity;


import lombok.*;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.Id;
import zerobase.reservation.entity.manager.Shop;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 예약 엔티티
 * <p>
 * 정보 : 손님 id, 매장 id, 예약 식별 코드, 예약 날짜, 예약 시간(enum), 예약 승인/방문/삭제 여부
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private Long customerId;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    private LocalDate reservedDate;

    private LocalDateTime visitedAt;


    @Nullable
    private Boolean accepted;
    private Boolean visited;
    private Boolean canceled;
}
