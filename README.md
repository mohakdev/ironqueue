# IronQueue

IronQueue is a lightweight distributed job queue written in Java using Redis. It allows producers to submit jobs while multiple workers consume and execute them concurrently.

The goal of this project is to understand how production job queues are designed by implementing the core concepts from scratch.

---

# Features

* Redis-backed job persistence
* Redis-backed distributed queue
* Multiple concurrent workers
* Worker registration and heartbeat mechanism
* Automatic retry mechanism
* Graceful worker shutdown
* Pluggable Job Executors using the Factory Pattern
* Interactive CLI powered by JLine
* Real email delivery using the Resend API
* Job inspection commands
* Worker inspection commands

---

# Prerequisites

Before running IronQueue, install:

* Java 11+
* Maven
* Redis Server

Start Redis locally:

```bash
redis-server
```

Verify it is running:

```bash
redis-cli ping
```

Expected output:

```text
PONG
```

---

# Clone the Repository

```bash
git clone https://github.com/mohakdev/ironqueue.git

cd IronQueue
```

---

# Build

```bash
mvn clean package
```

This creates an executable JAR:

```text
target/ironqueue.jar
```

---

# Running IronQueue

Start the interactive CLI:

```bash
java -jar target/ironqueue.jar
```

---

# Available Commands

## Create a Job

```text
job-create EMAIL recipient@example.com "Subject" "Hello from IronQueue"
```

## List Jobs

```text
job-list
```

## View Job Information

```text
job-info <job-id>
```

## Start a Worker

```text
worker-start
```

## List Workers

```text
worker-list
```

## Enable Background Logs

```text
logs on
```

Disable logs:

```text
logs off
```

## Show Help

```text
help
```

## Exit

```text
exit
```

---

# Email Jobs

IronQueue currently supports email jobs through the Resend API.

Before creating email jobs, configure the following environment variables.

Linux/macOS:

```bash
export RESEND_API_KEY=your_api_key
export IRONQUEUE_FROM_EMAIL=hello@yourdomain.com
```

Windows PowerShell:

```powershell
$env:RESEND_API_KEY="your_api_key"
$env:IRONQUEUE_FROM_EMAIL="hello@yourdomain.com"
```

If these variables are not present, IronQueue will notify you that email execution is unavailable.

---

# Project Architecture

```text
Producer
    │
    ▼
Redis Queue
    │
    ▼
Worker
    │
    ▼
Job Executor
    │
    ▼
External Service
```

Workers register themselves with Redis, periodically send heartbeats, and continuously consume jobs from the queue.

---

# Current Job Types

* EMAIL
* QUOTE

More job types are planned.

---

# Roadmap

* Scheduled Jobs
* Delayed Jobs
* Priority Queues
* Dead Letter Queue
* Worker Statistics
* Metrics Dashboard
* HTTP API
* Additional Job Executors
* Docker Support
* Native CLI Executable

---

# Design Patterns Used

* Factory Pattern
* Command Pattern
* Singleton-style Command Registry

---

# Project Status

IronQueue is currently under active development and is intended as both a learning project and a foundation for a production-style distributed task queue.

Contributions, suggestions, and feedback are welcome.
