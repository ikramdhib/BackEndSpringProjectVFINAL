# University Internship Management System

## Overview

This project is a Spring Boot application designed to manage internships for a university. It provides a secure backend API that handles student, teacher, and company data, internship applications, approvals, and status tracking. The application uses Spring Security and JWT (JSON Web Token) to secure the API endpoints.

## Features

- **User Management**: Handles roles and permissions for students, teachers, and administrators.
- **Internship Management**: CRUD operations for internships, including application, approval, and status tracking.
- **JWT Security**: Secures API endpoints using JWT tokens.
- **Role-Based Access Control (RBAC)**: Provides different levels of access based on user roles (Student, Teacher, Admin).
- **RESTful API**: Fully RESTful API for managing resources.

## Technologies Used

- **Spring Boot**: Framework for building the application.
- **Spring Security**: Handles authentication and authorization.
- **JWT (JSON Web Token)**: Secures the API by issuing and validating tokens.
- **Hibernate/JPA**: ORM framework for database interaction.
- **MongoDBL**: NOsql database to store application data.
- **Maven**: Dependency management and build tool.
- **Lombok**: Reduces boilerplate code.

## Getting Started

### Prerequisites

- **Java 17**: Ensure you have JDK 17 installed.
- **Maven**: Ensure Maven is installed and configured.
- **MongoDB**: Set up a Nosql database.
- **Postman** (optional): For testing the API endpoints.

### Installation

1. **Clone the repository**:
   ```bash
   git clone  https://github.com/ikramdhib/BackEndSpringProjectVFINAL.git
   cd PI-SAE-
2. **Set up the database**:
   spring.data.mongodb.uri=mongodb+srv://mangodb785:*****@cluster0.gc5omlq.mongodb.net/?retryWrites=true&w=majority
   spring.data.mongodb.database=Stage
3. **Build the project**:
   ```bash
   mvn clean install
5. **Run the application**:
   ```bash
   mvn spring-boot:run
6. **Access the API**:
   The application will be running at http://localhost:8081.
    Use Postman or a similar tool to interact with the API.
### Contributing
Contributions are welcome! Please follow the guidelines below:

Fork the repository.
Create a new branch (git checkout -b feature/new-feature).
Commit your changes (git commit -m 'Add new feature').
Push to the branch (git push origin feature/new-feature).
Open a pull request.

### License

This project is licensed under the MIT License - see the LICENSE file for details.

### Contact

For any inquiries, please contact the project maintainers at dhibikram50@gmail.com

### Instructions

-The database configuration section has now been adapted for MongoDB.
-The spring.data.mongodb.uri property is used to specify the connection URI for MongoDB.
-Ensure that MongoDB is running on your machine or on an accessible remote server.

