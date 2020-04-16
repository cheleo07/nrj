package com.itmation;

import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.licensing.LicenseState;
import com.inductiveautomation.ignition.common.script.ScriptManager;
import com.inductiveautomation.ignition.common.script.hints.PropertiesFileDocProvider;
import com.inductiveautomation.ignition.gateway.clientcomm.ClientReqSession;
import com.inductiveautomation.ignition.gateway.datasource.Datasource;
import com.inductiveautomation.ignition.gateway.datasource.DatasourceManager;
import com.inductiveautomation.ignition.gateway.localdb.persistence.RecordListenerAdapter;
import com.inductiveautomation.ignition.gateway.model.AbstractGatewayModuleHook;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import com.inductiveautomation.ignition.gateway.web.models.ConfigCategory;
import com.inductiveautomation.ignition.gateway.web.models.DefaultConfigTab;
import com.inductiveautomation.ignition.gateway.web.models.IConfigTab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GatewayHook extends AbstractGatewayModuleHook {

    private final Logger logger = LoggerFactory.getLogger("ITM_Energie");
    private GatewayContext gc;
    private DatasourceManager dm;
    private String dbName;
    private String Prefix;
    private Querys scriptModule;
    private GatewayConst pref;

    private RecordListenerAdapter<SettingsRecord> settingsListener = new RecordListenerAdapter<SettingsRecord>() {
        public void recordUpdated(SettingsRecord record) {
            updateSettings(record);
        };
    };

    public static final ConfigCategory CONFIG_CATEGORY = new ConfigCategory("SettingsPage", "SettingsPage.nav.header", 700);

    @Override
    public List<ConfigCategory> getConfigCategories() {
        return Collections.singletonList(CONFIG_CATEGORY);
    }

    public static final IConfigTab HCE_CONFIG_ENTRY = DefaultConfigTab.builder()
            .category(CONFIG_CATEGORY)
            .name("settings")
            .i18n("SettingsPage.nav.settings.title")
            .page(SettingsPage.class)
            .terms("settings")
            .build();

    @Override
    public List<? extends IConfigTab> getConfigPanels() {
        return Collections.singletonList(
                HCE_CONFIG_ENTRY
        );
    }

    @Override
    public void setup(GatewayContext gatewayContext) {
        this.gc = gatewayContext;

        // Register GatewayHook.properties by registering the GatewayHook.class with BundleUtils
        BundleUtil.get().addBundle("SettingsPage", getClass(), "SettingsPage");

        //Verify tables for persistent records if necessary
        verifySchema(gc);

        // create records if needed
        maybeCreateHCSettings(gc);

        SettingsRecord theOneRecord = gc.getLocalPersistenceInterface().find(SettingsRecord.META, 0L);
        //logger.info("db config: " + theOneRecord.getDBName());

    }

    private void verifySchema(GatewayContext context) {
        try {
            context.getSchemaUpdater().updatePersistentRecords(SettingsRecord.META);
        } catch (SQLException e) {
            logger.error("Error verifying persistent record schemas for SettingsPage records.", e);
        }
    }

    public void maybeCreateHCSettings(GatewayContext context) {
        logger.trace("Attempting to create HomeConnect Settings Record");
        try {
            SettingsRecord settingsRecord = context.getLocalPersistenceInterface().createNew(SettingsRecord.META);
            settingsRecord.setId(0L);
            gc.getPersistenceInterface().save(settingsRecord);
            /*
             * This doesn't override existing settings, only replaces it with these if we didn't
             * exist already.
             */
            context.getSchemaUpdater().ensureRecordExists(settingsRecord);
        } catch (Exception e) {
            logger.error("Failed to establish HCSettings Record exists", e);
        }
    }

    @Override
    public void startup(LicenseState licenseState) {
        SettingsRecord settings = null;
        SettingsRecord db = gc.getLocalPersistenceInterface().find(SettingsRecord.META, 0L);
        dbName = db.getHistoryProvider();
        Prefix = db.getPrefix();
        dm=gc.getDatasourceManager(); //liste datasources de la gateway
        pref = new GatewayConst(Prefix);
        scriptModule = new Querys(dm,dbName,pref);
        List datasources = dm.getDatasources(); //datasource dans liste objets
        logger.info("démarrage du module");

        //Cherche bdd par nom
        /*Datasource dbTest = dm.getDatasource("dbEnergy");
        String res=String.valueOf(dbTest);
        logger.info(res+" = "+((Datasource) dbTest).getName());*/
        //logger.info(res+" =dbEnergy");

        //All bdd in gateway
        DatasourceManager dm = gc.getDatasourceManager();
        List<Datasource> dbTesdt = dm.getDatasources();
        List<String> stores = new ArrayList<String>();
        for (Datasource dt : dbTesdt){
            String tt = dt.getName();
            stores.add(tt);
        }
        logger.info("Bases de données connectées à cette gateway:");
        for (String b : stores){
            logger.info(b);
        }
    }


    public void updateSettings(SettingsRecord settingsRecord){
        //Prefix = settingsRecord.getPrefix();
        // scriptModule.setPrefix(Prefix);
        dbName = settingsRecord.getHistoryProvider();
        scriptModule.setDbName(dbName);
        Prefix = settingsRecord.getPrefix();
        pref.setPrefix(Prefix);
    }

    @Override
    public void initializeScriptManager(ScriptManager manager) {
        super.initializeScriptManager(manager);

        manager.addScriptModule(
                "system.energie",
                scriptModule,
                new PropertiesFileDocProvider());
    }
    @Override
    public Object getRPCHandler(ClientReqSession session, String projectName) {
        return scriptModule;
    }

    @Override
    public void shutdown() {
    }

}
