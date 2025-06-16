# SecureSG

## Overview

This is the source code of paper "SecureSG : A Secure Computation System for Social Graph Search".

---

## Experimental Environment

- **Operating System**: Windows / Linux / MacOS
- **Databases**:
  - MySQL (version 5.7 or above recommended)
  - Redis 7.0.11
- **Java Version**: JDK 1.8
- **Build Tool**: Maven (optional)

---

## Project Structure

```
project-root
│
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── zhong
│   │               └── concurrent
│   │                   └── BlockingQueueModel.java          # Step 3: Index generation
│   └── test
│       └── java
│           └── com
│               └── zhong
│                   ├── rm
│                   │   └── test.java                        # Step 1: Data import to Redis
│                   └── SearchProtocol_Split_memory_Test.java # Step 4: Search testing
```

---

## Experimental Steps

### Step 1: Import Data into Redis

- Execution path:  
  `src/test/java/com/zhong/rm/test.java`

- Function: Load raw data into the Redis database for caching or preprocessing.

---

### Step 2: Create MySQL Tables

Execute the following SQL statements to create the required tables:

```sql
-- Table 1: tset
CREATE TABLE tset (
    record MEDIUMBLOB,
    stag VARCHAR(400)
);

-- Table 2: tsets
CREATE TABLE tsets (
    stag VARCHAR(400),
    t MEDIUMBLOB
);

-- Table 3: xsets
CREATE TABLE xsets (
    x MEDIUMBLOB,
    xset VARCHAR(400)
);
```

Ensure MySQL is running and properly configured in your database settings.

---

### Step 3: Generate Index and Store in MySQL

- Execution path:  
  `src/main/java/com/zhong/concurrent/BlockingQueueModel.java`

- Function: Read data from Redis, build inverted indexes, and store them in MySQL.

---

### Step 4: Perform Keyword Search

- Execution path:  
  `src/test/java/com/zhong/SearchProtocol_Split_memory_Test.java`

- Function: Test both single-keyword and multi-keyword search functionalities to evaluate accuracy and performance.

