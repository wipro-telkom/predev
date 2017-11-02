package com.telkom.apiDatabaseInterface.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Telkomapi_Users")
public class Telkomapi_Bookmark {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private int ID; // Primary Key
	@Column(name = "User_ID")
	private String User_ID;
	@Column(name = "API_ID")
	private String Api_ID;

	

}
