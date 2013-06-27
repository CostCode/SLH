/*
 * This source file was generated by FireStorm/DAO.
 * 
 * If you purchase a full license for FireStorm/DAO you can customize this header file.
 * 
 * For more information please visit http://www.codefutures.com/products/firestorm
 */

package edu.cmu.cc.slh.dto;

import edu.cmu.cc.slh.dao.*;
import edu.cmu.cc.slh.factory.*;
import edu.cmu.cc.slh.exceptions.*;
import java.io.Serializable;
import java.util.*;

public class TblWarehouse implements Serializable
{
	/** 
	 * This attribute maps to the column WID in the TBL_Warehouse table.
	 */
	protected int wid;

	/** 
	 * This attribute maps to the column Address in the TBL_Warehouse table.
	 */
	protected String address;

	/** 
	 * This attribute maps to the column UID in the TBL_Warehouse table.
	 */
	protected int uid;

	/** 
	 * This attribute represents whether the primitive attribute uid is null.
	 */
	protected boolean uidNull = true;

	/**
	 * Method 'TblWarehouse'
	 * 
	 */
	public TblWarehouse()
	{
	}

	/**
	 * Method 'getWid'
	 * 
	 * @return int
	 */
	public int getWid()
	{
		return wid;
	}

	/**
	 * Method 'setWid'
	 * 
	 * @param wid
	 */
	public void setWid(int wid)
	{
		this.wid = wid;
	}

	/**
	 * Method 'getAddress'
	 * 
	 * @return String
	 */
	public String getAddress()
	{
		return address;
	}

	/**
	 * Method 'setAddress'
	 * 
	 * @param address
	 */
	public void setAddress(String address)
	{
		this.address = address;
	}

	/**
	 * Method 'getUid'
	 * 
	 * @return int
	 */
	public int getUid()
	{
		return uid;
	}

	/**
	 * Method 'setUid'
	 * 
	 * @param uid
	 */
	public void setUid(int uid)
	{
		this.uid = uid;
		this.uidNull = false;
	}

	/**
	 * Method 'setUidNull'
	 * 
	 * @param value
	 */
	public void setUidNull(boolean value)
	{
		this.uidNull = value;
	}

	/**
	 * Method 'isUidNull'
	 * 
	 * @return boolean
	 */
	public boolean isUidNull()
	{
		return uidNull;
	}

	/**
	 * Method 'equals'
	 * 
	 * @param _other
	 * @return boolean
	 */
	public boolean equals(Object _other)
	{
		if (_other == null) {
			return false;
		}
		
		if (_other == this) {
			return true;
		}
		
		if (!(_other instanceof TblWarehouse)) {
			return false;
		}
		
		final TblWarehouse _cast = (TblWarehouse) _other;
		if (wid != _cast.wid) {
			return false;
		}
		
		if (address == null ? _cast.address != address : !address.equals( _cast.address )) {
			return false;
		}
		
		if (uid != _cast.uid) {
			return false;
		}
		
		if (uidNull != _cast.uidNull) {
			return false;
		}
		
		return true;
	}

	/**
	 * Method 'hashCode'
	 * 
	 * @return int
	 */
	public int hashCode()
	{
		int _hashCode = 0;
		_hashCode = 29 * _hashCode + wid;
		if (address != null) {
			_hashCode = 29 * _hashCode + address.hashCode();
		}
		
		_hashCode = 29 * _hashCode + uid;
		_hashCode = 29 * _hashCode + (uidNull ? 1 : 0);
		return _hashCode;
	}

	/**
	 * Method 'createPk'
	 * 
	 * @return TblWarehousePk
	 */
	public TblWarehousePk createPk()
	{
		return new TblWarehousePk(wid);
	}

	/**
	 * Method 'toString'
	 * 
	 * @return String
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append( "edu.cmu.cc.slh.dto.TblWarehouse: " );
		ret.append( "wid=" + wid );
		ret.append( ", address=" + address );
		ret.append( ", uid=" + uid );
		return ret.toString();
	}

}