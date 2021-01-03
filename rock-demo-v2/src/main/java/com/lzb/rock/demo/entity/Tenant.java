package com.lzb.rock.demo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lzb.rock.sharding.aop.annotation.ShardingBroadcast;

import lombok.Data;

/**
 * <p>
 * demo 测试 租户表，广播表
 * </p>
 *
 * @author lzb
 * @since 2020-12-09
 */
@Data
@Table(name = "demo_tenant")
@Entity
@ShardingBroadcast(logicTable = "demo_tenant",idColumn="tenant_id",actualDataNodes = "ds_${[0,1,2]}.demo_tenant")
public class Tenant implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "tenant_id", length = 17)
	private Long tenantId;

	/**
	 * 租户名称
	 */
	@Column(name = "tenant_name", length = 256)
	private String tenantName;

	
	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "last_time")
	private Date lastTime;

	@Column(name = "last_user")
	private String lastUser;

	@Column(name = "is_del", length = 2)
	private Integer isDel;

}
