package com.hfhk.cb.service.modules.project.authority.audit;

import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cb.project.authority.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Project/Authority/Audit")
public class ProjectAuthorityAuditApi {
	private final ProjectAuthorityAuditService projectAuthorityAuditService;

	public ProjectAuthorityAuditApi(ProjectAuthorityAuditService projectAuthorityAuditService) {
		this.projectAuthorityAuditService = projectAuthorityAuditService;
	}

	@PostMapping("/Apply")
	@PreAuthorize("isAuthenticated()")
	public ProjectAuthorityAudit apply(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditApplyParam param) {
		String uid = principal.getUser().getUid();
		return this.projectAuthorityAuditService.apply(uid, param);
	}

	@PatchMapping("/Pass")
	@PreAuthorize("isAuthenticated()")
	public List<ProjectAuthorityAudit> pass(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditPassParam param) {
		String uid = principal.getUser().getUid();
		return this.projectAuthorityAuditService.pass(uid, param);
	}

	@PatchMapping("/Reject")
	@PreAuthorize("isAuthenticated()")
	public List<ProjectAuthorityAudit> pass(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditRejectParam param) {
		String uid = principal.getUser().getUid();
		return this.projectAuthorityAuditService.reject(uid, param);
	}

	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public List<ProjectAuthorityAudit> find(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditFindParam param) {
		return this.projectAuthorityAuditService.find(param);
	}

	@PostMapping("/FindPage")
	@PreAuthorize("isAuthenticated()")
	public Page<ProjectAuthorityAudit> findPage(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditFindParam param) {
		return this.projectAuthorityAuditService.findPage(param);
	}
}
