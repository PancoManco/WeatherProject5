package ru.pancoManco.weatherViewer.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan
@EnableWebMvc
@Import(ThymeleafConfig.class)
@ComponentScan(basePackages = "ru.pancoManco.weatherViewer")
public class WebMvcConfig implements WebMvcConfigurer {

//    private final AuthInterceptor authInterceptor;
//
//    @Autowired
//    public WebMvcConfig(AuthInterceptor authInterceptor) {
//        this.authInterceptor = authInterceptor;
//    }
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authInterceptor).excludePathPatterns("/sign-in", "/sign-up","/css/**");
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");
    }

}
