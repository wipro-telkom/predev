package com.telkom.apiDatabaseInterface.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TelkomAPI_Categories")
public class TelkomAPI_Categories {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "cat_id")
	private int CAT_ID; // API Category ID
	@Column(name = "cat_name")
	private String CAT_NAME; // API Category Name
	@Column(name = "cat_desc")
	private String CAT_DESC; // API Category Description
	@Column(name = "cat_status")
	private Boolean CAT_STATUS; // API Category Status

	public String getIDname() {
		return "CAT_ID";
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

	/**
	 * @return the cAT_NAME
	 */
	public String getCAT_NAME() {
		return CAT_NAME;
	}

	/**
	 * @param cAT_NAME
	 *            the cAT_NAME to set
	 */
	public void setCAT_NAME(String cAT_NAME) {
		CAT_NAME = cAT_NAME;
	}

	/**
	 * @return the cAT_DESC
	 */
	public String getCAT_DESC() {
		return CAT_DESC;
	}

	/**
	 * @param cAT_DESC
	 *            the cAT_DESC to set
	 */
	public void setCAT_DESC(String cAT_DESC) {
		CAT_DESC = cAT_DESC;
	}

	/**
	 * @return the cAT_STATUS
	 */
	public Boolean getCAT_STATUS() {
		return CAT_STATUS;
	}

	/**
	 * @param cAT_STATUS
	 *            the cAT_STATUS to set
	 */
	public void setCAT_STATUS(Boolean cAT_STATUS) {
		CAT_STATUS = cAT_STATUS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TelkomAPI_Categories [CAT_ID=" + CAT_ID + ", CAT_NAME=" + CAT_NAME + ", CAT_DESC=" + CAT_DESC
				+ ", CAT_STATUS=" + CAT_STATUS + "]";
	}

}
