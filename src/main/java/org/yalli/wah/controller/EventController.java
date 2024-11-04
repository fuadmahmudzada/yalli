package org.yalli.wah.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.yalli.wah.model.dto.EventDetailDto;
import org.yalli.wah.model.dto.EventDto;
import org.yalli.wah.model.dto.EventSaveDto;
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

    @GetMapping("/{id}")
    public EventDetailDto getEvent(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @PatchMapping("/saveEvent")
    public void saveEvent(@RequestBody EventSaveDto eventSaveDto) {
        eventService.saveEvent(eventSaveDto);
    }

    @PatchMapping("/unsaveEvent")
    public void unsaveEvent(@RequestBody EventSaveDto eventSaveDto){
        eventService.unsaveEvent(eventSaveDto);
    }
}
