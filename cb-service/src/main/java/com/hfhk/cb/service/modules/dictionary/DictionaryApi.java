package com.hfhk.cb.service.modules.dictionary;

import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cb.dictionary.domain.Dictionary;
import com.hfhk.cb.service.modules.dictionary.domain.request.*;
import com.hfhk.system.service.modules.dictionary.domain.request.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * dictionary api
 */
@Slf4j(topic = "[dictionary][api]")
@RestController
@RequestMapping("/Dictionary")
public class DictionaryApi {
	private final DictionaryService dictionaryService;

	public DictionaryApi(DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	@PostMapping("/Find")
	@PreAuthorize("isAuthenticated()")
	public List<Dictionary> list(@AuthenticationPrincipal AuthPrincipal principal,
								 @RequestBody(required = false) DictionaryFindParams request) {
		String client = principal.getClient();
		return dictionaryService.find(client, request);
	}

	/**
	 * dictionary save
	 *
	 * @param principal principal
	 * @param request   request
	 * @return dictionary optional
	 */
	@PostMapping("/Save")
	@PreAuthorize("isAuthenticated()")
	public Optional<Dictionary> save(
		@AuthenticationPrincipal AuthPrincipal principal,
		@RequestBody DictionarySaveParams request) {
		String client = principal.getClient();
		return dictionaryService.save(client, request);
	}

	/**
	 * dictionary modify
	 *
	 * @param principal principal
	 * @param request   request
	 * @return dictionary optional
	 */
	@PutMapping("/Modify")
	@PreAuthorize("isAuthenticated()")
	public Optional<Dictionary> modify(
		@AuthenticationPrincipal AuthPrincipal principal,
		@RequestBody DictionarySaveParams request) {
		String client = principal.getClient();
		return dictionaryService.modify(client, request);
	}

	/**
	 * dictionary delete
	 *
	 * @param principal principal
	 * @param params    params
	 * @return dictionary
	 */
	@DeleteMapping("/Delete")
	@PreAuthorize("isAuthenticated()")
	public List<Dictionary> delete(
		@AuthenticationPrincipal AuthPrincipal principal,
		@RequestBody DictionaryDeleteParams params) {
		String client = principal.getClient();
		return dictionaryService.delete(client, params);
	}

	/**
	 * dictionary item put
	 *
	 * @param principal principal
	 * @param request   request
	 * @return dictionary optional
	 */
	@PutMapping("/Item/Put")
	@PreAuthorize("isAuthenticated()")
	public Optional<Dictionary> putItems(@AuthenticationPrincipal AuthPrincipal principal,
										 @RequestBody DictionaryItemPutParams request) {
		String client = principal.getClient();
		return dictionaryService.putItems(client, request);
	}

	/**
	 * dictionary item modify
	 *
	 * @param principal principal
	 * @param request   request
	 * @return dictionary optional
	 */
	@PutMapping("/Item/Modify")
	@PreAuthorize("isAuthenticated()")
	public Optional<Dictionary> modifyItems(@AuthenticationPrincipal AuthPrincipal principal,
											@RequestBody DictionaryItemModifyParams request) {
		String client = principal.getClient();
		return dictionaryService.modifyItems(client, request);
	}

	/**
	 * dictionary item delete
	 *
	 * @param principal principal
	 * @param request   request
	 * @return dictionary optional
	 */
	@DeleteMapping("/Item/Delete")
	@PreAuthorize("isAuthenticated()")
	public Optional<Dictionary> deleteItems(@AuthenticationPrincipal AuthPrincipal principal,
											@RequestBody DictionaryItemDeleteParams request) {
		String client = principal.getClient();
		return dictionaryService.deleteItems(client, request);
	}
}
