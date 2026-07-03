# 🧪 Postman Testing Guide — Employee Management API

> **Base URL:** `http://localhost:8080`  
> **Make sure the Spring Boot app is running before testing.**

---

## 📋 Table of Contents

1. [Setup Postman Environment](#1-setup-postman-environment)
2. [Auth — Login](#2-auth--login)
3. [Auto-Save JWT Token](#3-auto-save-jwt-token)
4. [Get All Employees](#4-get-all-employees)
5. [Create Department](#5-create-department)
6. [Create Employee](#6-create-employee)
7. [Update Employee](#7-update-employee)
8. [Delete Employee](#8-delete-employee)
9. [Error Cases to Test](#9-error-cases-to-test)
10. [Recommended Test Order](#10-recommended-test-order)

---

## 1. Setup Postman Environment

Save your JWT token as a variable so you never copy-paste it manually.

**Steps:**
1. Open Postman → click the **gear icon** (top-right) → **Manage Environments**
2. Click **Add** → name it `EmpManagement`
3. Add one variable:
   - **Variable:** `token`
   - **Initial Value:** *(leave blank)*
   - **Current Value:** *(leave blank)*
4. Click **Save**
5. Select `EmpManagement` from the environment dropdown (top-right corner)

---

## 2. Auth — Login

**This is the first request you must run.** It returns the JWT token required for all other endpoints.

| Field | Value |
|-------|-------|
| **Method** | `POST` |
| **URL** | `http://localhost:8080/auth/login` |
| **Auth** | None |

### Headers Tab
| Key | Value |
|-----|-------|
| `Content-Type` | `application/json` |

### Body Tab
Select **raw** → set format to **JSON** → paste:

```json
{
    "username": "admin",
    "password": "password123"
}
```

### ✅ Expected Response — `200 OK`
```json
{
    "accessToken": "eyJhbGciOiJIUzM4NCJ9......",
    "refreshToken": "daa096ab-7dea-4e9a-a908-d3e8460a818b"
}
```

---

## 3. Auto-Save JWT Token

Add a script to the **Login request** so the token is saved automatically.

1. In the Login request, go to the **Scripts** tab
2. Click **Post-response**
3. Paste this:

```javascript
var json = pm.response.json();
pm.environment.set("token", json.accessToken);
console.log("Token saved:", json.accessToken);
```

4. Send the Login request — token is now saved in `{{token}}`

### Using the token in other requests:
- Go to **Authorization** tab of any request
- Type: **Bearer Token**
- Token field: `{{token}}`

---

## 4. Get All Employees

Returns a list of all employees in the system.

| Field | Value |
|-------|-------|
| **Method** | `GET` |
| **URL** | `http://localhost:8080/api/employees` |

### Authorization Tab
| Type | Token |
|------|-------|
| Bearer Token | `{{token}}` |

### Body
*None — no body needed*

### ✅ Expected Response — `200 OK`
```json
[
    {
        "id": 1,
        "name": "Alice Johnson",
        "email": "alice@example.com"
    }
]
```

> Returns `[]` (empty array) if no employees exist yet. That is normal.

---

## 5. Create Department

**Create a department first** — employees require a valid `departmentId`.

| Field | Value |
|-------|-------|
| **Method** | `POST` |
| **URL** | `http://localhost:8080/api/admin/departments` |

### Authorization Tab
| Type | Token |
|------|-------|
| Bearer Token | `{{token}}` |

### Headers Tab
| Key | Value |
|-----|-------|
| `Content-Type` | `application/json` |

### Body Tab
Select **raw** → **JSON**:
```json
{
    "name": "Engineering"
}
```

Other examples you can try:
```json
{ "name": "HR" }
{ "name": "Finance" }
{ "name": "Marketing" }
```

### ✅ Expected Response — `200 OK`
```json
{
    "id": 1,
    "name": "Engineering",
    "employees": null
}
```

> 📝 **Note the `id` value** (e.g. `1`) from the response — you'll use it as `departmentId` when creating employees.

---

## 6. Create Employee

Creates a new employee and assigns them to a department.

| Field | Value |
|-------|-------|
| **Method** | `POST` |
| **URL** | `http://localhost:8080/api/admin/employees` |

### Authorization Tab
| Type | Token |
|------|-------|
| Bearer Token | `{{token}}` |

### Headers Tab
| Key | Value |
|-----|-------|
| `Content-Type` | `application/json` |

### Body Tab
Select **raw** → **JSON**:
```json
{
    "name": "Alice Johnson",
    "email": "alice@example.com",
    "departmentId": 1
}
```

> Replace `"departmentId": 1` with the actual ID returned from Step 5.

### ✅ Expected Response — `200 OK`
```json
{
    "id": 1,
    "name": "Alice Johnson",
    "email": "alice@example.com"
}
```

> 📝 **Note the `id`** (e.g. `1`) — you'll use it in the Update and Delete steps.

---

## 7. Update Employee

Updates an existing employee's details.

| Field | Value |
|-------|-------|
| **Method** | `PUT` |
| **URL** | `http://localhost:8080/api/admin/employees/1` |

> Replace `1` in the URL with the actual **employee ID** from Step 6.

### Authorization Tab
| Type | Token |
|------|-------|
| Bearer Token | `{{token}}` |

### Headers Tab
| Key | Value |
|-----|-------|
| `Content-Type` | `application/json` |

### Body Tab
Select **raw** → **JSON**:
```json
{
    "name": "Alice Smith",
    "email": "alice.smith@example.com",
    "departmentId": 1
}
```

### ✅ Expected Response — `200 OK`
```json
{
    "id": 1,
    "name": "Alice Smith",
    "email": "alice.smith@example.com"
}
```

---

## 8. Delete Employee

Deletes an employee by their ID.

| Field | Value |
|-------|-------|
| **Method** | `DELETE` |
| **URL** | `http://localhost:8080/api/admin/employees/1` |

> Replace `1` in the URL with the actual **employee ID** you want to delete.

### Authorization Tab
| Type | Token |
|------|-------|
| Bearer Token | `{{token}}` |

### Body
*None — no body needed*

### ✅ Expected Response — `204 No Content`
```
(empty response body)
```

---

## 9. Error Cases to Test

Test these to make sure the API handles bad input correctly.

---

### ❌ Wrong password on login

**Method:** `POST`  
**URL:** `http://localhost:8080/auth/login`  
**Body:**
```json
{
    "username": "admin",
    "password": "wrongpassword"
}
```

**Expected:**
```json
{ "error": "Bad credentials" }
```

---

### ❌ Request without token (no Authorization header)

**Method:** `GET`  
**URL:** `http://localhost:8080/api/employees`  
**Authorization:** `No Auth`

**Expected — `403 Forbidden`:**
```
(empty body)
```

---

### ❌ Create employee with non-existent department

**Method:** `POST`  
**URL:** `http://localhost:8080/api/admin/employees`  
**Body:**
```json
{
    "name": "Ghost User",
    "email": "ghost@example.com",
    "departmentId": 9999
}
```

**Expected — `404 Not Found`:**
```json
{ "error": "Department not found with id: 9999" }
```

---

### ❌ Update employee that doesn't exist

**Method:** `PUT`  
**URL:** `http://localhost:8080/api/admin/employees/9999`  
**Body:**
```json
{
    "name": "Nobody",
    "email": "nobody@example.com",
    "departmentId": 1
}
```

**Expected — `404 Not Found`:**
```json
{ "error": "Employee not found with id: 9999" }
```

---

### ❌ Delete employee that doesn't exist

**Method:** `DELETE`  
**URL:** `http://localhost:8080/api/admin/employees/9999`

**Expected — `404 Not Found`:**
```json
{ "error": "Employee not found with id: 9999" }
```

---

## 10. Recommended Test Order

Run requests in this exact order for a complete end-to-end test:

```
Step 1  →  POST  /auth/login                       Get JWT token
Step 2  →  GET   /api/employees                    Should return [] (empty)
Step 3  →  POST  /api/admin/departments             Create department (note id)
Step 4  →  POST  /api/admin/employees               Create employee (use dept id)
Step 5  →  GET   /api/employees                    Confirm employee is listed
Step 6  →  PUT   /api/admin/employees/{id}          Update employee details
Step 7  →  GET   /api/employees                    Confirm update worked
Step 8  →  DELETE /api/admin/employees/{id}         Delete the employee
Step 9  →  GET   /api/employees                    Confirm deletion (back to [])
```

---

## 🔑 Default Credentials

| Username | Password | Role | Access |
|----------|----------|------|--------|
| `admin` | `password123` | ADMIN | All routes including `/api/admin/**` |
| `user` | `user123` | USER | Read-only `/api/employees` only |

---

## 💡 Tips

- **Token expires** after 1 hour (`jwt.expiration=3600000` ms). Just re-run the Login request to get a new one.
- If you see `403` on an admin route, make sure your token is from the `admin` user, not `user`.
- If login returns a duplicate refresh token error, it means you logged in twice without the server restarting. Just send the login request one more time — it will work.
