/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.model;

import java.util.Date;

/**
 *  DESCRIPTION: Domain model data version
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: Jul 15, 2013
 */
public class DataVersion extends BaseEntity {

	//-------------------------------------------------------------------------
	// CONSTANTS
	//-------------------------------------------------------------------------
	
	public static final int DATA_CATEGORY = 1;
	
	public static final int DATA_SL = 2;
	
	public static final int DATA_SL_ITEM = 3;
	
	public static final int DATA_AP = 4;
	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	
	/** Data name */
	private String name;
	
	/** Data version */
	private int version;
	
	/** Data version update date */
	private Date updateDate;
	
	/** Data description */
	private String description;
	
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------
	
	
	public DataVersion() {}


	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}


	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(", ");
		builder.append(version);
		
		return builder.toString();
	}

}
