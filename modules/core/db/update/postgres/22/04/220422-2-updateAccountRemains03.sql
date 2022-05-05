alter table FINANCE_ACCOUNT_REMAINS rename column company_id to company_id__u16997 ;
alter table FINANCE_ACCOUNT_REMAINS alter column company_id__u16997 drop not null ;
alter table FINANCE_ACCOUNT_REMAINS drop constraint FK_FINANCE_ACCOUNT_REMAINS_ON_COMPANY ;
drop index IDX_FINANCE_ACCOUNT_REMAINS_ON_COMPANY ;
alter table FINANCE_ACCOUNT_REMAINS rename column bussines_id to bussines_id__u50677 ;
alter table FINANCE_ACCOUNT_REMAINS alter column bussines_id__u50677 drop not null ;
alter table FINANCE_ACCOUNT_REMAINS drop constraint FK_FINANCE_ACCOUNT_REMAINS_ON_BUSSINES ;
drop index IDX_FINANCE_ACCOUNT_REMAINS_ON_BUSSINES ;
