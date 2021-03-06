alter table FINANCE_REGISTER_TYPE_DETAIL add constraint FK_FINANCE_REGISTER_TYPE_DETAIL_ON_CASH_FLOW_ITEM foreign key (CASH_FLOW_ITEM_ID) references FINANCE_CASH_FLOW_ITEM(ID);
alter table FINANCE_REGISTER_TYPE_DETAIL add constraint FK_FINANCE_REGISTER_TYPE_DETAIL_ON_REGISTER_TYPE foreign key (REGISTER_TYPE_ID) references FINANCE_REGISTER_TYPE(ID);
create index IDX_FINANCE_REGISTER_TYPE_DETAIL_ON_CASH_FLOW_ITEM on FINANCE_REGISTER_TYPE_DETAIL (CASH_FLOW_ITEM_ID);
create index IDX_FINANCE_REGISTER_TYPE_DETAIL_ON_REGISTER_TYPE on FINANCE_REGISTER_TYPE_DETAIL (REGISTER_TYPE_ID);
