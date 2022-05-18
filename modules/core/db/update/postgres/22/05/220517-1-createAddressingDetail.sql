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
    USER_ID uuid not null,
    ADDRESSING_ID uuid not null,
    --
    primary key (ID)
);