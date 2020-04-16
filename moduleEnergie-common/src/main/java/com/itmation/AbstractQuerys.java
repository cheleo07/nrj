package com.itmation;

import com.inductiveautomation.ignition.common.BundleUtil;
import com.inductiveautomation.ignition.common.Dataset;
import com.inductiveautomation.ignition.common.script.hints.ScriptArg;
import com.inductiveautomation.ignition.common.script.hints.ScriptFunction;

import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;

public abstract class AbstractQuerys implements QuerysInterface{
    static {
        BundleUtil.get().addBundle(
                AbstractQuerys.class.getSimpleName(),
                AbstractQuerys.class.getClassLoader(),
                AbstractQuerys.class.getName().replace('.', '/')
        );
    }

    @Override
    @ScriptFunction(docBundlePrefix = "AbstractQuerys")
    public int addIndex(
            @ScriptArg("value") float value,
            @ScriptArg("nom") String nom,
            @ScriptArg("create") Long create) throws SQLException {

        return addIndexImp(value, nom, create);
    }

    @ScriptFunction(docBundlePrefix = "AbstractQuerys")
    public int updateIndex(
            @ScriptArg("value") float value,
            @ScriptArg("nom") String nom,
            @ScriptArg("create") Date create,
            @ScriptArg("id") int id){
        return updateIndexImp(value, nom, create, id);
    }

    @ScriptFunction(docBundlePrefix = "AbstractQuerys")
    public int updateConso(
            @ScriptArg("value") float value,
            @ScriptArg("nom") String nom,
            @ScriptArg("create") Date create,
            @ScriptArg("id") int id){
        return updateConsoImp(value, nom, create, id);
    }

    @ScriptFunction(docBundlePrefix = "AbstractQuerys")
    public Dataset getIndex(
            @ScriptArg("create") Date create,
            @ScriptArg("update") Date update){
        return getIndexImp(create, update);
    }

    @ScriptFunction(docBundlePrefix = "AbstractQuerys")
    public Dataset getConso(
            @ScriptArg("create") Date create,
            @ScriptArg("update") Date update){
        return getConsoImp(create, update);
    }

    @Override
    @ScriptFunction(docBundlePrefix = "AbstractQuerys")
    public TreeMap<String, Float> Test(
            @ScriptArg("nom") String nom,
            @ScriptArg("val") Float val,
            @ScriptArg("row") int row) {
        return TestImp(nom,val,row);
    }

    protected abstract int addIndexImp (float value, String nom, Long create) throws SQLException;
    protected abstract int updateIndexImp (float value, String nom, Date create, int id);
    protected abstract Dataset getIndexImp (Date create, Date update);
    protected abstract int updateConsoImp(float value, String nom, Date create, int id);
    protected abstract Dataset getConsoImp(Date create, Date update);
    protected abstract TreeMap<String, Float> TestImp(String nom, Float val,int row);

}
