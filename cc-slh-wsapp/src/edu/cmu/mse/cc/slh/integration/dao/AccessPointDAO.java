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

import edu.cmu.mse.cc.slh.integration.dao.util.ConnectionUtil;

/**
 * DAO for access point entity.
 * 
 * @version 1.0.0. 21 Jun 2013
 * @author M. A. Riveros T.
 */
public class AccessPointDAO {

	private final static Logger LOGGER = Logger.getLogger(AccessPointDAO.class
			.getSimpleName());

	public static AccessPointDAO getInstance() {
		return new AccessPointDAO();
	}

	/**
	 * 
	 * Obtains sections location for a particular warehouse
	 * 
	 * @param warehouseID Warehouse identifier
	 * 
	 * @return accessPoints A string of access points within the warehouse
	 * 
	 */
	public String getAccessPoints(String warehouseID) {
		String accessPoints = null;

		Connection connection = null;
		ResultSet resultSet = null;
		Statement statement = null;

		try {
			connection = ConnectionUtil.getConnection();
			statement = connection.createStatement();
			String query = "SELECT " + "SSID, " + "MID, " + "XCoordinates, "
					+ "YCoordinates " + "FROM " + "TBL_AccessPoints "
					+ "WHERE " + "MID =  " + warehouseID;

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				accessPoints = "<SSID>";
				accessPoints = accessPoints + resultSet.getString("SSID");
				accessPoints = accessPoints + "</SSID>";
				accessPoints = accessPoints + "<POSX>";
				accessPoints = accessPoints
						+ resultSet.getString("XCoordinates");
				accessPoints = accessPoints + "</POSX>";
				accessPoints = accessPoints + "<POSY>";
				accessPoints = accessPoints
						+ resultSet.getString("YCoordinates");
				accessPoints = accessPoints + "</POSY>";
				accessPoints = accessPoints + "\n";
			}
		} catch (NamingException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			e.printStackTrace();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, e.toString(), e);
			e.printStackTrace();
		} finally {
			ConnectionUtil.closeResources(resultSet, statement, connection);
		}

		return accessPoints;
	}

}
