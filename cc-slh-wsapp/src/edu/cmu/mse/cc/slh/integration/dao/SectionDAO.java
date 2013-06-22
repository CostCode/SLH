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
 * DAO for Section entity.
 * 
 * @version 1.0.0. 21 Jun 2013
 * @author M. A. Riveros T.
 */
public class SectionDAO {

	private final static Logger LOGGER = Logger.getLogger(SectionDAO.class
			.getSimpleName());

	public static SectionDAO getInstance() {
		return new SectionDAO();
	}

	/**
	 * Obtains sections of a particular warehouse
	 * 
	 * @param warehouseID Warehouse identifier.
	 * 
	 * @return sections A string of sections within the warehouse
	 * 
	 */
	public String getSectionsLocation(String warehouseID) {
		String sections = null;

		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;

		try {
			connection = ConnectionUtil.getConnection();
			statement = connection.createStatement();
			String query = "SELECT " + "SID, " + "MID, " + "CID, "
					+ "XCoordinates, " + "YCoordinates " + "FROM "
					+ "TBL_Sections " + "WHERE " + "MID =  " + warehouseID;

			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				sections = "<SID>";
				sections = sections + resultSet.getString("SID");
				sections = sections + "</SID>";
				sections = sections + "<POSX>";
				sections = sections + resultSet.getString("XCoordinates");
				sections = sections + "</POSX>";
				sections = sections + "<POSY>";
				sections = sections + resultSet.getString("YCoordinates");
				sections = sections + "</POSY>";
				sections = sections + "\n";
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

		return sections;
	}

}
