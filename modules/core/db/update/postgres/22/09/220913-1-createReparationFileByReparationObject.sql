create table FINANCE_REPARATION_FILE_BY_REPARATION_OBJECT (
    ID uuid,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    REPARATION_FILE_ID uuid,
    REPARATION_OBJECT_ID uuid,
    --
    primary key (ID)
);