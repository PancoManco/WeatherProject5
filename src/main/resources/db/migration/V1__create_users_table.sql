CREATE SCHEMA IF NOT EXISTS weather;

CREATE TABLE IF NOT EXISTS weather.users (
                                             id SERIAL PRIMARY KEY,
                                             login VARCHAR(120) UNIQUE NOT NULL,
    password VARCHAR(120) NOT NULL
    );