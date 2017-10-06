package com.telkom.apiDatabaseInterface.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TelkomAPI_Document")
public class TelkomAPI_Document {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "document_id")
	private int DOCUMENT_ID; // API Document ID
	@Column(name = "document_name")
	private String DOCUMENT_NAME; // API Document Name
	@Column(name = "document_desc")
	private String DOCUMENT_DESC; // API Document Description
	@Column(name = "document_path")
	private String DOCUMENT_PATH; // API Document Path
	@Column(name = "api_id")
	private String API_ID; // API ID Referenced from API Data table

	/**
	 * @return the dOCUMENT_ID
	 */
	public int getDOCUMENT_ID() {
		return DOCUMENT_ID;
	}

	/**
	 * @param dOCUMENT_ID
	 *            the dOCUMENT_ID to set
	 */
	public void setDOCUMENT_ID(int dOCUMENT_ID) {
		DOCUMENT_ID = dOCUMENT_ID;
	}

	/**
	 * @return the dOCUMENT_NAME
	 */
	public String getDOCUMENT_NAME() {
		return DOCUMENT_NAME;
	}

	/**
	 * @param dOCUMENT_NAME
	 *            the dOCUMENT_NAME to set
	 */
	public void setDOCUMENT_NAME(String dOCUMENT_NAME) {
		DOCUMENT_NAME = dOCUMENT_NAME;
	}

	/**
	 * @return the dOCUMENT_DESC
	 */
	public String getDOCUMENT_DESC() {
		return DOCUMENT_DESC;
	}

	/**
	 * @param dOCUMENT_DESC
	 *            the dOCUMENT_DESC to set
	 */
	public void setDOCUMENT_DESC(String dOCUMENT_DESC) {
		DOCUMENT_DESC = dOCUMENT_DESC;
	}

	/**
	 * @return the dOCUMENT_PATH
	 */
	public String getDOCUMENT_PATH() {
		return DOCUMENT_PATH;
	}

	/**
	 * @param dOCUMENT_PATH
	 *            the dOCUMENT_PATH to set
	 */
	public void setDOCUMENT_PATH(String dOCUMENT_PATH) {
		DOCUMENT_PATH = dOCUMENT_PATH;
	}

	/**
	 * @return the aPI_ID
	 */
	public String getAPI_ID() {
		return API_ID;
	}

	/**
	 * @param aPI_ID
	 *            the aPI_ID to set
	 */
	public void setAPI_ID(String aPI_ID) {
		API_ID = aPI_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TelkomAPI_Document [DOCUMENT_ID=" + DOCUMENT_ID + ", DOCUMENT_NAME=" + DOCUMENT_NAME
				+ ", DOCUMENT_DESC=" + DOCUMENT_DESC + ", DOCUMENT_PATH=" + DOCUMENT_PATH + ", API_ID=" + API_ID + "]";
	}

}
