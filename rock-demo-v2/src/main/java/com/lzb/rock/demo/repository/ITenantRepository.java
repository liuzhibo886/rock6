package com.lzb.rock.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lzb.rock.demo.entity.Tenant;

@Repository
public interface ITenantRepository extends JpaRepository<Tenant, Long> {

}
