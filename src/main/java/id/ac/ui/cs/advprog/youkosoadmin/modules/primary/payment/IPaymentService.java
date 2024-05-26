package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.payment;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPaymentService {
    Payment verifyPayment(Long paymentId);
    Payment rejectPayment(Long paymentId);
    Page<Payment> findAll(Pageable pageable);
    Page<Payment> findByStatus(String paymentStatus, Pageable pageable);
    Page<Payment> findByUserId(String userId, Pageable pageable);
    Page<Payment> findByStatusAndUserId(String paymentStatus, String userId, Pageable pageable);
    Payment findById(Long paymentId);
}