# **Yalli - Connecting Azerbaijani Communities Worldwide** 🌍
Overview
Yalli is a community platform designed to connect Azerbaijanis living abroad with each other and provide mentorship opportunities for those in Azerbaijan looking to study or live abroad. The platform facilitates community building, knowledge sharing, and mentorship connections through a robust backend API.
Key Features

## Advanced Search & Filtering System

Custom search functionality with multiple attribute filtering
Customizable search parameters for finding community members and mentors
Efficient query optimization for fast search results


## Review & Rating System

Comprehensive API for submitting and managing user reviews
Rating system for community members and mentors
Paginated comment system for improved performance
Structured feedback collection and display


## Post Management

Secure endpoints for post creation and modification
Bookmarking functionality for saving relevant content
Efficient post retrieval and pagination
Content moderation capabilities


## Mentorship Application System

Structured application process for mentorship programs
Built-in verification system for mentors
Status tracking for applications
Automated notification system



## Technical Stack 🛠️

Backend Framework

* Java Spring Boot
* RESTful API architecture
* MinIO File Storage


## Database

PostgreSQL for reliable data storage
Optimized database schema for community platform needs


## Documentation

Swagger UI for comprehensive API documentation
Detailed endpoint specifications and usage examples


## Version Control

Git for source code management
Collaborative development workflow



## Getting Started 🚀
### Prerequisites

Java JDK 11 or higher
PostgreSQL 12 or higher
Gradle 7.0 or higher

### Installation

Clone the repository

git clone https://github.com/fuadmahmudzada/yalli.git

Configure database properties in application.properties

spring.datasource.url=jdbc:postgresql://localhost:5432/yalli
spring.datasource.username=your_username
spring.datasource.password=your_password

### Build the project

./gradlew build

Run the application

./gradlew bootRun
## API Documentation
Access the Swagger UI documentation at:
http://localhost:8080/swagger-ui.html
## Contributing

Fork the repository
Create your feature branch (git checkout -b feature/AmazingFeature)
Commit your changes (git commit -m 'Add some AmazingFeature')
Push to the branch (git push origin feature/AmazingFeature)
Open a Pull Request
