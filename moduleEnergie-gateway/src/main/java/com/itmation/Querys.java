package com.itmation;

import com.inductiveautomation.ignition.common.Dataset;
import com.inductiveautomation.ignition.gateway.datasource.DatasourceManager;
import com.inductiveautomation.ignition.gateway.datasource.SRConnection;
import com.inductiveautomation.ignition.gateway.model.GatewayContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;

public class Querys extends AbstractQuerys{
    private String dbName;
    private GatewayConst pref;
    private GatewayContext gc;
    private DatasourceManager dm;
    private SettingsRecord sr;
    private final Logger logger = LoggerFactory.getLogger("myQuerys");
    private TreeMap<String, Float> index = new TreeMap<String,Float>();

    public Querys(DatasourceManager dm, String dbName, GatewayConst pref) {
        this.dm = dm;
        this.dbName = dbName;
        this.pref = pref;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    @Override
    protected int addIndexImp(float value, String nom, Long create) throws SQLException {
        SRConnection con;
        int retVal=0;
        int res=0;
        Dataset table;
        Dataset table1;
        Dataset data;
        Float lastval = (float) 0;
        Double vale = (double) 0;

        con = dm.getConnection(this.dbName);
        table1 = con.runQuery(pref.create1);
        table = con.runQuery(pref.create2);

        //LASTVALUE
        if (index.containsKey(nom)){
            lastval = index.get(nom);
        }
        else{
            data = con.runPrepQuery(pref.select,nom);

            if (data.getRowCount()>0){
                vale =  (double) data.getValueAt(0, "valeur");
                lastval = vale.floatValue();
            }
            else{
                lastval= Float.valueOf(0);
            }
        }
        Float conso = value-lastval;
        index.put(nom,conso);

        //CONSO
        res = con.runPrepUpdate(pref.insert1,nom, conso, create, con.getCurrentDatabaseTime(), con.getCurrentDatabaseTime());

        //NEWINDEX
        Dataset data1 = con.runPrepQuery(pref.select, nom);
        if (data1.getRowCount()>0){
            retVal = con.runPrepUpdate(pref.update,value,nom);
        }
        else{
            retVal = con.runPrepUpdate(pref.insert2,nom,value, create, con.getCurrentDatabaseTime(), con.getCurrentDatabaseTime());
        }
        con.close();
        return res;
    }

    @Override
    protected int updateIndexImp(float value, String nom, Date create, int id) {
        SRConnection con;
        int retVal=0;
        try {
            con = dm.getConnection("dbEnergy");
            retVal = con.runPrepUpdate("UPDATE newindex SET nomCompteur =?, valeur =?, dt_update =? WHERE id =?",nom,value,create,id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    protected int updateConsoImp(float value, String nom, Date create, int id) {
        SRConnection con;
        int retVal=0;
        try {
            con = dm.getConnection("dbEnergy");
            retVal = con.runPrepUpdate("UPDATE consoni SET nomCompteur =?, valeur =?, dt_update =? WHERE id =?",nom,value,create,id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    protected Dataset getIndexImp(Date create, Date update) {
        SRConnection con;
        Dataset data=null;
        try {
            con = dm.getConnection("dbEnergy");
            data = con.runPrepQuery("SELECT * FROM newindex where dt_update BETWEEN ? AND ?",create,update);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    protected Dataset getConsoImp(Date create, Date update) {
        SRConnection con;
        Dataset data=null;
        try {
            con = dm.getConnection("dbEnergy");
            data = con.runPrepQuery("SELECT * FROM consoni where dt_update BETWEEN ? AND ?",create,update);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }


    @Override
    protected TreeMap<String, Float> TestImp(String nom, Float val, int row) {
        SRConnection con;
        Dataset data;
        Float lastval = (float) 0;
        Double vale = (double) 0;
        String name="";
        if (index.containsKey(nom)){
            lastval = index.get(nom);
        }
        else{
            try {
                con = dm.getConnection("dbEnergy");
                data = con.runPrepQuery("SELECT valeur FROM newindex where nomCompteur =?",nom); //dataset
                vale =  (double) data.getValueAt(row, "valeur"); //object
                lastval = vale.floatValue();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Float conso = val-lastval;
        index.put(nom,conso);
        return index;
    }
}
