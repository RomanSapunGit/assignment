# RESTful API Implementation in Spring Boot

## Overview

This project implements a RESTful API using Spring Boot for managing user resources. The API follows best practices for RESTful design and includes error handling, unit tests, and JSON responses.

## Learning Resources

To understand RESTful API design and best practices for implementation, the following resources were used:

- [RESTful API Design: Best Practices in a Nutshell](https://restfulapi.net/)
- [Error Handling for REST with Spring | Baeldung](https://www.baeldung.com/rest-api-error-handling-best-practices)
- [Testing in Spring Boot | Baeldung](https://www.baeldung.com/spring-boot-testing)
- [Testing | Spring](https://spring.io/guides/gs/testing-web/)

## Requirements

### Fields of User Resource

1. **Email (required)** - Validated against email pattern
2. **First name (required)**
3. **Last name (required)**
4. **Birth date (required)** - Must be earlier than the current date
5. **Address (optional)**
6. **Phone number (optional)**

### Functionality

1. **Create User**: Allows registration of users who are more than 18 years old.
2. **Update User Fields**: Allows updating one or more user fields.
3. **Update All User Fields**: Allows updating all user fields.
4. **Delete User**: Removes a user from the system.
5. **Search Users by Birth Date Range**: Retrieves a list of users based on the specified birth date range. Validates that the "From" date is before the "To" date.

### Implementation Details

- Code is covered by unit tests using Spring.
- Error handling for REST is implemented.
- API responses are in JSON format.
- Database usage is not required; the data persistence layer is omitted.
- Any version of Spring Boot and Java can be used.
- Spring Initializr utility can be utilized to create the project.

## Assignment Implementation

This project fulfills the requirements specified in the assignment:

1. Implemented all required functionality for managing user resources.
2. Followed RESTful API design principles and best practices.
3. Included error handling for RESTful endpoints.
4. Covered code with unit tests using Spring testing framework.
5. Ensured API responses are in JSON format.
6. Used Database although it was not required.
7. Used Spring Boot for project setup and development.

## Usage

To run the project, follow these steps:

1. Clone the repository: `git clone <repository-url>`
2. Navigate to the project directory: `cd <project-directory>`
3. Create image for docker
4. Run docker compose file`docker-compose up` with environment variables:
-  DB_PASSWORD
-  DB_USERNAME

