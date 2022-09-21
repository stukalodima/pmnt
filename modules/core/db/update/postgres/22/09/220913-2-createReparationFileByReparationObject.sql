alter table FINANCE_REPARATION_FILE_BY_REPARATION_OBJECT add constraint FK_FINANCE_REPARAFILEBYREPARAOBJECT_ON_REPARATION_FILE foreign key (REPARATION_FILE_ID) references FINANCE_REPARATION_FILE(ID);
alter table FINANCE_REPARATION_FILE_BY_REPARATION_OBJECT add constraint FK_FINANCE_REPARAFILEBYREPARAOBJECT_ON_REPARATION_OBJECT foreign key (REPARATION_OBJECT_ID) references FINANCE_REPARATION_OBJECT(ID);
create index IDX_FINANCE_REPARAFILEBYREPARAOBJECT_ON_REPARATION_FILE on FINANCE_REPARATION_FILE_BY_REPARATION_OBJECT (REPARATION_FILE_ID);
create index IDX_FINANCE_REPARAFILEBYREPARAOBJECT_ON_REPARATION_OBJECT on FINANCE_REPARATION_FILE_BY_REPARATION_OBJECT (REPARATION_OBJECT_ID);