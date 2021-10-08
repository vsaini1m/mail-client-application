package com.sterp.multitenant.tenant.mail.client.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sterp.multitenant.tenant.core.services.AppService;
import com.sterp.multitenant.tenant.mail.client.dto.UserDetailsConfigurationDto;
import com.sterp.multitenant.tenant.mail.client.repositery.MailUserConfigRepositery;
import com.sterp.multitenant.tenant.mail.client.service.MailUserConfigService;

@Service
public class MailUserConfigServiceImpl implements MailUserConfigService{

	
	
	
	
	@Autowired
	AppService appService;
	
	@Autowired
	MailUserConfigRepositery mailConfigRepositery;
	
	
	@Override
	public UserDetailsConfigurationDto addOrUpdateUserConfig(UserDetailsConfigurationDto dto) {
		
		///user id
		dto.setUserId(this.appService.getCurrentUser().getId());
		
		
		
		return this.mailConfigRepositery.save(dto);
	}
	
	
	@Override
	public UserDetailsConfigurationDto getUserMailDetailsConfigurationByCurrentUser() {
		
		
	
		Optional<UserDetailsConfigurationDto> findedMailConfig=this
				.mailConfigRepositery.findByUserId(
						this.appService.getCurrentUser()
							.getId()
							);
		
		
		return findedMailConfig.isPresent()?
				findedMailConfig.get()
					:null;
		
	}
	
	@Override
	public boolean deleteUserMailDetailsConfigurationByCurrentUser() {
		
		
		
		Optional<UserDetailsConfigurationDto> findedMailConfig=this
				.mailConfigRepositery.findByUserId(
						this.appService.getCurrentUser()
							.getId()
							);
		
		
		if( !findedMailConfig.isPresent()) 
			return
					false;
				
		else {
			try {
				mailConfigRepositery.delete(findedMailConfig.get());
			} catch (Exception e) {
				return false;
			}
			return true;
		}
			 
					
		
		
	}
	
	

}
