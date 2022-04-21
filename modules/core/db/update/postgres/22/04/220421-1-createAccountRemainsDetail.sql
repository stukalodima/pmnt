create table FINANCE_ACCOUNT_REMAINS_DETAIL (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    COMPANY_ID uuid not null,
    ACCOUNT_ID uuid not null,
    SUMM double precision,
    ACCOUNT_REMAINS_ID uuid not null,
    --
    primary key (ID)
);