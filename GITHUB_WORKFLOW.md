# GitHub Workflow Guide

---

## Initial Setup

```bash
cd /path/to/web-customer-tracker

# Initialise Git
git init

# Add all files
git add .

# First commit
git commit -m "feat: initial CRM application — Spring MVC + Hibernate + MySQL"

# Create GitHub repo (requires GitHub CLI)
gh repo create web-customer-tracker --public --source=. --remote=origin

# Push to GitHub
git push -u origin main
```

---

## Branch Strategy

```
main        ← stable, production-ready
develop     ← integration branch
feature/*   ← new features (e.g. feature/search)
fix/*       ← bug fixes
```

### Create a feature branch

```bash
git checkout -b feature/customer-search
# ... make changes ...
git add src/main/java/com/crm/springmvc/dao/CustomerDAOImpl.java
git commit -m "feat: add customer search by name and email"
git push -u origin feature/customer-search

# Open a pull request
gh pr create --title "Add customer search" --body "Closes #12"
```

---

## Commit Message Convention

Format: `<type>: <short description>`

| Type | Use for |
|---|---|
| `feat` | New feature |
| `fix` | Bug fix |
| `refactor` | Code restructure without behaviour change |
| `test` | Adding or updating tests |
| `docs` | Documentation changes |
| `chore` | Build, CI, dependency updates |
| `style` | CSS/formatting changes |

Examples:
```
feat: add delete confirmation dialog
fix: handle null customer in showFormForUpdate
test: add CustomerDAO mock tests
docs: update SETUP_GUIDE with Docker instructions
chore: upgrade Hibernate to 5.6.15
```

---

## CI/CD Pipeline (GitHub Actions)

The pipeline at `.github/workflows/ci-cd.yml` runs on every push to `main` or `develop`.

### Jobs

| Job | Trigger | Steps |
|---|---|---|
| `build-and-test` | push / PR | Checkout → JDK 11 → MySQL service → Compile → Test → Package |
| `docker-build` | push to main | Build Docker image → push to Docker Hub (if secrets set) |
| `code-quality` | after build | Generate Surefire HTML report |

### Setting up Docker Hub secrets (optional)

In your GitHub repo: **Settings → Secrets → Actions**

| Secret | Value |
|---|---|
| `DOCKERHUB_USERNAME` | Your Docker Hub username |
| `DOCKERHUB_TOKEN` | Docker Hub access token |

---

## Useful GitHub CLI Commands

```bash
# List open pull requests
gh pr list

# Check PR status / CI
gh pr checks

# Merge a pull request
gh pr merge --squash

# Create a release
gh release create v1.0.0 target/web-customer-tracker.war \
  --title "v1.0.0 — Initial release" \
  --notes "Full CRUD CRM application"

# View issues
gh issue list

# Create an issue
gh issue create --title "Add pagination" --body "List page needs pagination for large datasets"
```

---

## Tagging a Release

```bash
git tag -a v1.0.0 -m "Release v1.0.0 — initial CRM application"
git push origin v1.0.0
```

---

## Pull Request Checklist

Before merging:
- [ ] Code compiles: `mvn clean compile`
- [ ] All tests pass: `mvn test`
- [ ] No `.env` or credential files staged
- [ ] Feature branch is up to date with `develop`
- [ ] `persistence.properties` uses placeholders, not hardcoded prod credentials
