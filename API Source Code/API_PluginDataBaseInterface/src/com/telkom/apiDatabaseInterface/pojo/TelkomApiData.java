package com.telkom.apiDatabaseInterface.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TelkomApiData")
public class TelkomApiData {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "API_ID")
	private int API_ID; // Primary Key
	@Column(name = "API_Mgr_ID")
	private int API_Mgr_ID; // API Manager ID : Name referenced from Another
							// Table
	@Column(name = "API_Name")
	private String API_Name; // API Name should Be Unique
	@Column(name = "API_Short_Desc")
	private String API_Short_Desc; // API Short Description
	@Column(name = "API_Summ")
	private String API_Summ; // API Summary
	@Column(name = "API_Prd_URL")
	private String API_Prd_URL; // API Production URL
	@Column(name = "API_Sand_URL")
	private String API_Sand_URL; // API Sandbox URL
	@Column(name = "API_Ver")
	private String API_Ver; // API Version
	@Column(name = "API_Pub_Env")
	private String API_Pub_Env; // API Production Environment
	@Column(name = "API_Status")
	private String API_Status; // API Status for Mediation Portal View
								// Enablement

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
	 * @return the aPI_Mgr_ID
	 */
	public int getAPI_Mgr_ID() {
		return API_Mgr_ID;
	}

	/**
	 * @param aPI_Mgr_ID
	 *            the aPI_Mgr_ID to set
	 */
	public void setAPI_Mgr_ID(int aPI_Mgr_ID) {
		API_Mgr_ID = aPI_Mgr_ID;
	}

	/**
	 * @return the aPI_Name
	 */
	public String getAPI_Name() {
		return API_Name;
	}

	/**
	 * @param aPI_Name
	 *            the aPI_Name to set
	 */
	public void setAPI_Name(String aPI_Name) {
		API_Name = aPI_Name;
	}

	/**
	 * @return the aPI_Short_Desc
	 */
	public String getAPI_Short_Desc() {
		return API_Short_Desc;
	}

	/**
	 * @param aPI_Short_Desc
	 *            the aPI_Short_Desc to set
	 */
	public void setAPI_Short_Desc(String aPI_Short_Desc) {
		API_Short_Desc = aPI_Short_Desc;
	}

	/**
	 * @return the aPI_Summ
	 */
	public String getAPI_Summ() {
		return API_Summ;
	}

	/**
	 * @param aPI_Summ
	 *            the aPI_Summ to set
	 */
	public void setAPI_Summ(String aPI_Summ) {
		API_Summ = aPI_Summ;
	}

	/**
	 * @return the aPI_Prd_URL
	 */
	public String getAPI_Prd_URL() {
		return API_Prd_URL;
	}

	/**
	 * @param aPI_Prd_URL
	 *            the aPI_Prd_URL to set
	 */
	public void setAPI_Prd_URL(String aPI_Prd_URL) {
		API_Prd_URL = aPI_Prd_URL;
	}

	/**
	 * @return the aPI_Sand_URL
	 */
	public String getAPI_Sand_URL() {
		return API_Sand_URL;
	}

	/**
	 * @param aPI_Sand_URL
	 *            the aPI_Sand_URL to set
	 */
	public void setAPI_Sand_URL(String aPI_Sand_URL) {
		API_Sand_URL = aPI_Sand_URL;
	}

	/**
	 * @return the aPI_Ver
	 */
	public String getAPI_Ver() {
		return API_Ver;
	}

	/**
	 * @param aPI_Ver
	 *            the aPI_Ver to set
	 */
	public void setAPI_Ver(String aPI_Ver) {
		API_Ver = aPI_Ver;
	}

	/**
	 * @return the aPI_Pub_Env
	 */
	public String getAPI_Pub_Env() {
		return API_Pub_Env;
	}

	/**
	 * @param aPI_Pub_Env
	 *            the aPI_Pub_Env to set
	 */
	public void setAPI_Pub_Env(String aPI_Pub_Env) {
		API_Pub_Env = aPI_Pub_Env;
	}

	/**
	 * @return the aPI_Status
	 */
	public String getAPI_Status() {
		return API_Status;
	}

	/**
	 * @param aPI_Status
	 *            the aPI_Status to set
	 */
	public void setAPI_Status(String aPI_Status) {
		API_Status = aPI_Status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TelkomApiData [API_ID=" + API_ID + ", API_Mgr_ID=" + API_Mgr_ID + ", API_Name=" + API_Name
				+ ", API_Short_Desc=" + API_Short_Desc + ", API_Summ=" + API_Summ + ", API_Prd_URL=" + API_Prd_URL
				+ ", API_Sand_URL=" + API_Sand_URL + ", API_Ver=" + API_Ver + ", API_Pub_Env=" + API_Pub_Env
				+ ", API_Status=" + API_Status + "]";
	}

}
