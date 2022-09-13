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