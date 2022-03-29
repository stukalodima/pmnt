package com.itk.finance.service;

import javax.xml.stream.XMLStreamException;

public interface XmlReaderService {
    String NAME = "finance_XmlReaderService";

    void getClientFromEdr() throws XMLStreamException;
}