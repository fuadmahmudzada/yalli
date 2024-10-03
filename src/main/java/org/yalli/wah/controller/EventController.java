package org.yalli.wah.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yalli.wah.model.dto.EventDto;
import org.yalli.wah.model.dto.EventSearchRequest;
import org.yalli.wah.service.EventService;


@RestController
@RequestMapping("/v1/events")
@RequiredArgsConstructor
@CrossOrigin
public class EventController {
    private final EventService eventService;

    @GetMapping
    public Page<EventDto> getEvents(
            @RequestHeader(value = "token") String token,
            @ModelAttribute EventSearchRequest searchRequest,
            Pageable pageable) {
        return eventService.getAllEvents(searchRequest, pageable, token);
    }
}
