package id.ac.ui.cs.advprog.youkosoadmin.modules.notification;

import id.ac.ui.cs.advprog.youkosoadmin.models.notification.Notification;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationStatus;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationType;

import java.util.List;
import java.util.UUID;

public interface INotificationService {
    List<Notification> findByTypeAndStatus(NotificationType type, NotificationStatus status);
    Notification readNotification(UUID id);
}
