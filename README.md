# Researcher-Data-Integration
This repository contains the source code and documentation for a backend application that automates the integration of researcher profiles and published articles from Google Scholar into a university's research database.
## Project purpose
The main goal of this project is to automate the retrieval and integration of academic data from Google Scholar using the SerpApi, enabling efficient updates to institutional research databases without manual intervention
## Key functionalities
- Authenticate and perform GET requests to the Google Scholar Author API via SerpApi.
- Parse JSON responses into Java objects using Jackson.
- Implement MVC pattern with Spring Boot for handling API interactions, data processing, and views.
- Integrate fetched data (for 2 researchers and 3 articles each) into a PostgreSQL database, handling pagination and errors.
- Basic error handling for API limits, network issues, and database operations.
## Project relevance
This project solves the problem of manual data entry and tracking in academic institutions, which is time-consuming and prone to errors. It facilitates real-time access to publication metrics (e.g., citations, h-index), supporting faculty evaluations, research funding decisions, and institutional rankings improvements.
## Technologies Used
- **Language**: **Java**.
- **Framework**: **Spring Boot** (for MVC architecture and application setup).
- **JSON Handling**: **Jackson** library.
- **Database**: **PostgreSQL** (for storing article data with fields like id, title, authors, publication_date, abstract, link, keywords, cited_by).
- **HTTP Client**: Built-in Spring RestTemplate or similar for API calls.
- **Other**: Maven for dependency management.
# Setup Instructions
1. Clone the repository: git clone https://github.com/ckacy01/Researcher-Data-Integration.git
2. Configure PostgreSQL: Create a database and update application.properties with connection details (e.g., URL, username, password)
3. Add SerpApi key: Set serpapi.key in application.properties.
4. Build and run: Use mvn spring-boot:run or via IDE.
5. Test: Use endpoints like /author/{id} to fetch and store data.
# Documentation
- **Technical Report** on Google Scholar API: See docs/technical-report.pdf
