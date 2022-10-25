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
);