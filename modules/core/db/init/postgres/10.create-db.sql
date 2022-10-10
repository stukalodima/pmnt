-- begin FINANCE_CLIENT
create table FINANCE_CLIENT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SHORT_NAME text not null,
    NAME text not null,
    CLIENT_TYPE varchar(50) not null,
    EDRPOU varchar(20) not null,
    ADDRESS text,
    KVED text,
    BOSS text,
    STAN varchar(255),
    --
    primary key (ID)
);
-- end FINANCE_CLIENT
-- begin FINANCE_PAYMENT_REGISTER
create table FINANCE_PAYMENT_REGISTER (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NUMBER_ bigint,
    SET_EXPRESS_FIELD boolean,
    EXPRESS boolean,
    ON_DATE date not null,
    BUSINESS_ID uuid not null,
    STATUS_ID uuid,
    PAYED_STATUS varchar(50),
    AUTHOR_ID uuid,
    REGISTER_TYPE_ID uuid,
    PROC_INSTANCE_ID uuid,
    SUMMA varchar(255),
    --
    primary key (ID)
)^
-- end FINANCE_PAYMENT_REGISTER
-- begin FINANCE_PAYMENT_TYPE
create table FINANCE_PAYMENT_TYPE
(
    ID         uuid,
    VERSION    integer      not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    NAME       varchar(255) not null,
    NUMBER     integer      not null,
    --
    primary key (ID)
);
-- end FINANCE_PAYMENT_TYPE
-- begin FINANCE_COMPANY
create table FINANCE_COMPANY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    SHORT_NAME varchar(255) not null,
    NAME text not null,
    EDRPOU varchar(10) not null,
    BUSINESS_ID uuid,
    INTEGRATION_ENABLE boolean,
    DATE_START_INTEGRATION date,
    --
    primary key (ID)
);
-- end FINANCE_COMPANY
-- begin FINANCE_BUSINESS
create table FINANCE_BUSINESS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    FIN_DIRECTOR_ID uuid,
    MANAGEMENT_COMPANY_ID uuid not null,
    PARENT_ID uuid,
    --
    primary key (ID)
);
-- end FINANCE_BUSINESS
-- begin FINANCE_PAYMENT_REGISTER_DETAIL
create table FINANCE_PAYMENT_REGISTER_DETAIL (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    APPROVED varchar(50),
    PAYED varchar(50),
    SUMA_TO_PAY double precision,
    PAYED_DATE timestamp,
    PAYED_SUMA double precision,
    PAYMENT_CLAIM_ID uuid,
    PAYMENT_REGISTER_ID uuid not null,
    COMMENT_ text,
    --
    primary key (ID)
);
-- end FINANCE_PAYMENT_REGISTER_DETAIL
-- begin FINANCE_USER_PROPERTY
create table FINANCE_USER_PROPERTY (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    USER_ID uuid not null,
    MANAGEMENT_COMPANY_ID uuid not null,
    BUSINESS_ID uuid not null,
    COMPANY_ID uuid not null,
    DONT_SEND_EMAIL_BY_TASK boolean,
    DONT_SEND_EMAIL_BY_APPROVAL_RESULT boolean,
    SEND_NOTIFICATION_TASK boolean,
    --
    primary key (ID)
);
-- end FINANCE_USER_PROPERTY
-- begin FINANCE_CASH_FLOW_ITEM
create table FINANCE_CASH_FLOW_ITEM
(
    ID         uuid,
    VERSION    integer      not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    NAME       varchar(255) not null,
    NUMBER     integer      not null,
    --
    primary key (ID)
);
-- end FINANCE_CASH_FLOW_ITEM
-- begin FINANCE_PAYMENT_CLAIM
create table FINANCE_PAYMENT_CLAIM (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NUMBER_ bigint,
    DOC_NUMBER varchar(255),
    CLIENT_ACCOUNT varchar(255),
    ON_DATE date,
    BUSINESS_ID uuid,
    COMPANY_ID uuid,
    CLIENT_ID uuid,
    ACCOUNT_ID uuid,
    CURRENCY_ID uuid,
    SUMM double precision,
    PLAN_PAYMENT_DATE date,
    PAYMENT_PURPOSE text,
    CASH_FLOW_ITEM_ID uuid,
    CASH_FLOW_ITEM_BUSINESS_ID uuid,
    PAYMENT_TYPE_ID uuid,
    COMMENT_ text,
    AUTHOR_ID uuid,
    STATUS_ID uuid,
    EXPRESS boolean,
    FROM_EXTERNAL_SYSTEM boolean,
    BUDGET_ANALITIC text,
    --
    primary key (ID)
)^
-- end FINANCE_PAYMENT_CLAIM
-- begin FINANCE_MANAGEMENT_COMPANY
create table FINANCE_MANAGEMENT_COMPANY
(
    ID               uuid,
    VERSION          integer      not null,
    CREATE_TS        timestamp,
    CREATED_BY       varchar(50),
    UPDATE_TS        timestamp,
    UPDATED_BY       varchar(50),
    DELETE_TS        timestamp,
    DELETED_BY       varchar(50),
    --
    SHORT_NAME       varchar(255) not null,
    NAME             text         not null,
    FIN_CONTROLER_ID uuid,
    FIN_DIRECTOR_ID  uuid,
    --
    primary key (ID)
);
-- end FINANCE_MANAGEMENT_COMPANY

-- begin FINANCE_CASH_FLOW_ITEM_BUSINESS
create table FINANCE_CASH_FLOW_ITEM_BUSINESS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    BUSINESS_ID uuid,
    COMPANY_ID uuid,
    CASH_FLOW_ITEM_ID uuid,
    CHECK_CASH_FLOW_ITEM boolean,
    --
    primary key (ID)
)^
-- end FINANCE_CASH_FLOW_ITEM_BUSINESS
-- begin FINANCE_BANK
create table FINANCE_BANK (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    FULL_NAME text,
    MFO varchar(6) not null,
    STAN varchar(255),
    PARENT_BANK_ID uuid,
    --
    primary key (ID)
);
-- end FINANCE_BANK
-- begin FINANCE_CURRENCY
create table FINANCE_CURRENCY
(
    ID            uuid,
    VERSION       integer      not null,
    CREATE_TS     timestamp,
    CREATED_BY    varchar(50),
    UPDATE_TS     timestamp,
    UPDATED_BY    varchar(50),
    DELETE_TS     timestamp,
    DELETED_BY    varchar(50),
    --
    CODE          varchar(3)   not null,
    SHORT_NAME    varchar(3)   not null,
    NAME          varchar(255) not null,
    BASE_CURRENCY boolean,
    --
    primary key (ID)
);
-- end FINANCE_CURRENCY
-- begin FINANCE_ACCOUNT
create table FINANCE_ACCOUNT
(
    ID          uuid,
    VERSION     integer     not null,
    CREATE_TS   timestamp,
    CREATED_BY  varchar(50),
    UPDATE_TS   timestamp,
    UPDATED_BY  varchar(50),
    DELETE_TS   timestamp,
    DELETED_BY  varchar(50),
    --
    COMPANY_ID  uuid        not null,
    TYPE_ID     uuid,
    CURRENCY_ID uuid        not null,
    IBAN        varchar(50) not null,
    BANK_ID     uuid        not null,
    START_DATE  date,
    END_DATE    date,
    LOCK_       boolean,
    CLOSE_      boolean,
    --
    primary key (ID)
);
-- end FINANCE_ACCOUNT
-- begin FINANCE_ACCOUNT_REMAINS
create table FINANCE_ACCOUNT_REMAINS
(
    ID              uuid,
    VERSION         integer not null,
    CREATE_TS       timestamp,
    CREATED_BY      varchar(50),
    UPDATE_TS       timestamp,
    UPDATED_BY      varchar(50),
    DELETE_TS       timestamp,
    DELETED_BY      varchar(50),
    --
    ON_DATE         date    not null,
    ACCOUNT_ID      uuid    not null,
    SUMM_BEFOR      double precision,
    SUMM            double precision,
    SUMM_IN_UAH     double precision,
    SUMM_IN_USD     double precision,
    SUM_IN_EUR      double precision,
    SUMM_EQUALS_UAH double precision,
    --
    primary key (ID)
);
-- end FINANCE_ACCOUNT_REMAINS
-- begin FINANCE_REGISTER_TYPE_DETAIL
create table FINANCE_REGISTER_TYPE_DETAIL
(
    ID                uuid,
    VERSION           integer not null,
    CREATE_TS         timestamp,
    CREATED_BY        varchar(50),
    UPDATE_TS         timestamp,
    UPDATED_BY        varchar(50),
    DELETE_TS         timestamp,
    DELETED_BY        varchar(50),
    --
    CASH_FLOW_ITEM_ID uuid    not null,
    USE_CONDITION     boolean,
    CONDITION_        varchar(255),
    CONDITION_VALUE   double precision,
    REGISTER_TYPE_ID  uuid    not null,
    --
    primary key (ID)
);
-- end FINANCE_REGISTER_TYPE_DETAIL
-- begin FINANCE_REGISTER_TYPE
create table FINANCE_REGISTER_TYPE
(
    ID                 uuid,
    VERSION            integer      not null,
    CREATE_TS          timestamp,
    CREATED_BY         varchar(50),
    UPDATE_TS          timestamp,
    UPDATED_BY         varchar(50),
    DELETE_TS          timestamp,
    DELETED_BY         varchar(50),
    --
    NAME               varchar(255) not null,
    EXPRESS            boolean,
    PROC_DEFINITION_ID uuid,
    USE_CONDITION      boolean,
    CONDITION_         varchar(255),
    CONDITION_VALUE    double precision,
    --
    primary key (ID)
);
-- end FINANCE_REGISTER_TYPE
-- begin FINANCE_ACCOUNT_TYPE
create table FINANCE_ACCOUNT_TYPE
(
    ID         uuid,
    VERSION    integer not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    NAME       varchar(255),
    --
    primary key (ID)
);
-- end FINANCE_ACCOUNT_TYPE
-- begin FINANCE_CURRENCY_RATE
create table FINANCE_CURRENCY_RATE
(
    ID           uuid,
    VERSION      integer not null,
    CREATE_TS    timestamp,
    CREATED_BY   varchar(50),
    UPDATE_TS    timestamp,
    UPDATED_BY   varchar(50),
    DELETE_TS    timestamp,
    DELETED_BY   varchar(50),
    --
    ON_DATE      date,
    CURRENCY_ID  uuid,
    MULTIPLICITY integer,
    RATE         double precision,
    --
    primary key (ID)
);
-- end FINANCE_CURRENCY_RATE
-- begin FINANCE_ADDRESSING
create table FINANCE_ADDRESSING
(
    ID                 uuid,
    VERSION            integer not null,
    CREATE_TS          timestamp,
    CREATED_BY         varchar(50),
    UPDATE_TS          timestamp,
    UPDATED_BY         varchar(50),
    DELETE_TS          timestamp,
    DELETED_BY         varchar(50),
    --
    BUSSINES_ID        uuid    not null,
    PROC_DEFINITION_ID uuid    not null,
    USE_COMPANY        boolean,
    COMPANY_ID         uuid,
    --
    primary key (ID)
);
-- end FINANCE_ADDRESSING
-- begin FINANCE_ADDRESSING_DETAIL
create table FINANCE_ADDRESSING_DETAIL (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    PROC_ROLE_ID uuid not null,
    USER_ID uuid,
    AUTO boolean,
    ADDRESSING_ID uuid not null,
    AUTODETECT boolean,
    --
    primary key (ID)
);
-- end FINANCE_ADDRESSING_DETAIL
-- begin FINANCE_PROC_STATUS
create table FINANCE_PROC_STATUS
(
    ID         uuid,
    VERSION    integer      not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    CODE       varchar(255) not null,
    NAME       varchar(255) not null,
    IS_NEW     boolean,
    IS_START   boolean,
    --
    primary key (ID)
);
-- end FINANCE_PROC_STATUS
-- begin BPM_PROC_INSTANCE
alter table BPM_PROC_INSTANCE
    add column PAYMENT_REGISTER_ID uuid;
alter table BPM_PROC_INSTANCE
    add column DTYPE varchar(31);
update BPM_PROC_INSTANCE
set DTYPE = 'finance_ExtProcInstance'
where DTYPE is null;
-- end BPM_PROC_INSTANCE
-- begin FINANCE_BUSINESS_OPERATORS
create table FINANCE_BUSINESS_OPERATORS
(
    ID          uuid,
    VERSION     integer not null,
    CREATE_TS   timestamp,
    CREATED_BY  varchar(50),
    UPDATE_TS   timestamp,
    UPDATED_BY  varchar(50),
    DELETE_TS   timestamp,
    DELETED_BY  varchar(50),
    --
    USER_ID     uuid,
    BUSINESS_ID uuid    not null,
    --
    primary key (ID)
);
-- end FINANCE_BUSINESS_OPERATORS
-- begin FINANCE_BUSINESS_CONTROLLERS
create table FINANCE_BUSINESS_CONTROLLERS
(
    ID          uuid,
    VERSION     integer not null,
    CREATE_TS   timestamp,
    CREATED_BY  varchar(50),
    UPDATE_TS   timestamp,
    UPDATED_BY  varchar(50),
    DELETE_TS   timestamp,
    DELETED_BY  varchar(50),
    --
    USER_ID     uuid,
    BUSINESS_ID uuid    not null,
    --
    primary key (ID)
);
-- end FINANCE_BUSINESS_CONTROLLERS
-- begin FINANCE_PARTITION
create table FINANCE_PARTITION (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (ID)
);
-- end FINANCE_PARTITION
-- begin FINANCE_PROPERTY_TYPE
create table FINANCE_PROPERTY_TYPE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (ID)
);
-- end FINANCE_PROPERTY_TYPE
-- begin FINANCE_DOCUMENT_TYPE
create table FINANCE_DOCUMENT_TYPE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (ID)
);
-- end FINANCE_DOCUMENT_TYPE
-- begin FINANCE_REPARATION_FILE
create table FINANCE_REPARATION_FILE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    DOCUMENT uuid,
    BUSINESS_ID uuid,
    COMPANY_ID uuid,
    PARTITION_ID uuid,
    PROPERTY_TYPE_ID uuid,
    DOCUMENT_TYPE_ID uuid,
    REPARATION_OBJECT uuid,
    --
    primary key (ID)
);
-- end FINANCE_REPARATION_FILE
-- begin FINANCE_REPARATION_FILES_STRUCTURE
create table FINANCE_REPARATION_FILES_STRUCTURE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    PID uuid,
    PARTITION_ID uuid,
    PROPERTY_TYPE_ID uuid,
    DOCUMENT_TYPE_ID uuid,
    --
    primary key (ID)
);
-- end FINANCE_REPARATION_FILES_STRUCTURE
-- begin FINANCE_REPARATION_OBJECT
create table FINANCE_REPARATION_OBJECT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    INV_NUMBER varchar(255),
    DESCRIPTION text,
    REPARATION_OBJECT_STATE_ID uuid,
    PROPERTY_TYPE_ID uuid,
    --
    primary key (ID)
);
-- end FINANCE_REPARATION_OBJECT
-- begin FINANCE_REPARATION_OBJECT_STATE
create table FINANCE_REPARATION_OBJECT_STATE (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    --
    primary key (ID)
);
-- end FINANCE_REPARATION_OBJECT_STATE
-- begin FINANCE_CASH_FLOW_ITEM_BUSINESS_ALTERNATIVE_VALUES
create table FINANCE_CASH_FLOW_ITEM_BUSINESS_ALTERNATIVE_VALUES (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CASH_FLOW_ITEM_BUSINESS_ID uuid,
    CASH_FLOW_ITEM_ID uuid,
    --
    primary key (ID)
)^
-- end FINANCE_CASH_FLOW_ITEM_BUSINESS_ALTERNATIVE_VALUES
