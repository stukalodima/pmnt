package com.itk.finance.web.screens.reparationfile;

import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.QueryUtils;
import com.haulmont.cuba.gui.ScreenBuilders;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.components.SuggestionPickerField;
import com.haulmont.cuba.gui.model.CollectionLoader;
import com.haulmont.cuba.gui.screen.*;
import com.itk.finance.entity.Business;
import com.itk.finance.entity.Company;
import com.itk.finance.entity.ReparationFile;
import com.itk.finance.entity.ReparationObject;
import com.itk.finance.web.screens.reparationobject.ReparationObjectEdit;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@UiController("finance_ReparationFile.edit")
@UiDescriptor("reparation-file-edit.xml")
@EditedEntityContainer("reparationFileDc")
@LoadDataBeforeShow
public class ReparationFileEdit extends StandardEditor<ReparationFile> {
    @Inject
    private CollectionLoader<Company> companiesDl;
    @Inject
    private SuggestionPickerField<ReparationObject> reparationObjectField;
    @Inject
    private DataManager dataManager;
    @Inject
    private ScreenBuilders screenBuilders;

    @Subscribe
    public void onAfterShow(AfterShowEvent event) {
        setCompanyParameter();
    }

    private void setCompanyParameter() {
        companiesDl.setParameter("business", getEditedEntity().getBusiness());
        companiesDl.load();
    }

    @Subscribe("businessField")
    public void onBusinessFieldValueChange(HasValue.ValueChangeEvent<Business> event) {
        if (getEditedEntity().getCompany() != null && !getEditedEntity().getCompany().getBusiness().equals(event.getValue())) {
            getEditedEntity().setCompany(null);
        }
        if (event.isUserOriginated()) {
            setCompanyParameter();
        }
    }

    @Subscribe
    public void onInit(InitEvent event) {
        reparationObjectField.setSearchExecutor((searchString, searchParam) -> getReparationObjectsBySearchString(searchString));

        reparationObjectField.setEnterActionHandler(currentSearchString -> {
            List<ReparationObject> reparationObjects = getReparationObjectsBySearchString(currentSearchString);
            if (reparationObjects.isEmpty()) {
                screenBuilders.editor(reparationObjectField)
                        .newEntity()
                        .withInitializer(reparationObjectNew -> {          // lambda to initialize new instance
                            reparationObjectNew.setName(currentSearchString);
                            reparationObjectNew.setPropertyType(getEditedEntity().getPropertyType());
                        })
                        .withScreenClass(ReparationObjectEdit.class)    // specific editor screen
                        .withLaunchMode(OpenMode.DIALOG)        // open as modal dialog
                        .build()
                        .show();
            } else {
                reparationObjectField.showSuggestions(reparationObjects);
            }
        });
    }

    private List<ReparationObject> getReparationObjectsBySearchString(String searchString) {
        searchString = QueryUtils.escapeForLike(searchString);
        return dataManager.load(ReparationObject.class)
                .query("select e from finance_ReparationObject e " +
                        "where e.name like ?1 or e.invNumber like ?1 " +
                        "order by e.name", "%" + searchString + "%")
                .list();
    }


}