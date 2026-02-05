# AI Chat API

A lightweight backend service that exposes an API for generating AI-powered chat responses using large language models (LLMs).

This service acts as a **gateway layer** between the frontend UI and external AI providers (e.g. Hugging Face, Gemini, etc.), handling:
- prompt orchestration
- conversational context
- provider abstraction
- response normalization

---

## 🚀 Features

- REST API for chat completions
- Provider-agnostic design (easy to switch LLMs)
- Supports conversational context (previous responses)
- Clean response extraction (text-only)
- Spring Boot based, production-ready structure

---

## 🛠 Tech Stack

- Java 17+
- Spring Boot
- OkHttp (HTTP client)
- Jackson (JSON parsing)
- External LLM providers (Hugging Face / Gemini)

---

## 📡 API Overview

### Generate Chat Response

**Endpoint**
```http
POST /api/ai/chat
