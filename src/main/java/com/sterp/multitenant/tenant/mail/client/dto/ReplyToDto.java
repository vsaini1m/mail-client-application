package com.sterp.multitenant.tenant.mail.client.dto;

public class ReplyToDto{

	private String folderName;

	private int messageNumber;

	private String content;

	///this is for only forward
	private String forwardTo;
	

	
	
	
	////////////this is for only mail recive///
	private int startFrom;
	
	////////////this is for only mail recive///	
	private int mailsLimit;
	
	public int getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(int startFrom) {
		this.startFrom = startFrom;
	}

	public int getMailsLimit() {
		return mailsLimit;
	}

	public void setMailsLimit(int mailsLimit) {
		this.mailsLimit = mailsLimit;
	}

	public String getForwardTo() {
		return forwardTo;
	}

	public void setForwardTo(String forwardTo) {
		this.forwardTo = forwardTo;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public int getMessageNumber() {
		return messageNumber;
	}

	public void setMessageNumber(int messageNumber) {
		this.messageNumber = messageNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ReplyToDto [folderName=" + folderName + ", messageNumber=" + messageNumber + ", content=" + content
				+ "]";
	}

	public ReplyToDto() {
		super();
		// TODO Auto-generated constructor stub
	}

}
