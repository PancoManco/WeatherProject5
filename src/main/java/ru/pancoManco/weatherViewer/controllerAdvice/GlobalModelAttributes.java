package ru.pancoManco.weatherViewer.controllerAdvice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.pancoManco.weatherViewer.context.UserContextHolder;
import ru.pancoManco.weatherViewer.model.User;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute
    public void addUserToModel(Model model) {
        User user = UserContextHolder.get();
        if (user != null) {
            model.addAttribute("currentUser", user);
        }
    }
}
