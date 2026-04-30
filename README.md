# FraudShield

Real-time fraud detection backend with asynchronous Redis-based processing, modular validation rules, and containerized deployment.

## Features
- **Async event processing**: transactions are enqueued to Redis (default) or Kafka (optional) and evaluated by background workers.
- **Redis queues** for distributed processing.
- **Kafka pipeline** for high‑throughput stream ingestion (optional).
- **Modular validation rules** with configurable scoring.
- **Sub-second evaluation** for high‑volume traffic.
- **Observability** via Spring Boot Actuator endpoints.
- **Dashboard UI** for recent evaluations.
- **Sample generator** to test end‑to‑end flows.

## Tech Stack
- Java 21, Spring Boot 3.x
- Redis 7.x
- Kafka (optional)
- Thymeleaf dashboard
- Docker / Docker Compose
- AWS EC2 deployment guide

---

## Quick Start (Local)

### 1) Run with Docker Compose (Redis)
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

## Dashboard
Visit:
```
http://localhost:8080/dashboard
```
Shows the latest evaluated transactions with score, flagged status, and latency.

---

<img width="1918" height="858" alt="Screenshot 2026-04-29 184006" src="https://github.com/user-attachments/assets/3009218b-c680-421f-bd88-2fdb3b633783" />


## Sample Transaction Generator
Generate random transactions and push them into Redis/Kafka:
```bash
curl -X POST "http://localhost:8080/api/simulate?count=100"
```

---

## Kafka Mode (Optional)
Kafka is supported for high‑throughput event streaming.

### Start Kafka stack
```bash
docker compose -f docker-compose.kafka.yml up --build
```

### Enable Kafka mode
Set environment variable:
```
FRAUDSHIELD_QUEUE_MODE=kafka
```

The app will switch from Redis polling to Kafka consumers automatically.

---

## API Endpoints
- **POST** `/api/transactions` – enqueue a transaction for evaluation
- **GET** `/api/transactions/{id}` – fetch evaluation result
- **POST** `/api/simulate?count=N` – generate and enqueue sample transactions
- **GET** `/dashboard` – dashboard UI
- **GET** `/actuator/health` – service health

---

## Architecture Overview
```
Client -> REST API -> Redis/Kafka Queue -> Worker -> Fraud Rules -> Result Store
```

**Key components**:
- `TransactionQueueService`: Redis-backed queue
- `KafkaQueuePublisher` + `KafkaQueueConsumer`: Kafka event pipeline
- `QueueWorker`: async processor consuming Redis queue
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
