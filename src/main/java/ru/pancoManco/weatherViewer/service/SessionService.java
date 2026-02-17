package ru.pancoManco.weatherViewer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.pancoManco.weatherViewer.model.Session;
import ru.pancoManco.weatherViewer.model.User;
import ru.pancoManco.weatherViewer.repository.SessionRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    @Transactional
    public UUID createNewSession(User user) {
      //  int deleted =  sessionRepository.deleteByUserId(user);

        Session session = new Session(UUID.randomUUID(),user, LocalDateTime.now().plusHours(1));
        sessionRepository.save(session);
        return session.getId();
    }

    public Optional<Session> getSessionById(UUID sessionId) {
        return sessionRepository.findById(sessionId);
    }

    public Optional<User> getUserById(UUID sessionId) {
      return sessionRepository.findUserById(sessionId);
    }

    @Transactional
    public void invalidateSession(UUID sessionId) {
       sessionRepository.deleteById(sessionId);
    }

}
