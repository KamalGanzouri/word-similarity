# File Similarity Spring Boot Application

## 📋 Overview

This Spring Boot application compares a target file (`fileA.txt`) with a pool of files in a directory and scores each file based on **word similarity** (Jaccard similarity). The result shows how much each file matches file A.

---

## ⚙️ Configuration

Update `application.properties`:

```properties
file.a.path=classpath:fileA.txt
file.pool.path=classpath:poolOfFiles/*.txt
