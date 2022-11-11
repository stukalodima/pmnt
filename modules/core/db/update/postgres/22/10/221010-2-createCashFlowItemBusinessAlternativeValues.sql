alter table FINANCE_CASH_FLOW_ITEM_BUSINESS_ALTERNATIVE_VALUES add constraint FK_FINANCE_CASHFLOWITEMBUSIALTEVALU_ON_CASH_FLOW_ITEM_BUSINESS foreign key (CASH_FLOW_ITEM_BUSINESS_ID) references FINANCE_CASH_FLOW_ITEM_BUSINESS(ID);
alter table FINANCE_CASH_FLOW_ITEM_BUSINESS_ALTERNATIVE_VALUES add constraint FK_FINANCE_CASHFLOWITEMBUSIALTEVALU_ON_CASH_FLOW_ITEM foreign key (CASH_FLOW_ITEM_ID) references FINANCE_CASH_FLOW_ITEM(ID);
create index IDX_FINANCE_CASHFLOWITEMBUSIALTEVALU_ON_CASHFLOWITEMBUSI on FINANCE_CASH_FLOW_ITEM_BUSINESS_ALTERNATIVE_VALUES (CASH_FLOW_ITEM_BUSINESS_ID);
create index IDX_FINANCE_CASHFLOWITEMBUSIALTEVALU_ON_CASH_FLOW_ITEM on FINANCE_CASH_FLOW_ITEM_BUSINESS_ALTERNATIVE_VALUES (CASH_FLOW_ITEM_ID);