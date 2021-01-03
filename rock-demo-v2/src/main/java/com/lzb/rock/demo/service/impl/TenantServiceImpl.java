package com.lzb.rock.demo.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.lzb.rock.demo.entity.Tenant;
import com.lzb.rock.demo.repository.ITenantRepository;
import com.lzb.rock.demo.service.ITenantService;

@Service
public class TenantServiceImpl implements ITenantService {

	@Autowired
	ITenantRepository tenantRepository;

	@Override
	public Tenant findTenantByTenantId(Long tenantId) {

		Tenant tenant = new Tenant();
		tenant.setTenantId(tenantId);
		Example<Tenant> example = Example.of(tenant);
		Optional<Tenant> optional = tenantRepository.findOne(example);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}

	@Override
	public Tenant save(Tenant tenant) {
		return tenantRepository.save(tenant);
	}

	@Override
	public void deleteByTenantId(Long tenantId) {
		Tenant tenant = new Tenant();
		tenant.setTenantId(tenantId);
		tenantRepository.delete(tenant);
	}

}
