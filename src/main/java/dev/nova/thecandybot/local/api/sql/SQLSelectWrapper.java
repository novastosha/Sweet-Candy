package dev.nova.thecandybot.local.api.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLSelectWrapper {

    private final String sqlStatment;
    private final Connection connection;
    private final Statement statment;

    public SQLSelectWrapper(Connection connection, String sqlStatment) throws Exception {
        this.sqlStatment = sqlStatment;
        this.connection = connection;
        this.statment = connection.createStatement();
    }

    public Statement getStatment() {
        return statment;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getSqlStatment() {
        return sqlStatment;
    }

    public ResultSet get() throws SQLException {
        return statment.executeQuery(sqlStatment);
    }
}
