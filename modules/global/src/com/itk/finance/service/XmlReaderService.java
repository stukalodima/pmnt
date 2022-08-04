package com.itk.finance.service;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public interface XmlReaderService {
    String NAME = "finance_XmlReaderService";

    void getClientFromEdrSax() throws ParserConfigurationException, SAXException, IOException;
}