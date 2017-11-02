package com.telkom.apiDatabaseInterface.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Telkomapi_Subscription")
public class Telkomapi_Subscription {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "sbscptn_Id")
	private int sbscptn_Id; // Primary Key
	@Column(name = "subscribed_dt")
	private String subscribed_dt;
	@Column(name = "sbscptn_action")
	private String sbscptn_action;
	@Column(name = "sbscptn_status")
	private String sbscptn_status;
	@Column(name = "product_id")
	private int product_id;
	@Column(name = "api_id")
	private int api_id;
	@Column(name = "sbscptn_txn_Id")
	private int sbscptn_txn_Id;
	@Column(name = "user_id")
	private int user_id;

	public int getSbscptn_Id() {
		return sbscptn_Id;
	}

	public void setSbscptn_Id(int sbscptn_Id) {
		this.sbscptn_Id = sbscptn_Id;
	}

	public String getSubscribed_dt() {
		return subscribed_dt;
	}

	public void setSubscribed_dt(String subscribed_dt) {
		this.subscribed_dt = subscribed_dt;
	}

	public String getSbscptn_action() {
		return sbscptn_action;
	}

	public void setSbscptn_action(String sbscptn_action) {
		this.sbscptn_action = sbscptn_action;
	}

	public String getSbscptn_status() {
		return sbscptn_status;
	}

	public void setSbscptn_status(String sbscptn_status) {
		this.sbscptn_status = sbscptn_status;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getApi_id() {
		return api_id;
	}

	public void setApi_id(int api_id) {
		this.api_id = api_id;
	}

	public int getSbscptn_txn_Id() {
		return sbscptn_txn_Id;
	}

	public void setSbscptn_txn_Id(int sbscptn_txn_Id) {
		this.sbscptn_txn_Id = sbscptn_txn_Id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "Telkomapi_Subscription [sbscptn_Id=" + sbscptn_Id + ", subscribed_dt=" + subscribed_dt
				+ ", sbscptn_action=" + sbscptn_action + ", sbscptn_status=" + sbscptn_status + ", product_id="
				+ product_id + ", api_id=" + api_id + ", sbscptn_txn_Id=" + sbscptn_txn_Id + ", user_id=" + user_id
				+ "]";
	}

}
