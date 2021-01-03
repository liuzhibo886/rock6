package com.lzb.rock.generator.generator;

import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.lzb.rock.generator.model.GenQo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MyPackageConfig extends PackageConfig {

	public MyPackageConfig(GenQo genQo) {
		this.entity = "open." + this.entity;
		this.service = "ms." + this.service;
		this.serviceImpl = "ms." + this.serviceImpl;
		this.mapper = "ms." + this.mapper;
		this.xml = "ms." + this.xml;
		this.controller = "ms." + this.controller;
	}

	private String entity = "entity";
	/**
	 * Service包名
	 */
	private String service = "service";
	/**
	 * Service Impl包名
	 */
	private String serviceImpl = "service.impl";
	/**
	 * Mapper包名
	 */
	private String mapper = "mapper";
	/**
	 * Mapper XML包名
	 */
	private String xml = "xml";

	/**
	 * Controller包名
	 */
	private String controller = "controller";

	private String facade = "open.facade";

	private String client = "open.client";

	private String admin = "admin.controller";

	private String dto = "open.dto";

}
