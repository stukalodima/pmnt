import { PaymentClaimManagement } from "./app/paymentclaim/PaymentClaimManagement";
import { BankManagement } from "./app/bank/BankManagement";
import { getMenuItems } from "@cuba-platform/react-core";

export const menuItems = getMenuItems();

const bankCatalog = {
  pathPattern: "/bankManagement/:entityId?",
  menuLink: "/bankManagement",
  component: BankManagement,
  caption: "BankManagement"
};

const catalog = {
  caption: "catalog",
  items: [bankCatalog]
};

menuItems.push(catalog);

const paymentClaimDocument = {
  pathPattern: "/paymentClaimManagement/:entityId?",
  menuLink: "/paymentClaimManagement",
  component: PaymentClaimManagement,
  caption: "PaymentClaimManagement"
};

const document = {
  caption: "document",
  items: [paymentClaimDocument]
};

menuItems.push(document);
