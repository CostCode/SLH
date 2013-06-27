/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package edu.cmu.cc.slh.factory;

import java.sql.Connection;
import edu.cmu.cc.slh.dao.*;
import edu.cmu.cc.slh.jdbc.*;

public class TblCategoriesDaoFactory
{
	/**
	 * Method 'create'
	 * 
	 * @return TblCategoriesDao
	 */
	public static TblCategoriesDao create()
	{
		return new TblCategoriesDaoImpl();
	}

	/**
	 * Method 'create'
	 * 
	 * @param conn
	 * @return TblCategoriesDao
	 */
	public static TblCategoriesDao create(Connection conn)
	{
		return new TblCategoriesDaoImpl( conn );
	}

}