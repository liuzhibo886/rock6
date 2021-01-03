package com.lzb.rock.generator;

import org.apache.log4j.PropertyConfigurator;

import com.lzb.rock.generator.generator.MyGenerator;
import com.lzb.rock.generator.model.GenQo;
import com.lzb.rock.generator.util.UtilLoadProperties;

public class RockCodeGenerator {

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure(UtilLoadProperties.init("log4j.properites"));
		MyGenerator rockGenerato = new MyGenerator();
		GenQo genQo = GenQo.create();
		rockGenerato.start(genQo);
	}
}
