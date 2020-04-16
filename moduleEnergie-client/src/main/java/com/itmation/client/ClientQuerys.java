package com.itmation.client;

import com.inductiveautomation.ignition.client.gateway_interface.ModuleRPCFactory;
import com.inductiveautomation.ignition.common.Dataset;
import com.itmation.AbstractQuerys;
import com.itmation.QuerysInterface;

import java.sql.SQLException;
import java.util.Date;
import java.util.TreeMap;

public class ClientQuerys extends AbstractQuerys {
    private final QuerysInterface rpc;

    public ClientQuerys() {
        rpc = ModuleRPCFactory.create(
                "com.itmation.moduleEnergie",
                QuerysInterface.class
        );
    }


    @Override
    protected int addIndexImp(float v, String s, Long d) throws SQLException {
        return rpc.addIndex(v, s, d);
    }

    @Override
    protected int updateIndexImp(float v, String s, Date d, int i) {
        return rpc.updateIndex(v, s, d, i);
    }

    @Override
    protected int updateConsoImp(float v, String s, Date d, int i) {
        return rpc.updateConso(v, s, d, i);
    }

    @Override
    protected Dataset getIndexImp(Date d, Date c) {
        return rpc.getIndex(d, c);
    }

    @Override
    protected Dataset getConsoImp(Date d, Date c) {
        return rpc.getConso(d, c);
    }

    @Override
    protected TreeMap<String, Float> TestImp(String s, Float v, int r){
        return rpc.Test(s ,v, r);
    }
}
