package id.ac.ui.cs.advprog.youkosoadmin.models.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationStatus;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "message",nullable = false)
    private String message;

    @Column(name = "type",nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @Column(name = "user_id",nullable = false)
    private String  userId;

    @Column(name ="product_id")
    private String productId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "shipment_id")
    private String shipmentId;

    @Column(name = "created_at",nullable = false, updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}


