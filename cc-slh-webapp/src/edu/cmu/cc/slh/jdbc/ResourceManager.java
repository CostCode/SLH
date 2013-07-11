package edu.cmu.cc.slh.jdbc;

import java.sql.*;

public class ResourceManager {
	/*private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static String JDBC_URL = "jdbc:mysql://localhost:3306/mysql";

	private static String JDBC_USER = "root";
	private static String JDBC_PASSWORD = "root";

	private static Driver driver = null;
	*/
    private static String JDBC_DRIVER   			= "com.mysql.jdbc.Driver";
    
    private static String OPENSHIFT_MYSQL_DB_HOST 	= System.getenv("OPENSHIFT_MYSQL_DB_HOST");
    private static String OPENSHIFT_MYSQL_DB_PORT 	= System.getenv("OPENSHIFT_MYSQL_DB_PORT");
    private static String OPENSHIFT_APP_NAME 		= System.getenv("OPENSHIFT_APP_NAME");
    
    private static String JDBC_USER     			= System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
    private static String JDBC_PASSWORD 			= System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");
    
    private static String JDBC_URL 					= "jdbc:mysql://"+ OPENSHIFT_MYSQL_DB_HOST +":"+ OPENSHIFT_MYSQL_DB_PORT +"/" + OPENSHIFT_APP_NAME;

    private static Driver driver = null;
    
	public static synchronized Connection getConnection() throws SQLException {
		if (driver == null) {
			try {
				Class jdbcDriverClass = Class.forName(JDBC_DRIVER);
				driver = (Driver) jdbcDriverClass.newInstance();
				DriverManager.registerDriver(driver);
			} catch (Exception e) {
				System.out.println("Failed to initialise JDBC driver");
				e.printStackTrace();
			}
		}

		return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
	}

	public static void close(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public static void close(PreparedStatement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}

	public static void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}

	}

}
