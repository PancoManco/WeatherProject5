CREATE TABLE IF NOT EXISTS weather.locations (
                                                 id SERIAL PRIMARY KEY,
                                                 name VARCHAR(120) NOT NULL,
    user_id INT NOT NULL,
    latitude DECIMAL(9,6) NOT NULL,
    longitude DECIMAL(9,6) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES weather.users(id)
    );