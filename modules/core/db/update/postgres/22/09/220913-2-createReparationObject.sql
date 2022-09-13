alter table FINANCE_REPARATION_OBJECT add constraint FK_FINANCE_REPARATION_OBJECT_ON_REPARATION_OBJECT_STATE foreign key (REPARATION_OBJECT_STATE_ID) references FINANCE_REPARATION_OBJECT_STATE(ID);
alter table FINANCE_REPARATION_OBJECT add constraint FK_FINANCE_REPARATION_OBJECT_ON_PROPERTY_TYPE foreign key (PROPERTY_TYPE_ID) references FINANCE_PROPERTY_TYPE(ID);
create index IDX_FINANCE_REPARATION_OBJECT_ON_REPARATION_OBJECT_STATE on FINANCE_REPARATION_OBJECT (REPARATION_OBJECT_STATE_ID);
create index IDX_FINANCE_REPARATION_OBJECT_ON_PROPERTY_TYPE on FINANCE_REPARATION_OBJECT (PROPERTY_TYPE_ID);
