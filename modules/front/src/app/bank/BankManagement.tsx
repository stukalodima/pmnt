import React, { useCallback } from "react";
import { RouteComponentProps } from "react-router";
import { useLocalStore, useObserver } from "mobx-react";
import BankEdit from "./BankEdit";
import BankList from "./BankList";
import { action } from "mobx";
import { PaginationConfig } from "antd/es/pagination";
import { addPagingParams, createPagingConfig } from "@cuba-platform/react-ui";

type Props = RouteComponentProps<{ entityId?: string }>;

type BankManagementLocalStore = {
  paginationConfig: PaginationConfig;
};

export const PATH = "/bankManagement";
export const NEW_SUBPATH = "new";

export const BankManagement = (props: Props) => {
  const { entityId } = props.match.params;

  const store: BankManagementLocalStore = useLocalStore(() => ({
    paginationConfig: createPagingConfig(props.location.search)
  }));

  const onPagingChange = useCallback(
    action((current: number, pageSize: number) => {
      props.history.push(addPagingParams("bankManagement", current, pageSize));
      store.paginationConfig = { ...store.paginationConfig, current, pageSize };
    }),
    []
  );

  return useObserver(() => {
    return entityId != null ? (
      <BankEdit entityId={entityId} />
    ) : (
      <BankList
        onPagingChange={onPagingChange}
        paginationConfig={store.paginationConfig}
      />
    );
  });
};
