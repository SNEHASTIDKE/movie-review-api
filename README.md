# Movie Review API

A full-stack movie review application built with Spring Boot, MySQL, and JWT authentication, with a plain HTML/CSS/JavaScript frontend.

## Features
- User registration and login with BCrypt password hashing
- JWT-based stateless authentication
- Role-based access: only admins can add/edit/delete movies
- Users can post, edit, and delete their own reviews
- Average movie rating recalculated automatically
- Pagination and search/filter on movie listings
- Swagger API documentation

## Tech Stack
- Java 8, Spring Boot 2.7
- Spring Data JPA, Spring Security
- MySQL
- JWT (jjwt)
- Vanilla HTML/CSS/JavaScript frontend

## Running locally

### Backend
1. Start MySQL (e.g. via XAMPP) and create a database named `moviedb`
2. Update `src/main/resources/application.properties` with your DB credentials if needed
3. Run `MovieReviewApplication.java`
4. API runs at `http://localhost:8080`
5. Swagger docs: `http://localhost:8080/swagger-ui.html`

### Frontend
Open `frontend/movie-review-frontend.html` directly in your browser while the backend is running.

## API Overview

| Method | Endpoint | Access |
|---|---|---|
| POST | /api/users/register | Public |
| POST | /api/users/login | Public |
| GET | /api/movies | Public |
| POST | /api/movies | Admin only |
| PUT /DELETE | /api/movies/{id} | Admin only |
| GET | /api/movies/{id}/reviews | Public |
| POST | /api/movies/{id}/reviews | Authenticated |
| PUT /DELETE | /api/reviews/{id} | Owner only |
