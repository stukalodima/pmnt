update FINANCE_PAYMENT_CLAIM set SUMM = 0 where SUMM is null ;
alter table FINANCE_PAYMENT_CLAIM alter column SUMM set not null ;
