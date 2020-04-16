package com.itmation;

import com.inductiveautomation.ignition.gateway.localdb.persistence.*;
import com.inductiveautomation.ignition.gateway.web.components.editors.DateEditorSource;
import com.inductiveautomation.ignition.gateway.web.components.editors.LocaleEditor;
import com.inductiveautomation.ignition.gateway.web.components.editors.PasswordEditorSource;
import simpleorm.dataset.SFieldFlags;
import simpleorm.dataset.SRecordMeta;

public class SettingsRecord extends PersistentRecord {
    public static final RecordMeta<SettingsRecord> META = new RecordMeta<SettingsRecord>(
            SettingsRecord.class, "SettingsRecord").setNounKey("SettingsRecord.Noun").setNounPluralKey(
            "SettingsRecord.Noun.Plural");

    public static final IdentityField Id = new IdentityField(META);

    //Interface settings
    public static final StringField HistoryProvider = new StringField(META, "historyProvider");
    public static final StringField Prefix = new StringField(META, "Prefix",5).setDefault("NRJ_");

    // create categories, getting titles from the .properties
    static final Category Configuration = new Category("SettingsRecord.Category.Configuration", 1000).include(HistoryProvider, Prefix);

    static {
       HistoryProvider.getFormMeta().setEditorSource(HistorySource.getSharedInstance());
       // HistoryProvider.getFormMeta().setEditorSource(LocaleEditor.LocaleEditorSource.INSTANCE);
    }


    // accessors for our record entries

    public void setId(Long id) {
        setLong(Id, id);
    }

    public Long getId() {
        return getLong(Id);
    }

    public void setHistoryProvider(String name) {
        setString(HistoryProvider, name);
    }

    public String getHistoryProvider() {
        return getString(HistoryProvider);
    }

    public void setPrefix(String name) {
        setString(Prefix, name);
    }

    public String getPrefix() {
        return getString(Prefix);
    }

    @Override
    public RecordMeta<?> getMeta() {
        return META;
    }
}
