package ru.pancoManco.weatherViewer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final LocationService locationService;

    @GetMapping
    public String getHomePage(Model model) {
        List<OpenWeatherCityResponseDto> locations = locationService.getAllLocationForUser();
        model.addAttribute("locationList",locations);
       return "index";
   }

    @PostMapping("/location")
    public String saveLocation(@ModelAttribute("location") @Valid LocationRequestDto locationRequestDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//           bindingResult.getAllErrors().forEach(error -> logger.error("Saving location validation error : {}", error));
//           return "redirect:/";
//        }
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
    public String DeleteLocation(@ModelAttribute("location") @Valid LocationRequestDto locationRequestDto,
                                 BindingResult bindingResult) {
//       if (bindingResult.hasErrors()) {
//           bindingResult.getAllErrors().forEach(error -> logger.error("Deleting Location validation error: {}", error));
//           return "redirect:/";
//       }
        locationService.deleteLocation(locationRequestDto);
        return "redirect:/";
   }
}
