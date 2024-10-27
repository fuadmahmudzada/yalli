package org.yalli.wah.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yalli.wah.dao.repository.NotificationRepository;
import org.yalli.wah.model.dto.NotificationDto;
import org.yalli.wah.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("v1/notifications")
@CrossOrigin
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;



    @GetMapping("/getAll")
    public List<NotificationDto> getAll(@RequestParam Long userId){
        return notificationService.getAll(userId);
    }

    @PostMapping("/getAll")
    public void getAll(@RequestBody List<NotificationDto> notificationDto){
        notificationService.setRead(notificationDto);
    }
}
