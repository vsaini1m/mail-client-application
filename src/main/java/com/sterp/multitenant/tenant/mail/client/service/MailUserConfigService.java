package com.sterp.multitenant.tenant.mail.client.service;

import com.sterp.multitenant.tenant.mail.client.dto.UserDetailsConfigurationDto;

public interface MailUserConfigService {

	UserDetailsConfigurationDto addOrUpdateUserConfig(UserDetailsConfigurationDto dto);

	UserDetailsConfigurationDto getUserMailDetailsConfigurationByCurrentUser();

	boolean deleteUserMailDetailsConfigurationByCurrentUser();

}
