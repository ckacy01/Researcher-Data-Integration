# Researcher Data Integration

A Spring Boot backend application that automates the integration of researcher profiles and published articles from Google Scholar into a university's research database.

## Overview

This project solves the problem of manual data entry and tracking in academic institutions, which is time-consuming and prone to errors. It automates the retrieval and integration of academic data from Google Scholar using SerpAPI, enabling efficient updates to institutional research databases without manual intervention.

The system facilitates real-time access to publication metrics (e.g., citations, h-index), supporting:
- Faculty evaluations
- Research funding decisions
- Institutional rankings improvements

## Features

- **Author Profile Integration**: Retrieve researcher profiles from Google Scholar
- **Article Importation**: Automated import of published articles with metadata
- **Batch Processing**: Import articles for multiple researchers simultaneously
- **Duplicate Prevention**: Smart checking to avoid duplicate entries
- **Citation Tracking**: Automatic extraction of citation counts
- **RESTful API**: Clean REST endpoints for easy integration
- **Error Handling**: Comprehensive exception handling for API limits, network issues, and database operations
- **Transaction Management**: Safe database operations with Spring transactions

## Technologies

- **Language**: Java
- **Framework**: Spring Boot 
- **Database**: PostgreSQL with JPA/Hibernate
- **Connection Pool**: HikariCP
- **JSON Parsing**: Jackson
- **HTTP Client**: Spring RestTemplate
- **Build Tool**: Maven
- **External API**: [SerpAPI](https://serpapi.com/) (Google Scholar integration)

## Prerequisites

Before you begin, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- SerpAPI account and API key ([Get one here](https://serpapi.com/))

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/ckacy01/Researcher-Data-Integration.git
cd Researcher-Data-Integration
git checkout sprint3
```

### 2. Configure PostgreSQL

Create a PostgreSQL database:

```sql
CREATE DATABASE researcher_db;
```

### 3. Configure Application Properties

Update `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/researcher_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# SerpAPI Configuration
serpapi.key=your_serpapi_key_here
serpapi.base-url=https://serpapi.com/search
```

### 4. Build and Run

Using Maven:

```bash
mvn clean install
mvn spring-boot:run
```

Or using your IDE:
- Import as Maven project
- Run the main application class

The application will start on `http://localhost:8080`

## API Endpoints

### Get All Articles

Retrieves all articles stored in the database.

```http
GET http://localhost:8080/api/article/articles
```

**Response**: `200 OK`

```json
[
  {
    "id": "abc123",
    "title": "Deep Learning for Computer Vision",
    "authors": "Andrew Ng, Geoffrey Hinton",
    "publicationDate": "2020-01-01",
    "abstractText": "This paper presents...",
    "link": "https://scholar.google.com/...",
    "keywords": "deep, learning, vision",
    "citedBy": 1250,
    "researcherName": "Andrew Ng"
  }
]
```

### Import Articles

Imports articles for specified researchers from Google Scholar.

```http
POST http://localhost:8080/api/article/import
Content-Type: application/json
```

**Request Body**:

```json
{
  "researchers": ["Andrew Ng", "Geoffrey Hinton"],
  "articlesPerResearcher": 3
}
```

**Response**: `201 CREATED`

```json
{
  "status": "success",
  "message": "Articles imported successfully",
  "totalResearchers": 2,
  "articlesPerResearcher": 3,
  "totalArticlesImported": 6,
  "articles": [...]
}
```

### Get Author Profile

Retrieves author information from Google Scholar.

```http
GET http://localhost:8080/api/author/{authorId}
```

**Response**: `200 OK`

```json
{
  "name": "Andrew Ng",
  "affiliations": "Stanford University",
  "email": "ang@cs.stanford.edu",
  "citedBy": 150000,
  "hIndex": 120
}
```

## Testing with cURL

### Import Articles Example

```bash
curl -X POST http://localhost:8080/api/article/import \
  -H "Content-Type: application/json" \
  -d '{
    "researchers": ["Andrew Ng", "Yann LeCun"],
    "articlesPerResearcher": 5
  }'
```

### Get All Articles Example

```bash
curl -X GET http://localhost:8080/api/article/articles
```

## Database Schema

### Articles Table

| Column           | Type         | Description                          |
|------------------|--------------|--------------------------------------|
| id               | VARCHAR(255) | Primary Key (SerpAPI result ID)      |
| title            | VARCHAR(500) | Article title                        |
| authors          | TEXT         | Comma-separated list of authors      |
| publication_date | DATE         | Publication date                     |
| abstract_text    | TEXT         | Article abstract or snippet          |
| link             | VARCHAR(500) | URL to the article                   |
| keywords         | VARCHAR(255) | Extracted keywords                   |
| cited_by         | INTEGER      | Number of citations                  |
| researcher_name  | VARCHAR(255) | Associated researcher name           |

## 🏗Project Structure

```
src/main/java/org/technoready/researcherdataintegration/
├── config/
│   ├── DatabaseConfiguration.java
│   └── ScholarApiConfiguration.java
├── controller/
│   └── ArticleController.java
├── dto/
│   ├── ArticleResponseDTO.java
│   ├── ImportRequestDTO.java
│   └── ImportResponseDTO.java
├── entity/
│   ├── Article.java
│   └── Author.java
├── exception/
│   ├── ApiKeyException.java
│   ├── AuthorNotFoundException.java
│   ├── DatabaseException.java
│   ├── ExternalApiException.java
│   └── ValidationException.java
├── repository/
│   └── ArticleRepository.java
└── service/
    ├── ArticleService.java
    └── AuthorService.java
```

## Error Handling

The application includes comprehensive error handling:

- **ApiKeyException**: Invalid or missing SerpAPI key
- **AuthorNotFoundException**: Author not found in Google Scholar
- **DatabaseException**: Database operation failures
- **ExternalApiException**: SerpAPI communication errors
- **ValidationException**: Invalid input parameters

All exceptions are handled globally and return appropriate HTTP status codes with error messages.

## Validation Rules

- Researchers list cannot be empty
- Articles per researcher must be between 1 and 20
- Researcher names cannot be null or empty
- API key must be configured before making requests

## Logging

The application uses SLF4J with Logback for comprehensive logging:

- INFO: General operation flow
- DEBUG: Detailed API calls and responses
- WARN: Non-critical issues (duplicates, missing data)
- ERROR: Critical failures with stack traces

## Documentation

For more detailed information, see:
- [Technical Report](docs/technical-report.pdf) - Comprehensive documentation on Google Scholar API integration

## Authors

**Jorge Armando Avila Carrillo** | NAOID: 3310

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- [SerpAPI](https://serpapi.com/) for providing the Google Scholar API integration
- Spring Boot community for excellent documentation
- PostgreSQL team for the robust database system


---

**Note**: Remember to keep your SerpAPI key secure and never commit it to version control. Use environment variables or secure configuration management in production environments.
