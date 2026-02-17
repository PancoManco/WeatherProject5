package ru.pancoManco.weatherViewer.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.repository.SessionRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class SessionCleanupScheduler
{
    private final SessionRepository sessionRepository;
    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void cleanupExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        int deleted = sessionRepository.deleteByExpiresAtBefore(now);
    }
}
