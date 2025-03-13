## TollPlazaFinder

This project is a Spring Boot application designed to determine toll plazas between two Indian pincodes. It integrates external APIs for route calculations, uses PostgreSQL (Dockerized) for data storage, and employs spatial queries for toll plaza identification.</br></br>

## 🚀 How to Run the Project

1. **Clone the Repository**📥</br>
   git clone https://github.com/Wantstocode/TollPlazaFinder.git</br>
   cd TollPlazaFinder</br></br>

2. **Start PostgreSQL & Redis in Docker** 🐳</br>
   docker-compose up -d</br>
      1. PostgreSQL → **localhost:5432**</br>
      2. Redis Cache → **localhost:6379**</br>
      3. PGAdmin UI → **http://localhost:5050**</br></br>

3. **Build & Run the Project**⚙️**</br>
   ./gradlew clean build  </br>
   ./gradlew bootRun</br>
   The app will start at **http://localhost:8080** 🎉</br></br>

4. **API Testing**📡</br>
   Swagger UI →**http://localhost:8080/swagger-ui/index.html**</br>
   Postman Example:</br>
   curl -X 'POST' **'http://localhost:8080/api/v1/toll-plazas'** -H 'Content-Typ</br></br>

5. **SonarQube Configuration (Code Quality & Test Coverage)** 📊</br>
   1. Running SonarQube Installed on Your Machine</br>
      1. Start SonarQube manually if it's not running.</br>
      2. Configure pom.xml for SonarQube.</br>
      3. Run SonarQube Analysis:<br>
          ./gradlew sonarqube</br>
      4. View Reports in Browser:<br>
          Open http://localhost:9000/dashboard?id=TollPlazaFinder</br>
      
   2. Running SonarQube in Docker</br>
      1. Start SonarQube in Docker:<br>
          docker run -d --name sonarqube -p 9000:9000 sonarqube<br>
      2. Run the Analysis:<br>
          ./gradlew sonarqube<br>
      3. Check Reports:<br>
          Open http://localhost:9000/dashboard?id=TollPlazaFinder<br>
      

## Swagger UI - API Documentation
![Screenshot (101)](https://github.com/user-attachments/assets/e86294c0-7a63-48c4-b323-d68cb51acf7d)
<br><br>

## SonarQube - Code Quality & Test Coverage
![Screenshot (97)](https://github.com/user-attachments/assets/25937813-ce5f-4d7f-b0c6-7240b91e41c8)
</br></br>

## Docker PostgreSQL & PGAdmin - Database Management
![Screenshot (100)](https://github.com/user-attachments/assets/61cbd61e-944b-4607-956b-31106ec445f2)


