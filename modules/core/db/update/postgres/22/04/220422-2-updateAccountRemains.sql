alter table FINANCE_ACCOUNT_REMAINS add column SUMM double precision ;
-- alter table FINANCE_ACCOUNT_REMAINS add column ACCOUNT_ID uuid ^
-- update FINANCE_ACCOUNT_REMAINS set ACCOUNT_ID = <default_value> ;
-- alter table FINANCE_ACCOUNT_REMAINS alter column ACCOUNT_ID set not null ;
alter table FINANCE_ACCOUNT_REMAINS add column ACCOUNT_ID uuid not null ;
-- alter table FINANCE_ACCOUNT_REMAINS add column COMPANY_ID uuid ^
-- update FINANCE_ACCOUNT_REMAINS set COMPANY_ID = <default_value> ;
-- alter table FINANCE_ACCOUNT_REMAINS alter column COMPANY_ID set not null ;
alter table FINANCE_ACCOUNT_REMAINS add column COMPANY_ID uuid not null ;
