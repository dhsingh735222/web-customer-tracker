# CRM — Customer Relationship Manager

A full-stack CRUD web application built with **Spring MVC 5**, **Hibernate 5**, and **MySQL 8**.  
Managers can create, read, update, and delete customer records through a clean browser-based UI.

---

## Features

| Feature | Details |
|---|---|
| List customers | Paginated table sorted by last name |
| Add customer | Form with server-side validation |
| Update customer | Pre-populated form with existing data |
| Delete customer | JS confirmation dialog before deletion |
| Search customers | Full-text search on name and email |
| Flash messages | Success notifications after CUD operations |
| Responsive UI | Works on desktop and mobile |

---

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 11 |
| Framework | Spring MVC 5.3 |
| ORM | Hibernate 5.6 |
| Database | MySQL 8.0 |
| Connection Pool | C3P0 |
| View | JSP + JSTL |
| Styling | CSS3 |
| Build | Apache Maven 3.9 |
| Container | Apache Tomcat 9 |
| Tests | JUnit 4 + Mockito |
| CI/CD | GitHub Actions |
| Docker | Docker + Docker Compose |

---

## Quick Start (Local)

### Prerequisites
- Java 11+
- Maven 3.6+
- MySQL 8.0
- Apache Tomcat 9 (or use the embedded Tomcat Maven plugin)

```bash
# 1. Clone the repository
git clone https://github.com/YOUR_USERNAME/web-customer-tracker.git
cd web-customer-tracker

# 2. Set up the database
mysql -u root -p < sql/setup.sql

# 3. Build
mvn clean package -DskipTests

# 4. Run (embedded Tomcat)
mvn tomcat7:run
```

Open: http://localhost:8080/web-customer-tracker/customer/list

---

## Quick Start (Docker)

```bash
# Build WAR first
mvn clean package -DskipTests

# Start MySQL + Tomcat containers
docker-compose up --build
```

Open: http://localhost:8080/web-customer-tracker/customer/list

---

## Project Structure

```
web-customer-tracker/
├── src/main/java/com/crm/springmvc/
│   ├── controller/    # Spring MVC controllers
│   ├── dao/           # Data Access layer (Hibernate)
│   ├── entity/        # JPA entity classes
│   └── service/       # Business logic layer
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── jsp/       # JSP view templates
│   │   ├── web.xml
│   │   └── spring-mvc-crud-demo-servlet.xml
│   └── resources/css/ # Stylesheet
├── src/main/resources/
│   └── persistence.properties  # DB connection config
├── src/test/          # Unit & integration tests
├── sql/               # Database scripts
├── docker/            # Docker-specific configs
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## Documentation

| Document | Description |
|---|---|
| [SETUP_GUIDE.md](SETUP_GUIDE.md) | Step-by-step local setup |
| [ARCHITECTURE.md](ARCHITECTURE.md) | System design & MVC flow |
| [API_DOCUMENTATION.md](API_DOCUMENTATION.md) | All HTTP endpoints |
| [DATABASE_GUIDE.md](DATABASE_GUIDE.md) | Schema, queries, migrations |
| [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) | Local, Docker, AWS, Railway |
| [TESTING_GUIDE.md](TESTING_GUIDE.md) | Running and writing tests |
| [GITHUB_WORKFLOW.md](GITHUB_WORKFLOW.md) | Git workflow & CI/CD |

---

## License

MIT — free to use for learning and commercial projects.
