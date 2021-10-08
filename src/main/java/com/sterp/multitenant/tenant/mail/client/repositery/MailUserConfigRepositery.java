package com.sterp.multitenant.tenant.mail.client.repositery;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sterp.multitenant.tenant.mail.client.dto.UserDetailsConfigurationDto;

@Repository
public interface MailUserConfigRepositery extends JpaRepository<UserDetailsConfigurationDto, Long>{

	Optional<UserDetailsConfigurationDto> findByUserId(Long id);

}
