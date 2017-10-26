package com.telkom.apiDatabaseInterface.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TelkomApiManager")
public class TelkomApiManager {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int API_MNGR_ID; // API Manager ID Referenced to API
	private String API_MNGR_NAME; // API Manager Name
	private String PUBLISHER_URL; // API Publisher URL
	private String STORE_URL; // API Store URL
	private String USER_ID; // API USER ID
	private String PASSWORD; // API Password
	private String TOKEN_URL; // API Token API URL
	private Boolean API_MANAGER_STATUS; // API MANAGER STATUS

	public String getIDname() {
		return "API_MNGR_ID";
	}

	/**
	 * @return the aPI_MNGR_ID
	 */
	public int getAPI_MNGR_ID() {
		return API_MNGR_ID;
	}

	/**
	 * @param aPI_MNGR_ID
	 *            the aPI_MNGR_ID to set
	 */
	public void setAPI_MNGR_ID(int aPI_MNGR_ID) {
		API_MNGR_ID = aPI_MNGR_ID;
	}

	/**
	 * @return the aPI_MNGR_NAME
	 */
	public String getAPI_MNGR_NAME() {
		return API_MNGR_NAME;
	}

	/**
	 * @param aPI_MNGR_NAME
	 *            the aPI_MNGR_NAME to set
	 */
	public void setAPI_MNGR_NAME(String aPI_MNGR_NAME) {
		API_MNGR_NAME = aPI_MNGR_NAME;
	}

	/**
	 * @return the pUBLISHER_URL
	 */
	public String getPUBLISHER_URL() {
		return PUBLISHER_URL;
	}

	/**
	 * @param pUBLISHER_URL
	 *            the pUBLISHER_URL to set
	 */
	public void setPUBLISHER_URL(String pUBLISHER_URL) {
		PUBLISHER_URL = pUBLISHER_URL;
	}

	/**
	 * @return the sTORE_URL
	 */
	public String getSTORE_URL() {
		return STORE_URL;
	}

	/**
	 * @param sTORE_URL
	 *            the sTORE_URL to set
	 */
	public void setSTORE_URL(String sTORE_URL) {
		STORE_URL = sTORE_URL;
	}

	/**
	 * @return the uSER_ID
	 */
	public String getUSER_ID() {
		return USER_ID;
	}

	/**
	 * @param uSER_ID
	 *            the uSER_ID to set
	 */
	public void setUSER_ID(String uSER_ID) {
		USER_ID = uSER_ID;
	}

	/**
	 * @return the pASSWORD
	 */
	public String getPASSWORD() {
		return PASSWORD;
	}

	/**
	 * @param pASSWORD
	 *            the pASSWORD to set
	 */
	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	/**
	 * @return the tOKEN_URL
	 */
	public String getTOKEN_URL() {
		return TOKEN_URL;
	}

	/**
	 * @param tOKEN_URL
	 *            the tOKEN_URL to set
	 */
	public void setTOKEN_URL(String tOKEN_URL) {
		TOKEN_URL = tOKEN_URL;
	}

	/**
	 * @return the aPI_MANAGER_STATUS
	 */
	public Boolean getAPI_MANAGER_STATUS() {
		return API_MANAGER_STATUS;
	}

	/**
	 * @param aPI_MANAGER_STATUS
	 *            the aPI_MANAGER_STATUS to set
	 */
	public void setAPI_MANAGER_STATUS(Boolean aPI_MANAGER_STATUS) {
		API_MANAGER_STATUS = aPI_MANAGER_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TelkomApiManager [API_MNGR_ID=" + API_MNGR_ID + ", API_MNGR_NAME=" + API_MNGR_NAME + ", PUBLISHER_URL="
				+ PUBLISHER_URL + ", STORE_URL=" + STORE_URL + ", USER_ID=" + USER_ID + ", PASSWORD=" + PASSWORD
				+ ", TOKEN_URL=" + TOKEN_URL + ", API_MANAGER_STATUS=" + API_MANAGER_STATUS + "]";
	}

}
