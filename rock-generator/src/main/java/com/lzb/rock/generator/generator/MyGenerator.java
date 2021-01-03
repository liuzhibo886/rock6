package com.lzb.rock.generator.generator;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.lzb.rock.generator.model.GenQo;

/**
 * 代码生成器
 *
 * @author lzbAdministrator
 *
 * @date 2019年12月2日 下午10:35:31
 */
public class MyGenerator {
	/**
	 * 开始代码生成
	 */
	public void start(GenQo genQo) {
		// 输出路径
		String projectPath = genQo.getProjectPath();
		if (!genQo.getProjectPath().endsWith(File.separator)) {
			projectPath = projectPath + File.separator;
		}
		projectPath = projectPath + genQo.getProjectName() + File.separator + "src" + File.separator + "main"
				+ File.separator + "java";

		// 代码生成器
		MyAutoGenerator mpg = new MyAutoGenerator();
		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		// 输出路径
		gc.setOutputDir(projectPath);
		// 作者
		gc.setAuthor(genQo.getAuthor());
		// 不打开输出目录
		gc.setOpen(false);
		// 实体属性 Swagger2 注解
		gc.setSwagger2(true);
		// 覆盖文件
		gc.setFileOverride(true);
		// xml生成
		gc.setBaseColumnList(true);
		gc.setBaseResultMap(true);
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setUrl(genQo.getUrl());
		dsc.setDriverName(genQo.getDriverName());
		dsc.setUsername(genQo.getUserName());
		dsc.setPassword(genQo.getPassword());
		// 数据库字段类型定义
		dsc.setTypeConvert(new MyMySqlTypeConvert());

		mpg.setDataSource(dsc);

		// 包配置
		final MyPackageConfig pc = new MyPackageConfig(genQo);
		// 根包
		pc.setParent(genQo.getPackageParent() + "." + genQo.getProjectName());
		// 服务名称
		pc.setModuleName(genQo.getModuleName());

//		 pc.setEntity("open.entity");
//		 pc.setService("ms.service");
//		 pc.setServiceImpl("ms.service.impl");
//		 pc.setMapper("ms.mapper");
//		 pc.setXml("ms.xml");
//		 pc.setController("ms.controller");
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig icfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};

		mpg.setCfg(icfg);

		// 配置模板
		MyTemplateConfig templateConfig = new MyTemplateConfig();
		mpg.setTemplate(templateConfig);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		// 自定义继承的Service类全称，带包名
		strategy.setSuperServiceClass("com.lzb.rock.base.facade.IService");
		strategy.setSuperServiceImplClass("com.lzb.rock.base.facade.impl.ServiceImpl");

		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		// strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
		/**
		 * 使用Lombok
		 */
		strategy.setEntityLombokModel(true);
		/**
		 * 逻辑删除字段
		 */
		strategy.setLogicDeleteFieldName("is_del");

		/**
		 * 数据库版本控制字段
		 */
		strategy.setVersionFieldName("version");
		// 生成控Controll
		strategy.setRestControllerStyle(true);
		// 生成实体时，生成字段注解
		strategy.setEntityTableFieldAnnotationEnable(true);
		// 公共父类
		// strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
		// 写于父类中的公共字段
		// strategy.setSuperEntityColumns("id");
		// 表名
		if (StringUtils.isNotBlank(genQo.getIncludeTableName())) {
			String[] arr = genQo.getIncludeTableName().split(",");
			strategy.setInclude(arr);
		} else if (StringUtils.isNotBlank(genQo.getExcludeTableName())) {
			String[] arr = genQo.getExcludeTableName().split(",");
			strategy.setExclude(arr);
		} else {
			strategy.setInclude("(.*)");
		}

		strategy.setControllerMappingHyphenStyle(true);

		if (genQo.getTablePrefixs() != null) {
			strategy.setTablePrefix(genQo.getTablePrefixs());
		}

		mpg.setStrategy(strategy);
		MyBeetlTemplateEngine template = new MyBeetlTemplateEngine();

		template.setGenQo(genQo);
		mpg.setTemplateEngine(template);
		mpg.execute();

	}
}
