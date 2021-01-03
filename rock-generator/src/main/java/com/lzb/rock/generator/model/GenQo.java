package com.lzb.rock.generator.model;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

import com.lzb.rock.generator.util.UtilLoadProperties;

import lombok.Data;
import lombok.Setter;

/**
 * 代码生成的查询参数
 * 
 * @author lzb
 *
 *         Mar 16, 2019 8:28:03 PM
 */
@Data
@Setter
public class GenQo {

	private GenQo() {

	}

	public static GenQo create() {
		/**
		 * 读取配置文件初始化对象
		 */
		GenQo genQo = new GenQo();
		Properties genQoProp = UtilLoadProperties.init("generator.properties");
		Map<String, String> genQoMap = UtilLoadProperties.getMapsKeyIsString(genQoProp);
		try {
			BeanUtils.copyProperties(genQo, genQoMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * 初始化分库分表属性
		 */
		Properties prop = UtilLoadProperties.init("subTreasury.properties");
		Map<String, String> subTreasuryMap = UtilLoadProperties.getMapsKeyIsString(prop);
		genQo.setSubTreasuryMap(subTreasuryMap);
		return genQo;
	}

	/**
	 * 数据库账号
	 */
	private String userName;

	/**
	 * 数据库密码
	 */
	private String password;

	/**
	 * 数据库url
	 */
	private String url;
	/**
	 * 驱动
	 */
	private String driverName = "com.mysql.cj.jdbc.Driver";

	/**
	 * 输出路径
	 */
	private String projectPath;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 需要包含的表名，允许正则表达式（与exclude二选一配置），多个逗号分隔
	 */
	private String includeTableName = "(.*)";
	/**
	 * 需要排除的表名，允许正则表达式，多个逗号分隔
	 */
	private String excludeTableName;

	/**
	 * 忽略的表前缀
	 */
	private String tablePrefix;

	public String[] getTablePrefixs() {
		String[] tablePrefixArr = null;
		if (StringUtils.isNotBlank(this.tablePrefix)) {
			tablePrefixArr = this.tablePrefix.split(",");
		}
		return tablePrefixArr;
	}

	/**
	 * 父包名
	 */
	String packageParent;
	/**
	 * 项目名
	 */
	private String projectName;

	/**
	 * 模块名
	 */
	private String moduleName;

	/**
	 * 是否生成控制器代码开关
	 */
	private Boolean controllerSwitch = true;

	/**
	 * 主页
	 */
	private Boolean indexPageSwitch = true;

	/**
	 * 添加页面
	 */
	private Boolean addPageSwitch = true;

	/**
	 * 编辑页面
	 */
	private Boolean editPageSwitch = true;

	/**
	 * 主页的js
	 */
	private Boolean jsSwitch = true;

	/**
	 * 详情页面js
	 */
	private Boolean infoJsSwitch = true;

	/**
	 * dao的开关
	 */
	private Boolean daoSwitch = true;

	/**
	 * service
	 */
	private Boolean serviceSwitch = true;

	/**
	 * 生成实体的开关
	 */
	private Boolean entitySwitch = true;

	/**
	 * 表 分库字段配置表
	 */
	private Map<String, String> subTreasuryMap;

}
