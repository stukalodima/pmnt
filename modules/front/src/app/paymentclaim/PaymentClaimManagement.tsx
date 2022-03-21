import React, { useCallback } from "react";
import { RouteComponentProps } from "react-router";
import { useLocalStore, useObserver } from "mobx-react";
import PaymentClaimEdit from "./PaymentClaimEdit";
import PaymentClaimList from "./PaymentClaimList";
import { action } from "mobx";
import { PaginationConfig } from "antd/es/pagination";
import { addPagingParams, createPagingConfig } from "@cuba-platform/react-ui";

type Props = RouteComponentProps<{ entityId?: string }>;

type PaymentClaimManagementLocalStore = {
  paginationConfig: PaginationConfig;
};

export const PATH = "/paymentClaimManagement";
export const NEW_SUBPATH = "new";

export const PaymentClaimManagement = (props: Props) => {
  const { entityId } = props.match.params;

  const store: PaymentClaimManagementLocalStore = useLocalStore(() => ({
    paginationConfig: createPagingConfig(props.location.search)
  }));

  const onPagingChange = useCallback(
    action((current: number, pageSize: number) => {
      props.history.push(
        addPagingParams("paymentClaimManagement", current, pageSize)
      );
      store.paginationConfig = { ...store.paginationConfig, current, pageSize };
    }),
    []
  );

  return useObserver(() => {
    return entityId != null ? (
      <PaymentClaimEdit entityId={entityId} />
    ) : (
      <PaymentClaimList
        onPagingChange={onPagingChange}
        paginationConfig={store.paginationConfig}
      />
    );
  });
};
