alter table FINANCE_REPARATION_FILE add constraint FK_FINANCE_REPARATION_FILE_ON_DOCUMENT foreign key (DOCUMENT) references SYS_FILE(ID);
alter table FINANCE_REPARATION_FILE add constraint FK_FINANCE_REPARATION_FILE_ON_BUSINESS foreign key (BUSINESS_ID) references FINANCE_BUSINESS(ID);
alter table FINANCE_REPARATION_FILE add constraint FK_FINANCE_REPARATION_FILE_ON_COMPANY foreign key (COMPANY_ID) references FINANCE_COMPANY(ID);
alter table FINANCE_REPARATION_FILE add constraint FK_FINANCE_REPARATION_FILE_ON_PARTITION foreign key (PARTITION_ID) references FINANCE_PARTITION(ID);
alter table FINANCE_REPARATION_FILE add constraint FK_FINANCE_REPARATION_FILE_ON_PROPERTY_TYPE foreign key (PROPERTY_TYPE_ID) references FINANCE_PROPERTY_TYPE(ID);
alter table FINANCE_REPARATION_FILE add constraint FK_FINANCE_REPARATION_FILE_ON_DOCUMENT_TYPE foreign key (DOCUMENT_TYPE_ID) references FINANCE_DOCUMENT_TYPE(ID);
alter table FINANCE_REPARATION_FILE add constraint FK_FINANCE_REPARATION_FILE_ON_REPARATION_OBJECT foreign key (REPARATION_OBJECT) references FINANCE_REPARATION_OBJECT(ID);
create index IDX_FINANCE_REPARATION_FILE_ON_DOCUMENT on FINANCE_REPARATION_FILE (DOCUMENT);
create index IDX_FINANCE_REPARATION_FILE_ON_BUSINESS on FINANCE_REPARATION_FILE (BUSINESS_ID);
create index IDX_FINANCE_REPARATION_FILE_ON_COMPANY on FINANCE_REPARATION_FILE (COMPANY_ID);
create index IDX_FINANCE_REPARATION_FILE_ON_PARTITION on FINANCE_REPARATION_FILE (PARTITION_ID);
create index IDX_FINANCE_REPARATION_FILE_ON_PROPERTY_TYPE on FINANCE_REPARATION_FILE (PROPERTY_TYPE_ID);
create index IDX_FINANCE_REPARATION_FILE_ON_DOCUMENT_TYPE on FINANCE_REPARATION_FILE (DOCUMENT_TYPE_ID);
create index IDX_FINANCE_REPARATION_FILE_ON_REPARATION_OBJECT on FINANCE_REPARATION_FILE (REPARATION_OBJECT);
