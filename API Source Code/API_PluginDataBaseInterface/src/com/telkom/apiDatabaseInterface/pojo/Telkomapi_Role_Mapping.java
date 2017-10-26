package com.telkom.apiDatabaseInterface.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Telkomapi_Role_Mapping")
public class Telkomapi_Role_Mapping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int ID;
	@Column(name = "api_id")
	private int API_ID;
	@Column(name = "role_id")
	private int ROLE_ID;

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
	 * @return the aPI_ID
	 */
	public int getAPI_ID() {
		return API_ID;
	}

	/**
	 * @param aPI_ID
	 *            the aPI_ID to set
	 */
	public void setAPI_ID(int aPI_ID) {
		API_ID = aPI_ID;
	}

	/**
	 * @return the rOLE_ID
	 */
	public int getROLE_ID() {
		return ROLE_ID;
	}

	/**
	 * @param rOLE_ID
	 *            the rOLE_ID to set
	 */
	public void setROLE_ID(int rOLE_ID) {
		ROLE_ID = rOLE_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Telkomapi_Role_Mapping [ID=" + ID + ", API_ID=" + API_ID + ", ROLE_ID=" + ROLE_ID + "]";
	}

}
