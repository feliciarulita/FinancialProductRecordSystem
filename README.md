# FinancialProductRecordSystem

## Financial Product Record System

## 1. Setup Database

Execute the database SQL scripts in order:

1. Create tables:
   FinancialProductPreference/DB/ddl.sql

2. Insert dummy data:
   FinancialProductPreference/DB/dml.sql

3. Create stored procedures:
   FinancialProductPreference/DB/stored_procedures.sql

## 2. Setup Environment

Before running the frontend, please configure your environment variables.

1. Copy the example environment file: cp .env.example .env.local
2. Open .env.local and update the backend API URL: VITE_API_BASE=http://localhost:your-backend-port

## 3. Start Frontend

cd finance-frontend
npm install
npm run dev

## 4. Start Backend

cd FinancialProductPreference
./mvnw spring-boot:run
