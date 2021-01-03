package com.lzb.rock.demo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.lzb.rock.sharding.aop.annotation.ShardingDatabaseRule;
import com.lzb.rock.sharding.aop.annotation.ShardingRule;
import com.lzb.rock.sharding.aop.annotation.ShardingTableRule;

import lombok.Data;

/**
 * <p>
 * demo 部门表测试
 * </p>
 *
 * @author lzb
 * @since 2020-12-09
 */
@Data
@Table(name = "demo_dept")
@Entity
@ShardingRule(logicTable = "demo_dept", idColumn = "dept_id", actualDataNodes = "ds_${[0,1,2]}.demo_dept")
@ShardingDatabaseRule(ruleColumn = "tenant_id", algorithmExpression = "ds_${tenant_id % 3}")
@DynamicInsert
@DynamicUpdate
public class Dept implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "dept_id", length = 17)
	private Long deptId;

	/**
	 * 测试名称,加密
	 */
	@Column(name = "dept_name", length = 256)
	private String deptName;
	/**
	 * 租户ID
	 */
	@Column(name = "tenant_id", length = 17)
	private Long tenantId;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "last_time")
	private Date lastTime;

	@Column(name = "last_user")
	private String lastUser;

	@Column(name = "is_del", length = 2)
	private Integer isDel;

}
