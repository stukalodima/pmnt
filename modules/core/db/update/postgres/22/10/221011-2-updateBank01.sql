alter table FINANCE_BANK add constraint FK_FINANCE_BANK_ON_BANK_GROUP foreign key (BANK_GROUP_ID) references FINANCE_BANK_GROUP(ID);
create index IDX_FINANCE_BANK_ON_BANK_GROUP on FINANCE_BANK (BANK_GROUP_ID);