package id.ac.ui.cs.advprog.youkosoadmin.modules.notification;

import id.ac.ui.cs.advprog.youkosoadmin.exceptions.NotFoundException;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.Notification;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationStatus;
import id.ac.ui.cs.advprog.youkosoadmin.models.notification.enumaration.NotificationType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements INotificationService{

    private final INotificationRepository notificationRepository;

    public NotificationServiceImpl(INotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    @Override
    public List<Notification> findByTypeAndStatus(NotificationType type, NotificationStatus status) {
        if (type == null && status == null) {
            return notificationRepository.findAll();
        } else if (type == null) {
            return notificationRepository.findByStatus(status);
        } else if (status == null) {
            return notificationRepository.findByType(type);
        } else {
            return notificationRepository.findByTypeAndStatus(type, status);
        }
    }

    @Override
    public Notification readNotification(UUID id) {
        Notification notification  = notificationRepository.findById(id).orElseThrow(() -> new NotFoundException("Notification not found"));
        notification.setStatus(NotificationStatus.READ);
        notification = notificationRepository.save(notification);
        return notification;
    }
}
