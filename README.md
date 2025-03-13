# TollPlazaFinder
This project is a Spring Boot application designed to determine toll plazas between two Indian pincodes. It integrates external APIs for route calculations, uses PostgreSQL (Dockerized) for data storage, and employs spatial queries for toll plaza identification.

🚀 How to Run the Project:
📥 1. Clone the Repository
git clone https://github.com/Wantstocode/TollPlazaFinder.git
cd TollPlazaFinder
🐳 2. Start PostgreSQL in Docker
docker-compose up -d
⚙️ 3. Build & Run the Project
./gradlew clean build  
./gradlew bootRun
The app will start at http://localhost:8080 🎉
📡 4. API Testing
Swagger UI → http://localhost:8080/swagger-ui/index.html
Postman Example:
curl -X 'POST' 'http://localhost:8080/api/v1/toll-plazas' -H 'Content-Typ
