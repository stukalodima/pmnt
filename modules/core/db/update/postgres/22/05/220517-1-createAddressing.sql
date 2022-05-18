create table FINANCE_ADDRESSING (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    BUSSINES_ID uuid not null,
    PROC_DEFINITION_ID uuid not null,
    COMPANY_ID uuid,
    --
    primary key (ID)
);