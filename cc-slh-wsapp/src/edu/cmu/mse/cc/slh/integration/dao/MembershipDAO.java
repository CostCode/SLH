/**
 * %W% %E% M. A. Riveros T.
 *
 * Copyright (c) 2012-2013 MSE, Carnegie Mellon University. All Rights Reserved.
 * 
 */

package edu.cmu.mse.cc.slh.integration.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

import edu.cmu.mse.cc.slh.integration.dao.util.ResourceUtil;

/**
 * DAO for Memberships entity.
 * 
 * @version 1.0.0. 21 Jun 2013
 * @author M. A. Riveros T.
 */
public class MembershipDAO {

	private final static Logger LOGGER = Logger.getLogger(MembershipDAO.class
			.getSimpleName());

	public static MembershipDAO getInstance() {
		return new MembershipDAO();
	}

	/**
	 * Obtains the version of the floor map for a particular warehouse
	 * 
	 * @param warehouseID Warehouse identifier.
	 * 
	 * @return version A string with the version.
	 * 
	 */
	public String validateMembership(String warehouseID) {
		String validity = null;

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = ResourceUtil.getConnection();
			statement = connection.createStatement();
			String query = "SELECT " + 
					"validity " +
					"FROM " +
					"TBL_Memberships " +
					"WHERE " +
					"ID = '" +
					warehouseID +
					"'";		

			resultSet = statement.executeQuery(query);
 
			while (resultSet.next()) {
				validity = resultSet.getString("validity");
				break;
			}

		} catch (NamingException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			e.printStackTrace();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			e.printStackTrace();
		} finally {
			ResourceUtil.closeResources(resultSet, statement, connection);
		}

		return validity;
	}

}
