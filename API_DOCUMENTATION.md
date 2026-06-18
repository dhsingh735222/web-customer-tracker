# API Documentation

All endpoints are served under the context path `/web-customer-tracker`.

Base URL: `http://localhost:8080/web-customer-tracker`

---

## Endpoints

### 1. List All Customers

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/customer/list` |
| **Description** | Returns the customer list page |
| **Query Params** | None |
| **Success** | HTTP 200 — renders `list-customers.jsp` |
| **Model** | `customers` — `List<Customer>` sorted by last name |

**Example:**
```
GET http://localhost:8080/web-customer-tracker/customer/list
```

---

### 2. Show Add Customer Form

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/customer/showFormForAdd` |
| **Description** | Renders an empty customer form for creating a new record |
| **Query Params** | None |
| **Success** | HTTP 200 — renders `customer-form.jsp` |
| **Model** | `customer` — empty `Customer` object |

**Example:**
```
GET http://localhost:8080/web-customer-tracker/customer/showFormForAdd
```

---

### 3. Save Customer (Create or Update)

| | |
|---|---|
| **Method** | `POST` |
| **URL** | `/customer/saveCustomer` |
| **Description** | Saves a new customer or updates an existing one. Determined by the `id` field: `id=0` → insert, `id>0` → update |
| **Content-Type** | `application/x-www-form-urlencoded` |
| **Success** | HTTP 302 — redirect to `/customer/list` with flash message |
| **Validation Error** | HTTP 200 — re-renders `customer-form.jsp` with error messages |

**Form Fields:**

| Field | Type | Required | Validation |
|---|---|---|---|
| `id` | hidden | yes | — |
| `firstName` | text | yes | 1–50 chars, not blank |
| `lastName` | text | yes | 1–50 chars, not blank |
| `email` | email | yes | Valid email format |

**Example cURL:**
```bash
curl -X POST http://localhost:8080/web-customer-tracker/customer/saveCustomer \
  -d "id=0&firstName=New&lastName=User&email=new@gmail.com"
```

---

### 4. Show Update Customer Form

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/customer/showFormForUpdate` |
| **Description** | Renders the customer form pre-populated with the specified customer's data |
| **Query Params** | `customerId` (int, required) |
| **Success** | HTTP 200 — renders `customer-form.jsp` |
| **Not Found** | HTTP 302 — redirect to `/customer/list` |
| **Model** | `customer` — existing `Customer` object |

**Example:**
```
GET http://localhost:8080/web-customer-tracker/customer/showFormForUpdate?customerId=1
```

---

### 5. Delete Customer

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/customer/delete` |
| **Description** | Permanently deletes the customer with the given ID |
| **Query Params** | `customerId` (int, required) |
| **Success** | HTTP 302 — redirect to `/customer/list` with flash message |
| **Not Found** | HTTP 302 — redirect to `/customer/list` (no-op) |

**Example:**
```
GET http://localhost:8080/web-customer-tracker/customer/delete?customerId=1
```

---

### 6. Search Customers

| | |
|---|---|
| **Method** | `GET` |
| **URL** | `/customer/search` |
| **Description** | Searches for customers by first name, last name, or email (case-insensitive) |
| **Query Params** | `searchName` (string, optional) |
| **Success** | HTTP 200 — renders `list-customers.jsp` with filtered results |
| **Model** | `customers` — filtered `List<Customer>`, `searchName` — echoed search term |

**Example:**
```
GET http://localhost:8080/web-customer-tracker/customer/search?searchName=john
```

---

## URL Summary

| Method | URL | Action |
|---|---|---|
| GET | `/customer/list` | List all customers |
| GET | `/customer/showFormForAdd` | Show add form |
| POST | `/customer/saveCustomer` | Create or update customer |
| GET | `/customer/showFormForUpdate?customerId={id}` | Show update form |
| GET | `/customer/delete?customerId={id}` | Delete customer |
| GET | `/customer/search?searchName={query}` | Search customers |

---

## Customer Object Model

```json
{
  "id":        1,
  "firstName": "John",
  "lastName":  "Adams",
  "email":     "john@gmail.com"
}
```

| Field | Type | Constraints |
|---|---|---|
| `id` | int | Auto-generated, unique |
| `firstName` | String | Not blank, 1–50 chars |
| `lastName` | String | Not blank, 1–50 chars |
| `email` | String | Not blank, valid email |

---

## HTTP Status Codes Used

| Code | Meaning |
|---|---|
| 200 | Page rendered successfully |
| 302 | Redirect after POST/DELETE |
| 404 | Page not found |
| 500 | Server error |
