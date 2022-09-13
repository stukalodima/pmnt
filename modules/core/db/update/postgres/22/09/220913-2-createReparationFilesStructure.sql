alter table FINANCE_REPARATION_FILES_STRUCTURE add constraint FK_FINANCE_REPARATION_FILES_STRUCTURE_ON_PID foreign key (PID) references FINANCE_REPARATION_FILES_STRUCTURE(ID);
alter table FINANCE_REPARATION_FILES_STRUCTURE add constraint FK_FINANCE_REPARATION_FILES_STRUCTURE_ON_PARTITION foreign key (PARTITION_ID) references FINANCE_PARTITION(ID);
alter table FINANCE_REPARATION_FILES_STRUCTURE add constraint FK_FINANCE_REPARATION_FILES_STRUCTURE_ON_PROPERTY_TYPE foreign key (PROPERTY_TYPE_ID) references FINANCE_PROPERTY_TYPE(ID);
alter table FINANCE_REPARATION_FILES_STRUCTURE add constraint FK_FINANCE_REPARATION_FILES_STRUCTURE_ON_DOCUMENT_TYPE foreign key (DOCUMENT_TYPE_ID) references FINANCE_DOCUMENT_TYPE(ID);
create index IDX_FINANCE_REPARATION_FILES_STRUCTURE_ON_PID on FINANCE_REPARATION_FILES_STRUCTURE (PID);
create index IDX_FINANCE_REPARATION_FILES_STRUCTURE_ON_PARTITION on FINANCE_REPARATION_FILES_STRUCTURE (PARTITION_ID);
create index IDX_FINANCE_REPARATION_FILES_STRUCTURE_ON_PROPERTY_TYPE on FINANCE_REPARATION_FILES_STRUCTURE (PROPERTY_TYPE_ID);
create index IDX_FINANCE_REPARATION_FILES_STRUCTURE_ON_DOCUMENT_TYPE on FINANCE_REPARATION_FILES_STRUCTURE (DOCUMENT_TYPE_ID);
