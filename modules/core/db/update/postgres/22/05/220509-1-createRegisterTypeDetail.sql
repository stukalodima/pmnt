create table FINANCE_REGISTER_TYPE_DETAIL (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CASH_FLOW_ITEM_ID uuid not null,
    REGISTER_TYPE_ID uuid not null,
    --
    primary key (ID)
);