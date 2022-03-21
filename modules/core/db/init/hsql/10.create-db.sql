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
    NAME varchar(255) not null,
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
    --
    primary key (ID)
)^
-- end FINANCE_PAYMENT_TYPE
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
    NAME varchar(255) not null,
    BUSINESS_ID varchar(36) not null,
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
    FIN_CONTROLER_ID varchar(36),
    FIN_DIRECTOR_ID varchar(36),
    GEN_DIRECTOR_ID varchar(36),
    FIN_CONTROLER_SH_ID varchar(36),
    GEN_DIRECTOR_SH_ID varchar(36),
    USE_PAYMANT_CLAIM boolean,
    USE_PAYMENT_CLAIM_APPROVAL boolean,
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
    APPROVED boolean,
    COMPANY_ID varchar(36) not null,
    CLIENT_ID varchar(36) not null,
    SUMM double precision not null,
    PAYMENT_PURPOSE longvarchar not null,
    CASH_FLOW_ITEM_ID varchar(36) not null,
    PAYMENT_TYPE_ID varchar(36) not null,
    COMMENT_ longvarchar,
    PAYMENT_CLAIM_ID varchar(36),
    PAYMENT_REGISTER_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end FINANCE_PAYMENT_REGISTER_DETAIL
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
    ON_DATE date not null,
    BUSINESS_ID varchar(36) not null,
    COMPANY_ID varchar(36) not null,
    CLIENT_ID varchar(36) not null,
    SUMM double precision not null,
    PLAN_PAYMENT_DATE date not null,
    PAYMENT_PURPOSE longvarchar not null,
    CASH_FLOW_ITEM_ID varchar(36) not null,
    PAYMENT_TYPE_ID varchar(36) not null,
    COMMENT_ longvarchar,
    AUTHOR_ID varchar(36) not null,
    STATUS varchar(50) not null,
    --
    primary key (ID)
)^
-- end FINANCE_PAYMENT_CLAIM
-- begin SEC_USER
-- end SEC_USER
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
    BUSINESS_ID varchar(36) not null,
    COMPANY_ID varchar(36) not null,
    --
    primary key (ID)
)^
-- end FINANCE_USER_PROPERTY
