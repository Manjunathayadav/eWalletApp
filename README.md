We designed the e-wallet system using a domain-driven microservices architecture separating wallet, payment, and notification services. We ensured transactional consistency using local transactions with compensation handling, secured endpoints using JWT-based stateless authentication, implemented centralized exception handling, structured audit logging for traceability, and followed REST & OpenAPI standards. The system is horizontally scalable, production-ready, and extensible for future settlement integration.

We will design 4 core microservices:

Services Responsibility:

Auth Service	User login, JWT generation
Wallet Service	Manage wallet, balance, validations
Payment Service	Process transaction, deduct, ledger update
Notification Service	Notify merchant & user
API Gateway	Routing + JWT validation

Microservice Breakdown

1. Auth Service (Spring Security + JWT)
Responsibilities:

User registration,Login,Generate JWT token,Validate token

Tech:
Spring Security,JWT,BCrypt,MySQL

JWT Flow:
User login → generate JWT
JWT passed in Authorization header
Gateway validates token
Forward to services

2. Wallet Service

Responsibilities:

Fetch wallet details
Validate balance
Update wallet balance
Maintain audit

APIs:
GET   /wallet/{customerId}
POST  /wallet/validate
POST  /wallet/debit
POST  /wallet/credit

3. Payment Service
Responsibilities:

Initiate payment,Call wallet service,Deduct amount,Credit merchant,Update ledger,Deduct wallet fee,Maintain transaction state,Handle compensation logic

APIs:
POST /payment/initiate
GET  /payment/{transactionId}

4. Notification Service

Responsibilities:

Send merchant notification
Send user notification

Payment Flow Design (Transactional & Safe)
-----------------------------------------
Step-by-step flow:

User calls /payment/initiate
Validate JWT
Fetch wallet details

Validate:
Balance sufficient
Currency valid
Start DB Transaction
Deduct wallet balance
Calculate wallet fee (say 2%)
Credit merchant (amount - fee)
Save transaction ledger (status = SUCCESS)
Save audit logs
Commit transaction
Call notification service
Return response

If failure:

Rollback transaction
Save FAILED transaction
Send failure notification
