package com.itmation;

import com.inductiveautomation.ignition.gateway.localdb.persistence.FormMeta;
import com.inductiveautomation.ignition.gateway.web.components.RecordEditMode;
import com.inductiveautomation.ignition.gateway.web.components.editors.IEditorSource;
import org.apache.wicket.Component;
import simpleorm.dataset.SRecordInstance;

public class HistorySource implements IEditorSource {
    static final HistorySource _instance = new HistorySource();

    public static HistorySource getSharedInstance() {
        return _instance;
    }

    public HistorySource(){

    }

    @Override
    public Component newEditorComponent(String id, RecordEditMode editMode, SRecordInstance record, FormMeta formMeta) {
        return new HistoryEditor(id, formMeta, editMode, record);
    }

}
