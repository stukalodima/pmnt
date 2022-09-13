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