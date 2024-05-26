package id.ac.ui.cs.advprog.youkosoadmin.modules.primary.payment;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.UnauthorizedException;
import id.ac.ui.cs.advprog.youkosoadmin.models.primary.Payment;
import id.ac.ui.cs.advprog.youkosoadmin.modules.primary.payment.dto.UpdatePaymentStatusRequest;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthService;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private final AuthService authService;
    private final IPaymentService paymentService;

    @Autowired
    public PaymentController(AuthService authService, IPaymentService paymentService) {
        this.authService = authService;
        this.paymentService = paymentService;
    }

    @GetMapping("")
    public ResponseEntity<DefaultResponse<Page<Payment>>> getAllPayments(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            Pageable pageable,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String userId
    ){
        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        if(status != null && userId != null){
            Page<Payment> payments = paymentService.findByStatusAndUserId(status, userId, pageable);
            DefaultResponse<Page<Payment>> response = new DefaultResponseBuilder<Page<Payment>>()
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Success get payments")
                    .data(payments)
                    .build();
            return ResponseEntity.ok(response);
        }

        if(status != null){
            Page<Payment> payments = paymentService.findByStatus(status, pageable);
            DefaultResponse<Page<Payment>> response = new DefaultResponseBuilder<Page<Payment>>()
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Success get payments")
                    .data(payments)
                    .build();
            return ResponseEntity.ok(response);
        }

        if(userId != null){
            Page<Payment> payments = paymentService.findByUserId(userId, pageable);
            DefaultResponse<Page<Payment>> response = new DefaultResponseBuilder<Page<Payment>>()
                    .statusCode(HttpStatus.OK.value())
                    .success(true)
                    .message("Success get payments")
                    .data(payments)
                    .build();
            return ResponseEntity.ok(response);
        }

        Page<Payment> payments = paymentService.findAll(pageable);
        DefaultResponse<Page<Payment>> response = new DefaultResponseBuilder<Page<Payment>>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success get payments")
                .data(payments)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<DefaultResponse<Payment>> updatePaymentStatus(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody UpdatePaymentStatusRequest request
    ){
        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        Payment payment = paymentService.verifyPayment(request.getPaymentId());

        DefaultResponse<Payment> response = new DefaultResponseBuilder<Payment>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success verified payment status")
                .data(payment)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reject")
    public ResponseEntity<DefaultResponse<Payment>> rejectPayment(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestBody UpdatePaymentStatusRequest request
    ){
        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        Payment payment = paymentService.rejectPayment(request.getPaymentId());

        DefaultResponse<Payment> response = new DefaultResponseBuilder<Payment>()
                .statusCode(HttpStatus.OK.value())
                .success(true)
                .message("Success reject payment")
                .data(payment)
                .build();
        return ResponseEntity.ok(response);
    }
}
