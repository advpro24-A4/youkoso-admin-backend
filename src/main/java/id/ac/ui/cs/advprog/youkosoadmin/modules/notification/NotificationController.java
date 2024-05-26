package id.ac.ui.cs.advprog.youkosoadmin.modules.notification;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.UnauthorizedException;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.Notification;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationStatus;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationType;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.AuthService;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponse;
import id.ac.ui.cs.advprog.youkosoadmin.utils.DefaultResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notification")
public class NotificationController {
    private final AuthService authService;
    private final INotificationService notificationService;

    @Autowired
    public NotificationController(AuthService authService, INotificationService notificationService) {
        this.authService = authService;
        this.notificationService = notificationService;
    }

    @GetMapping("")
    public ResponseEntity<DefaultResponse<List<Notification>>> getAllNotification(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @RequestParam(value = "type", required = false) NotificationType type,
            @RequestParam(value = "status", required = false) NotificationStatus status
    ) {
        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        List<Notification> notifications = notificationService.findByTypeAndStatus(type, status);
        DefaultResponse<List<Notification>> response = new DefaultResponseBuilder<List<Notification>>()
                .statusCode(HttpStatus.OK.value())
                .message("Success get notification")
                .data(notifications)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/read/{notificationId}")
    public ResponseEntity<DefaultResponse<Notification>> getNotificationById(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathVariable UUID notificationId
    ) {
        AuthResponse authResponse = authService.validateToken(authHeader).join();

        if (authResponse == null) {
            throw new UnauthorizedException("Authorization token is invalid");
        }

        Notification notification = notificationService.readNotification(notificationId);
        DefaultResponse<Notification> response = new DefaultResponseBuilder<Notification>()
                .statusCode(HttpStatus.OK.value())
                .message("Success read notification")
                .data(notification)
                .success(true)
                .build();
        return ResponseEntity.ok(response);
    }


}
