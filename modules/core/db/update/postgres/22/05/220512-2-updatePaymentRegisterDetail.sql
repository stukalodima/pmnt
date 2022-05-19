alter table FINANCE_PAYMENT_REGISTER_DETAIL rename column comment_ to comment___u45500 ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL rename column payment_type_id to payment_type_id__u67233 ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL alter column payment_type_id__u67233 drop not null ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL drop constraint FK_FINANCE_PAYMENT_REGISTER_DETAIL_ON_PAYMENT_TYPE ;
drop index IDX_FINANCE_PAYMENT_REGISTER_DETAIL_ON_PAYMENT_TYPE ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL rename column cash_flow_item_id to cash_flow_item_id__u90340 ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL alter column cash_flow_item_id__u90340 drop not null ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL drop constraint FK_FINANCE_PAYMENT_REGISTER_DETAIL_ON_CASH_FLOW_ITEM ;
drop index IDX_FINANCE_PAYMENT_REGISTER_DETAIL_ON_CASH_FLOW_ITEM ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL rename column payment_purpose to payment_purpose__u80103 ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL alter column payment_purpose__u80103 drop not null ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL rename column summ to summ__u25508 ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL alter column summ__u25508 drop not null ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL rename column client_id to client_id__u96295 ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL alter column client_id__u96295 drop not null ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL drop constraint FK_FINANCE_PAYMENT_REGISTER_DETAIL_ON_CLIENT ;
drop index IDX_FINANCE_PAYMENT_REGISTER_DETAIL_ON_CLIENT ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL rename column company_id to company_id__u11837 ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL alter column company_id__u11837 drop not null ;
alter table FINANCE_PAYMENT_REGISTER_DETAIL drop constraint FK_FINANCE_PAYMENT_REGISTER_DETAIL_ON_COMPANY ;
drop index IDX_FINANCE_PAYMENT_REGISTER_DETAIL_ON_COMPANY ;