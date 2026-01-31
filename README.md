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

Api:

/auth/register  - registring the user
/auth/login - vaidate user , generating token & Forward to services

2. Wallet Service

Responsibilities:
Fetch wallet details
Validate balance
Update wallet balance
Maintain audit

APIs:
GET   /wallet/{customerId} - fetch wallet details
Validate Coustmer exists, fetch wallet, return wallet info

POST  /wallet/validate

POST  /wallet/debit - debit amount from wallet
Lock wallet row,validate balance,deduct amount,save,Audit log

POST  /wallet/credit - credit money back (refund & reversal)
validate wallet,add amount,save

3. Payment Service
Responsibilities:

Initiate payment,Call wallet service,Deduct amount,Credit merchant,Update ledger,Deduct wallet fee,Maintain transaction state,Handle compensation logic

APIs:
POST /payment/initiate - initiate wallet based purchase
Validations: Customer exists,Merchant exists,Currency valid,Amount > 0
Complete Flow :
Generate transactionRef
Call Wallet Service /wallet/debit
Calculate wallet fee (2%)
Credit merchant (amount - fee)
Save transaction in DB:
amount
walletFee
status = SUCCESS
Save audit logs
Call Notification Service
Return response

GET  /payment/{transactionId} - Fetch transaction details.

4. Notification Service
/notify - Send notification after payment.

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


Database Design :

CREATE TABLE customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE wallet (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT NOT NULL,
    currency VARCHAR(10) NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.0,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_wallet_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);


CREATE TABLE merchant (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    bank_account VARCHAR(100) NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_ref VARCHAR(100) UNIQUE NOT NULL,
    customer_id BIGINT NOT NULL,
    merchant_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    wallet_fee DECIMAL(10,2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_transaction_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE audit_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_ref VARCHAR(100) NOT NULL,
    action VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL,
    message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_audit_transaction FOREIGN KEY (transaction_ref) REFERENCES transaction(transaction_ref)
        ON DELETE CASCADE ON UPDATE CASCADE
);




