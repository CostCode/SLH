/**
 * %W% %E% M. A. Riveros T.
 *
 * Copyright (c) 2012-2013 MSE, Carnegie Mellon University. All Rights Reserved.
 * 
 */

package edu.cmu.mse.cc.slh.integration.dao.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Common services for back-end communication.
 * 
 * @version 1.0.0. 21 Jun 2013
 * @author M. A. Riveros T.
 */
public class ResourceUtil {

	private final static Logger LOGGER = Logger.getLogger(ResourceUtil.class
			.getSimpleName());

	private final static String JNDI_NAME = "java:comp/env/jdbc/MysqlDS";

	/**
	 * Obtains a connection to the underlying data repository
	 * 
	 * @return connection A connection object.
	 * 
	 * @throws NamingException, SQLException
	 * 
	 */
	public static Connection getConnection() throws NamingException,
			SQLException {
		Context initialContext = new InitialContext();
		DataSource dataSource = (javax.sql.DataSource) initialContext
				.lookup(JNDI_NAME);

		return dataSource.getConnection();
	}

	/**
	 * Releases data repository object's immediately
	 * 
	 */
	public static void closeResources(ResultSet resultSet, Statement statement,
			Connection connection) {
		if (resultSet != null)
			try {
				resultSet.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				e.printStackTrace();
			}

		if (statement != null)
			try {
				statement.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				e.printStackTrace();
			}

		if (connection != null)
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.log(Level.SEVERE, e.toString(), e);
				e.printStackTrace();
			}
	}

}
