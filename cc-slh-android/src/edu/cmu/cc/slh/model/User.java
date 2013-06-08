/**
 * Copyright (c) 2013, CostCode. All rights reserved.
 * Use is subject to license terms.
 */
package edu.cmu.cc.slh.model;


/**
 *  DESCRIPTION: User of the system (Costco member customer)
 *	
 *  @author Azamat Samiyev
 *	@version 1.0
 *  Date: May 31, 2013
 */
public class User extends BaseEntity {

	
	//-------------------------------------------------------------------------
	// FIELDS
	//-------------------------------------------------------------------------
	
	
	/** Costco membership id */
	private String memberId;
	
	/** Username: used in user authorization */
	private String username;
	
	/** First name of the user */
	private String firstName;
	
	/** Last name of the user */
	private String lastName;
	
	/** Password: used in user authorization */
	private String password;
	
	
	//-------------------------------------------------------------------------
	// CONSTRUCTORS
	//-------------------------------------------------------------------------	
	
	
	public User() {}
	
	public User(String memberId, String firstName, String lastName, 
			String username, String password) {
		
		this.memberId = memberId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		
	}
	
	public User(long id, String memberId, String firstName, String lastName, 
			String username, String password) {
		
		this.id = id;
		this.memberId = memberId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		
	}


	//-------------------------------------------------------------------------
	// PUBLIC METHODS
	//-------------------------------------------------------------------------

	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		builder.append(", ");
		builder.append(memberId);
		builder.append(", ");
		builder.append(username);
		
		return builder.toString();
		
	}//toString

	
	
	//-------------------------------------------------------------------------
	// GETTERS - SETTERS
	//-------------------------------------------------------------------------
	
	
	public String getMemberId() {
		return memberId;
	}


	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}	
	
}
