/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package edu.cmu.cc.slh.jdbc;

import edu.cmu.cc.slh.dao.*;
import edu.cmu.cc.slh.factory.*;
import edu.cmu.cc.slh.dto.*;
import edu.cmu.cc.slh.exceptions.*;

import java.sql.Connection;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class TblManagersDaoImpl extends AbstractDAO implements TblManagersDao
{
	/** 
	 * The factory class for this DAO has two versions of the create() method - one that
takes no arguments and one that takes a Connection argument. If the Connection version
is chosen then the connection will be stored in this attribute and will be used by all
calls to this DAO, otherwise a new Connection will be allocated for each operation.
	 */
	protected java.sql.Connection userConn;

	/** 
	 * All finder methods in this class use this SELECT constant to build their queries
	 */
	protected final String SQL_SELECT = "SELECT UID, FirstName, LastName, Username, Password FROM " + getTableName() + "";

	/** 
	 * Finder methods will pass this value to the JDBC setMaxRows method
	 */
	protected int maxRows;

	/** 
	 * SQL INSERT statement for this table
	 */
	protected final String SQL_INSERT = "INSERT INTO " + getTableName() + " ( UID, FirstName, LastName, Username, Password ) VALUES ( ?, ?, ?, ?, ? )";

	/** 
	 * SQL UPDATE statement for this table
	 */
	protected final String SQL_UPDATE = "UPDATE " + getTableName() + " SET UID = ?, FirstName = ?, LastName = ?, Username = ?, Password = ? WHERE UID = ?";

	/** 
	 * SQL DELETE statement for this table
	 */
	protected final String SQL_DELETE = "DELETE FROM " + getTableName() + " WHERE UID = ?";

	/** 
	 * Index of column UID
	 */
	protected static final int COLUMN_UID = 1;

	/** 
	 * Index of column FirstName
	 */
	protected static final int COLUMN_FIRST_NAME = 2;

	/** 
	 * Index of column LastName
	 */
	protected static final int COLUMN_LAST_NAME = 3;

	/** 
	 * Index of column Username
	 */
	protected static final int COLUMN_USERNAME = 4;

	/** 
	 * Index of column Password
	 */
	protected static final int COLUMN_PASSWORD = 5;

	/** 
	 * Number of columns
	 */
	protected static final int NUMBER_OF_COLUMNS = 5;

	/** 
	 * Index of primary-key column UID
	 */
	protected static final int PK_COLUMN_UID = 1;

	/** 
	 * Inserts a new row in the TBL_Managers table.
	 */
	public TblManagersPk insert(TblManagers dto) throws TblManagersDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			stmt = conn.prepareStatement( SQL_INSERT, Statement.RETURN_GENERATED_KEYS );
			int index = 1;
			stmt.setInt( index++, dto.getUid() );
			stmt.setString( index++, dto.getFirstName() );
			stmt.setString( index++, dto.getLastName() );
			stmt.setString( index++, dto.getUsername() );
			stmt.setString( index++, dto.getPassword() );
			System.out.println( "Executing " + SQL_INSERT + " with DTO: " + dto );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		
			// retrieve values from auto-increment columns
			rs = stmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				dto.setUid( rs.getInt( 1 ) );
			}
		
			reset(dto);
			return dto.createPk();
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TblManagersDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Updates a single row in the TBL_Managers table.
	 */
	public void update(TblManagersPk pk, TblManagers dto) throws TblManagersDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_UPDATE + " with DTO: " + dto );
			stmt = conn.prepareStatement( SQL_UPDATE );
			int index=1;
			stmt.setInt( index++, dto.getUid() );
			stmt.setString( index++, dto.getFirstName() );
			stmt.setString( index++, dto.getLastName() );
			stmt.setString( index++, dto.getUsername() );
			stmt.setString( index++, dto.getPassword() );
			stmt.setInt( 6, pk.getUid() );
			int rows = stmt.executeUpdate();
			reset(dto);
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TblManagersDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Deletes a single row in the TBL_Managers table.
	 */
	public void delete(TblManagers pk) throws TblManagersDaoException
	{
		long t1 = System.currentTimeMillis();
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			System.out.println( "Executing " + SQL_DELETE + " with PK: " + pk );
			stmt = conn.prepareStatement( SQL_DELETE );
			stmt.setInt( 1, pk.getUid() );
			int rows = stmt.executeUpdate();
			long t2 = System.currentTimeMillis();
			System.out.println( rows + " rows affected (" + (t2-t1) + " ms)" );
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TblManagersDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns the rows from the TBL_Managers table that matches the specified primary-key value.
	 */
	public TblManagers findByPrimaryKey(TblManagersPk pk) throws TblManagersDaoException
	{
		return findByPrimaryKey( pk.getUid() );
	}

	/** 
	 * Returns all rows from the TBL_Managers table that match the criteria 'UID = :uid'.
	 */
	public TblManagers findByPrimaryKey(int uid) throws TblManagersDaoException
	{
		TblManagers ret[] = findByDynamicSelect( SQL_SELECT + " WHERE UID = ?", new Object[] {  new Integer(uid) } );
		return ret.length==0 ? null : ret[0];
	}

	/** 
	 * Returns all rows from the TBL_Managers table that match the criteria ''.
	 */
	public TblManagers[] findAll() throws TblManagersDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " ORDER BY UID", null );
	}

	/** 
	 * Returns all rows from the TBL_Managers table that match the criteria 'UID = :uid'.
	 */
	public TblManagers[] findWhereUidEquals(int uid) throws TblManagersDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE UID = ? ORDER BY UID", new Object[] {  new Integer(uid) } );
	}

	/** 
	 * Returns all rows from the TBL_Managers table that match the criteria 'FirstName = :firstName'.
	 */
	public TblManagers[] findWhereFirstNameEquals(String firstName) throws TblManagersDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE FirstName = ? ORDER BY FirstName", new Object[] { firstName } );
	}

	/** 
	 * Returns all rows from the TBL_Managers table that match the criteria 'LastName = :lastName'.
	 */
	public TblManagers[] findWhereLastNameEquals(String lastName) throws TblManagersDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE LastName = ? ORDER BY LastName", new Object[] { lastName } );
	}

	/** 
	 * Returns all rows from the TBL_Managers table that match the criteria 'Username = :username'.
	 */
	public TblManagers[] findWhereUsernameEquals(String username) throws TblManagersDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE Username = ? ORDER BY Username", new Object[] { username } );
	}

	/** 
	 * Returns all rows from the TBL_Managers table that match the criteria 'Password = :password'.
	 */
	public TblManagers[] findWherePasswordEquals(String password) throws TblManagersDaoException
	{
		return findByDynamicSelect( SQL_SELECT + " WHERE Password = ? ORDER BY Password", new Object[] { password } );
	}
	
	public TblManagers findWhereUsernamePassEquals(String username, String password) throws TblManagersDaoException
	{
		TblManagers ret[] = findByDynamicSelect( SQL_SELECT + " WHERE Username = ? AND Password = ? ORDER BY Username", new Object[] { username, password });
		return ret.length==0 ? null : ret[0];
	}
	/**
	 * Method 'TblManagersDaoImpl'
	 * 
	 */
	public TblManagersDaoImpl()
	{
	}

	/**
	 * Method 'TblManagersDaoImpl'
	 * 
	 * @param userConn
	 */
	public TblManagersDaoImpl(final java.sql.Connection userConn)
	{
		this.userConn = userConn;
	}

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows)
	{
		this.maxRows = maxRows;
	}

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows()
	{
		return maxRows;
	}

	/**
	 * Method 'getTableName'
	 * 
	 * @return String
	 */
	public String getTableName()
	{
		return "SLH_Web_DB.TBL_Managers";
	}

	/** 
	 * Fetches a single row from the result set
	 */
	protected TblManagers fetchSingleResult(ResultSet rs) throws SQLException
	{
		if (rs.next()) {
			TblManagers dto = new TblManagers();
			populateDto( dto, rs);
			return dto;
		} else {
			return null;
		}
		
	}

	/** 
	 * Fetches multiple rows from the result set
	 */
	protected TblManagers[] fetchMultiResults(ResultSet rs) throws SQLException
	{
		Collection resultList = new ArrayList();
		while (rs.next()) {
			TblManagers dto = new TblManagers();
			populateDto( dto, rs);
			resultList.add( dto );
		}
		
		TblManagers ret[] = new TblManagers[ resultList.size() ];
		resultList.toArray( ret );
		return ret;
	}

	/** 
	 * Populates a DTO with data from a ResultSet
	 */
	protected void populateDto(TblManagers dto, ResultSet rs) throws SQLException
	{
		dto.setUid( rs.getInt( COLUMN_UID ) );
		dto.setFirstName( rs.getString( COLUMN_FIRST_NAME ) );
		dto.setLastName( rs.getString( COLUMN_LAST_NAME ) );
		dto.setUsername( rs.getString( COLUMN_USERNAME ) );
		dto.setPassword( rs.getString( COLUMN_PASSWORD ) );
	}

	/** 
	 * Resets the modified attributes in the DTO
	 */
	protected void reset(TblManagers dto)
	{
	}

	/** 
	 * Returns all rows from the TBL_Managers table that match the specified arbitrary SQL statement
	 */
	public TblManagers[] findByDynamicSelect(String sql, Object[] sqlParams) throws TblManagersDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TblManagersDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	/** 
	 * Returns all rows from the TBL_Managers table that match the specified arbitrary SQL statement
	 */
	public TblManagers[] findByDynamicWhere(String sql, Object[] sqlParams) throws TblManagersDaoException
	{
		// declare variables
		final boolean isConnSupplied = (userConn != null);
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		
		try {
			// get the user-specified connection or get a connection from the ResourceManager
			conn = isConnSupplied ? userConn : ResourceManager.getConnection();
		
			// construct the SQL statement
			final String SQL = SQL_SELECT + " WHERE " + sql;
		
		
			System.out.println( "Executing " + SQL );
			// prepare statement
			stmt = conn.prepareStatement( SQL );
			stmt.setMaxRows( maxRows );
		
			// bind parameters
			for (int i=0; sqlParams!=null && i<sqlParams.length; i++ ) {
				stmt.setObject( i+1, sqlParams[i] );
			}
		
		
			rs = stmt.executeQuery();
		
			// fetch the results
			return fetchMultiResults(rs);
		}
		catch (Exception _e) {
			_e.printStackTrace();
			throw new TblManagersDaoException( "Exception: " + _e.getMessage(), _e );
		}
		finally {
			ResourceManager.close(rs);
			ResourceManager.close(stmt);
			if (!isConnSupplied) {
				ResourceManager.close(conn);
			}
		
		}
		
	}

	@Override
	public void delete(TblManagersPk pk) throws TblManagersDaoException {
		// TODO Auto-generated method stub
		
	}

}
