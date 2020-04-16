package com.itmation;

public class GatewayConst {
    String Prefix;

    public String db1;
    public String db2;

    public String create1;
    public String insert1;

    public String create2;
    public String select;
    public String update;
    public String insert2;

    public GatewayConst(String Prefix) {
        setPrefix(Prefix);
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String Prefix) {
        this.Prefix = Prefix;
        db1 = this.Prefix + "consoni";
        db2 = this.Prefix + "newindex";

        //consoni
        create1 = "CREATE TABLE IF NOT EXISTS " + db1 + " (id int(2) NOT NULL  AUTO_INCREMENT, nomCompteur varchar(50) NOT NULL, valeur float, t_stamps long, dt_create Date, dt_update Date, PRIMARY KEY(id));";
        insert1 = "INSERT INTO " + db1 + " (nomCompteur, valeur, t_stamps, dt_create, dt_update) VALUES (?,?,?,?,?)";

        //newindex
        create2 = "CREATE TABLE IF NOT EXISTS " + db2 + " (id int(2) NOT NULL  AUTO_INCREMENT, nomCompteur varchar(50) NOT NULL, valeur float, t_stamps long, dt_create Date, dt_update Date, PRIMARY KEY(id));";
        select = "SELECT valeur FROM " + db2 + " where nomCompteur =?";
        update = "update " + db2 + " set valeur = ? where nomCompteur = ?";
        insert2 = "INSERT INTO " + db2 + " (nomCompteur, valeur, t_stamps, dt_create, dt_update) VALUES (?,?,?,?,?)";

    }
}
