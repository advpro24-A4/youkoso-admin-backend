package id.ac.ui.cs.advprog.youkosoadmin.modules.notification;

import id.ac.ui.cs.advprog.youkosoadmin.models.notification.Notification;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationStatus;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, UUID>{
    List<Notification> findByTypeAndStatus(NotificationType type, NotificationStatus status);
    List<Notification> findByType(NotificationType type);
    List<Notification> findByStatus(NotificationStatus status);
}
