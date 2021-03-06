alter table FINANCE_ADDRESSING add constraint FK_FINANCE_ADDRESSING_ON_BUSSINES foreign key (BUSSINES_ID) references FINANCE_BUSINESS(ID);
alter table FINANCE_ADDRESSING add constraint FK_FINANCE_ADDRESSING_ON_PROC_DEFINITION foreign key (PROC_DEFINITION_ID) references BPM_PROC_DEFINITION(ID);
alter table FINANCE_ADDRESSING add constraint FK_FINANCE_ADDRESSING_ON_COMPANY foreign key (COMPANY_ID) references FINANCE_COMPANY(ID);
create index IDX_FINANCE_ADDRESSING_ON_BUSSINES on FINANCE_ADDRESSING (BUSSINES_ID);
create index IDX_FINANCE_ADDRESSING_ON_PROC_DEFINITION on FINANCE_ADDRESSING (PROC_DEFINITION_ID);
create index IDX_FINANCE_ADDRESSING_ON_COMPANY on FINANCE_ADDRESSING (COMPANY_ID);
