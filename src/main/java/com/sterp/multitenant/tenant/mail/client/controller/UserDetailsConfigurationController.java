package com.sterp.multitenant.tenant.mail.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sterp.multitenant.tenant.mail.client.dto.UserDetailsConfigurationDto;
import com.sterp.multitenant.tenant.mail.client.service.MailUserConfigService;

@RestController
@RequestMapping("/mail/config")
public class UserDetailsConfigurationController {

	
	@Autowired
	MailUserConfigService service;
	
	
	@PostMapping("/add")
	@PutMapping("/update")
	public ResponseEntity<?> addOrUpdateUserConfig(@RequestBody UserDetailsConfigurationDto dto){
		
		UserDetailsConfigurationDto savedDto=this.service.addOrUpdateUserConfig(dto);
		
		
		
		
		return
				savedDto!=null?
					ResponseEntity.ok(savedDto)
						:ResponseEntity.badRequest().body("Something went wrong.");
	}
	
	
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUserMailDetailsConfiguration(){
		
		
		
		return
				this.service.deleteUserMailDetailsConfigurationByCurrentUser()?
						ResponseEntity.ok("Deleted successfully")
							:ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/get")
	public ResponseEntity<?> getUserMailConfigByCurrentUser() {
		
		UserDetailsConfigurationDto userMailDetailsConfigurationByCurrentUser = this.service.getUserMailDetailsConfigurationByCurrentUser();
	
		return
				userMailDetailsConfigurationByCurrentUser!=null
					
				   ?ResponseEntity.ok(userMailDetailsConfigurationByCurrentUser)
					  	:ResponseEntity.badRequest().body("Mail Configuration not found.");

	}
	
	
	
	
}
