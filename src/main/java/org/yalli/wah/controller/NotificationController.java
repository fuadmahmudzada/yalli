package org.yalli.wah.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.yalli.wah.model.dto.NotificationDto;
import org.yalli.wah.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("v1/notifications")
@CrossOrigin
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;



    @GetMapping("/getAll")
    public List<NotificationDto> getAll(@RequestParam Long userId,
                                        @RequestParam String country){
        return notificationService.getAll(userId, country);
    }

}
