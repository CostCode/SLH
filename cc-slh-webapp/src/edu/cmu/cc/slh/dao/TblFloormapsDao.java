/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package edu.cmu.cc.slh.dao;

import edu.cmu.cc.slh.dto.*;
import edu.cmu.cc.slh.exceptions.*;

public interface TblFloormapsDao
{
	/** 
	 * Inserts a new row in the TBL_FloorMaps table.
	 */
	public TblFloormapsPk insert(TblFloormaps dto) throws TblFloormapsDaoException;

	/** 
	 * Updates a single row in the TBL_FloorMaps table.
	 */
	public void update(TblFloormapsPk pk, TblFloormaps dto) throws TblFloormapsDaoException;

	public void versionupdate(TblFloormaps pk, TblFloormaps dto) throws TblFloormapsDaoException;
	
	/** 
	 * Deletes a single row in the TBL_FloorMaps table.
	 */
	public void delete(TblFloormapsPk pk) throws TblFloormapsDaoException;

	/** 
	 * Returns the rows from the TBL_FloorMaps table that matches the specified primary-key value.
	 */
	public TblFloormaps findByPrimaryKey(TblFloormapsPk pk) throws TblFloormapsDaoException;

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the criteria 'ID = :id'.
	 */
	public TblFloormaps findByPrimaryKey(int id) throws TblFloormapsDaoException;

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the criteria ''.
	 */
	public TblFloormaps[] findAll() throws TblFloormapsDaoException;

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the criteria 'WID = :wid'.
	 */
	public TblFloormaps[] findByTblWarehouse(int wid) throws TblFloormapsDaoException;

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the criteria 'ID = :id'.
	 */
	public TblFloormaps[] findWhereIdEquals(int id) throws TblFloormapsDaoException;

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the criteria 'WID = :wid'.
	 */
	public TblFloormaps[] findWhereWidEquals(int wid) throws TblFloormapsDaoException;

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the criteria 'Path = :path'.
	 */
	public TblFloormaps[] findWherePathEquals(String path) throws TblFloormapsDaoException;

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the criteria 'Width = :width'.
	 */
	public TblFloormaps[] findWhereWidthEquals(String width) throws TblFloormapsDaoException;

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the criteria 'Version = :version'.
	 */
	public TblFloormaps[] findWhereVersionEquals(int version) throws TblFloormapsDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the specified arbitrary SQL statement
	 */
	public TblFloormaps[] findByDynamicSelect(String sql, Object[] sqlParams) throws TblFloormapsDaoException;

	/** 
	 * Returns all rows from the TBL_FloorMaps table that match the specified arbitrary SQL statement
	 */
	public TblFloormaps[] findByDynamicWhere(String sql, Object[] sqlParams) throws TblFloormapsDaoException;

}