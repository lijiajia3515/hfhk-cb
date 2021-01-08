package com.hfhk.cb.service.modules.project;

import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cb.review.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Project/Review")
public class ProjectReviewApi {
	private final ProjectReviewService projectReviewService;

	public ProjectReviewApi(ProjectReviewService projectReviewService) {
		this.projectReviewService = projectReviewService;
	}

	@PostMapping("/Apply")
	@PreAuthorize("isAuthenticated()")
	public ProjectReview apply(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectReviewApplyParam param) {
		String uid = principal.getUser().getUid();
		return this.projectReviewService.apply(uid, param);
	}

	@PatchMapping("/Pass")
	@PreAuthorize("isAuthenticated()")
	public ProjectReview pass(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectReviewPassParam param) {
		String uid = principal.getUser().getUid();
		return this.projectReviewService.pass(uid, param);
	}

	@PatchMapping("/Reject")
	@PreAuthorize("isAuthenticated()")
	public ProjectReview pass(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectReviewRejectParam param) {
		String uid = principal.getUser().getUid();
		return this.projectReviewService.reject(uid, param);
	}

	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public List<ProjectReview> find(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectReviewFindParam param) {
		return this.projectReviewService.find(param);
	}

	@PostMapping("/FindPage")
	@PreAuthorize("isAuthenticated()")
	public Page<ProjectReview> findPage(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectReviewFindParam param) {
		return this.projectReviewService.findPage(param);
	}
}
