# FraudShield

Real-time fraud detection backend with asynchronous Redis-based processing, modular validation rules, and containerized deployment.

## Features
- **Async event processing**: transactions are enqueued to Redis and evaluated by background workers.
- **Redis queues** for distributed processing.
- **Modular validation rules** with configurable scoring.
- **Sub-second evaluation** for high‑volume traffic.
- **Observability** via Spring Boot Actuator endpoints.
- **Sample generator** to test end‑to‑end flows.

## Tech Stack
- Java 21, Spring Boot 3.x
- Redis 7.x
- Docker / Docker Compose
- AWS EC2 deployment guide

---

## Quick Start (Local)

### 1) Run with Docker Compose
```bash
docker compose up --build
```

The API will be available at: `http://localhost:8080`

### 2) Health check
```bash
curl http://localhost:8080/actuator/health
```

### 3) Submit a transaction
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H 'Content-Type: application/json' \
  -d '{
    "transactionId": "tx-1001",
    "userId": "u-123",
    "amount": 1250.50,
    "currency": "USD",
    "merchantId": "m-74",
    "country": "US",
    "cardCountry": "US"
  }'
```

Response:
```json
{
  "ingestionId": "tx-1001",
  "status": "QUEUED"
}
```

### 4) Fetch result (after a short delay)
```bash
curl http://localhost:8080/api/transactions/tx-1001
```

---

## Sample Transaction Generator
Generate random transactions and push them into Redis:
```bash
curl -X POST "http://localhost:8080/api/simulate?count=100"
```

---

## API Endpoints
- **POST** `/api/transactions` – enqueue a transaction for evaluation
- **GET** `/api/transactions/{id}` – fetch evaluation result
- **POST** `/api/simulate?count=N` – generate and enqueue sample transactions
- **GET** `/actuator/health` – service health

---

## Architecture Overview
```
Client -> REST API -> Redis Queue -> Worker -> Fraud Rules -> Result Store
```

**Key components**:
- `TransactionQueueService`: pushes/pops events in Redis
- `QueueWorker`: async processor consuming events
- `FraudDetectionService`: runs modular fraud rules
- `TransactionResultStore`: in-memory result cache

---

## AWS EC2 Deployment (Guide)
1. Launch EC2 (Amazon Linux 2023 or Ubuntu 22.04).
2. Install Docker & Docker Compose.
3. Clone repo and run:
   ```bash
   docker compose up --build -d
   ```
4. Open ports **8080** and **6379** (optional) in the Security Group.
5. Use `/actuator/health` for monitoring.

**Logging & Monitoring**:
- Container logs: `docker compose logs -f app`
- Health: `/actuator/health`
- Metrics: `/actuator/metrics`

---

## Notes
- Result storage is in-memory for simplicity. For production, persist to a DB.
- Rules are modular; add new rules in `com.fraudshield.rules`.
