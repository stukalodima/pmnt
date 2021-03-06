alter table FINANCE_ACCOUNT_REMAINS_DETAIL add constraint FK_FINANCE_ACCOUNT_REMAINS_DETAIL_ON_COMPANY foreign key (COMPANY_ID) references FINANCE_COMPANY(ID);
alter table FINANCE_ACCOUNT_REMAINS_DETAIL add constraint FK_FINANCE_ACCOUNT_REMAINS_DETAIL_ON_ACCOUNT foreign key (ACCOUNT_ID) references FINANCE_ACCOUNT(ID);
alter table FINANCE_ACCOUNT_REMAINS_DETAIL add constraint FK_FINANCE_ACCOUNT_REMAINS_DETAIL_ON_ACCOUNT_REMAINS foreign key (ACCOUNT_REMAINS_ID) references FINANCE_ACCOUNT_REMAINS(ID);
create index IDX_FINANCE_ACCOUNT_REMAINS_DETAIL_ON_COMPANY on FINANCE_ACCOUNT_REMAINS_DETAIL (COMPANY_ID);
create index IDX_FINANCE_ACCOUNT_REMAINS_DETAIL_ON_ACCOUNT on FINANCE_ACCOUNT_REMAINS_DETAIL (ACCOUNT_ID);
create index IDX_FINANCE_ACCOUNT_REMAINS_DETAIL_ON_ACCOUNT_REMAINS on FINANCE_ACCOUNT_REMAINS_DETAIL (ACCOUNT_REMAINS_ID);
