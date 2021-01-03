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

import com.lzb.rock.demo.config.UserDatabasePreciseShardingAlgorithm;
import com.lzb.rock.demo.config.UserTablePreciseShardingAlgorithm;
import com.lzb.rock.sharding.aop.annotation.ShardingDatabaseRule;
import com.lzb.rock.sharding.aop.annotation.ShardingRule;
import com.lzb.rock.sharding.aop.annotation.ShardingTableRule;

import lombok.Data;

/**
 * <p>
 * demo user测试
 * </p>
 *
 * @author lzb
 * @since 2020-12-09
 */
@Data
@Table(name = "demo_user")
@Entity
@ShardingRule(logicTable = "demo_user", idColumn = "user_id", actualDataNodes = "ds_${[0,1,2]}.demo_user_${[0,1]}")
//@ShardingDatabaseRule(ruleColumn = "tenant_id", algorithmExpression = "ds_${tenant_id % 3}")
//@ShardingTableRule(ruleColumn = "dept_id", algorithmExpression = "ds_${dept_id % 2}", preciseShardingAlgorithm = UserTablePreciseShardingAlgorithm.class)

@ShardingDatabaseRule(ruleColumn = "tenant_id", preciseShardingAlgorithm = UserDatabasePreciseShardingAlgorithm.class)
@ShardingTableRule(ruleColumn = "dept_id", preciseShardingAlgorithm = UserTablePreciseShardingAlgorithm.class)
@DynamicInsert
@DynamicUpdate
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "user_id", length = 17)
	private Long userId;

	/**
	 * 测试名称
	 */
	@Column(name = "user_name", length = 30)
	private String userName;

	/**
	 * 账号
	 */
	@Column(name = "user_account", length = 256)
	private String userAccount;

	/**
	 * 密码
	 */
	@Column(name = "userPassword", length = 256)
	private String userPassword;

	/**
	 * 用户md5密码盐
	 */
	@Column(name = "user_salt", length = 256)
	private String userSalt;

	/**
	 * 部门ID
	 */
	@Column(name = "dept_id", length = 17)
	private Long deptId;

	@Column(name = "token", length = 256)
	private String token;

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
