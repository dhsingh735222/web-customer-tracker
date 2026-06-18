# Deployment Guide

---

## 1. Local (Embedded Tomcat)

```bash
# Build
mvn clean package -DskipTests

# Run
mvn tomcat7:run
```

URL: http://localhost:8080/web-customer-tracker/customer/list

---

## 2. Local (External Tomcat)

```bash
# Build WAR
mvn clean package -DskipTests

# Copy to Tomcat
cp target/web-customer-tracker.war $CATALINA_HOME/webapps/

# Start Tomcat
$CATALINA_HOME/bin/startup.sh

# Stop Tomcat
$CATALINA_HOME/bin/shutdown.sh
```

---

## 3. Docker (Single Machine)

### Build & Run

```bash
# Build WAR first
mvn clean package -DskipTests

# Start all services (MySQL + Tomcat)
docker-compose up --build -d

# View logs
docker-compose logs -f

# Stop
docker-compose down

# Stop and remove volumes (wipes database!)
docker-compose down -v
```

URL: http://localhost:8080/web-customer-tracker/customer/list

### Rebuild after code changes

```bash
mvn clean package -DskipTests
docker-compose up --build -d
```

---

## 4. AWS EC2 Deployment

### Prerequisites
- EC2 instance (Ubuntu 22.04 LTS recommended, t2.micro is sufficient)
- Security group: inbound TCP 8080 and 22 open
- MySQL either on RDS or on the same instance

### Step-by-step

```bash
# SSH into instance
ssh -i your-key.pem ubuntu@<EC2_PUBLIC_IP>

# Install Java 11
sudo apt update
sudo apt install -y openjdk-11-jdk

# Install Tomcat 9
wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.80/bin/apache-tomcat-9.0.80.tar.gz
tar -xzf apache-tomcat-9.0.80.tar.gz
sudo mv apache-tomcat-9.0.80 /opt/tomcat9

# Install MySQL
sudo apt install -y mysql-server
sudo systemctl start mysql
sudo mysql < /path/to/sql/setup.sql

# Copy WAR (from your local machine)
scp -i your-key.pem target/web-customer-tracker.war \
    ubuntu@<EC2_PUBLIC_IP>:/opt/tomcat9/webapps/

# Start Tomcat
sudo /opt/tomcat9/bin/startup.sh
```

URL: http://\<EC2_PUBLIC_IP\>:8080/web-customer-tracker/customer/list

### Production tips
- Run Tomcat on port 80 via iptables redirect or put Nginx in front
- Use AWS RDS (MySQL) instead of a local MySQL instance
- Update `persistence.properties` with the RDS endpoint

---

## 5. Railway Deployment

Railway does not natively support WAR + Tomcat. Use the Docker approach:

```bash
# Push to GitHub first, then in Railway:
# 1. New project → Deploy from GitHub repo
# 2. Railway auto-detects Dockerfile
# 3. Add MySQL plugin in Railway dashboard
# 4. Set environment variables:
#    JDBC_URL, JDBC_USER, JDBC_PASSWORD
```

For Railway, convert `persistence.properties` to read from env vars:
```properties
jdbc.url=${JDBC_URL}
jdbc.user=${JDBC_USER}
jdbc.password=${JDBC_PASSWORD}
```

---

## 6. Render Deployment

```bash
# In render.com:
# 1. New Web Service → Connect GitHub repo
# 2. Runtime: Docker
# 3. Build Command: (empty — uses Dockerfile)
# 4. Add environment group with DB credentials
# 5. Add a Render managed PostgreSQL
#    (or external MySQL, adjust dialect accordingly)
```

---

## 7. VPS (Ubuntu) with Nginx Reverse Proxy

```bash
# Install Nginx
sudo apt install -y nginx

# Configure reverse proxy
sudo tee /etc/nginx/sites-available/crm << 'EOF'
server {
    listen 80;
    server_name your-domain.com;

    location /web-customer-tracker/ {
        proxy_pass http://localhost:8080/web-customer-tracker/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
EOF

sudo ln -s /etc/nginx/sites-available/crm /etc/nginx/sites-enabled/
sudo nginx -t && sudo systemctl reload nginx
```

URL: http://your-domain.com/web-customer-tracker/customer/list

---

## Environment Variables Reference

| Variable | Default | Description |
|---|---|---|
| `jdbc.driver` | `com.mysql.cj.jdbc.Driver` | JDBC driver class |
| `jdbc.url` | `jdbc:mysql://localhost:3306/...` | Full JDBC connection URL |
| `jdbc.user` | `springstudent` | DB username |
| `jdbc.password` | `springstudent` | DB password |
| `hibernate.dialect` | `MySQL8Dialect` | Hibernate SQL dialect |
| `hibernate.show_sql` | `true` | Log SQL (set false in prod) |
| `hibernate.hbm2ddl.auto` | `update` | DDL strategy |
