package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements IOrderService{
    private final IOrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order updateOrderStatus(Long orderId, String orderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(orderStatus);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAllBy(pageable);
    }

    @Override
    public Page<Order> findByStatus(String orderStatus, Pageable pageable) {
        return orderRepository.findByStatusContainingIgnoreCase(orderStatus, pageable);
    }

    @Override
    public Page<Order> findByUserId(String userId, Pageable pageable) {
        return orderRepository.findByUserIdContainingIgnoreCase(userId, pageable);
    }

    @Override
    public Page<Order> findByStatusAndUserId(String orderStatus, String userId, Pageable pageable) {
        return orderRepository.findByStatusContainingIgnoreCaseAndUserIdContainingIgnoreCase(orderStatus, userId, pageable);
    }

    @Override
    public Order findById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found"));
    }
}
