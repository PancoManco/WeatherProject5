package ru.pancoManco.weatherViewer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.pancoManco.weatherViewer.dto.LocationRequestDto;
import ru.pancoManco.weatherViewer.dto.OpenWeatherCityResponseDto;
import ru.pancoManco.weatherViewer.service.LocationService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final LocationService locationService;

    @GetMapping
    public String getHomePage(Model model) {
        List<OpenWeatherCityResponseDto> locations = locationService.getAllLocationForUser();
        model.addAttribute("locationList",locations);
       return "index";
   }

    @PostMapping("/location")
    public String saveLocation(@ModelAttribute("location") LocationRequestDto locationRequestDto,
                               RedirectAttributes redirectAttributes) {
        boolean exists = locationService.existsLocation(locationRequestDto);
        if (exists) {
            redirectAttributes.addFlashAttribute("errorMessage", "You already added this location");
        } else {
            locationService.saveLocation(locationRequestDto);
            redirectAttributes.addFlashAttribute("successMessage", "The location has been saved");
        }
        return "redirect:/";
    }

   @PostMapping("/location/delete")
    public String DeleteLocation(@ModelAttribute("location")LocationRequestDto locationRequestDto) {
        locationService.deleteLocation(locationRequestDto);
        return "redirect:/";
   }
}
