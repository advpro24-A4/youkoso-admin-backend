package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long>{
    Page<Order> findAllBy(Pageable pageable);
    Page<Order> findByStatusContainingIgnoreCase(String orderStatus, Pageable pageable);
    Page<Order> findByUserIdContainingIgnoreCase(String userId, Pageable pageable);
    Page<Order> findByStatusContainingIgnoreCaseAndUserIdContainingIgnoreCase(String orderStatus, String userId, Pageable pageable);
}