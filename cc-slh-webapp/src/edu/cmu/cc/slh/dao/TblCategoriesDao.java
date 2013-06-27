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

public interface TblCategoriesDao
{
	/** 
	 * Inserts a new row in the TBL_Categories table.
	 */
	public TblCategoriesPk insert(TblCategories dto) throws TblCategoriesDaoException;

	/** 
	 * Updates a single row in the TBL_Categories table.
	 */
	public void update(TblCategoriesPk pk, TblCategories dto) throws TblCategoriesDaoException;

	/** 
	 * Deletes a single row in the TBL_Categories table.
	 */
	public void delete(TblCategoriesPk pk) throws TblCategoriesDaoException;

	/** 
	 * Returns the rows from the TBL_Categories table that matches the specified primary-key value.
	 */
	public TblCategories findByPrimaryKey(TblCategoriesPk pk) throws TblCategoriesDaoException;

	/** 
	 * Returns all rows from the TBL_Categories table that match the criteria 'SID = :sid'.
	 */
	public TblCategories findByPrimaryKey(int sid) throws TblCategoriesDaoException;

	/** 
	 * Returns all rows from the TBL_Categories table that match the criteria ''.
	 */
	public TblCategories[] findAll() throws TblCategoriesDaoException;

	/** 
	 * Returns all rows from the TBL_Categories table that match the criteria 'SID = :sid'.
	 */
	public TblCategories[] findWhereSidEquals(int sid) throws TblCategoriesDaoException;

	/** 
	 * Returns all rows from the TBL_Categories table that match the criteria 'Name = :name'.
	 */
	public TblCategories[] findWhereNameEquals(String name) throws TblCategoriesDaoException;

	/** 
	 * Sets the value of maxRows
	 */
	public void setMaxRows(int maxRows);

	/** 
	 * Gets the value of maxRows
	 */
	public int getMaxRows();

	/** 
	 * Returns all rows from the TBL_Categories table that match the specified arbitrary SQL statement
	 */
	public TblCategories[] findByDynamicSelect(String sql, Object[] sqlParams) throws TblCategoriesDaoException;

	/** 
	 * Returns all rows from the TBL_Categories table that match the specified arbitrary SQL statement
	 */
	public TblCategories[] findByDynamicWhere(String sql, Object[] sqlParams) throws TblCategoriesDaoException;

}