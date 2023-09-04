package zerobase.reservation.entity.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.reservation.entity.manager.Shop;

import java.util.Optional;

/**
 * Repository for shop entity
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByIdAndManagerId(Long id, Long managerId);

    boolean existsByIdAndManagerId(Long id, Long managerId);

    Page<Shop> findAllByOrderByRateDesc(Pageable pageable);

    Page<Shop> findAllByTypeLikeAndNameLikeOrderByNameAsc(String type, String name, Pageable pageable);

}
