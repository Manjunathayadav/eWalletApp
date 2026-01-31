# eWalletApp

E-Wallet Payment System – Microservices Design (Minimal)
Overview

The E-Wallet system is designed using a minimal microservices architecture consisting of three independent microservices.

Each microservice:

Runs as a standalone Spring Boot application

Exposes REST APIs

Is tested independently using Postman or Swagger

Does not communicate with other microservices at runtime

The focus is on clear service boundaries and API contracts rather than integration.

Microservices Summary

Microservice		Responsibility

Payment Service		Accepts and tracks payment requests
Wallet Service		Manages wallet and merchant balances
Notification Service	Sends payment notifications

1. Payment Service

Purpose

The Payment Service acts as the entry point for payment initiation.

It is responsible for:

Accepting payment requests

Validating request data

Generating a transaction ID

Tracking payment status

It does not handle wallet balance or notifications directly.

Base URL (Example)
http://localhost:8081

API Endpoints
1.1 Initiate Payment

Endpoint

POST /api/payments


Description
Creates a new payment request and generates a transaction ID.

Request

{
  "walletId": "W123",
  "merchantId": "M456",
  "amount": 1000,
  "currency": "INR"
}

Response (Accepted)

{
  "transactionId": "TXN10001",
  "status": "ACCEPTED",
  "message": "Payment request accepted"
}


Response (Failure)

{
  "status": "FAILED",
  "message": "Invalid payment request"
}

1.2 Get Payment Status

Endpoint

GET /api/payments/{transactionId}


Response

{
  "transactionId": "TXN10001",
  "walletId": "W123",
  "merchantId": "M456",
  "amount": 1000,
  "status": "ACCEPTED",
  "timestamp": "2026-01-31T10:30:00"
}

2. Wallet Service
Purpose

The Wallet Service manages all balance-related operations.

To keep the system minimal:

Wallet balance and merchant balance are handled in the same service

Responsibilities:

Maintain wallet balances

Debit and refund wallet amounts

Credit merchant accounts

Base URL (Example)
http://localhost:8082

API Endpoints

2.1 Create Wallet

Endpoint

POST /api/wallets


Request

{
  "walletId": "W123",
  "balance": 5000,
  "currency": "INR"
}


Response

{
  "walletId": "W123",
  "balance": 5000,
  "currency": "INR",
  "status": "ACTIVE"
}

2.2 Get Wallet Details

Endpoint

GET /api/wallets/{walletId}


Response

{
  "walletId": "W123",
  "balance": 4000,
  "currency": "INR",
  "status": "ACTIVE",
  "customer": {
    "customerId": "C789",
    "name": "Rupinder Singh",
    "email": "rupinder@example.com",
    "mobile": "9876543210"
  }
}


2.3 Debit Wallet

Endpoint

POST /api/wallets/debit


Request

{
  "walletId": "W123",
  "amount": 1000,
  "transactionId": "TXN10001"
}


Response

{
  "status": "DEBIT_SUCCESS"
}

2.4 Refund Wallet

Endpoint

POST /api/wallets/refund

Request

{
  "walletId": "W123",
  "amount": 1000,
  "transactionId": "TXN10001"
}


Response

{
  "status": "REFUND_SUCCESS"
}

2.5 Credit Merchant

Endpoint

POST /api/merchants/credit


Request

{
  "merchantId": "M456",
  "amount": 980,
  "transactionId": "TXN10001"
}


Response

{
  "status": "CREDIT_SUCCESS"
}

2.6 Get Merchant Details

Endpoint

GET /api/merchants/{merchantId}


Response

{
  "merchantId": "M456",
  "balance": 980,
  "status": "ACTIVE"
}

3. Notification Service
Purpose

The Notification Service is responsible for sending payment-related notifications.

It is fully decoupled from payment and wallet logic and can be extended to support:

Email

SMS

Push notifications

Base URL (Example)
http://localhost:8083

API Endpoints
3.1 Send Notification

Endpoint

POST /api/notifications


Description
Triggers a notification related to a payment event.

Request

{
  "transactionId": "TXN10001",
  "type": "PAYMENT_SUCCESS",
  "recipient": "USER"
}


Response

{
  "status": "NOTIFICATION_SENT"
}

3.2 Get Notification Status

Endpoint

GET /api/notifications/{transactionId}


Response

{
  "transactionId": "TXN10001",
  "type": "PAYMENT_SUCCESS",
  "status": "DELIVERED",
  "timestamp": "2026-01-31T10:35:00"
}

Manual Testing Strategy

Each microservice is tested independently using Postman.

Suggested demo flow:

Call Payment Service to create a payment and generate transaction ID

Call Wallet Service to debit wallet and credit merchant

Call Notification Service to send payment notification

This avoids runtime coupling while clearly demonstrating the system design.

Design Justification

To keep the architecture simple and demo-friendly:

Only three microservices are used

Each service owns a single responsibility

No inter-service communication is implemented

APIs are designed to support future integration
