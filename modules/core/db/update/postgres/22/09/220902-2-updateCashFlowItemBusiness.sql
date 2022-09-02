alter table FINANCE_CASH_FLOW_ITEM_BUSINESS add column BUSINESS_ID uuid ;
alter table FINANCE_CASH_FLOW_ITEM_BUSINESS alter column COMPANY_ID drop not null ;
