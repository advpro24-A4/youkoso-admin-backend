package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.payment;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.BadRequestException;
import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Order;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Payment;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.order.IOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements IPaymentService {
    private final IPaymentRepository paymentRepository;
    private final IOrderRepository orderRepository;

    @Autowired
    public PaymentServiceImpl(IPaymentRepository paymentRepository, IOrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional("primaryTransactionManager")
    public Payment verifyPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new NotFoundException("Payment not found"));
        Order order = orderRepository.findById(payment.getOrderId()).orElseThrow(() -> new NotFoundException("Order not found"));

        if(order.getStatus().equals("CANCELLED")){
            throw new BadRequestException("Order already cancelled");
        }

        if(!order.getStatus().equals("WAITING_PAYMENT_CONFIRMATION")){
            throw new BadRequestException("You can only verify payment for order with status WAITING_PAYMENT_CONFIRMATION");
        }

        if(payment.getPaymentStatus().equals("VERIFIED")){
            throw new BadRequestException("Payment already verified");
        }

        payment.setPaymentStatus("VERIFIED");
        payment = paymentRepository.save(payment);
        order.setStatus("WAITING_SHIPMENT");
        orderRepository.save(order);
        return payment;
    }

    @Override
    @Transactional("primaryTransactionManager")
    public Payment rejectPayment(Long paymentId){
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new NotFoundException("Payment not found"));
        Order order = orderRepository.findById(payment.getOrderId()).orElseThrow(() -> new NotFoundException("Order not found"));

        if(order.getStatus().equals("CANCELLED")){
            throw new BadRequestException("Order already cancelled");
        }

        if(!order.getStatus().equals("WAITING_PAYMENT_CONFIRMATION")){
            throw new BadRequestException("You can only reject payment for order with status WAITING_PAYMENT_CONFIRMATION");
        }

        if (payment.getPaymentStatus().equals("REJECTED")) {
            throw new NotFoundException("Payment already rejected");
        }

        if (payment.getPaymentStatus().equals("VERIFIED")) {
            throw new NotFoundException("Payment already verified");
        }

        payment.setPaymentStatus("REJECTED");
        payment = paymentRepository.save(payment);

        order.setStatus("CANCELLED");
        orderRepository.save(order);
        
        return payment;
    }

    @Override
    public Page<Payment> findAll(Pageable pageable) {
        return paymentRepository.findAllBy(pageable);
    }

    @Override
    public Page<Payment> findByStatus(String paymentStatus, Pageable pageable) {
        return paymentRepository.findByPaymentStatusContainingIgnoreCase(paymentStatus, pageable);
    }

    @Override
    public Page<Payment> findByUserId(String userId, Pageable pageable) {
        return paymentRepository.findByUserIdContainingIgnoreCase(userId, pageable);
    }

    @Override
    public Page<Payment> findByStatusAndUserId(String paymentStatus, String userId, Pageable pageable) {
        return paymentRepository.findByPaymentStatusContainingIgnoreCaseAndUserIdContainingIgnoreCase(paymentStatus, userId, pageable);
    }

    @Override
    public Payment findById(Long paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow(() -> new NotFoundException("Payment not found"));
    }
}
