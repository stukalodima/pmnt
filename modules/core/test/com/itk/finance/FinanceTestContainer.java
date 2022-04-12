package com.itk.finance;

import com.haulmont.cuba.testsupport.TestContainer;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.ArrayList;
import java.util.Arrays;

public class FinanceTestContainer extends TestContainer {

    public FinanceTestContainer() {
        super();
        //noinspection ArraysAsListWithZeroOrOneArgument
        appComponents = Arrays.asList(
                "com.haulmont.cuba",
                "com.haulmont.addon.helium",
                "com.haulmont.addon.restapi",
                "com.haulmont.bpm",
                "de.diedavids.cuba.dataimport");
        appPropertiesFiles = Arrays.asList(
                // List the files defined in your web.xml
                // in appPropertiesConfig context parameter of the core module
                "com/itk/finance/app.properties",
                // Add this file which is located in CUBA and defines some properties
                // specifically for test environment. You can replace it with your own
                // or add another one in the end.
                "com/itk/finance/test-app.properties");
        autoConfigureDataSource();
    }

    

    public static class Common extends FinanceTestContainer {

        public static final FinanceTestContainer.Common INSTANCE = new FinanceTestContainer.Common();

        private static volatile boolean initialized;

        private Common() {
        }

        @Override
        public void beforeAll(ExtensionContext extensionContext) throws Exception {
            if (!initialized) {
                super.beforeAll(extensionContext);
                initialized = true;
            }
            setupContext();
        }
        

        @SuppressWarnings("RedundantThrows")
        @Override
        public void afterAll(ExtensionContext extensionContext) throws Exception {
            cleanupContext();
            // never stops - do not call super
        }
        
    }
}