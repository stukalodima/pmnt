alter table FINANCE_ACCOUNT rename column name to name__u81010 ;
alter table FINANCE_ACCOUNT alter column name__u81010 drop not null ;
alter table FINANCE_ACCOUNT add column TYPE_ID uuid ;
