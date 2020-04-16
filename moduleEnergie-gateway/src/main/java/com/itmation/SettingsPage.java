package com.itmation;

import com.inductiveautomation.ignition.gateway.model.IgnitionWebApp;
import com.inductiveautomation.ignition.gateway.web.components.RecordEditForm;
import com.inductiveautomation.ignition.gateway.web.models.LenientResourceModel;
import com.inductiveautomation.ignition.gateway.web.pages.IConfigPage;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.wicket.Application;

public class SettingsPage extends RecordEditForm {
    public static final Pair<String, String> MENU_LOCATION =
            Pair.of(GatewayHook.CONFIG_CATEGORY.getName(), "settings");

    public SettingsPage(final IConfigPage configPage) {
        super(configPage, null, new LenientResourceModel("SettingsPage.nav.settings.panelTitle"),
                ((IgnitionWebApp) Application.get()).getContext().getPersistenceInterface().find(SettingsRecord.META, 0L)
        );
    }

    @Override
    public Pair<String, String> getMenuLocation() {
        return MENU_LOCATION;
    }
}
