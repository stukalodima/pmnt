-- alter table FINANCE_PAYMENT_REGISTER add column REGISTER_TYPE_ID uuid ^
-- update FINANCE_PAYMENT_REGISTER set REGISTER_TYPE_ID = <default_value> ;
-- alter table FINANCE_PAYMENT_REGISTER alter column REGISTER_TYPE_ID set not null ;
alter table FINANCE_PAYMENT_REGISTER add column REGISTER_TYPE_ID uuid;
