package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;

    @BeforeEach
    public void setup() {
        order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");
        order.setUserId("user123");
    }

    @Test
    public void updateOrderStatusReturnsUpdatedOrder() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order updatedOrder = orderService.updateOrderStatus(1L, "SHIPPED");

        assertEquals("SHIPPED", updatedOrder.getStatus());
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    public void updateOrderStatusThrowsNotFoundException() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.updateOrderStatus(1L, "SHIPPED"));
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    public void findAllReturnsPageOfOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findAllBy(pageable)).thenReturn(orderPage);

        Page<Order> result = orderService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(order, result.getContent().getFirst());
        verify(orderRepository, times(1)).findAllBy(pageable);
    }

    @Test
    public void findByStatusReturnsPageOfOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findByStatusContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(orderPage);

        Page<Order> result = orderService.findByStatus("PENDING", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(order, result.getContent().getFirst());
        verify(orderRepository, times(1)).findByStatusContainingIgnoreCase("PENDING", pageable);
    }

    @Test
    public void findByUserIdReturnsPageOfOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findByUserIdContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(orderPage);

        Page<Order> result = orderService.findByUserId("user123", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(order, result.getContent().getFirst());
        verify(orderRepository, times(1)).findByUserIdContainingIgnoreCase("user123", pageable);
    }

    @Test
    public void findByStatusAndUserIdReturnsPageOfOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Order> orderPage = new PageImpl<>(Collections.singletonList(order));
        when(orderRepository.findByStatusContainingIgnoreCaseAndUserIdContainingIgnoreCase(anyString(), anyString(), any(Pageable.class))).thenReturn(orderPage);

        Page<Order> result = orderService.findByStatusAndUserId("PENDING", "user123", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(order, result.getContent().getFirst());
        verify(orderRepository, times(1)).findByStatusContainingIgnoreCaseAndUserIdContainingIgnoreCase("PENDING", "user123", pageable);
    }

    @Test
    public void findByIdReturnsOrderWhenFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        Order result = orderService.findById(1L);

        assertEquals(order, result);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void findByIdThrowsNotFoundExceptionWhenNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.findById(1L));
        verify(orderRepository, times(1)).findById(1L);
    }
}
