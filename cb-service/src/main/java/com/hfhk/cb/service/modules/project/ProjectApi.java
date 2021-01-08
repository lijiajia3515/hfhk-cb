package com.hfhk.cb.service.modules.project;

import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cb.project.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Project")
public class ProjectApi {
	private final ProjectService projectService;

	public ProjectApi(ProjectService projectService) {
		this.projectService = projectService;
	}

	@PostMapping("/Save")
	@PreAuthorize("isAuthenticated()")
	public Project save(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectSaveParam param) {
		String client = principal.getClient();
		return projectService.save(param);
	}

	@PutMapping("/Modify")
	@PreAuthorize("isAuthenticated()")
	public Project modify(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectModifyParam param) {
		String client = principal.getClient();
		return projectService.modify(param);
	}

	@DeleteMapping("/Delete")
	@PreAuthorize("isAuthenticated()")
	public List<Project> delete(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectDeleteParam param) {
		String client = principal.getClient();
		return projectService.delete(param);
	}

	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public List<Project> find(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectFindParam param) {
		String client = principal.getClient();
		return projectService.find(param);
	}

	@PostMapping("/FindPage")
	@PreAuthorize("isAuthenticated()")
	public Page<Project> findPage(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody ProjectFindParam param) {
		String client = principal.getClient();
		return projectService.findPage(param);
	}
}
