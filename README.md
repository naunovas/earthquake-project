# Earthquake Monitoring System

This project is a simple web application for fetching, filtering and displaying recent earthquake data.

## Technologies Used

Backend:
- Java
- Spring Boot
- PostgreSQL

Frontend:
- React (JavaScript)
- Bootstrap

## Features

- Fetch latest earthquake data from USGS API
- Filter earthquakes with magnitude greater than 2.0
- Filter earthquakes after a selected time
- Store data in database
- Delete existing records before inserting new ones
- Display data in table format
- Delete individual records

## How to run

### Backend

1. Configure PostgreSQL in `application.properties`
2. Run the Spring Boot application

Server runs on:  
http://localhost:8081

### Frontend

1. Open terminal in `frontend` folder  
2. Run:
```
npm install
npm run dev
```
App runs on:  
http://localhost:5173

## Database Configuration

- PostgreSQL is used as the main database
- Create a database named `earthquake_db`
- Update the following properties in `application.properties`:
```
spring.datasource.url=jdbc:postgresql://localhost:5433/earthquake_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

- The application uses Hibernate with `ddl-auto=update` to manage schema

## Assumptions

- Only earthquakes with magnitude greater than 2.0 are stored
- External API data may change between requests
- Database is cleared before inserting new data to avoid duplicates



## Notes
- Data is fetched from USGS public API

