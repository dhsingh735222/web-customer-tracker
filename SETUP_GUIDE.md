# Setup Guide

Complete step-by-step guide to get the CRM application running on your local machine.

---

## Prerequisites

| Tool | Version | Download |
|---|---|---|
| Java JDK | 11+ | https://adoptium.net |
| Apache Maven | 3.6+ | https://maven.apache.org |
| MySQL | 8.0 | https://dev.mysql.com/downloads |
| Apache Tomcat | 9.x (optional) | https://tomcat.apache.org |
| Git | Any | https://git-scm.com |

Verify your installations:
```bash
java -version        # should show 11+
mvn -version         # should show 3.6+
mysql --version      # should show 8.x
```

---

## Step 1 — Clone the Repository

```bash
git clone https://github.com/dhsingh735222/web-customer-tracker.git
cd web-customer-tracker
```

---

## Step 2 — Set Up MySQL Database

Start MySQL and run the setup script:

```bash
# Connect as root
mysql -u root -p

# Inside MySQL shell:
source /full/path/to/web-customer-tracker/sql/setup.sql
```

This creates:
- Database: `web_customer_tracker`
- User: `springstudent` / `springstudent`
- Table: `customer`
- 5 sample rows

**Verify:**
```sql
USE web_customer_tracker;
SELECT * FROM customer;
```

---

## Step 3 — Configure Database Connection

Edit `src/main/resources/persistence.properties`:

```properties
jdbc.url=jdbc:mysql://localhost:3306/web_customer_tracker?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
jdbc.user=springstudent
jdbc.password=springstudent
```

Change these values if your MySQL setup differs.

---

## Step 4 — Build the Project

```bash
mvn clean package -DskipTests
```

A successful build produces: `target/web-customer-tracker.war`

---

## Step 5A — Run with Embedded Tomcat (Recommended)

```bash
mvn package cargo:run
```

Open: **http://localhost:8090/web-customer-tracker/customer/list**

---

## Step 5B — Deploy to External Tomcat

1. Copy the WAR to Tomcat's webapps folder:
   ```bash
   cp target/web-customer-tracker.war /path/to/tomcat/webapps/
   ```

2. Start Tomcat:
   ```bash
   /path/to/tomcat/bin/startup.sh      # macOS/Linux
   /path/to/tomcat/bin/startup.bat     # Windows
   ```

3. Open: **http://localhost:8080/web-customer-tracker/customer/list**

---

## Step 6 — Run Tests

```bash
mvn test
```

---

## Troubleshooting

### Cannot connect to MySQL

Check MySQL is running:
```bash
mysql -u springstudent -pspringstudent -e "SELECT 1"
```

If it fails:
```bash
# macOS
brew services start mysql

# Linux
sudo systemctl start mysql
```

### Port 8080 already in use

Either stop the conflicting process:
```bash
lsof -i :8080        # find the PID
kill -9 <PID>
```

Or change the port in `pom.xml`:
```xml
<configuration>
    <port>9090</port>
</configuration>
```

### ClassNotFoundException for MySQL driver

Make sure `mysql-connector-java` is in your `pom.xml` and run:
```bash
mvn dependency:resolve
```

### Hibernate creates no tables

Set `hibernate.hbm2ddl.auto=create` on first run in `persistence.properties`, then switch back to `update`.

---

## Environment Variables (Optional)

You can override `persistence.properties` via JVM system properties:

```bash
mvn package cargo:run \
  -Djdbc.url="jdbc:mysql://localhost:3306/web_customer_tracker?useSSL=false" \
  -Djdbc.user=springstudent \
  -Djdbc.password=springstudent
```
