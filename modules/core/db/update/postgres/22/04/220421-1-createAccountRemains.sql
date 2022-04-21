create table FINANCE_ACCOUNT_REMAINS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ON_DATE date not null,
    BUSSINES_ID uuid not null,
    --
    primary key (ID)
);