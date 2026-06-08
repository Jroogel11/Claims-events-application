# Claims Event Sourcing

A claims management system for the insurance sector built with **Event Sourcing** and **CQRS**. Instead of updating records in place, every state change is stored as an immutable event — giving the system a complete, auditable history of every claim from the moment it's opened to the moment it's closed.

## Why Event Sourcing for insurance claims?

Traditional CRUD systems overwrite data on every update. That means you lose the audit trail — you can't tell who changed what, when, or from which state. In claims processing, that history matters: regulators, auditors, and internal teams all need to answer questions like *"how long was this claim in evaluation?"* or *"what was the state of this claim on a specific date?"*.

With Event Sourcing, the state of a claim is the result of replaying all its events in order. PostgreSQL acts as the read model for fast queries, while Kafka serves as the immutable event log that drives all state transitions.

## Architecture

```
POST /claims  ──► CommandService ──► Kafka (claim-events)
                        │                     │
                  saves ClaimEntity      KafkaConsumer
                  to PostgreSQL               │
                                        updates status
                                        saves ClaimEvent
                                        to PostgreSQL

GET /claims/{id}         ──► reads from PostgreSQL (ClaimEntity)
GET /claims/{id}/history ──► reads from PostgreSQL (ClaimEvent[])
```

The write side (commands) and read side (queries) are fully separated — commands publish events to Kafka, queries read directly from the database. This is the core of CQRS.

## Tech Stack

- **Java 21** + **Spring Boot 4**
- **Apache Kafka** — event log and async communication
- **PostgreSQL** — persistent read model
- **Docker Compose** — local infrastructure
- **Testcontainers** — integration testing with real dependencies
- **GitHub Actions** — CI pipeline on every push

## Domain Model

A `Claim` goes through a defined lifecycle. Transitions between states are validated — you can't move a claim directly from `DECLARED` to `CLOSED` without going through the proper evaluation steps.

```
DECLARED → UNDER_EVALUATION → UNDER_REPAIR ──────► RESOLVED → CLOSED
                           └─► UNDER_DOCUMENTATION ──► UNDER_EVALUATION
                           └─► REJECTED
```

### Claim types
`VEHICLE · HOME · HEALTH · LIFE · TRAVEL · LIABILITY`

## API Endpoints

### Write side

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/claims` | Open a new claim |
| `PATCH` | `/api/claims/{id}/status` | Transition claim to a new state |

**Open a claim:**
```json
POST /api/claims
{
  "policyHolderId": "user-123",
  "description": "Water damage in kitchen",
  "amount": 3500.00,
  "type": "HOME"
}
```

**Update state:**
```json
PATCH /api/claims/{id}/status
"EVALUATION_STARTED"
```

### Read side

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/claims` | List all claims |
| `GET` | `/api/claims/{id}` | Get a specific claim |
| `GET` | `/api/claims/{id}/history` | Full event history of a claim |

The history endpoint is where Event Sourcing pays off — it returns every event that ever happened to a claim, in chronological order:

```json
[
  {
    "eventId": "138518f9-...",
    "claimId": "2e642be3-...",
    "type": "CLAIM_DECLARED",
    "policyHolderId": "user-123",
    "createdAt": "2026-06-07T18:09:11"
  },
  {
    "eventId": "48a58610-...",
    "claimId": "2e642be3-...",
    "type": "EVALUATION_STARTED",
    "policyHolderId": "user-123",
    "createdAt": "2026-06-07T10:30:00"
  }
]
```

## Running locally

**Prerequisites:** Docker, Java 21, Maven

```bash
# Start infrastructure
docker-compose up -d

# Run the application
mvn spring-boot:run
```

Services available:
- API: `http://localhost:8080`
- Kafka UI: `http://localhost:8090`
- PostgreSQL: `localhost:5433`

## Running tests

```bash
mvn test
```

Unit tests cover the core business logic of `ClaimCommandService` — claim creation, valid state transitions, and rejection of invalid transitions — using Mockito to isolate the service from its dependencies.

## Key technical decisions

**Kafka as the event backbone** — events are published asynchronously. The consumer updates the read model independently of the write side, which means reads never block writes.

**Eventual consistency** — there is a small window between when a command is processed and when the read model reflects the change. In practice this is under 100ms. The command response always includes the updated state, so clients don't need to immediately re-fetch.

**State machine validation** — invalid transitions are rejected at the service layer before any event is published. This prevents data corruption and makes the system's behaviour predictable.

**String-based Kafka DTOs** — `LocalDateTime` and `UUID` fields are serialized as `String` in Kafka messages to avoid Jackson serialization issues with `kafka-clients 4.x`, which no longer includes Jackson as a transitive dependency.
