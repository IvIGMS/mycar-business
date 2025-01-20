package com.mycar.business.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycar.business.entities.IssueEntity;
import com.mycar.business.entities.NotificationEntity;
import com.mycar.business.repositories.NotificationRepository;
import com.mycar.business.services.IssueService;
import com.mycar.business.services.NotificationService;
import com.mycar.business.services.NotificationTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationTypeService notificationTypeService;
    private final IssueService issueService;
    private final ObjectMapper objectMapper;

    public NotificationServiceImpl(NotificationRepository notificationRepository, NotificationTypeService notificationTypeService, IssueService issueService, ObjectMapper objectMapper){
        this.notificationRepository = notificationRepository;
        this.notificationTypeService = notificationTypeService;
        this.issueService = issueService;
        this.objectMapper = objectMapper;
    }
    @Override
    public NotificationEntity saveNotification(NotificationEntity notificationEntity) {
        return notificationRepository.save(notificationEntity);
    }

    @Override
    public List<NotificationEntity> createNotifications(List<Long> ids, Long typeId) {
        List<IssueEntity> allIssues = issueService.getIssuesByIds(ids);

        List<NotificationEntity> notifications = allIssues.stream().map(issue -> NotificationEntity.builder()
                .header("Notificación de mantenimiento vencido")
                .message("El mantenimiento " + issue.getName() + " ha vencido.")
                .sender("admin@mycar.com")
                .receiver(issue.getCarEntity().getUserEntity().getEmail())
                .notificationTypeEntity(notificationTypeService.findById(typeId))
                .build()
        ).toList();

        return notificationRepository.saveAll(notifications);
    }

    @Override
    public void sendNotification(NotificationEntity notificationEntity) {
        // Implementar
    }
}
