package com.sterp.multitenant.tenant.mail.client.dto;

public class Mail extends ReplyToDto{
	
	private String mailFrom;
	
	private String bcc;
	
	private String sentDate;
	
	private String mailCc;
	
	private String subject;
	
	private String content;

	private String messageId;
	
	private int messageNumber;
	
	private String contentType;
	
	private String to;
	
	
	

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getMailCc() {
		return mailCc;
	}

	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}

	public int getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMailFrom() {
		return mailFrom;
	}

	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}

	

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getSentDate() {
		return sentDate;
	}

	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Mail() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
	
