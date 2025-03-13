## TollPlazaFinder

This project is a Spring Boot application designed to determine toll plazas between two Indian pincodes. It integrates external APIs for route calculations, uses PostgreSQL (Dockerized) for data storage, and employs spatial queries for toll plaza identification.</br></br>

## 🚀 How to Run the Project:

**📥 1. Clone the Repository**</br>
git clone https://github.com/Wantstocode/TollPlazaFinder.git</br>
cd TollPlazaFinder</br></br>
**🐳 2. Start PostgreSQL in Docker**</br>
docker-compose up -d</br></br>
**⚙️ 3. Build & Run the Project**</br>
./gradlew clean build  </br>
./gradlew bootRun</br>
The app will start at **http://localhost:8080** 🎉</br></br>
**📡 4. API Testing**</br>
Swagger UI →**http://localhost:8080/swagger-ui/index.html**</br>
Postman Example:</br>
curl -X 'POST' **'http://localhost:8080/api/v1/toll-plazas'** -H 'Content-Typ</br></br>


## Swagger UI - API Documentation
![Screenshot (101)](https://github.com/user-attachments/assets/e86294c0-7a63-48c4-b323-d68cb51acf7d)
<br><br>

## SonarQube - Code Quality & Test Coverage
![Screenshot (97)](https://github.com/user-attachments/assets/25937813-ce5f-4d7f-b0c6-7240b91e41c8)
</br></br>

## Docker PostgreSQL & PGAdmin - Database Management
![Screenshot (100)](https://github.com/user-attachments/assets/61cbd61e-944b-4607-956b-31106ec445f2)


