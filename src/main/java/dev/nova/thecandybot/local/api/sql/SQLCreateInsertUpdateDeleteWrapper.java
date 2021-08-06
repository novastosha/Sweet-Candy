package dev.nova.thecandybot.local.api.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class SQLCreateInsertUpdateDeleteWrapper {

    private final String sqlStatment;
    private final Statement statment;
    private final Connection connection;
    private final String[] sqlStatmentOther;

    public SQLCreateInsertUpdateDeleteWrapper(Connection connection,String sqlStatment,String... sqlStatmentOther) throws Exception {
        this.sqlStatment = sqlStatment;
        this.sqlStatmentOther = sqlStatmentOther;
        this.statment = connection.createStatement();
        this.connection = connection;
    }

    public String[] getSqlStatmentOther() {
        return sqlStatmentOther;
    }

    public Statement getStatment() {
        return statment;
    }

    public String getSqlStatment() {
        return sqlStatment;
    }

    public void push() throws Exception {
        statment.executeUpdate(sqlStatment);
        Arrays.stream(sqlStatmentOther).forEach(s -> {
            try {
                statment.executeUpdate(s);
            } catch (SQLException throwables) {
                System.out.println("[DATABASE WRAPPER] Unable to execute update!");

            }
        });

    }

    public Connection getConnection() {
        return connection;
    }

    public void pushAndClose() throws Exception {
        statment.executeUpdate(sqlStatment);
        Arrays.stream(sqlStatmentOther).forEach(s -> {
            try {
                statment.executeUpdate(s);
            } catch (SQLException throwables) {
                System.out.println("[DATABASE WRAPPER] Unable to execute update!");

            }
        });
        connection.commit();
        statment.close();

    }
}
