package ru.pancoManco.weatherViewer;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.pancoManco.weatherViewer.config.FlywayConfig;
import ru.pancoManco.weatherViewer.config.HibernateConfig;

public class testConnection {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HibernateConfig.class, FlywayConfig.class);

    }
}
