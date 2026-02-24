# WeatherViewer 

## 🧱Built with
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) [![Hibernate](https://img.shields.io/badge/Hibernate-ORM-blue?style=for-the-badge&logo=hibernate&logoColor=white)](https://hibernate.org/)
  ![APACHE MAVEN](https://img.shields.io/badge/Apache%20Maven-blue?style=for-the-badge&logo=apachemaven&logoSize=auto&color=%23C71A36) [![JUnit 5](https://img.shields.io/badge/JUnit_5-testing-green?style=for-the-badge&logo=junit5&logoColor=white)](https://junit.org/junit5/)
 ![Apache Tomcat](https://img.shields.io/badge/apache%20tomcat-%23F8DC75.svg?style=for-the-badge&logo=apache-tomcat&logoColor=black) [![H2 Database](https://img.shields.io/badge/H2-Database-blue?style=for-the-badge)](https://www.h2database.com/)
 [![Spring Core](https://img.shields.io/badge/Spring_Core-green?style=for-the-badge)]()
[![Mockito](https://img.shields.io/badge/Mockito-yellow?style=for-the-badge)]()
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-red?style=for-the-badge)]()
[![Flyway](https://img.shields.io/badge/Flyway-purple?style=for-the-badge)]()
[![Spring MVC](https://img.shields.io/badge/Spring_MVC-orange?style=for-the-badge)]()
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-teal?style=for-the-badge)]()

## 📋Application features 
The Weather Viewer Project is a Java-based backend system designed to provide weather information for specific cities using external APIs. Below are the key features of this project:

## Main Functionalities
1.Weather Data Retrieval
* The application fetches real-time and forecasted weather data from third-party API services like OpenWeatherMap or similar providers.
* It supports multiple cities and allows users to retrieve detailed weather conditions such as temperature, humidity, wind speed, etc.
2. RESTful Web Services
* Provides RESTful endpoints that allow clients (web apps, mobile apps, other systems) to access current and future weather details in JSON format.
3. Unit Testing with Mockito
* Extensive unit testing coverage ensures robustness and reliability by mocking dependencies where necessary.
* Tests verify business logic correctness, error handling mechanisms, edge cases, and proper interaction between components.
4. Security Measures
* Implements basic authentication measures through Spring Security configuration to protect sensitive resources from unauthorized access.
* Ensures secure communication channels via HTTPS protocol when interacting with external APIs.
5. Monitoring & Logging
* Includes logging facilities powered by Logback/Slf4j libraries to track events, errors, warnings during runtime operations.
* Integrates health check endpoints enabling administrators to monitor service availability efficiently.
6. Dependency Management
* Leverages Maven build tool for dependency management ensuring consistent builds across different environments.
* Easily integrates new libraries or updates existing ones without breaking functionality.

# ⚙️ Installation
Follow these steps to build and run the Tennis Scoreboard application locally or on a remote server.

✅ Prerequisites
* Java 17+
* Maven 3.6+
* Apache Tomcat 9 or 10
* Git
* (Optional) IntelliJ IDEA or another Java IDE

## 🛠️ Steps to Run
1. **Clone the Repository**

Clone the project from GitHub to your local machine:
```bash
git clone https://github.com/your-username/your-project.git
cd your-project
```

2. **Build the Project**
If your project uses Maven, build the project with:
```bash
mvn clean package
```
This will generate a .war file inside the target/ directory

3. **Deploy the .war File to Tomcat**
* Copy the generated .war file from target/ into the webapps folder of your local Tomcat installation.
Example:
```bash
cp target/your-project.war /path/to/tomcat/webapps/
```
4. **Start Tomcat**
Start your Tomcat server:
On Linux/macOS:
```bash
/path/to/tomcat/bin/startup.sh
```
On Windows:
```bash
\path\to\tomcat\bin\startup.bat
```
5. **Access the Application**
Open your browser and navigate to:
```bash
http://localhost:8080/your-project/
```
Replace your-project with the name of your .war file (without the .war extension).
6. **Don't forget to change login and password from your PostgreSql database and api-key for OpenWeather in database.properties.**
