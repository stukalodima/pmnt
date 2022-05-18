alter table FINANCE_PAYMENT_CLAIM rename column status to status__u62880 ;
alter table FINANCE_PAYMENT_CLAIM alter column status__u62880 drop not null ;
alter table FINANCE_PAYMENT_CLAIM add column STATUS_ID uuid ;
