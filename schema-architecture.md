# Smart Clinic Management System: Architecture Design

## Section 1: Architecture Summary
The Smart Clinic Management System leverages a decoupled, three-tier architecture designed for high scalability, separation of concerns, and optimized data storage. The presentation tier is built as a responsive Single Page Application (SPA) using React, which interacts asynchronously with the application tier via RESTful APIs. The backend application tier is powered by Java Spring Boot, acting as a stateless service layer secured by JSON Web Tokens (JWT). To support the diverse data needs of a clinical environment, the data tier utilizes a polyglot persistence strategy: structured, highly transactional data (users, roles, and schedules) is stored in a relational MySQL database managed via Spring Data JPA, while flexible, unstructured clinical data (prescriptions and medical records) is managed within a NoSQL MongoDB document store via Spring Data MongoDB.

## Section 2: Numbered Flow (Request/Response Cycle)

The following sequence details how an asynchronous client request flows through the decoupled architecture to the dual-database tier and returns an updated state to the user interface:

1. **Client Action & Trigger:** A user interacts with the React frontend (e.g., submitting an appointment booking form or creating a prescription) which dispatches an HTTP request (via `fetch` or `axios`) containing a JSON payload and an `Authorization: Bearer <JWT>` header.
2. **Security & Routing Layer:** The request arrives at the Spring Boot application, where it is intercepted by the Spring Security filter chain. The system parses and validates the JWT signature and extracts user roles/permissions.
3. **Controller Interception (REST Endpoint):** Once authorized, the request is mapped to a specific `@RestController` endpoint (e.g., `@PostMapping("/api/appointments")`), which acts as the entry point for the Application layer.
4. **Business Logic Execution (Service Layer):** The controller delegates the request payload to the corresponding Service component (annotated with `@Service`), where transactional logic, domain rules, and data validations are enforced.
5. **Data Layer Delegation (Repository):** The service layer invokes the appropriate repository interface:
   - For relational/transactional data, it calls a `JpaRepository` to interface with MySQL.
   - For unstructured or document-heavy data, it calls a `MongoRepository` to interface with MongoDB.
6. **Database Persistence:** The selected database executes the operation:
   - MySQL performs ACID-compliant transactions or updates using organized tables.
   - MongoDB inserts or updates flexible JSON-like BSON documents.
7. **Response Assembly:** The database acknowledges the operation, and the repository maps the data back into a Java entity or Data Transfer Object (DTO). The service layer wraps this into the final business response state.
8. **HTTP Serialization:** The `@RestController` receives the DTO and transmits an HTTP status code (e.g., `201 Created`) along with the serialized JSON response body back across the network.
9. **UI State Update:** The React frontend receives the asynchronous JSON response, updates its local state hook or global state manager, and automatically re-renders the minimalist UI components to reflect the current data to the user.

# Architecture Diagram

```mermaid
sequenceDiagram
    autonumber
    actor User as Client (User)
    participant React as <<Presentation Tier>> React SPA
    box "Application Tier (Java Spring Boot)" #f9f9f9
        participant Gateway as <<Security/Filter>> Spring Security
        participant Controller as <<RestController>> API Endpoint
        participant Service as <<Service>> Business Logic
        participant Repo as <<Repository>> Data Access
    end
    box "Data Tier (Polyglot Persistence)" #eaeaea
        participant MySQL as <<RDBMS>> MySQL (Structured)
        participant Mongo as <<NoSQL>> MongoDB (Unstructured)
    end

    Note over User, React: Phase 1: Action & Trigger
    User->>React: Interacts with UI (e.g., Submit Form)
    activate React
    React->>React: Prepares JSON payload
    React->>Gateway: HTTP Request + Header(Authorization: Bearer <JWT>)
    activate Gateway

    Note over Gateway, Controller: Phase 2: Authorization
    Gateway->>Gateway: Parses & Validates JWT
    Gateway->>Controller: Routes Request (if valid)
    deactivate Gateway
    activate Controller

    Note over Controller, Service: Phase 3: Business Logic
    Controller->>Service: Delegates payload (@RequestBody)
    activate Service
    Service->>Service: Executes validation & rules

    Note over Service, Repo: Phase 4: Data Delegation
    alt Relational Data (User, Appt)
        Service->>Repo: Calls JpaRepository
        activate Repo
        Repo->>MySQL: Executes SQL/JPA Operation
        activate MySQL
        MySQL-->>Repo: Returns Entity
        deactivate MySQL
    else Unstructured Data (Prescription)
        Service->>Repo: Calls MongoRepository
        activate Repo
        Repo->>Mongo: Executes BSON/NoSQL Operation
        activate Mongo
        Mongo-->>Repo: Returns Document
        deactivate Mongo
    end
    Repo-->>Service: Maps data to DTO
    deactivate Repo
    Service-->>Controller: Returns business response
    deactivate Service

    Note over Controller, React: Phase 5: Response & UI Update
    Controller-->>React: HTTP Response (e.g., 200 OK / 201 Created)
    deactivate Controller
    React->>React: Updates Local/Global State
    React-->>User: Re-renders UI components
    deactivate React
```
```mermaid
graph TD
    %% Presentation Tier
    subgraph Presentation_Tier ["Presentation Tier (Client-Side SPA)"]
        ReactApp["React.js View Components<br>(Dashboard, Booking, Records)"]
        StateMgmt["State Management<br>(Hooks / Context API)"]
        HTTPClient["API Client<br>(Axios / Fetch)"]
        
        ReactApp <--> StateMgmt
        StateMgmt --> HTTPClient
    end

    %% Application Tier Gateway/Security
    subgraph Security_Layer ["API Security & Routing Layer"]
        CORSFilter["CORS Filter<br>(Cross-Origin Policy)"]
        JWTAuthFilter["JWT Authentication Filter<br>(Token Verification & Context)"]
        
        CORSFilter --> JWTAuthFilter
    end

    %% Application Tier Core
    subgraph Application_Tier ["Application Tier (Spring Boot Engine)"]
        subgraph Controllers ["REST Controller Layer (@RestController)"]
            AuthController["Auth Controller<br>(/api/auth)"]
            ClinicController["Appointment Controller<br>(/api/appointments)"]
            MedicalController["Clinical Records Controller<br>(/api/prescriptions)"]
        end

        subgraph Services ["Service Layer (@Service - Business Logic)"]
            UserService["User & Role Service"]
            ApptService["Appointment Domain Service"]
            MedicalService["Clinical Record Domain Service"]
        end

        subgraph Repositories ["Data Access Layer (Spring Data)"]
            JPARepo["Spring Data JPA<br>(Hibernate ORM)"]
            MongoRepo["Spring Data MongoDB<br>(Document Mapping)"]
        end
    end

    %% Data Tier
    subgraph Data_Tier ["Data Tier (Polyglot Persistence)"]
        MySQLDB[("MySQL Database<br>(Relational / Structured)")]
        MongoDB[("MongoDB Cluster<br>(NoSQL / Document Store)")]
    end

    %% System Connections
    HTTPClient -->|"HTTPS Requests + JWT Bearer"| CORSFilter
    JWTAuthFilter -->|"Authorized Sub-Route Execution"| Controllers

    %% Internal Tier Dependencies
    AuthController --> UserService
    ClinicController --> ApptService
    MedicalController --> MedicalService

    UserService --> JPARepo
    ApptService --> JPARepo
    MedicalService --> MongoRepo

    %% Storage Routing
    JPARepo -->|"SQL / Transaction Management"| MySQLDB
    MongoRepo -->|"BSON Protocol / Document Streams"| MongoDB

    %% Component Layout Adjustments / Styles
    style ReactApp fill:#1e1e24,stroke:#61dafb,stroke-width:2px,color:#61dafb
    style Security_Layer fill:#f4f6f9,stroke:#2b579a,stroke-width:2px,color:#000
    style Application_Tier fill:#fafafa,stroke:#4caf50,stroke-width:2px,color:#000
    style Data_Tier fill:#fcf8f2,stroke:#ff9800,stroke-width:2px,color:#000
    style MySQLDB fill:#e8f0fe,stroke:#3e6b89,stroke-width:2px,color:#000
    style MongoDB fill:#e6f4ea,stroke:#13aa52,stroke-width:2px,color:#000

```