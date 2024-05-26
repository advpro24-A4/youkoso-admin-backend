package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    Order updateOrderStatus(Long orderId, String orderStatus);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByStatus(String orderStatus, Pageable pageable);
    Page<Order> findByUserId(String userId, Pageable pageable);
    Page<Order> findByStatusAndUserId(String orderStatus, String userId, Pageable pageable);
    Order findById(Long orderId);
}
