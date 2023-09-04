package zerobase.reservation.entity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.reservation.entity.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for reservation entity
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByCustomerId(Long customerId);

    List<Reservation> findAllByShopIdOrderByReservedDate(Long shopId);

    List<Reservation> findAllByShopIdAndReservedDate(Long shopId, LocalDate reservedDate);

    List<Reservation> findAllByReservedDate(LocalDate date);

    Optional<Reservation> findByCode(String code);

}
