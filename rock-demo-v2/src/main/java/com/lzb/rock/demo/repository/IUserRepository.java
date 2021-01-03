package com.lzb.rock.demo.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.lzb.rock.demo.entity.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	public User findByUserAccount(String userAccount);
	
	@Modifying
	@Transactional
	@Query(value = "update demo_user set token =:token  where user_id = :userId and tenant_id= :tenantId",nativeQuery = true)
	public Integer updateByUserIdAndTenantId(String token,Long userId,Long tenantId);
}
