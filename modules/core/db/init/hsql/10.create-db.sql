-- begin FINANCE_CURRENCY
create table FINANCE_CURRENCY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(3) not null,
    SHORT_NAME varchar(3) not null,
    NAME varchar(255) not null,
    --
    primary key (ID)
)^
-- end FINANCE_CURRENCY
-- begin FINANCE_CLIENT
create table FINANCE_CLIENT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SHORT_NAME varchar(255) not null,
    NAME longvarchar not null,
    CLIENT_TYPE varchar(50) not null,
    EDRPOU varchar(20) not null,
    ADDRESS longvarchar,
    KVED longvarchar,
    BOSS varchar(255),
    STAN varchar(255),
    --
    primary key (ID)
)^
-- end FINANCE_CLIENT
-- begin FINANCE_PAYMENT_REGISTER
create table FINANCE_PAYMENT_REGISTER (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NUMBER_ bigint,
    ON_DATE date not null,
    BUSINESS_ID varchar(36) not null,
    STATUS varchar(50),
    AUTHOR_ID varchar(36),
    --
    primary key (ID)
)^
-- end FINANCE_PAYMENT_REGISTER
-- begin FINANCE_PAYMENT_TYPE
create table FINANCE_PAYMENT_TYPE (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    NUMBER integer not null,
    --
    primary key (ID)
)^
-- end FINANCE_PAYMENT_TYPE
-- begin FINANCE_CASH_FLOW_ITEM_BUSINESS
create table FINANCE_CASH_FLOW_ITEM_BUSINESS (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    COMPANY_ID varchar(36) not null,
    CASH_FLOW_ITEM_ID varchar(36),
    --
    primary key (ID)
)^
-- end FINANCE_CASH_FLOW_ITEM_BUSINESS
-- begin FINANCE_BANK
create table FINANCE_BANK (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    MFO varchar(6) not null,
    --
    primary key (ID)
)^
-- end FINANCE_BANK
-- begin FINANCE_COMPANY
create table FINANCE_COMPANY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SHORT_NAME varchar(255) not null,
    NAME longvarchar not null,
    EDRPOU varchar(10) not null,
    BUSINESS_ID varchar(36),
    --
    primary key (ID)
)^
-- end FINANCE_COMPANY
-- begin FINANCE_BUSINESS
create table FINANCE_BUSINESS (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    FIN_DIRECTOR_ID varchar(36),
    MANAGEMENT_COMPANY_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end FINANCE_BUSINESS
-- begin FINANCE_PAYMENT_REGISTER_DETAIL
create table FINANCE_PAYMENT_REGISTER_DETAIL (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    APPROVED varchar(50),
    COMPANY_ID varchar(36) not null,
    CLIENT_ID varchar(36) not null,
    SUMM double precision not null,
    PAYMENT_PURPOSE longvarchar not null,
    CASH_FLOW_ITEM_ID varchar(36) not null,
    PAYMENT_TYPE_ID varchar(36) not null,
    COMMENT_ longvarchar,
    PAYMENT_CLAIM_ID varchar(36),
    PAYMENT_REGISTER_ID varchar(36) not null,
    PAYMENT_STATUS_ROW varchar(50),
    --
    primary key (ID)
)^
-- end FINANCE_PAYMENT_REGISTER_DETAIL
-- begin FINANCE_USER_PROPERTY
create table FINANCE_USER_PROPERTY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USER_ID varchar(36) not null,
    MANAGEMENT_COMPANY_ID varchar(36) not null,
    BUSINESS_ID varchar(36) not null,
    COMPANY_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end FINANCE_USER_PROPERTY
-- begin FINANCE_ACCOUNT
create table FINANCE_ACCOUNT (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    COMPANY_ID varchar(36) not null,
    NAME varchar(255) not null,
    CURRENCY_ID varchar(36) not null,
    IBAN varchar(29) not null,
    BANK_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end FINANCE_ACCOUNT
-- begin FINANCE_CASH_FLOW_ITEM
create table FINANCE_CASH_FLOW_ITEM (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    NUMBER integer not null,
    PAYMENT_REGISTER_TYPE_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end FINANCE_CASH_FLOW_ITEM
-- begin FINANCE_PAYMENT_CLAIM
create table FINANCE_PAYMENT_CLAIM (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NUMBER_ bigint,
    ON_DATE date not null,
    BUSINESS_ID varchar(36) not null,
    COMPANY_ID varchar(36) not null,
    CLIENT_ID varchar(36) not null,
    ACCOUNT_ID varchar(36) not null,
    CURRENCY_ID varchar(36) not null,
    SUMM double precision,
    PLAN_PAYMENT_DATE date not null,
    PAYMENT_PURPOSE longvarchar not null,
    CASH_FLOW_ITEM_ID varchar(36) not null,
    CASH_FLOW_ITEM_BUSINESS_ID varchar(36),
    PAYMENT_TYPE_ID varchar(36) not null,
    COMMENT_ longvarchar,
    AUTHOR_ID varchar(36) not null,
    STATUS varchar(50) not null,
    --
    primary key (ID)
)^
-- end FINANCE_PAYMENT_CLAIM
-- begin FINANCE_MANAGEMENT_COMPANY
create table FINANCE_MANAGEMENT_COMPANY (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SHORT_NAME varchar(255) not null,
    NAME longvarchar not null,
    FIN_CONTROLER_ID varchar(36),
    FIN_DIRECTOR_ID varchar(36),
    --
    primary key (ID)
)^
-- end FINANCE_MANAGEMENT_COMPANY
