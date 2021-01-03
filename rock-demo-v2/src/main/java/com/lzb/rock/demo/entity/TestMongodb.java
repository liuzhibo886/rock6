package com.lzb.rock.demo.entity;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * demo测试
 * </p>
 *
 * @author lzb
 * @since 2020-12-09
 */
@Data
public class TestMongodb {

    @ApiModelProperty(value = "测试ID")
    @Id
    private ObjectId testId;

    @ApiModelProperty(value = "测试名称")
    private String testName;

    @ApiModelProperty(value = "部门ID")
    @Indexed
    private Long deptId;

    @ApiModelProperty(value = "租户ID")
    private Long tenantId;

    @ApiModelProperty(value = "创建时间")
    @CreatedDate
    private Date createTime;

    @ApiModelProperty(value = "最后修改时间")
    @LastModifiedDate
    private Date lastTime;

    @ApiModelProperty(value = "是否删除0正常 1 删除")
    private Integer isDel;

}
