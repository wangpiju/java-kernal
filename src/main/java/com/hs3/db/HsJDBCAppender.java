package com.hs3.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.hs3.utils.DateUtils;
import com.hs3.utils.ip.IPUtils;
import com.hs3.web.utils.SpringContext;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.jdbc.JDBCAppender;

public class HsJDBCAppender
        extends JDBCAppender {
    private static final String SQL = "INSERT INTO t_log4j(serverIp,createdate,lever,clazz,method,message)VALUES('" + IPUtils.getServerIP() + "',?,?,?,?,?)";
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss SSS";

    protected void execute(String sql)
            throws SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            String[] ss = sql.split("\\###", -1);

            con = getConnection();

            ps = con.prepareStatement(SQL);
            ps.setString(1, DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss SSS"));
            ps.setString(2, ss[0]);
            ps.setString(3, ss[1]);
            ps.setString(4, ss[2]);
            ps.setString(5, ss[3]);
            ps.execute();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new SQLException(e2.getMessage());
        } finally {
            if (ps != null) {
                ps.close();
            }
            closeConnection(con);
        }
    }

    protected Connection getConnection()
            throws SQLException {
        if ((this.connection == null) || (this.connection.isClosed())) {
            DruidDataSource ds = (DruidDataSource) SpringContext.getBean(DruidDataSource.class);
            setDriver(ds.getDriverClassName());
            this.connection = DriverManager.getConnection(ds.getUrl(), ds.getUsername(), ds.getPassword());
        }
        return this.connection;
    }

    public void closeConnection(Connection connection) {
    }
}
