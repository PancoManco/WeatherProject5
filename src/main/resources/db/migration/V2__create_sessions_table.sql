CREATE TABLE IF NOT EXISTS weather.sessions (
                                                id UUID PRIMARY KEY,
                                                user_id INT NOT NULL,
                                                expires_at TIMESTAMP NOT NULL,
                                                FOREIGN KEY (user_id) REFERENCES weather.users(id)
    );