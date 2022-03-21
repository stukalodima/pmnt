import React, { useCallback, useEffect, RefObject } from "react";
import { Form, Alert, Button, Card, message } from "antd";
import { FormInstance } from "antd/es/form";
import useForm from "antd/lib/form/hooks/useForm";
import { useLocalStore, useObserver } from "mobx-react";
import { PATH, NEW_SUBPATH } from "./PaymentClaimManagement";
import { Link, Redirect } from "react-router-dom";
import { toJS } from "mobx";
import { FormattedMessage, useIntl } from "react-intl";
import {
  defaultHandleFinish,
  createAntdFormValidationMessages
} from "@cuba-platform/react-ui";
import {
  useInstance,
  MainStore,
  useMainStore,
  useReaction
} from "@cuba-platform/react-core";
import { Field, MultilineText, Spinner } from "@cuba-platform/react-ui";
import "../../app/App.css";
import { PaymentClaim } from "../../cuba/entities/finance_PaymentClaim";

type Props = {
  entityId: string;
};

type PaymentClaimEditAssociationOptions = {};

type PaymentClaimEditLocalStore = PaymentClaimEditAssociationOptions & {
  updated: boolean;
  globalErrors: string[];
  formRef: RefObject<FormInstance>;
};

const FIELDS = [
  "onDate",
  "summ",
  "planPaymentDate",
  "paymentPurpose",
  "comment",
  "status"
];

const isNewEntity = (entityId: string) => {
  return entityId === NEW_SUBPATH;
};

const getAssociationOptions = (
  mainStore: MainStore
): PaymentClaimEditAssociationOptions => {
  const { getAttributePermission } = mainStore.security;
  const associationOptions: PaymentClaimEditAssociationOptions = {};

  return associationOptions;
};

const PaymentClaimEdit = (props: Props) => {
  const { entityId } = props;

  const intl = useIntl();
  const mainStore = useMainStore();
  const [form] = useForm();

  const dataInstance = useInstance<PaymentClaim>(PaymentClaim.NAME, {
    view: "_local",
    loadImmediately: false
  });

  const store: PaymentClaimEditLocalStore = useLocalStore(() => ({
    // Association options

    // Other
    updated: false,
    globalErrors: [],
    formRef: React.createRef()
  }));

  useEffect(() => {
    if (isNewEntity(entityId)) {
      dataInstance.current.setItem(new PaymentClaim());
    } else {
      dataInstance.current.load(entityId);
    }
  }, [entityId, dataInstance]);

  // Create a reaction that displays request failed error message
  useReaction(
    () => dataInstance.current.status,
    () => {
      if (
        dataInstance.current.lastError != null &&
        dataInstance.current.lastError !== "COMMIT_ERROR"
      ) {
        message.error(intl.formatMessage({ id: "common.requestFailed" }));
      }
    }
  );

  // Create a reaction that waits for permissions data to be loaded,
  // loads Association options and disposes itself
  useReaction(
    () => mainStore.security.isDataLoaded,
    (isDataLoaded, permsReaction) => {
      if (isDataLoaded === true) {
        // User permissions has been loaded.
        // We can now load association options.
        const associationOptions = getAssociationOptions(mainStore); // Calls REST API
        Object.assign(store, associationOptions);
        permsReaction.dispose();
      }
    },
    { fireImmediately: true }
  );

  // Create a reaction that sets the fields values based on dataInstance.current.item
  useReaction(
    () => [store.formRef.current, dataInstance.current.item],
    ([formInstance]) => {
      if (formInstance != null) {
        form.setFieldsValue(dataInstance.current.getFieldValues(FIELDS));
      }
    },
    { fireImmediately: true }
  );

  const handleFinishFailed = useCallback(() => {
    message.error(
      intl.formatMessage({ id: "management.editor.validationError" })
    );
  }, [intl]);

  const handleFinish = useCallback(
    (values: { [field: string]: any }) => {
      if (form != null) {
        defaultHandleFinish(
          values,
          dataInstance.current,
          intl,
          form,
          isNewEntity(entityId) ? "create" : "edit"
        ).then(({ success, globalErrors }) => {
          if (success) {
            store.updated = true;
          } else {
            store.globalErrors = globalErrors;
          }
        });
      }
    },
    [entityId, intl, form, store.globalErrors, store.updated, dataInstance]
  );

  return useObserver(() => {
    if (store.updated) {
      return <Redirect to={PATH} />;
    }

    if (!mainStore.isEntityDataLoaded()) {
      return <Spinner />;
    }

    const { status, lastError, load } = dataInstance.current;

    // do not stop on "COMMIT_ERROR" - it could be bean validation, so we should show fields with errors
    if (status === "ERROR" && lastError === "LOAD_ERROR") {
      return (
        <>
          <FormattedMessage id="common.requestFailed" />.
          <br />
          <br />
          <Button htmlType="button" onClick={() => load(entityId)}>
            <FormattedMessage id="common.retry" />
          </Button>
        </>
      );
    }

    return (
      <Card className="narrow-layout">
        <Form
          onFinish={handleFinish}
          onFinishFailed={handleFinishFailed}
          layout="vertical"
          ref={store.formRef}
          form={form}
          validateMessages={createAntdFormValidationMessages(intl)}
        >
          <Field
            entityName={PaymentClaim.NAME}
            propertyName="onDate"
            formItemProps={{
              style: { marginBottom: "12px" },
              rules: [{ required: true }]
            }}
          />

          <Field
            entityName={PaymentClaim.NAME}
            propertyName="summ"
            formItemProps={{
              style: { marginBottom: "12px" },
              rules: [{ required: true }]
            }}
          />

          <Field
            entityName={PaymentClaim.NAME}
            propertyName="planPaymentDate"
            formItemProps={{
              style: { marginBottom: "12px" },
              rules: [{ required: true }]
            }}
          />

          <Field
            entityName={PaymentClaim.NAME}
            propertyName="paymentPurpose"
            formItemProps={{
              style: { marginBottom: "12px" },
              rules: [{ required: true }]
            }}
          />

          <Field
            entityName={PaymentClaim.NAME}
            propertyName="comment"
            formItemProps={{
              style: { marginBottom: "12px" }
            }}
          />

          <Field
            entityName={PaymentClaim.NAME}
            propertyName="status"
            formItemProps={{
              style: { marginBottom: "12px" },
              rules: [{ required: true }]
            }}
          />

          {store.globalErrors.length > 0 && (
            <Alert
              message={<MultilineText lines={toJS(store.globalErrors)} />}
              type="error"
              style={{ marginBottom: "24px" }}
            />
          )}

          <Form.Item style={{ textAlign: "center" }}>
            <Link to={PATH}>
              <Button htmlType="button">
                <FormattedMessage id="common.cancel" />
              </Button>
            </Link>
            <Button
              type="primary"
              htmlType="submit"
              disabled={status !== "DONE" && status !== "ERROR"}
              loading={status === "LOADING"}
              style={{ marginLeft: "8px" }}
            >
              <FormattedMessage id="common.submit" />
            </Button>
          </Form.Item>
        </Form>
      </Card>
    );
  });
};

export default PaymentClaimEdit;
