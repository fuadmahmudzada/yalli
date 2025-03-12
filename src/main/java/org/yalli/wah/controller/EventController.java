package org.yalli.wah.controller;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonArray;
import com.nimbusds.jose.shaded.gson.JsonObject;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.yalli.wah.model.dto.*;
import org.yalli.wah.model.dto.impl.SearchRequest;
import org.yalli.wah.model.exception.InvalidInputException;
import org.yalli.wah.service.EventService;
import org.yalli.wah.util.TranslateUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@RestController
@RequestMapping("/v1/events")
@RequiredArgsConstructor
@CrossOrigin
public class EventController {
    private final EventService eventService;

    @GetMapping
    public Page<EventDto> getEvents(
            @RequestHeader(value = "token", required = false) String token,
            @ModelAttribute EventSearchRequest searchRequest,
            Pageable pageable) throws IOException, InterruptedException {
        if(searchRequest.getCity()!=null &&!searchRequest.getCity().isEmpty()){
            removeCountryOfCity(searchRequest);
        }

        return eventService.getAllEvents(searchRequest, pageable, token);
    }


    //This method is used for extracting the city's countries from country list. Because in searching logic we don't need the country just need city
    //For example if we have Canada, Hungary and Toronto as ciy then our request will be
    //(Canada or Hungary) and Toronto this will obviously bring us only Toronto, but we need Hungary also
    //We can change this logic in filtering specification. But we need to make it like
    //if we have city then (City and City's Country) or Other Country
    // whcih complicates the search logic. Because we have to find the city's country and put
    // them to them and logic then bind other countries to them. The simplest way is this:
    static <T extends SearchRequest> void removeCountryOfCity(T filter) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        if(filter.getCountry() == null || filter.getCountry().isEmpty()){
            throw new InvalidInputException("If there is a city country can't be empty");
        }
        for(String city : filter.getCity()) {
            String searchCity = TranslateUtil.getCityTranslation(city);
            if(searchCity.contains(" ")){
                searchCity = String.join("%20" ,searchCity.split(" "));
            }
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://secure.geonames.org/searchJSON?q="+ searchCity+ "&maxRows=1&username=dedatom596minduls.c"))
                    .header("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            System.out.println(response.headers());
            System.out.println(response.statusCode());
            System.out.println(response);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response.body());
            System.out.println("rootNode"+ rootNode);
            String countryName = "";
            System.out.println(rootNode.path("geonames").getNodeType());
            System.out.println(rootNode.path("geonames").fields().toString());
            for (JsonNode node : rootNode.path("geonames")){
                System.out.println("a" + node.path("countryName").textValue());
            }
            System.out.println("iterator"+countryName);
//            System.out.println("nodecountry "+ nodeCountry);

//            StreamSupport.stream()
            System.out.println(rootNode.findValuesAsText("countryName"));
            System.out.println("path "+ rootNode.findPath("countryName").textValue());
            System.out.println("resutl "+ rootNode.path("totalResultsCount").intValue());
            System.out.println("resut with path "+ rootNode.path("geonames").path("population").intValue());
            System.out.println("nodecountry "+ rootNode.get("geonames").get("countryName"));
            System.out.println("nodecountry ");
            System.out.println("jsonpointer "+ rootNode.at("/geonames/countryName").textValue());
            String country = response.body().replaceAll("^.*?countryName\":\"", "").replaceAll("\",\".*", "");
            for(JsonNode node : rootNode.path("geonames"))
                country = node.path("countryName").asText();

            filter.getCountry().remove(TranslateUtil.getAzerbaijani(country));
        }
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
    public void unsaveEvent(@RequestBody EventSaveDto eventSaveDto) {
        eventService.unsaveEvent(eventSaveDto);
    }

    @PostMapping
    public void addEvent(EventDetailDto eventDetailDto) {
        eventService.addEvent(eventDetailDto);
    }
}
