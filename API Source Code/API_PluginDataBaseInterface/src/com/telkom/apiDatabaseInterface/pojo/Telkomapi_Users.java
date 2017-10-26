package com.telkom.apiDatabaseInterface.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Telkomapi_Users")
public class Telkomapi_Users {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int ID; // Primary Key
	@Column(name = "User_ID")
	private String User_ID;
	@Column(name = "Password")
	private String Password;
	@Column(name = "Access_Code")
	private String Access_Code;
	@Column(name = "validity")
	private String validity;

	/**
	 * @return the iD
	 */
	public int getID() {
		return ID;
	}

	/**
	 * @param iD
	 *            the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * @return the user_ID
	 */
	public String getUser_ID() {
		return User_ID;
	}

	/**
	 * @param user_ID
	 *            the user_ID to set
	 */
	public void setUser_ID(String user_ID) {
		User_ID = user_ID;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return Password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		Password = password;
	}

	/**
	 * @return the access_Code
	 */
	public String getAccess_Code() {
		return Access_Code;
	}

	/**
	 * @param access_Code
	 *            the access_Code to set
	 */
	public void setAccess_Code(String access_Code) {
		Access_Code = access_Code;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	@Override
	public String toString() {
		return "Telkomapi_Users [ID=" + ID + ", User_ID=" + User_ID + ", Password=" + Password + ", Access_Code="
				+ Access_Code + ", validity=" + validity + "]";
	}

}
