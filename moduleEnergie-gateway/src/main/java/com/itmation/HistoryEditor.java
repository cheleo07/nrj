package com.itmation;

import com.inductiveautomation.ignition.gateway.datasource.Datasource;
import com.inductiveautomation.ignition.gateway.datasource.DatasourceManager;
import com.inductiveautomation.ignition.gateway.history.HistoryFlavor;
import com.inductiveautomation.ignition.gateway.localdb.persistence.FormMeta;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.model.IgnitionWebApp;
import com.inductiveautomation.ignition.gateway.web.components.RecordEditMode;
import com.inductiveautomation.ignition.gateway.web.components.editors.AbstractEditor;
import com.inductiveautomation.ignition.gateway.web.models.IRecordFieldComponent;
import com.inductiveautomation.ignition.gateway.web.models.LenientResourceModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import simpleorm.dataset.SFieldMeta;
import simpleorm.dataset.SRecordInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryEditor extends AbstractEditor {
    @SuppressWarnings("unchecked")
    public HistoryEditor(String id, FormMeta formMeta, RecordEditMode editMode, SRecordInstance record) {
        super(id, formMeta, editMode, record);

        TagHistoryDropdownChoice dropdown = new TagHistoryDropdownChoice("editor", record);

        formMeta.installValidators(dropdown);

        dropdown.setLabel(new LenientResourceModel(formMeta.getFieldNameKey()));

        add(dropdown);

    }

    private class TagHistoryDropdownChoice extends DropDownChoice<String> implements IRecordFieldComponent {

        @SuppressWarnings("unchecked")
        public TagHistoryDropdownChoice(String id, SRecordInstance record) {
            super(id);

            GatewayContext context = ((IgnitionWebApp) Application.get()).getContext();

            List<String> stores = context.getHistoryManager().getStores(HistoryFlavor.SQLTAG);
            setChoices(stores);
        }

        public SFieldMeta getFieldMeta() {
            return getFormMeta().getField();
        }
    }
}
