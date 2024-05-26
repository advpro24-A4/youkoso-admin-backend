package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.payment;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.BadRequestException;
import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Order;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Payment;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order.IOrderRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private IPaymentRepository paymentRepository;

    @Mock
    private IOrderRepository orderRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment payment;
    private Order order;

    @BeforeEach
    void setup() {
        payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(1L);
        payment.setPaymentStatus("PENDING");

        order = new Order();
        order.setId(1L);
        order.setStatus("WAITING_PAYMENT_CONFIRMATION");
        order.setUserId("user123");
    }

    @Test
    void verifyPaymentReturnsVerifiedPayment() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Payment verifiedPayment = paymentService.verifyPayment(1L);

        assertEquals("VERIFIED", verifiedPayment.getPaymentStatus());
        assertEquals("WAITING_SHIPMENT", order.getStatus());
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(payment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void verifyPaymentThrowsNotFoundExceptionForInvalidPayment() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.verifyPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, never()).findById(anyLong());
    }

    @Test
    void verifyPaymentThrowsNotFoundExceptionForInvalidOrder() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.verifyPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void verifyPaymentThrowsBadRequestExceptionForCancelledOrder() {
        order.setStatus("CANCELLED");
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> paymentService.verifyPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void verifyPaymentThrowsBadRequestExceptionForInvalidOrderStatus() {
        order.setStatus("PROCESSING");
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> paymentService.verifyPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void verifyPaymentThrowsBadRequestExceptionForAlreadyVerifiedPayment() {
        payment.setPaymentStatus("VERIFIED");
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> paymentService.verifyPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void rejectPaymentReturnsRejectedPayment() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Payment rejectedPayment = paymentService.rejectPayment(1L);

        assertEquals("REJECTED", rejectedPayment.getPaymentStatus());
        assertEquals("CANCELLED", order.getStatus());
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
        verify(paymentRepository, times(1)).save(payment);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void rejectPaymentThrowsNotFoundExceptionForInvalidPayment() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.rejectPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, never()).findById(anyLong());
    }

    @Test
    void rejectPaymentThrowsNotFoundExceptionForInvalidOrder() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.rejectPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void rejectPaymentThrowsBadRequestExceptionForCancelledOrder() {
        order.setStatus("CANCELLED");
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> paymentService.rejectPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void rejectPaymentThrowsBadRequestExceptionForInvalidOrderStatus() {
        order.setStatus("PROCESSING");
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class, () -> paymentService.rejectPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void rejectPaymentThrowsNotFoundExceptionForAlreadyRejectedPayment() {
        payment.setPaymentStatus("REJECTED");
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(NotFoundException.class, () -> paymentService.rejectPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void rejectPaymentThrowsNotFoundExceptionForAlreadyVerifiedPayment() {
        payment.setPaymentStatus("VERIFIED");
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThrows(NotFoundException.class, () -> paymentService.rejectPayment(1L));
        verify(paymentRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void findAllReturnsPageOfPayments() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> paymentPage = new PageImpl<>(Collections.singletonList(payment));
        when(paymentRepository.findAllBy(pageable)).thenReturn(paymentPage);

        Page<Payment> result = paymentService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(payment, result.getContent().getFirst());
        verify(paymentRepository, times(1)).findAllBy(pageable);
    }

    @Test
    void findByStatusReturnsPageOfPayments() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> paymentPage = new PageImpl<>(Collections.singletonList(payment));
        when(paymentRepository.findByPaymentStatusContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(paymentPage);

        Page<Payment> result = paymentService.findByStatus("PENDING", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(payment, result.getContent().getFirst());
        verify(paymentRepository, times(1)).findByPaymentStatusContainingIgnoreCase("PENDING", pageable);
    }

    @Test
    void findByUserIdReturnsPageOfPayments() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> paymentPage = new PageImpl<>(Collections.singletonList(payment));
        when(paymentRepository.findByUserIdContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(paymentPage);

        Page<Payment> result = paymentService.findByUserId("user123", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(payment, result.getContent().getFirst());
        verify(paymentRepository, times(1)).findByUserIdContainingIgnoreCase("user123", pageable);
    }

    @Test
    void findByStatusAndUserIdReturnsPageOfPayments() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Payment> paymentPage = new PageImpl<>(Collections.singletonList(payment));
        when(paymentRepository.findByPaymentStatusContainingIgnoreCaseAndUserIdContainingIgnoreCase(anyString(), anyString(), any(Pageable.class))).thenReturn(paymentPage);

        Page<Payment> result = paymentService.findByStatusAndUserId("PENDING", "user123", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(payment, result.getContent().getFirst());
        verify(paymentRepository, times(1)).findByPaymentStatusContainingIgnoreCaseAndUserIdContainingIgnoreCase("PENDING", "user123", pageable);
    }

    @Test
    void findByIdReturnsPaymentWhenFound() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));

        Payment result = paymentService.findById(1L);

        assertEquals(payment, result);
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void findByIdThrowsNotFoundExceptionWhenNotFound() {
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> paymentService.findById(1L));
        verify(paymentRepository, times(1)).findById(1L);
    }
}
