package com.sterp.multitenant.tenant.mail.client.service;

import java.util.List;

import com.sterp.multitenant.tenant.mail.client.dto.EmailReciverFolders;
import com.sterp.multitenant.tenant.mail.client.dto.Mail;
import com.sterp.multitenant.tenant.mail.client.dto.ReplyToDto;
import com.sterp.multitenant.tenant.mail.client.dto.UserDetailsConfigurationDto;



public interface MailService {
	


	boolean sendEmail(Mail mail);
	
	List<Mail> reciverIMAPEmail(ReplyToDto dto);

	

	List<EmailReciverFolders> allImapEmailFoldersWithEmailCount();


	boolean forwardTo(ReplyToDto dto);



	boolean deleteTo(ReplyToDto dto);

	
	

	boolean replyTo(ReplyToDto dto);

	boolean seenTo(ReplyToDto dto);

	Mail reciveMailById(ReplyToDto dto);

	
	
}
