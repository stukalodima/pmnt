package com.itk.finance.web.screens.reparationfilebyreparationobject.action;

import com.haulmont.cuba.gui.screen.StandardCloseAction;
import com.itk.finance.entity.ReparationObject;

import java.util.List;

public class AddReparationObjectsAction extends StandardCloseAction {

    private final List<ReparationObject> reparationObjects;

    public AddReparationObjectsAction(String actionId, List<ReparationObject> reparationObjectList) {
        super(actionId);
        this.reparationObjects = reparationObjectList;
    }


    public List<ReparationObject> getReparationObjects() {
        return reparationObjects;
    }
}
