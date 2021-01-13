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
	private final ProjectAuthorityAuditService projectReviewService;

	public ProjectAuthorityAuditApi(ProjectAuthorityAuditService projectReviewService) {
		this.projectReviewService = projectReviewService;
	}

	@PostMapping("/Apply")
	@PreAuthorize("isAuthenticated()")
	public ProjectAuthorityAudit apply(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditApplyParam param) {
		String uid = principal.getUser().getUid();
		return this.projectReviewService.apply(uid, param);
	}

	@PatchMapping("/Pass")
	@PreAuthorize("isAuthenticated()")
	public ProjectAuthorityAudit pass(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditPassParam param) {
		String uid = principal.getUser().getUid();
		return this.projectReviewService.pass(uid, param);
	}

	@PatchMapping("/Reject")
	@PreAuthorize("isAuthenticated()")
	public ProjectAuthorityAudit pass(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditRejectParam param) {
		String uid = principal.getUser().getUid();
		return this.projectReviewService.reject(uid, param);
	}

	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public List<ProjectAuthorityAudit> find(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditFindParam param) {
		return this.projectReviewService.find(param);
	}

	@PostMapping("/FindPage")
	@PreAuthorize("isAuthenticated()")
	public Page<ProjectAuthorityAudit> findPage(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectAuthorityAuditFindParam param) {
		return this.projectReviewService.findPage(param);
	}
}
