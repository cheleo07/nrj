package com.itmation;

import com.inductiveautomation.ignition.common.Dataset;

import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;

public interface QuerysInterface {
    public int addIndex(
            float value,
            String nom,
            Long create) throws SQLException;

    public int updateIndex(
            float value,
            String nom,
            Date create,
            int id);

    public int updateConso(
            float value,
            String nom,
            Date create,
            int id);

    public Dataset getIndex(
            Date create,
            Date update);

    public Dataset getConso(
            Date create,
            Date update);

    public TreeMap<String, Float> Test(
            String nom,
            Float val,
            int row);
}
