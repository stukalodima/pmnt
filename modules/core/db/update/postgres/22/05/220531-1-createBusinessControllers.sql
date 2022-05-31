create table FINANCE_BUSINESS_CONTROLLERS
(
    ID          uuid,
    VERSION     integer not null,
    CREATE_TS   timestamp,
    CREATED_BY  varchar(50),
    UPDATE_TS   timestamp,
    UPDATED_BY  varchar(50),
    DELETE_TS   timestamp,
    DELETED_BY  varchar(50),
    --
    USER_ID     uuid,
    BUSINESS_ID uuid    not null,
    --
    primary key (ID)
);