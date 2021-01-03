package com.lzb.rock.generator.generator;

import com.baomidou.mybatisplus.generator.config.TemplateConfig;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class MyTemplateConfig extends TemplateConfig {

	private String controller = "/templates/msController.java";
	
	private String facadeController = "/templates/facadeController.java";
	
	private String clientController = "/templates/clientController.java";
	private String adminController = "/templates/adminController.java";
	
	private String entityListReq = "/templates/entityListReq.java";
	
	private String pageHtml = "/templates/page.html";
	
	private String pageJs = "/templates/page.js";
	
	private String pageAdd = "/templates/page_add.html";
	
	private String pageEdit = "/templates/page_edit.html";
	
	private String pageInif = "/templates/page_info.js";



}
