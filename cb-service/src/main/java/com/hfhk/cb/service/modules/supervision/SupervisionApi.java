package com.hfhk.cb.service.modules.supervision;

import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.security.oauth2.user.AuthPrincipal;
import com.hfhk.cb.supervision.Supervision;
import com.hfhk.cb.supervision.SupervisionDeleteParam;
import com.hfhk.cb.supervision.SupervisionFindParam;
import com.hfhk.cb.supervision.SupervisionSaveParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Supervision")
public class SupervisionApi {
	private final SupervisionService supervisionService;

	public SupervisionApi(SupervisionService supervisionService) {
		this.supervisionService = supervisionService;
	}

	@PostMapping("/Save")
	public Supervision save(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody SupervisionSaveParam param) {
		return this.supervisionService.save(param);
	}

	@DeleteMapping("/Delete")
	public List<Supervision> delete(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody SupervisionDeleteParam param) {
		return this.supervisionService.delete(param);
	}

	@PostMapping("/Find")
	public List<Supervision> auth(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody SupervisionFindParam param) {
		return this.supervisionService.find(param);
	}

	@PostMapping("/FindPage")
	public Page<Supervision> findPage(@AuthenticationPrincipal AuthPrincipal principal, @RequestBody SupervisionFindParam param) {
		return this.supervisionService.findPage(param);
	}
}
