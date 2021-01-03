package com.lzb.rock.generator.generator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.FileType;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.lzb.rock.generator.model.GenQo;
import com.lzb.rock.generator.util.ToolUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 重写模板类
 *
 * @author Administrator
 *
 * @date 2019年12月3日 下午12:49:16
 */
@Slf4j
public class MyBeetlTemplateEngine extends AbstractTemplateEngine {

	private GenQo genQo;

	private GroupTemplate groupTemplate;

	@Override
	public AbstractTemplateEngine init(ConfigBuilder configBuilder) {

		super.init(configBuilder);
		try {
			Configuration cfg = Configuration.defaultConfiguration();
			// 设置禁用TAG标签
			cfg.setHtmlTagSupport(false);
			groupTemplate = new GroupTemplate(new ClasspathResourceLoader("/"), cfg);
			groupTemplate.registerFunctionPackage("tool", new ToolUtil());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return this;
	}

	@Override
	public AbstractTemplateEngine batchOutput() {
		// 生成文件完整跟路径
		String rootPath = genQo.getProjectPath() + File.separator + genQo.getProjectName() + File.separator + "src"
				+ File.separator + "main" + File.separator + "java";
		// facade 包路径
		String controllerfacadePackage = genQo.getPackageParent() + "." + genQo.getProjectName() + "."
				+ genQo.getModuleName() + ".open.facade";
		// client 包路径
		String controllerClientPackage = genQo.getPackageParent() + "." + genQo.getProjectName() + "."
				+ genQo.getModuleName() + ".open.client";
		// admin 路径
		String controllerAdminPackage = genQo.getPackageParent() + "." + genQo.getProjectName() + "."
				+ genQo.getModuleName() + ".admin.controller";
		// listReq包路径
		String entityListReqPackageRoot = genQo.getPackageParent() + "." + genQo.getProjectName() + "."
				+ genQo.getModuleName() + ".open.dto";

		// html根路径
		String htmlRootPath = genQo.getProjectPath() + File.separator + genQo.getProjectName() + File.separator + "src"
				+ File.separator + "main" + File.separator + "webapp" + File.separator + "WEB-INF" + File.separator
				+ "view" + File.separator + genQo.getProjectName() + File.separator + genQo.getModuleName();

		try {
			List<TableInfo> tableInfoList = getConfigBuilder().getTableInfoList();

			for (TableInfo tableInfo : tableInfoList) {
				tableInfo.getImportPackages().add("com.baomidou.mybatisplus.annotation.TableName");

				// 首字母大写的名字
				String bizEnBigName = ToolUtil.firstCharToUpperCase(ToolUtil.toCamelCase(tableInfo.getEntityName()));
				// 首字母小大写的名字
				String bizEnName = ToolUtil.firstCharToLowerCase(ToolUtil.toCamelCase(tableInfo.getEntityName()));
				// servicer 首字母小写的名字
				String bizEnNameService = ToolUtil.firstCharToLowerCase(ToolUtil.toCamelCase(tableInfo.getEntityName()))
						+ "Service";
				// 首字母小大写的名字
				String msControllerName = tableInfo.getEntityName() + "Ms" + ConstVal.CONTROLLER;

				String entityListReqPackage = entityListReqPackageRoot + "." + bizEnName;
				TableField keyTableField = null; // 主键字段信息
				TableField subTableField = null; // 分库字段信息，当主键跟分库字段一致，分库字段为空，全局表没有分库字段
				Integer keyCount = 0;
				StringBuffer sb = new StringBuffer();
				// 主键字段
				for (TableField field : tableInfo.getFields()) {
					if (field.isKeyFlag()) {
						keyCount++;
						sb.append(field.getName()).append(",");
						keyTableField = field;
					}
				}
				if (keyCount > 1) {
					throw new RuntimeException("有多个主键：" + tableInfo.getName() + "keys=" + sb.toString());
				}
				// 分库分表字段
				String subTreasury = null;
				Integer subCount = 0;
				StringBuffer subSb = new StringBuffer();
				if (genQo.getSubTreasuryMap() != null) {
					subTreasury = genQo.getSubTreasuryMap().get(tableInfo.getName());
				}
				if (StringUtils.isNoneBlank(subTreasury)) {
					for (TableField field : tableInfo.getFields()) {
						// 判断是否为主键
						if (field.getName().equals(subTreasury) && !keyTableField.getName().equals(subTreasury)) {
							subTableField = field;
							subSb.append(field.getName()).append(",");
							subCount++;
						}
						// 判断分库字段
					}
					if (subCount > 1) {
						throw new RuntimeException("有多个分库字段：" + tableInfo.getName() + "keys=" + subSb.toString());
					}
				}

				Map<String, Object> objectMap = getObjectMap(tableInfo);
				objectMap.put("keyTableField", keyTableField);
				objectMap.put("subTableField", subTableField);
				objectMap.put("controllerfacadePackage", controllerfacadePackage);
				objectMap.put("entityListReqPackage", entityListReqPackage);
				objectMap.put("controllerClientPackage", controllerClientPackage);
				objectMap.put("controllerAdminPackage", controllerAdminPackage);
				objectMap.put("bizEnNameService", bizEnNameService);

				// 表注释
				objectMap.put("comment", tableInfo.getComment());
				// 首字母大写
				objectMap.put("bizEnBigName", bizEnBigName);
				objectMap.put("bizProjectName", genQo.getProjectName());
				objectMap.put("moduleName", genQo.getModuleName());
				// 首字母小写
				objectMap.put("bizEnName", bizEnName);
				objectMap.put("msControllerName", msControllerName);
				Map<String, String> pathInfo = getConfigBuilder().getPathInfo();
				MyTemplateConfig template = (MyTemplateConfig) getConfigBuilder().getTemplate();
				// 自定义内容
				InjectionConfig injectionConfig = getConfigBuilder().getInjectionConfig();
				if (null != injectionConfig) {
					injectionConfig.initMap();
					objectMap.put("cfg", injectionConfig.getMap());
					List<FileOutConfig> focList = injectionConfig.getFileOutConfigList();
					if (CollectionUtils.isNotEmpty(focList)) {
						for (FileOutConfig foc : focList) {
							if (isCreate(FileType.OTHER, foc.outputFile(tableInfo))) {
								writer(objectMap, foc.getTemplatePath(), foc.outputFile(tableInfo));
							}
						}
					}
				}
				// Mp.java
				String entityName = tableInfo.getEntityName();
				if (null != entityName && null != pathInfo.get(ConstVal.ENTITY_PATH)) {
					String entityFile = String.format(
							(pathInfo.get(ConstVal.ENTITY_PATH) + File.separator + "%s" + suffixJavaOrKt()),
							entityName);
					if (isCreate(FileType.ENTITY, entityFile)) {
						writer(objectMap,
								templateFilePath(template.getEntity(getConfigBuilder().getGlobalConfig().isKotlin())),
								entityFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成entity成功！");
					// listReq
					String entityListReqPath = joinPath(rootPath, entityListReqPackage);

					String entityListReqFile = String.format(
							(entityListReqPath + File.separator + "%s" + suffixJavaOrKt()), entityName + "ListReq");
					if (isCreate(FileType.ENTITY, entityListReqFile)) {
						writer(objectMap, templateFilePath(template.getEntityListReq()), entityListReqFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成entityListReq成功！");
				}
				// MpMapper.java
				if (null != tableInfo.getMapperName() && null != pathInfo.get(ConstVal.MAPPER_PATH)) {
					String mapperFile = String.format((pathInfo.get(ConstVal.MAPPER_PATH) + File.separator
							+ tableInfo.getMapperName() + suffixJavaOrKt()), entityName);
					if (isCreate(FileType.MAPPER, mapperFile)) {
						writer(objectMap, templateFilePath(template.getMapper()), mapperFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成Mapper成功！");

				}
				// MpMapper.xml
				if (null != tableInfo.getXmlName() && null != pathInfo.get(ConstVal.XML_PATH)) {
					String xmlFile = String.format((pathInfo.get(ConstVal.XML_PATH) + File.separator
							+ tableInfo.getXmlName() + ConstVal.XML_SUFFIX), entityName);
					if (isCreate(FileType.XML, xmlFile)) {
						writer(objectMap, templateFilePath(template.getXml()), xmlFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成xml成功！");
				}
				// IMpService.java
				if (null != tableInfo.getServiceName() && null != pathInfo.get(ConstVal.SERVICE_PATH)) {
					String serviceFile = String.format((pathInfo.get(ConstVal.SERVICE_PATH) + File.separator
							+ tableInfo.getServiceName() + suffixJavaOrKt()), entityName);
					if (isCreate(FileType.SERVICE, serviceFile)) {
						writer(objectMap, templateFilePath(template.getService()), serviceFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成IService成功！");
				}
				// MpServiceImpl.java
				if (null != tableInfo.getServiceImplName() && null != pathInfo.get(ConstVal.SERVICE_IMPL_PATH)) {
					String implFile = String.format((pathInfo.get(ConstVal.SERVICE_IMPL_PATH) + File.separator
							+ tableInfo.getServiceImplName() + suffixJavaOrKt()), entityName);
					if (isCreate(FileType.SERVICE_IMPL, implFile)) {
						writer(objectMap, templateFilePath(template.getServiceImpl()), implFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成service成功！");
				}
				// MpController.java
				if (null != tableInfo.getControllerName() && null != pathInfo.get(ConstVal.CONTROLLER_PATH)) {

					String controllerFile = String.format((pathInfo.get(ConstVal.CONTROLLER_PATH) + File.separator
							+ msControllerName + suffixJavaOrKt()), entityName);
					if (isCreate(FileType.CONTROLLER, controllerFile)) {
						writer(objectMap, templateFilePath(template.getController()), controllerFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成msController成功！");

					String controllerfacadePath = joinPath(rootPath, controllerfacadePackage);

					String controllerFacadeFile = String.format((controllerfacadePath + File.separator
							+ tableInfo.getEntityName() + "Facade" + suffixJavaOrKt()), entityName);

					if (isCreate(FileType.CONTROLLER, controllerFacadeFile)) {
						writer(objectMap, templateFilePath(template.getFacadeController()), controllerFacadeFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成FacadeController成功！");

					String controllerClientPath = joinPath(rootPath, controllerClientPackage);

					String controllerClientFile = String.format((controllerClientPath + File.separator
							+ tableInfo.getEntityName() + "Client" + suffixJavaOrKt()), entityName);

					if (isCreate(FileType.CONTROLLER, controllerClientFile)) {
						writer(objectMap, templateFilePath(template.getClientController()), controllerClientFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成client成功！");

					String controllerAdminPath = joinPath(rootPath, controllerAdminPackage);

					String controllerAdminFile = String.format((controllerAdminPath + File.separator
							+ tableInfo.getEntityName() + "Controller" + suffixJavaOrKt()), entityName);

					if (isCreate(FileType.CONTROLLER, controllerAdminFile)) {
						writer(objectMap, templateFilePath(template.getAdminController()), controllerAdminFile);
					}
					log.info("表:" + tableInfo.getName() + ";生成admin Controller成功！");

				}
				// 生成HTML页面
				String htmlPath = htmlRootPath + File.separator + bizEnName;

				String pageHtmlFile = htmlPath + File.separator + bizEnName + ".html";
				if (isCreate(FileType.CONTROLLER, pageHtmlFile)) {
					writer(objectMap, templateFilePath(template.getPageHtml()), pageHtmlFile);
				}
				String pageJsFile = htmlPath + File.separator + bizEnName + ".js";
				if (isCreate(FileType.CONTROLLER, pageJsFile)) {
					writer(objectMap, templateFilePath(template.getPageJs()), pageJsFile);
				}

				String pageInfoJsFile = htmlPath + File.separator + bizEnName + "_info.js";
				if (isCreate(FileType.CONTROLLER, pageInfoJsFile)) {
					writer(objectMap, templateFilePath(template.getPageInif()), pageInfoJsFile);
				}

				String pageAddHtmlFile = htmlPath + File.separator + bizEnName + "_add.html";
				if (isCreate(FileType.CONTROLLER, pageAddHtmlFile)) {
					writer(objectMap, templateFilePath(template.getPageAdd()), pageAddHtmlFile);
				}

				String pageEditHtmlFile = htmlPath + File.separator + bizEnName + "_edit.html";
				if (isCreate(FileType.CONTROLLER, pageEditHtmlFile)) {
					writer(objectMap, templateFilePath(template.getPageEdit()), pageEditHtmlFile);
				}

			}
		} catch (Exception e) {
			logger.error("无法创建文件，请检查配置信息！", e);
		}
		return this;
	}

	public GenQo getGenQo() {
		return genQo;
	}

	public void setGenQo(GenQo genQo) {
		this.genQo = genQo;
	}

	/**
	 * 连接路径字符串
	 *
	 * @param parentDir   路径常量字符串
	 * @param packageName 包名
	 * @return 连接后的路径
	 */
	private String joinPath(String parentDir, String packageName) {
		if (StringUtils.isEmpty(parentDir)) {
			parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
		}
		if (!StringUtils.endsWith(parentDir, File.separator)) {
			parentDir += File.separator;
		}
		packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
		String path = parentDir + packageName;
		path = path.replaceAll(StringPool.BACK_SLASH + File.separator + StringPool.BACK_SLASH + File.separator,
				StringPool.BACK_SLASH + File.separator);
		return path;
	}

	@Override
	public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
		Template template = groupTemplate.getTemplate(templatePath);
		try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
			template.binding(objectMap);
			template.renderTo(fileOutputStream);
		}
		logger.debug("模板:" + templatePath + ";  文件:" + outputFile);
	}

	@Override
	public String templateFilePath(String filePath) {
		return filePath + ".btl";
	}
}
