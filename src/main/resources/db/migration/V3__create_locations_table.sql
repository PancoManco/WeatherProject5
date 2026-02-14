CREATE TABLE IF NOT EXISTS weather.locations (
                                                 id SERIAL PRIMARY KEY,
                                                 name VARCHAR(120) NOT NULL,
    user_id INT NOT NULL,
    latitude NUMERIC(10,8) NOT NULL,
    longitude NUMERIC(11,8) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES weather.users(id)
    );
