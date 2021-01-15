package com.hfhk.cb.service.modules.instruct;

import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cb.instruct.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Instruct")
public class InstructApi {
	private final InstructService instructService;

	public InstructApi(InstructService instructService) {
		this.instructService = instructService;
	}

	@PostMapping("/Apply")
	@PreAuthorize("isAuthenticated()")
	public Instruct apply(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody InstructApplyParam param) {
		return instructService.apply(param);
	}

	@PatchMapping("/Pass")
	@PreAuthorize("isAuthenticated()")
	public List<Instruct> pass(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody InstructPassParam param) {
		String uid = principal.getUser().getUid();
		return instructService.pass(uid, param);
	}

	@PatchMapping("/Reject")
	@PreAuthorize("isAuthenticated()")
	public List<Instruct> reject(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody InstructRejectParam param) {
		String uid = principal.getUser().getUid();
		return instructService.reject(uid, param);
	}

	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public List<Instruct> find(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody InstructFindParam param) {
		return instructService.find(param);
	}

	@PostMapping("/FindPage")
	@PreAuthorize("isAuthenticated()")
	public Page<Instruct> findPage(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody InstructFindParam param) {
		return instructService.findPage(param);
	}
}
