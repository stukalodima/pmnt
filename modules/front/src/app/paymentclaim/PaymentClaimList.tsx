import React, { useEffect, useCallback } from "react";
import { useObserver } from "mobx-react";
import { Link } from "react-router-dom";
import { DeleteOutlined, EditOutlined, PlusOutlined } from "@ant-design/icons";
import { Modal, Button, Card, message } from "antd";
import {
  useCollection,
  useMainStore,
  useReaction,
  EntityPermAccessControl
} from "@cuba-platform/react-core";
import {
  EntityProperty,
  Paging,
  setPagination,
  Spinner
} from "@cuba-platform/react-ui";
import { PaymentClaim } from "../../cuba/entities/finance_PaymentClaim";
import { SerializedEntity, getStringId } from "@cuba-platform/rest";
import { PATH, NEW_SUBPATH } from "./PaymentClaimManagement";
import { FormattedMessage, useIntl } from "react-intl";
import { PaginationConfig } from "antd/es/pagination";

type Props = {
  paginationConfig: PaginationConfig;
  onPagingChange: (current: number, pageSize: number) => void;
};

const FIELDS = [
  "number",
  "onDate",
  "business",
  "company",
  "client",
  "cashFlowItem",
  "currency",
  "summ",
  "planPaymentDate",
  "paymentPurpose",
  "paymentType",
  "comment",
  "status",
  "author"
];

const PaymentClaimList = (props: Props) => {
  const { paginationConfig, onPagingChange } = props;

  const intl = useIntl();
  const mainStore = useMainStore();

  const dataCollection = useCollection<PaymentClaim>(PaymentClaim.NAME, {
    view: "paymentClaimReact-view",
    sort: "-updateTs",
    loadImmediately: false
  });

  useEffect(() => {
    setPagination(paginationConfig, dataCollection.current, true);
  }, [paginationConfig, dataCollection]);

  useReaction(
    () => dataCollection.current.status,
    status => {
      if (status === "ERROR") {
        message.error(intl.formatMessage({ id: "common.requestFailed" }));
      }
    }
  );

  const showDeletionDialog = useCallback(
    (e: SerializedEntity<PaymentClaim>) => {
      Modal.confirm({
        title: intl.formatMessage(
          { id: "management.browser.delete.areYouSure" },
          { instanceName: e._instanceName }
        ),
        okText: intl.formatMessage({
          id: "management.browser.delete.ok"
        }),
        cancelText: intl.formatMessage({ id: "common.cancel" }),
        onOk: () => {
          return dataCollection.current.delete(e);
        }
      });
    },
    [intl, dataCollection]
  );

  return useObserver(() => {
    const { status, items, count } = dataCollection.current;

    if (status === "LOADING" || mainStore?.isEntityDataLoaded() !== true) {
      return <Spinner />;
    }

    return (
      <div className="narrow-layout">
        <EntityPermAccessControl
          entityName={PaymentClaim.NAME}
          operation="create"
        >
          <div style={{ marginBottom: "12px" }}>
            <Link to={PATH + "/" + NEW_SUBPATH}>
              <Button htmlType="button" type="primary" icon={<PlusOutlined />}>
                <span>
                  <FormattedMessage id="common.create" />
                </span>
              </Button>
            </Link>
          </div>
        </EntityPermAccessControl>
        {items == null || items.length === 0 ? (
          <p>
            <FormattedMessage id="management.browser.noItems" />
          </p>
        ) : null}
        {items.map(e => (
          <Card
            title={e._instanceName}
            key={e.id ? getStringId(e.id) : undefined}
            style={{ marginBottom: "12px" }}
            actions={[
              <DeleteOutlined
                key="delete"
                onClick={() => showDeletionDialog(e)}
              />,
              <Link to={PATH + "/" + getStringId(e.id!)} key="edit">
                <EditOutlined />
              </Link>
            ]}
          >
            {FIELDS.map(p => (
              <EntityProperty
                entityName={PaymentClaim.NAME}
                propertyName={p}
                value={e[p]}
                key={p}
              />
            ))}
          </Card>
        ))}

        {!paginationConfig.disabled && (
          <div style={{ margin: "12px 0 12px 0", float: "right" }}>
            <Paging
              paginationConfig={paginationConfig}
              onPagingChange={onPagingChange}
              total={count}
            />
          </div>
        )}
      </div>
    );
  });
};

export default PaymentClaimList;
