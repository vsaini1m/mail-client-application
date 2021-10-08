package com.sterp.multitenant.tenant.mail.client.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.sterp.multitenant.tenant.mail.client.dto.EmailReciverFolders;
import com.sterp.multitenant.tenant.mail.client.dto.Mail;
import com.sterp.multitenant.tenant.mail.client.dto.ReplyToDto;
import com.sterp.multitenant.tenant.mail.client.dto.UserDetailsConfigurationDto;
import com.sterp.multitenant.tenant.mail.client.service.MailService;
import com.sterp.multitenant.tenant.mail.client.service.MailUserConfigService;

@Service
public class MailSeriveImpl implements MailService {

	
	@Autowired
	MailUserConfigService mailUserConfigService;
	
	@Override
	public boolean sendEmail(Mail mail) {
		
		
		UserDetailsConfigurationDto configurationByCurrentUser = mailUserConfigService.getUserMailDetailsConfigurationByCurrentUser();
		
		/// set user configuration
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost(configurationByCurrentUser.getSendHost());
		javaMailSender.setPort(configurationByCurrentUser.getSendPort());
		javaMailSender.setUsername(configurationByCurrentUser.getUserName());
		javaMailSender.setPassword(configurationByCurrentUser.getPassword());

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.debug", "true");
		javaMailProperties.put("mail.smtp.ssl.trust", "*");

		javaMailSender.setJavaMailProperties(javaMailProperties);

		/// set mail properties
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setSubject(mail.getSubject());
			mimeMessageHelper.setFrom(new InternetAddress(configurationByCurrentUser.getUserName()));
			mimeMessageHelper.setTo(mail.getTo());
			mimeMessageHelper.setText(mail.getContent());
			javaMailSender.send(mimeMessageHelper.getMimeMessage());

			return true;
		} catch (MessagingException e) {
			e.printStackTrace();

			return false;
		}
	}

	@Override
	public List<EmailReciverFolders> allImapEmailFoldersWithEmailCount() {
		 UserDetailsConfigurationDto configurationByCurrentUser = mailUserConfigService.getUserMailDetailsConfigurationByCurrentUser();
		
		Properties props = new Properties();

		System.out.println(configurationByCurrentUser);
		List<EmailReciverFolders> listOfFolders = new ArrayList<EmailReciverFolders>();

		props.setProperty("mail.store.protocol", configurationByCurrentUser.getProtocol());

		try {
			Session session = Session.getInstance(props, null);
			Store store = session.getStore();

			store.connect(configurationByCurrentUser.getHost(), configurationByCurrentUser.getUserName(), configurationByCurrentUser.getPassword());

			Folder[] folderList = null ;
			
			
			if (configurationByCurrentUser.getUserName().toLowerCase().contains("gmail"))
				folderList=store.getFolder("[Gmail]").list();
			else 
				folderList=store.getDefaultFolder().list();
			

			for (int i = 0; i < folderList.length; i++) {

				String fullName = folderList[i].getFullName();
				Folder folder = store.getFolder(folderList[i].getFullName());

				EmailReciverFolders emailReciverFolders = new EmailReciverFolders();
				emailReciverFolders.setFolderName(fullName);
				emailReciverFolders.setCount(folder.getMessageCount() + "");

				listOfFolders.add(emailReciverFolders);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return listOfFolders;
	}

	@Override
	public List<Mail> reciverIMAPEmail(ReplyToDto dto) {
		 UserDetailsConfigurationDto configurationByCurrentUser = mailUserConfigService.getUserMailDetailsConfigurationByCurrentUser();
			
			
		List<Mail> mailList = new ArrayList<Mail>();
		Properties props = new Properties();

		props.setProperty("mail.store.protocol", configurationByCurrentUser.getProtocol());

		try {
			Session session = Session.getInstance(props, null);
			Store store = session.getStore();
			store.connect(configurationByCurrentUser.getHost(), configurationByCurrentUser.getUserName(), configurationByCurrentUser.getPassword());

			Folder inbox = store.getFolder(dto.getFolderName());

			inbox.open(Folder.READ_ONLY);

			int messageCount = inbox.getMessageCount();

			

			if (messageCount >= 10)
				messageCount = 10;

			for (int i = dto.getStartFrom(); i < dto.getStartFrom() + dto.getMailsLimit(); i++) {

				Mail mail = new Mail();

				MimeMessage msg = (MimeMessage) inbox.getMessage(inbox.getMessageCount() - i);

				mail.setMessageNumber(msg.getMessageNumber());

				mail.setMailFrom(msg.getFrom()[0].toString());
				mail.setBcc(InternetAddress.toString(msg.getRecipients(Message.RecipientType.BCC)));
				mail.setSentDate(msg.getSentDate().toString());
				mail.setSubject(msg.getSubject());
				mail.setContentType(msg.getContentType());
				mail.setMessageId(msg.getMessageID());
				mail.setFolderName(dto.getFolderName());
				Object content = msg.getContent();
				if (content instanceof String) {
					System.out.println("its string");
					System.out.println(content.toString());
					mail.setContent(content.toString());
				} else if (content instanceof Multipart) {
					Multipart mp = (Multipart) content;

					BodyPart bp = mp.getBodyPart(0);
					System.out.println("its multipart");
					System.out.println(bp.getContent().toString());

					String textFromMimeMultipart = getTextFromMimeMultipart((MimeMultipart) mp);

					System.out.println("after convert to mime part to text");
					System.out.println(textFromMimeMultipart);

					mail.setContent(textFromMimeMultipart);

				}
				
				System.out.println(mail);

				mailList.add(mail);

			}

		} catch (Exception mex) {
			mex.printStackTrace();
		}

		return mailList;
	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = result + "" + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return result;
	}

	@Override
	public boolean deleteTo(ReplyToDto dto) {
		
		 UserDetailsConfigurationDto configurationByCurrentUser = mailUserConfigService.getUserMailDetailsConfigurationByCurrentUser();
			
	
		Properties properties = new Properties();
		// server setting
		properties.put("mail.imap.host", configurationByCurrentUser.getHost());
		properties.put("mail.imap.port", configurationByCurrentUser.getPort());

		// SSL setting
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port", String.valueOf(configurationByCurrentUser.getPort()));

		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore("imap");
			store.connect(configurationByCurrentUser.getUserName(), configurationByCurrentUser.getPassword());

			// opens the inbox folder
			Folder folderInbox = store.getFolder(dto.getFolderName());
			folderInbox.open(Folder.READ_WRITE);

			// fetches new messages from server
		
		
			Message message = folderInbox.getMessage(dto.getMessageNumber());
			
			
				int messageNumber = message.getMessageNumber();

				if (messageNumber == dto.getMessageNumber()) {
					System.out.println(message + " is delete successfully!");

					message.setFlag(Flags.Flag.DELETED, true);
					System.out.println("Marked DELETE for message: ");
					
					// expunges the folder to remove messages which are marked deleted
					boolean expunge = true;
					folderInbox.close(expunge);
				}

			// another way:
			// folderInbox.expunge();
			// folderInbox.close(false);

			// disconnect
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider.");
			ex.printStackTrace();
			return false;
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store.");
			ex.printStackTrace();
			return false;
		}

		return true;
	}




	@Override
	public boolean forwardTo(ReplyToDto dto) {
		 
		 UserDetailsConfigurationDto configurationByCurrentUser = mailUserConfigService.getUserMailDetailsConfigurationByCurrentUser();
			
		 
		 System.out.println(configurationByCurrentUser);
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		String username = configurationByCurrentUser.getUserName();
		String password = configurationByCurrentUser.getPassword();

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		
		  // Get a Store object and connect to the current host   
		
		try {
			  Store store = session.getStore(configurationByCurrentUser.getProtocol());
			  
			  store.connect(configurationByCurrentUser.getHost(),configurationByCurrentUser.getUserName(),password);  
			  
			  //Create a Folder object and open the folder  
			  Folder folder = store.getFolder(dto.getFolderName());  
			  folder.open(Folder.READ_ONLY);  
			    
			  Message message = folder.getMessage(1);  
			  
			  // Get all the information from the message  
			  String from = InternetAddress.toString(message.getFrom());  
			  if (from != null) {  
			  System.out.println("From: " + from);  
			  }  
			 
			  
			  String to = dto.getForwardTo();
			  if (to != null) {  
			  System.out.println("To: " + to);  
			  }  
			  
			  String subject = message.getSubject();  
			  if (subject != null) {  
			  System.out.println("Subject: " + subject);  
			  }  
			  Date sent = message.getSentDate();  
			  if (sent != null) {  
			  System.out.println("Sent: " + sent);  
			  }  
			  System.out.println(message.getContent());  
			  
			  // compose the message to forward  
			  Message message2 = new MimeMessage(session);  
			  message2.setSubject("Fwd: " + message.getSubject());  
			  message2.setFrom(new InternetAddress(from));  
			  message2.addRecipient(Message.RecipientType.TO,  
			  new InternetAddress(to));  
			  
			  // Create your new message part  
			  BodyPart messageBodyPart = new MimeBodyPart();  
			  messageBodyPart.setText("This is Forwarded Mail :\n\n");  
			  
			  // Create a multi-part to combine the parts  
			  Multipart multipart = new MimeMultipart();  
			  multipart.addBodyPart(messageBodyPart);  
			  
			  // Create and fill part for the forwarded content  
			  messageBodyPart = new MimeBodyPart();  
			  messageBodyPart.setDataHandler(message.getDataHandler());  
			  
			  // Add part to multi part  
			  multipart.addBodyPart(messageBodyPart);  
			  
			  // Associate multi-part with message  
			  message2.setContent(multipart);  
			  
			  // Send message  
			  Transport.send(message2);  
			  
			  System.out.println("message forwarded ....");  
			  
			  
			  
			  
			  
			  
		} catch (MessagingException e) {
		
			e.printStackTrace();
			
			return false;
		} catch (IOException e) {
		
			e.printStackTrace();
			return false;
		}  
		 
		    
	return true;

	}

	@Override
	public boolean replyTo(ReplyToDto dto) {
		 
		 ///username, password, host, port,protocol, getFolderName, messageumber, 
		 UserDetailsConfigurationDto configurationByCurrentUser = mailUserConfigService.getUserMailDetailsConfigurationByCurrentUser();
			 
		 System.out.println(configurationByCurrentUser);
		Properties props = new Properties();
		props.put("mail.smtp.host", configurationByCurrentUser.getSendHost());
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", configurationByCurrentUser.getSendPort());

		String username = configurationByCurrentUser.getUserName();
		String password = configurationByCurrentUser.getPassword();

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		
		
		try {

			// connect to the host and port
			Store store = session.getStore(configurationByCurrentUser.getProtocol());
			store.connect(configurationByCurrentUser.getHost(), username, password);

			// Create a Folder object and open the folder
			Folder folder = store.getFolder(dto.getFolderName());
			folder.open(Folder.READ_WRITE);

			/// get message from the folder by message number
			Message message = folder.getMessage(dto.getMessageNumber());

			// Get all the information from the message
			String from = InternetAddress.toString(message.getFrom());

			

			

		

			// compose the message to forward
			Message message2 = new MimeMessage(session);
			message2 = (MimeMessage) message.reply(false);
			message2.setSubject("RE: " + message.getSubject());
			message2.setFrom(new InternetAddress(from));
			message2.setReplyTo(message.getReplyTo());


			// Create your new message part
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(dto.getContent());

			// Create a multi-part to combine the parts
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Create and fill part for the forwarded content
			messageBodyPart = new MimeBodyPart();
			messageBodyPart.setDataHandler(message.getDataHandler());

			// Add part to multi part
			multipart.addBodyPart(messageBodyPart);

			// Associate multi-part with message
			message2.setContent(multipart);

			// Send message
			Transport.send(message2);

			System.out.println("message replied successfully ....");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
	
	@Override
	public boolean seenTo(ReplyToDto dto) {
		 UserDetailsConfigurationDto configurationByCurrentUser = mailUserConfigService.getUserMailDetailsConfigurationByCurrentUser();
			

		
		Properties properties = new Properties();
		// server setting
		properties.put("mail.imap.host", configurationByCurrentUser.getHost());
		properties.put("mail.imap.port", configurationByCurrentUser.getPort());

		// SSL setting
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port", String.valueOf(configurationByCurrentUser.getPort()));

		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore("imap");
			store.connect(configurationByCurrentUser.getUserName(), configurationByCurrentUser.getPassword());

			// opens the inbox folder
			Folder folderInbox = store.getFolder(dto.getFolderName());
			folderInbox.open(Folder.READ_WRITE);

			// fetches new messages from server
		
		
			MimeMessage message = (MimeMessage) folderInbox.getMessage(dto.getMessageNumber());
			
			
				int messageNumber = message.getMessageNumber();

				if (messageNumber == dto.getMessageNumber()) {
					

					message.setFlag(Flags.Flag.SEEN, true);
					System.out.println("Mail Seen Successfully");
					
					// expunges the folder to remove messages which are marked deleted
					boolean expunge = true;
					folderInbox.close(expunge);
				}

			// another way:
			// folderInbox.expunge();
			// folderInbox.close(false);

			// disconnect
			store.close();
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider.");
			ex.printStackTrace();
			return false;
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store.");
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	
	////TODO: find mail by id
	
	@Override
	public Mail reciveMailById(ReplyToDto dto) {
		 UserDetailsConfigurationDto configurationByCurrentUser = mailUserConfigService.getUserMailDetailsConfigurationByCurrentUser();
			
		Properties properties = new Properties();
		// server setting
		properties.put("mail.imap.host", configurationByCurrentUser.getHost());
		properties.put("mail.imap.port", configurationByCurrentUser.getPort());

		// SSL setting
		properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.imap.socketFactory.fallback", "false");
		properties.setProperty("mail.imap.socketFactory.port", String.valueOf(configurationByCurrentUser.getPort()));

		Session session = Session.getDefaultInstance(properties);

		try {
			// connects to the message store
			Store store = session.getStore("imap");
			store.connect(configurationByCurrentUser.getUserName(), configurationByCurrentUser.getPassword());

			// opens the inbox folder
			Folder folderInbox = store.getFolder(dto.getFolderName());
			folderInbox.open(Folder.READ_WRITE);

			// fetches new messages from server
		
		
			MimeMessage msg = (MimeMessage) folderInbox.getMessage(dto.getMessageNumber());
		
		

			
			Mail mail = new Mail();

			

			mail.setMessageNumber(msg.getMessageNumber());

			mail.setMailFrom(msg.getFrom()[0].toString());
			mail.setBcc(InternetAddress.toString(msg.getRecipients(Message.RecipientType.BCC)));
			mail.setSentDate(msg.getSentDate().toString());
			mail.setSubject(msg.getSubject());
			mail.setContentType(msg.getContentType());
			
			mail.setContentType(msg.getContentType());
			mail.setMessageId(msg.getMessageID());
			mail.setFolderName(dto.getFolderName());
			Object content = msg.getContent();
			
			if (content instanceof String) {
				System.out.println("its string");
				System.out.println(content.toString());
				mail.setContent(content.toString());
			} else if (content instanceof Multipart) {
				Multipart mp = (Multipart) content;

				BodyPart bp = mp.getBodyPart(0);
				System.out.println("its multipart");
				System.out.println(bp.getContent().toString());

				String textFromMimeMultipart = getTextFromMimeMultipart((MimeMultipart) mp);

				System.out.println("after convert to mime part to text");
				System.out.println(textFromMimeMultipart);

				mail.setContent(textFromMimeMultipart);
			
			}
				
					folderInbox.close();
			

			store.close();
			
			
			
			return mail;
		} catch (NoSuchProviderException ex) {
			System.out.println("No provider.");
			ex.printStackTrace();
			return null;
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store.");
			ex.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	
	}

}
