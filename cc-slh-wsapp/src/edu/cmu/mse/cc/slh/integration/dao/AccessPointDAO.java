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
 * DAO for access point entity.
 * 
 * @version 1.0.0. 21 Jun 2013
 * @author M. A. Riveros T.
 */
public class AccessPointDAO {

	private final static Logger LOGGER = Logger.getLogger(AccessPointDAO.class
			.getSimpleName());			
	
	private final static String ACCESSPOINTS_CATEGORYNAME = "Access Points";

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
	public String getHotSpotsByCategoryName(String warehouseID, String categoryName) {
		String accessPoints = null;

		Connection connection = null;
		ResultSet resultSet = null;
		Statement statement = null;

		try {
			connection = ResourceUtil.getConnection();
			statement = connection.createStatement();
			String query = "SELECT " +
					"DISTINCT " + 
					"Coordinates, " + 
					"CID " + 
					"FROM " + 
					"TBL_HotSpots, " + 
					"TBL_FloorMaps, " +
					"TBL_Categories " +
					"WHERE " + 
					"TBL_HotSpots.MID = TBL_FloorMaps.WID " +
					"AND TBL_HotSpots.SID = TBL_Categories.SID " +
					"AND TBL_FloorMaps.WID = " + 
					warehouseID +
					" AND TBL_Categories.NAME = '" +
					categoryName +
					"'";

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				accessPoints = accessPoints + "<SID>";
				accessPoints = accessPoints + resultSet.getString("CID");
				accessPoints = accessPoints + "</SID>";
				
				String[] coordinates = resultSet.getString("Coordinates").split(",");
				double posX = (Double.parseDouble(coordinates[0]) + Double.parseDouble(coordinates[1])) / 2.0;
				double posY = (Double.parseDouble(coordinates[2]) + Double.parseDouble(coordinates[3])) / 2.0;
				
				accessPoints = accessPoints + "<POSX>";
				accessPoints = accessPoints + posX;
				accessPoints = accessPoints + "</POSX>";
				accessPoints = accessPoints + "<POSY>";
				accessPoints = accessPoints + posY;
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
			ResourceUtil.closeResources(resultSet, statement, connection);
		}

		return accessPoints;
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
	public String getSectionsByWarehouseID(String warehouseID) {
		String accessPoints = null;

		Connection connection = null;
		ResultSet resultSet = null;
		Statement statement = null;

		try {
			connection = ResourceUtil.getConnection();
			statement = connection.createStatement();
			String query = "SELECT " +
					"DISTINCT " + 
					"Coordinates, " + 
					"CID " + 
					"FROM " + 
					"TBL_HotSpots, " + 
					"TBL_FloorMaps, " +
					"TBL_Categories " +
					"WHERE " + 
					"TBL_HotSpots.MID = TBL_FloorMaps.WID " +
					"AND TBL_HotSpots.SID = TBL_Categories.SID " +
					"AND TBL_FloorMaps.WID = " + 
					warehouseID +
					" AND TBL_Categories.NAME <> '" +
					ACCESSPOINTS_CATEGORYNAME +
					"'";

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				accessPoints = accessPoints + "<SID>";
				accessPoints = accessPoints + resultSet.getString("CID");
				accessPoints = accessPoints + "</SID>";
				
				String[] coordinates = resultSet.getString("Coordinates").split(",");
				double posX = (Double.parseDouble(coordinates[0]) + Double.parseDouble(coordinates[2])) / 2.0;
				double posY = (Double.parseDouble(coordinates[1]) + Double.parseDouble(coordinates[3])) / 2.0;
				
				accessPoints = accessPoints + "<POSX>";
				accessPoints = accessPoints + posX;
				accessPoints = accessPoints + "</POSX>";
				accessPoints = accessPoints + "<POSY>";
				accessPoints = accessPoints + posY;
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
			ResourceUtil.closeResources(resultSet, statement, connection);
		}

		return accessPoints;
	}

}
