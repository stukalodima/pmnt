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