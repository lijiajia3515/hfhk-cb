package com.hfhk.cb.service.modules.file;

import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cb.file.domain.Folder;
import com.hfhk.cb.file.domain.request.FolderFindParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folder")
public class FolderApi {
	private final FolderService folderService;

	public FolderApi(FolderService folderService) {
		this.folderService = folderService;
	}

	@PostMapping
	@PreAuthorize("isAuthenticated()")
	public void save(@AuthenticationPrincipal AuthPrincipal principal, String path) {
		folderService.create(principal.getClient(), path);
	}

	@PutMapping
	@PreAuthorize("isAuthenticated()")
	public void put(@AuthenticationPrincipal AuthPrincipal principal, String path, String newPath) {
		folderService.rename(principal.getClient(), path, newPath);
	}

	@DeleteMapping
	@PreAuthorize("isAuthenticated()")
	public void delete(@AuthenticationPrincipal AuthPrincipal principal,
					   String path) {
		folderService.delete(principal.getClient(), path);
	}

	@GetMapping("/find")
	@PreAuthorize("isAuthenticated()")
	public Page<String> find(
		@AuthenticationPrincipal AuthPrincipal principal,
		@RequestBody FolderFindParam request) {
		return folderService.pageFind(principal.getClient(), request);
	}

	@GetMapping("/find_tree")
	@PreAuthorize("isAuthenticated()")
	public List<Folder> treeFind(@AuthenticationPrincipal AuthPrincipal token, String path) {
		return folderService.treeFind(token.getClient(), path);
	}
}
