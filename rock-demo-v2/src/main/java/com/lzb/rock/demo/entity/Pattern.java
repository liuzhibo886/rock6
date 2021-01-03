package com.lzb.rock.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.lzb.rock.sharding.aop.annotation.ShardingBroadcast;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 规则表
 * </p>
 *
 * @author lzb
 * @since 2020-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("demo_pattern")
@ApiModel(value = "Pattern对象", description = "规则表")
@ShardingBroadcast(logicTable = "demo_pattern", idColumn = "pattern_id", actualDataNodes = "ds_${[0,1,2]}.demo_pattern")
public class Pattern implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	@TableId(value = "pattern_id", type = IdType.AUTO)
	private Long patternId;

	@ApiModelProperty(value = "规则文本")
	@TableField("pattern_text")
	private String patternText;

	@ApiModelProperty(value = "请求方法,GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE")
	@TableField("method")
	private String method;

	@ApiModelProperty(value = "规则类型,ANONS白名单规则,AUTHC登录鉴权规则，GRANT，需授权规则")
	@TableField("pattern_type")
	private String patternType;

	@ApiModelProperty(value = "创建时间")
	@TableField("create_time")
	private Date createTime;

	@ApiModelProperty(value = "最后修改时间")
	@TableField("last_time")
	private Date lastTime;

	@ApiModelProperty(value = "最后修改人")
	@TableField("last_user")
	private String lastUser;

	@ApiModelProperty(value = "是否删除0正常 1 删除")
	@TableField("is_del")
	@TableLogic
	private Integer isDel;

}
