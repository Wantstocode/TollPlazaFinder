## TollPlazaFinder

This project is a Spring Boot application designed to determine toll plazas between two Indian pincodes. It integrates external APIs for route calculations, uses PostgreSQL (Dockerized) for data storage, and employs spatial queries for toll plaza identification.</br></br>

## 🚀 How to Run the Project:

**📥 1. Clone the Repository**</br>
git clone https://github.com/Wantstocode/TollPlazaFinder.git</br>
cd TollPlazaFinder</br></br></br>
**🐳 2. Start PostgreSQL in Docker**</br>
docker-compose up -d</br></br></br>
**⚙️ 3. Build & Run the Project**</br>
./gradlew clean build  </br>
./gradlew bootRun</br></br>
The app will start at **http://localhost:8080** 🎉</br></br></br>
**📡 4. API Testing**</br>
Swagger UI →**http://localhost:8080/swagger-ui/index.html**</br>
Postman Example:</br>
curl -X 'POST' **'http://localhost:8080/api/v1/toll-plazas'** -H 'Content-Typ</br>
