package com.sterp.multitenant.tenant.mail.client.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_mail_config")
public class UserDetailsConfigurationDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "password")
	private String password;
	
	private String protocol="imaps";
	
	private String host;
	
	private int port;
	
	
	@Column(name = "send_host")
	private String sendHost;
	
	@Column(name="send_port")
	private int sendPort;
	
	
	
	@Column(name="user_id")
	private long userId;
	
	
	
	

	public String getSendHost() {
		return sendHost;
	}

	public void setSendHost(String sendHost) {
		this.sendHost = sendHost;
	}

	public int getSendPort() {
		return sendPort;
	}

	public void setSendPort(int sendPort) {
		this.sendPort = sendPort;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	
	@Override
	public String toString() {
		return "UserDetailsConfigurationDto [userName=" + userName + ", password=" + password + ", protocol=" + protocol
				+ ", host=" + host + ", port=" + port + "]";
	}
	
	
	
}
