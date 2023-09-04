package zerobase.reservation.entity.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.reservation.entity.manager.Manager;

import java.util.Optional;

/**
 * Repository for manager entity
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByEmail(String email);

    Boolean existsByEmail(String email);

    Object save(Manager manager);
}
