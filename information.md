# 📘 Information Guide — Employee Management System

---

## 📋 Table of Contents

- [Part 1 — Project Overview](#part-1--project-overview)
- [Part 2 — Every File Explained](#part-2--every-file-explained)
- [Part 3 — 10 Scenarios: File-to-File Flow](#part-3--10-scenarios-file-to-file-flow)
- [Part 4 — 10 Scenarios: Code Flow](#part-4--10-scenarios-code-flow)

---

# PART 1 — Project Overview

## What Is This Project?

This is a **REST API backend** for managing employees inside a company. It allows an admin to create, update, and delete employees and departments, while regular users can only view employees. Everything is protected by **JWT-based authentication** — you must log in first to get a token, and then send that token with every request.

---

## What Problem It Solves

Without this system, managing employee records would require direct database access. This API provides a clean, secure, HTTP-based interface that any frontend (React, Angular, mobile app, Postman) can talk to.

---

## Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 26 | Programming language |
| **Spring Boot** | 3.2.5 | Main framework — makes building REST APIs easy |
| **Spring Security** | (included) | Handles authentication, authorization, JWT filter |
| **Spring Data JPA** | (included) | Talks to the database using Java objects instead of raw SQL |
| **Hibernate** | 6.4.x | ORM — maps Java classes to database tables automatically |
| **MySQL** | 8.x | Relational database where all data is stored |
| **JWT (jjwt)** | 0.12.5 | Creates and validates JSON Web Tokens for secure login |
| **Lombok** | 1.18.38 | Removes boilerplate code — auto-generates getters, setters, builders |
| **Maven** | (wrapper) | Build tool — compiles, packages, and runs the project |
| **BCrypt** | (included) | Hashes passwords before storing in database |

---

## Project Structure at a Glance

```
src/main/java/com/demo/empmanagement/
│
├── Application.java             ← Entry point
├── config/
│   └── DataInitializer1.java    ← Seeds default users and departments
├── controllers/
│   ├── AuthController.java      ← Login endpoint
│   ├── EmployeeController.java  ← Read employees (any logged-in user)
│   ├── AdminEmployeeController.java ← Create/Update/Delete employees (admin)
│   └── DepartmentController.java    ← Create departments (admin)
├── dto/
│   ├── AuthRequest.java         ← Login input shape
│   ├── AuthResponse.java        ← Login output shape (tokens)
│   └── EmployeeDto.java         ← Employee input shape
├── exceptions/
│   ├── ResourceNotFoundException.java    ← Custom 404 error
│   └── GlobalExceptionController.java    ← Catches all errors, formats JSON response
├── models/
│   ├── User.java                ← User table (admins and users)
│   ├── Employee.java            ← Employee table
│   ├── Department.java          ← Department table
│   └── RefreshToken.java        ← Refresh token table
├── repository/
│   ├── UserRepository.java      ← DB queries for User
│   ├── EmployeeRepository.java  ← DB queries for Employee
│   ├── DepartmentRepository.java← DB queries for Department
│   └── RefreshTokenRepository.java ← DB queries for RefreshToken
├── security/
│   ├── JwtService.java          ← Creates and validates JWT tokens
│   ├── JwtAuthenticationFilter.java ← Intercepts every request, checks JWT
│   └── SecurityConfig.java      ← Defines which routes are public vs protected
└── services/
    ├── AuthService.java         ← Login logic
    ├── EmployeeService.java     ← Employee business logic
    ├── RefreshTokenService.java ← Refresh token logic
    └── CustomUserDetailsService.java ← Loads user from DB for Spring Security
```

---

---

# PART 2 — Every File Explained

## Application.java
**Why it exists:** This is the starting point of the entire application. 
When you run the project, Java looks for a `main()` method — this file has it. 
The `@SpringBootApplication` annotation tells Spring to scan all files in the project, 
set up the database connection, start the web server, 
and get everything ready automatically.

---

## config/DataInitializer1.java
**Why it exists:** When the application starts fresh with an empty database, 
there are no users — meaning nobody can log in. 
This file runs automatically on startup and checks if the `users` table is empty. 
If it is, it creates two default accounts (`admin` with ADMIN role, `user` with USER 
role) and three departments (HR, IT, Finance). 
Without this file, you would have to manually insert users into 
the database every time.

---

## controllers/AuthController.java
**Why it exists:** This file handles the `/auth/login` endpoint. 
A controller is the "door" of the application — 
it receives HTTP requests from the outside world. 
This specific controller's only job is to accept a username and password, 
hand it off to `AuthService`, and return the JWT token back to the caller.

---

## controllers/EmployeeController.java
**Why it exists:** This file handles `GET /api/employees`. 
Any authenticated user (admin or regular user) can call this to get the full list 
of employees. It is kept separate from the admin controller because it 
has different access rules — you only need to be logged in, not necessarily an admin.

---

## controllers/AdminEmployeeController.java
**Why it exists:** This file handles `POST`, `PUT`, and `DELETE` on 
`/api/admin/employees`. Only admins can access these routes 
(enforced by `SecurityConfig`). The operations here modify data, 
so they are locked behind admin-only access to prevent regular users from 
creating or deleting employee records.

---

## controllers/DepartmentController.java
**Why it exists:** This file handles `POST /api/admin/departments`. 
It lets admins create departments. 
Departments are needed before employees can be created, 
because every employee must belong to a department. 
This is a lightweight controller that directly uses 
`DepartmentRepository` without a service layer.

---

## dto/AuthRequest.java
**Why it exists:** DTO stands for Data Transfer Object. 
This file defines the exact shape of the JSON body the client must send 
when logging in — it must have `username` and `password` fields. 
Without this class, Spring wouldn't know how to read the incoming JSON into a 
Java object.

---

## dto/AuthResponse.java
**Why it exists:** This defines what the server sends back after a successful login
— specifically `accessToken` and `refreshToken`. 
It separates the internal `User` model from what the client actually 
needs to receive, keeping the API clean and secure.

---

## dto/EmployeeDto.java
**Why it exists:** When creating or updating an employee, the client sends 
`name`, `email`, and `departmentId`. 
This DTO captures exactly those three fields. 
The actual `Employee` model has more data (like `id`, database relationships), 
so using a DTO prevents exposing internal details and keeps the input clean.

---

## exceptions/ResourceNotFoundException.java
**Why it exists:** This is a custom exception class. 
When someone tries to update or delete an employee or department 
that doesn't exist in the database, 
the code throws this exception with a message like 
`"Employee not found with id: 99"`. 
It extends `RuntimeException` so it doesn't need to be declared in method 
signatures everywhere.

---

## exceptions/GlobalExceptionController.java
**Why it exists:** Without this, when an error is thrown anywhere in the 
application, Spring returns a generic HTML error page or ugly stack trace. 
This file uses `@ControllerAdvice` to intercept ALL exceptions across 
the entire application and convert them into clean JSON responses like 
`{"error": "Employee not found with id: 99"}` with the appropriate HTTP status code
(`404`, `500`, etc.).

---

## models/User.java
**Why it exists:** This is the Java representation of the `users` database table. 
Every row in `users` maps to a `User` object. It also implements `UserDetails` from 
Spring Security, which means Spring Security can use it directly to understand 
who this user is, what roles they have, and whether their account is active. 
Fields: `id`, `username`, `password`, `role`.

---

## models/Employee.java
**Why it exists:** Represents the `employees` table. Stores the employee's 
`name`, `email`, and which `Department` they belong to (via a foreign key). 
The `@JsonIgnore` on `department` prevents infinite loops when converting 
to JSON (since Department also has a list of Employees).

---

## models/Department.java
**Why it exists:** Represents the `departments` table. 
A department has a `name` and a list of `Employee` objects. 
The `@OneToMany` annotation tells Hibernate that one department can have many 
employees.

---

## models/RefreshToken.java
**Why it exists:** A JWT access token expires quickly (1 hour). 
The refresh token is a long-lived token (24 hours) stored in the database. 
When the access token expires, the client can use the refresh token to get a 
new access token without logging in again. This file represents the 
`refresh_tokens` table — storing the token string, its expiry date, 
and which user it belongs to.

---

## repository/UserRepository.java
**Why it exists:** This interface gives the application ready-made database 
operations for the `User` model — `save()`, `findById()`, `delete()`, etc. — 
all provided by Spring Data JPA automatically. The custom `findByUsername()` 
method lets us look up a user by their username (needed during login).

---

## repository/EmployeeRepository.java
**Why it exists:** Provides database operations for `Employee` — 
find all, find by id, save, delete. Spring Data JPA generates all 
the SQL automatically. No custom queries needed here since the basic CRUD 
operations are sufficient.

---

## repository/DepartmentRepository.java
**Why it exists:** Provides database operations for `Department`. The custom 
`findByName()` method allows checking whether a department with a given name 
already exists (useful to avoid duplicates).

---

## repository/RefreshTokenRepository.java
**Why it exists:** Provides database operations for `RefreshToken`. 
The custom `findByToken()` method looks up a token by its string value. 
The `deleteByUser()` method deletes any existing token for a user 
(needed to prevent duplicate token errors on re-login).

---

## security/JwtService.java
**Why it exists:** This file is the JWT toolkit. It handles everything related to 
JWT tokens — generating a new token with a username embedded inside, 
extracting the username back out of a token, checking whether a token has 
expired, and validating that a token belongs to the correct user. 
It uses the secret key from `application.properties` to sign and verify tokens.

---

## security/JwtAuthenticationFilter.java
**Why it exists:** This filter runs on **every single HTTP request** before it 
reaches any controller. It checks whether the request has an 
`Authorization: Bearer <token>` header. If yes, it extracts the token, 
validates it via `JwtService`, and sets the authenticated user in Spring's 
`SecurityContext`. If no token, it just lets the request pass 
(Spring Security will then deny it if the route is protected).

---

## security/SecurityConfig.java
**Why it exists:** This is the master security configuration. It defines:
- Which routes are public (`/auth/**` — anyone can access login)
- Which routes require ADMIN role (`/api/admin/**`)
- Which routes just need any valid token (`/api/employees`)
- Uses stateless sessions (no cookies — JWT only)
- Sets up the `AuthenticationProvider` with BCrypt password checking

---

## services/AuthService.java
**Why it exists:** Contains the actual login business logic. It takes the username 
and password, authenticates using Spring's `AuthenticationManager`, fetches the 
user from the database, generates a JWT access token via `JwtService`, creates a 
refresh token via `RefreshTokenService`, and returns both tokens packaged in 
`AuthResponse`.

---

## services/EmployeeService.java
**Why it exists:** Contains all the business logic for employee operations — get all employees, create a new employee (with department lookup), update an employee, delete an employee. By keeping this logic in a service (not in the controller), it's reusable, testable, and keeps the controller thin.

---

## services/RefreshTokenService.java
**Why it exists:** Manages refresh token lifecycle — creating a new refresh token for a user, finding a token by its string value, and verifying whether a token has expired. The expiry duration is configurable via `application.properties` (`jwt.refresh-token.expiration`).

---

## services/CustomUserDetailsService.java
**Why it exists:** Spring Security needs a `UserDetailsService` to load a user by username during authentication. This class implements that interface and fetches the user from `UserRepository`. It bridges the application's `User` model with Spring Security's authentication system.

---

---

# PART 3 — 10 Scenarios: File-to-File Flow

Each scenario shows which files are involved in a request and in what order.

---

## Scenario 1 — Admin logs in successfully

**Request:** `POST /auth/login` with `{"username":"admin","password":"password123"}`

```
Client
  → AuthController          (receives the request)
  → AuthService             (handles login logic)
  → CustomUserDetailsService (loads user from DB)
  → UserRepository          (queries users table)
  → JwtService              (generates access token)
  → RefreshTokenService     (creates refresh token)
  → RefreshTokenRepository  (saves refresh token to DB)
  → AuthResponse            (packages both tokens)
  → AuthController          (sends response back)
Client ← { accessToken, refreshToken }
```

---

## Scenario 2 — User logs in with wrong password

**Request:** `POST /auth/login` with wrong password

```
Client
  → AuthController
  → AuthService             (calls AuthenticationManager.authenticate)
  → AuthenticationManager   (Spring Security checks password with BCrypt)
  ← throws BadCredentialsException
  → GlobalExceptionController (catches the exception)
Client ← { "error": "Bad credentials" }  [500]
```

---

## Scenario 3 — Authenticated user fetches all employees

**Request:** `GET /api/employees` with `Authorization: Bearer <token>`

```
Client
  → JwtAuthenticationFilter     (reads token from header)
  → JwtService                  (extracts username from token)
  → CustomUserDetailsService    (loads user from DB)
  → UserRepository              (queries users table)
  → JwtService                  (validates token)
  → SecurityConfig              (confirms route is accessible with valid token)
  → EmployeeController          (handles request)
  → EmployeeService             (fetches employees)
  → EmployeeRepository          (queries employees table)
Client ← [ { id, name, email }, ... ]  [200]
```

---

## Scenario 4 — User tries to access employees with NO token

**Request:** `GET /api/employees` — no Authorization header

```
Client
  → JwtAuthenticationFilter   (no Authorization header found — skips JWT validation)
  → SecurityConfig            (route requires authentication — user is anonymous)
  → Access Denied
Client ← 403 Forbidden
```

---

## Scenario 5 — Admin creates a new department

**Request:** `POST /api/admin/departments` with `{"name":"Engineering"}`

```
Client
  → JwtAuthenticationFilter       (validates token)
  → JwtService                    (extracts username, checks validity)
  → UserRepository                (loads user)
  → SecurityConfig                (confirms user has ADMIN role)
  → DepartmentController          (handles request)
  → DepartmentRepository          (saves new department to DB)
Client ← { id, name, employees: null }  [200]
```

---

## Scenario 6 — Admin creates a new employee

**Request:** `POST /api/admin/employees` with `{"name":"Alice","email":"alice@x.com","departmentId":1}`

```
Client
  → JwtAuthenticationFilter       (validates token)
  → SecurityConfig                (confirms ADMIN role)
  → AdminEmployeeController       (receives request, parses EmployeeDto)
  → EmployeeService               (createEmployee logic)
  → DepartmentRepository          (finds department by ID — must exist)
  → EmployeeRepository            (saves new employee to DB)
Client ← { id, name, email }  [200]
```

---

## Scenario 7 — Admin creates employee with invalid departmentId

**Request:** `POST /api/admin/employees` with `departmentId: 9999` (doesn't exist)

```
Client
  → JwtAuthenticationFilter
  → AdminEmployeeController
  → EmployeeService               (calls departmentRepository.findById(9999))
  → DepartmentRepository          (returns empty Optional)
  ← throws ResourceNotFoundException("Department not found with id: 9999")
  → GlobalExceptionController     (catches it, formats JSON)
Client ← { "error": "Department not found with id: 9999" }  [404]
```

---

## Scenario 8 — Admin updates an existing employee

**Request:** `PUT /api/admin/employees/1`

```
Client
  → JwtAuthenticationFilter
  → SecurityConfig                (ADMIN check)
  → AdminEmployeeController       (receives id=1 and EmployeeDto)
  → EmployeeService               (updateEmployee logic)
  → EmployeeRepository            (finds employee by id)
  → DepartmentRepository          (finds new department if changed)
  → EmployeeRepository            (saves updated employee)
Client ← { id, name, email }  [200]
```

---

## Scenario 9 — Admin deletes an employee

**Request:** `DELETE /api/admin/employees/1`

```
Client
  → JwtAuthenticationFilter
  → SecurityConfig                (ADMIN check)
  → AdminEmployeeController       (receives id=1)
  → EmployeeService               (deleteEmployee logic)
  → EmployeeRepository            (finds employee by id)
  → EmployeeRepository            (deletes from DB)
Client ← (empty body)  [204 No Content]
```

---

## Scenario 10 — Admin tries to delete employee that doesn't exist

**Request:** `DELETE /api/admin/employees/999`

```
Client
  → JwtAuthenticationFilter
  → SecurityConfig
  → AdminEmployeeController
  → EmployeeService               (deleteEmployee — calls findById(999))
  → EmployeeRepository            (returns empty Optional)
  ← throws ResourceNotFoundException("Employee not found with id: 999")
  → GlobalExceptionController     (catches, formats JSON)
Client ← { "error": "Employee not found with id: 999" }  [404]
```

---

---

# PART 4 — 10 Scenarios: Code Flow

Same 10 scenarios — now showing the exact methods and code that runs.

---

## Scenario 1 — Admin logs in successfully

**File: AuthController.java**
```java
@PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    return ResponseEntity.ok(authService.authenticate(request));
    // request = { username: "admin", password: "password123" }
}
```

**File: AuthService.java**
```java
public AuthResponse authenticate(AuthRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),   // "admin"
            request.getPassword()    // "password123"
        )
    );
    // ↑ Spring calls CustomUserDetailsService.loadUserByUsername("admin")
    // ↑ Then BCrypt checks "password123" against stored hash

    var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

    return AuthResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken.getToken())
        .build();
}
```

**File: JwtService.java**
```java
public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
        .subject(userDetails.getUsername())    // "admin"
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtExpiration))  // +1 hour
        .signWith(getSignInKey())
        .compact();
    // Returns: "eyJhbGci..." (the JWT string)
}
```

**File: RefreshTokenService.java**
```java
public RefreshToken createRefreshToken(String username) {
    RefreshToken refreshToken = RefreshToken.builder()
        .user(userRepository.findByUsername(username).get())
        .token(UUID.randomUUID().toString())   // random UUID string
        .expiryDate(Instant.now().plusMillis(refreshTokenDurationMs))  // +24 hours
        .build();
    return refreshTokenRepository.save(refreshToken);  // saved to DB
}
```

---

## Scenario 2 — Wrong password on login

**File: AuthService.java**
```java
authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken("admin", "wrongpass")
);
// Spring Security's DaoAuthenticationProvider runs BCrypt.matches("wrongpass", storedHash)
// BCrypt returns false → throws BadCredentialsException
```

**File: GlobalExceptionController.java**
```java
@ExceptionHandler(Exception.class)
public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", ex.getMessage());  // "Bad credentials"
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);  // 500
}
```

---

## Scenario 3 — Fetch all employees with valid token

**File: JwtAuthenticationFilter.java**
```java
// Runs on EVERY request before the controller
String authHeader = request.getHeader("Authorization");
// authHeader = "Bearer eyJhbGci..."

String jwt = authHeader.substring(7);  // removes "Bearer " prefix
String username = jwtService.extractUsername(jwt);  // "admin"

UserDetails userDetails = userDetailsService.loadUserByUsername(username);
// loads admin from DB

if (jwtService.isTokenValid(jwt, userDetails)) {
    // sets admin as authenticated in Spring's SecurityContext
    SecurityContextHolder.getContext().setAuthentication(authToken);
}
// request continues to controller
```

**File: EmployeeController.java**
```java
@GetMapping
public ResponseEntity<List<Employee>> getAllEmployees() {
    return ResponseEntity.ok(employeeService.getAllEmployees());
}
```

**File: EmployeeService.java**
```java
public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
    // Hibernate runs: SELECT * FROM employees
}
```

---

## Scenario 4 — No token on protected route

**File: JwtAuthenticationFilter.java**
```java
String authHeader = request.getHeader("Authorization");
// authHeader = null

if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    filterChain.doFilter(request, response);  // pass to next filter
    return;  // no token set in SecurityContext
}
```

**File: SecurityConfig.java**
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/auth/**").permitAll()
    .anyRequest().authenticated()  // ← needs login — user is anonymous → 403
)
```
Spring Security sees no authentication in SecurityContext → sends `403 Forbidden`.

---

## Scenario 5 — Admin creates department

**File: DepartmentController.java**
```java
@PostMapping
public Department createDepartment(@RequestBody Department department) {
    return departmentRepository.save(department);
    // department = { name: "Engineering" }
    // Hibernate runs: INSERT INTO departments (name) VALUES ("Engineering")
    // Returns: { id: 4, name: "Engineering" }
}
```

---

## Scenario 6 — Admin creates employee

**File: AdminEmployeeController.java**
```java
@PostMapping
public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDto employeeDto) {
    return ResponseEntity.ok(employeeService.createEmployee(employeeDto));
    // employeeDto = { name: "Alice", email: "alice@x.com", departmentId: 1 }
}
```

**File: EmployeeService.java**
```java
public Employee createEmployee(EmployeeDto employeeDto) {
    Department department = departmentRepository.findById(employeeDto.getDepartmentId())
        .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + employeeDto.getDepartmentId()));
    // Hibernate: SELECT * FROM departments WHERE id = 1  ← found

    Employee employee = Employee.builder()
        .name(employeeDto.getName())     // "Alice"
        .email(employeeDto.getEmail())   // "alice@x.com"
        .department(department)          // links to Department with id=1
        .build();

    return employeeRepository.save(employee);
    // Hibernate: INSERT INTO employees (name, email, department_id) VALUES (...)
}
```

---

## Scenario 7 — Employee creation with bad departmentId

**File: EmployeeService.java**
```java
Department department = departmentRepository.findById(9999L)
    .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: 9999"));
// findById(9999) returns Optional.empty()
// .orElseThrow() fires → throws ResourceNotFoundException
```

**File: GlobalExceptionController.java**
```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", ex.getMessage());  // "Department not found with id: 9999"
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  // 404
}
```

---

## Scenario 8 — Update existing employee

**File: AdminEmployeeController.java**
```java
@PutMapping("/{id}")
public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto employeeDto) {
    return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDto));
    // id = 1, employeeDto = { name: "Alice Smith", email: "...", departmentId: 1 }
}
```

**File: EmployeeService.java**
```java
public Employee updateEmployee(Long id, EmployeeDto employeeDto) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    // Hibernate: SELECT * FROM employees WHERE id = 1  ← found

    Department department = departmentRepository.findById(employeeDto.getDepartmentId())
        .orElseThrow(...);
    // Hibernate: SELECT * FROM departments WHERE id = 1  ← found

    employee.setName(employeeDto.getName());         // "Alice Smith"
    employee.setEmail(employeeDto.getEmail());        // updated email
    employee.setDepartment(department);              // same department

    return employeeRepository.save(employee);
    // Hibernate: UPDATE employees SET name=?, email=? WHERE id=1
}
```

---

## Scenario 9 — Delete existing employee

**File: AdminEmployeeController.java**
```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();  // 204
}
```

**File: EmployeeService.java**
```java
public void deleteEmployee(Long id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    // Hibernate: SELECT * FROM employees WHERE id = 1  ← found

    employeeRepository.delete(employee);
    // Hibernate: DELETE FROM employees WHERE id = 1
}
```

---

## Scenario 10 — Delete employee that doesn't exist

**File: EmployeeService.java**
```java
public void deleteEmployee(Long id) {
    Employee employee = employeeRepository.findById(999L)
        .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: 999"));
    // findById(999) → Optional.empty()
    // .orElseThrow() → throws ResourceNotFoundException
}
```

**File: GlobalExceptionController.java**
```java
@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<Map<String, String>> handleResourceNotFoundException(ResourceNotFoundException ex) {
    Map<String, String> response = new HashMap<>();
    response.put("error", "Employee not found with id: 999");
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);  // 404
}
```

---

*End of information.md*
