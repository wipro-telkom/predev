package com.telkom.apiDatabaseInterface.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Telkomapi_Cat_Mapping")
public class Telkomapi_Cat_Mapping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private int ID;
	@Column(name = "api_id")
	private int API_ID;
	@Column(name = "cat_id")
	private int CAT_ID;

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
	 * @return the cAT_ID
	 */
	public int getCAT_ID() {
		return CAT_ID;
	}

	/**
	 * @param cAT_ID
	 *            the cAT_ID to set
	 */
	public void setCAT_ID(int cAT_ID) {
		CAT_ID = cAT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Telkomapi_Cat_Mapping [ID=" + ID + ", API_ID=" + API_ID + ", CAT_ID=" + CAT_ID + "]";
	}

}
