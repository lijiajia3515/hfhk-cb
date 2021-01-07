package com.hfhk.cb.service.config;

import com.hfhk.cairo.core.security.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SecurityConstant {

	@Bean("ROLE_ADMIN")
	public String roleAdmin() {
		return "ADMIN";
	}

	@Component("ROLE")
	public class RoleRole {
		// role
		public String ROLE = "ROLE";
		public String READ = "READ";
		private final String WRITE = "WRITE";
	}

	@Component
	public class SystemSecurityRole extends Role {

	}
}
