package com.lzb.rock.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lzb.rock.base.model.Result;
import com.lzb.rock.demo.entity.Tenant;
import com.lzb.rock.demo.service.ITenantService;

@RestController
@RequestMapping(value = "/tenant")
public class TenantController {

	@Autowired
	private ITenantService tenantService;

	@GetMapping
	public Result<Tenant> findTenantByTenantId(@RequestParam Long tenantId) {
		Tenant tenant = tenantService.findTenantByTenantId( tenantId);
		return new Result<Tenant>(tenant);

	}

	@PostMapping
	public Result<Tenant> save(@RequestBody Tenant tenant) {
		tenantService.save(tenant);
		return new Result<Tenant>(tenant);
	}

	@DeleteMapping
	public Result<Void> deleteByTenantId(@RequestParam Long tenantId) {
		tenantService.deleteByTenantId(tenantId);
		return new Result<Void>();
	}

}
