create table FINANCE_ADDRESING_ESCALATION_USERS (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    ADDRESING_DETAIL_ID uuid,
    USER_ID uuid,
    --
    primary key (ID)
);