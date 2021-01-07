package com.hfhk.cb.service.modules.file;

import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cb.file.domain.File;
import com.hfhk.cb.file.domain.request.FileFindParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/service/file")
public class FileServiceApi {
	private final FileService fileService;

	public FileServiceApi(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping("/upload")
	@PreAuthorize("isAuthenticated()")
	public List<File> upload(@AuthenticationPrincipal AuthPrincipal principal,
							 @RequestParam(defaultValue = "/") String folderPath,
							 @RequestPart Collection<MultipartFile> files) {
		return fileService.store(principal.getClient(), Optional.ofNullable(principal.getUser().getUid()).orElse("admin"), folderPath, files);
	}

	@DeleteMapping("/delete")
	@PreAuthorize("isAuthenticated()")
	public void delete(@AuthenticationPrincipal AuthPrincipal principal,
					   @RequestParam String[] fileIds) {
		fileService.delete(principal.getClient(), principal.getUser().getUid(), Arrays.asList(fileIds));
	}

	@PostMapping("/find")
	@PreAuthorize("isAuthenticated()")
	public List<File> find(
		@AuthenticationPrincipal AuthPrincipal principal,
		@RequestBody FileFindParam param) {
		String client = principal.getClient();
		return fileService.find(client, param);
	}

	@GetMapping("/find_page")
	@PreAuthorize("isAuthenticated()")
	public Page<File> pageFind(
		@AuthenticationPrincipal AuthPrincipal principal,
		@RequestBody FileFindParam request) {
		return fileService.pageFind(principal.getClient(), request);
	}
}
