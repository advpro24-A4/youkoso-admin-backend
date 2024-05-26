package id.ac.ui.cs.advprog.youkosoadmin.models.primary;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "shipments")
public class Shipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "shipment_method", nullable = false)
    private String shipmentMethod;

    @Column(name = "tracking_number", nullable = false, length = 16)
    private String trackingNumber;

    @Column(name = "shipment_status", nullable = false)
    private String shipmentStatus;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Shipment() {}

    public Shipment(Long orderId, String shipmentMethod, String trackingNumber, String shipmentStatus) {
        this.orderId = orderId;
        this.shipmentMethod = shipmentMethod;
        this.trackingNumber = trackingNumber;
        this.shipmentStatus = shipmentStatus;
    }

    @PrePersist
    @PreUpdate
    public void validateTrackingNumber() {
        switch (this.shipmentMethod) {
            case "JTE":
                if (!this.trackingNumber.matches("^JTE-\\d{12}$")) {
                    throw new IllegalArgumentException("Invalid JTE tracking number");
                }
                break;
            case "Go-bek":
                if (!this.trackingNumber.matches("^GBK-[A-Z0-9]{12}$")) {
                    throw new IllegalArgumentException("Invalid Go-bek tracking number");
                }
                break;
            case "SiWuzz":
                if (!this.trackingNumber.matches("^SWZ-[A-Z]{12}$")) {
                    throw new IllegalArgumentException("Invalid SiWuzz tracking number");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid shipment method");
        }
    }
}