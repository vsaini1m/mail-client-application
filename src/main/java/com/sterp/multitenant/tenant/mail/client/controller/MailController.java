package com.sterp.multitenant.tenant.mail.client.controller;





import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sterp.multitenant.tenant.mail.client.dto.EmailReciverFolders;
import com.sterp.multitenant.tenant.mail.client.dto.Mail;
import com.sterp.multitenant.tenant.mail.client.dto.ReplyToDto;
import com.sterp.multitenant.tenant.mail.client.service.MailService;
import com.sterp.multitenant.tenant.settings.smtp.entity.MailBody;
import com.sterp.multitenant.tenant.settings.smtp.service.EmailService;



@RestController
@RequestMapping("/mail")
public class MailController {

	@Autowired
	MailService mailService;
//
	//@PostMapping("/send")
	//public ResponseEntity<?> sendMail(@RequestBody MailBody mail) {

//		return mailService.sendEmail(mail) 
//				? ResponseEntity.ok().build() 
//						: ResponseEntity.badRequest().build();

		
		
		
	//}
	
	
	
	
	
	
	
	@Autowired
	EmailService emailService;

	@RequestMapping(value = "/send", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
					MediaType.APPLICATION_JSON_VALUE }, method = { RequestMethod.POST, RequestMethod.PUT })
	public ResponseEntity<?> sendMail(HttpServletRequest request, @RequestPart MailBody object, @RequestPart(required = false) MultipartFile[] multipartfile)
			throws MessagingException, IOException {
		return ResponseEntity.ok(this.emailService.composeMail(request,object, multipartfile));
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@GetMapping("/folders")
	private ResponseEntity<?> getAllFolders() {

		List<EmailReciverFolders> allImapEmailFoldersWithEmailCount = mailService.allImapEmailFoldersWithEmailCount();
		
		return allImapEmailFoldersWithEmailCount!=null
				? ResponseEntity.ok(allImapEmailFoldersWithEmailCount)  
						:ResponseEntity.badRequest().build();
		
	}
	
	
	@PostMapping("/recive")
	private ResponseEntity<?> reciveAllMAils(@RequestBody ReplyToDto dto) {

		 List<Mail> reciverIMAPEmail = mailService.reciverIMAPEmail(dto);
		
		return reciverIMAPEmail!=null
				? ResponseEntity.ok(reciverIMAPEmail)
						:ResponseEntity.badRequest().build();
		
	}
	
	
	
	
	@PostMapping("/forwardto")
	private ResponseEntity<?> forwardTo(@RequestBody ReplyToDto dto){
		
		
		
		return mailService.forwardTo(dto) 
				? ResponseEntity.ok().build()
						:ResponseEntity.badRequest().build();
	}
	
	@PostMapping("/replyto")
	private ResponseEntity<?> replyTo(@RequestBody ReplyToDto dto) {

		
		
		return mailService.replyTo(dto)
				? ResponseEntity.ok("Mail Replyed successfully .")
						:ResponseEntity.badRequest().build();
		
	}
	
	@PostMapping("/deleteto")
	private ResponseEntity<?> deleteMailByMailNumber(@RequestBody ReplyToDto dto) {

		
		
		return mailService.deleteTo(dto)
				? ResponseEntity.ok("Mail deleted successfully")
						:ResponseEntity.badRequest().build();
		
	}
	
	@PostMapping("/seento")
	private ResponseEntity<?> seenMailByMailNumber(@RequestBody ReplyToDto dto) {

		
		
		return mailService.seenTo(dto)
				? ResponseEntity.ok("Mail deleted successfully")
						:ResponseEntity.badRequest().build();
		
	}
	
	
	@GetMapping("/get")
	private ResponseEntity<?> getMailByMailId(@RequestBody ReplyToDto dto) {
		
		Mail reciveMailById = mailService.reciveMailById(dto);
		
		return reciveMailById!=null ?
				ResponseEntity.ok(reciveMailById)
					:ResponseEntity.badRequest().build();
		

	}
	
}
