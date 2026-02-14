package ru.pancoManco.weatherViewer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.pancoManco.weatherViewer.dto.OpenWeatherGeoResponseDto;
import ru.pancoManco.weatherViewer.service.OpenWeatherService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final OpenWeatherService openWeatherService;

    @GetMapping
    public String search(@RequestParam("location") String location, Model model) {
        List<OpenWeatherGeoResponseDto> locations = openWeatherService.getGeoByCityName(location);
        model.addAttribute("locations", locations);
        return "search-result";
    }
}
