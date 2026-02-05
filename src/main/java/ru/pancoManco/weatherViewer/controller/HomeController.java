package ru.pancoManco.weatherViewer.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

   @GetMapping("/index")
    public String getHomePage(HttpSession session) {
       if (session.getAttribute("username") == null) {
           return "redirect:/sign-in";
       }
       return "index";
   }
}
