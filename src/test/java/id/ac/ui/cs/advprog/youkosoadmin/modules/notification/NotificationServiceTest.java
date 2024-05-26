package id.ac.ui.cs.advprog.youkosoadmin.modules.notification;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.Notification;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationStatus;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @Mock
    private INotificationRepository notificationRepository;

    private NotificationServiceImpl notificationService;

    AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        notificationService = new NotificationServiceImpl(notificationRepository);
    }

    @AfterEach
    void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    void findByTypeAndStatusReturnsAllWhenTypeAndStatusAreNull() {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> result = notificationService.findByTypeAndStatus(null, null);

        assertEquals(notifications, result);
    }

    @Test
    void findByTypeAndStatusReturnsByStatusWhenTypeIsNull() {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationRepository.findByStatus(NotificationStatus.READ)).thenReturn(notifications);

        List<Notification> result = notificationService.findByTypeAndStatus(null, NotificationStatus.READ);

        assertEquals(notifications, result);
    }

    @Test
    void findByTypeAndStatusReturnsByTypeWhenStatusIsNull() {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationRepository.findByType(NotificationType.ORDER)).thenReturn(notifications);

        List<Notification> result = notificationService.findByTypeAndStatus(NotificationType.ORDER, null);

        assertEquals(notifications, result);
    }

    @Test
    void findByTypeAndStatusReturnsByTypeAndStatusWhenBothAreNotNull() {
        List<Notification> notifications = Arrays.asList(new Notification(), new Notification());
        when(notificationRepository.findByTypeAndStatus(NotificationType.ORDER, NotificationStatus.READ)).thenReturn(notifications);

        List<Notification> result = notificationService.findByTypeAndStatus(NotificationType.ORDER, NotificationStatus.READ);

        assertEquals(notifications, result);
    }

    @Test
    void readNotificationReturnsNotificationWhenFound() {
        UUID id = UUID.randomUUID();
        Notification notification = new Notification();
        notification.setStatus(NotificationStatus.UNREAD);
        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.readNotification(id);

        assertEquals(NotificationStatus.READ, result.getStatus());
    }

    @Test
    void readNotificationThrowsExceptionWhenNotFound() {
        UUID id = UUID.randomUUID();
        when(notificationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> notificationService.readNotification(id));
    }
}