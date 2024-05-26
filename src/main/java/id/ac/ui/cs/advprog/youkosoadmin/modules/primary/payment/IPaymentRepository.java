package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.payment;

import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findAllBy(Pageable pageable);
    Page<Payment> findByPaymentStatusContainingIgnoreCase(String paymentStatus, Pageable pageable);
    Page<Payment> findByUserIdContainingIgnoreCase(String userId, Pageable pageable);
    Page<Payment> findByPaymentStatusContainingIgnoreCaseAndUserIdContainingIgnoreCase(String paymentStatus, String userId, Pageable pageable);
}