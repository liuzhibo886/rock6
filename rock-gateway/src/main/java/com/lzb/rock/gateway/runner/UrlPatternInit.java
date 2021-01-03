package com.lzb.rock.gateway.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.lzb.rock.base.model.UrlPattern;
import com.lzb.rock.gateway.context.GatewayContext;

/**
 * 启动初始化需要授权的url列表
 * 
 * @author lzb
 *
 *         2020年12月21日 上午11:46:41
 *
 */
@Component
public class UrlPatternInit implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		GatewayContext.URL_PATTERNS.add(UrlPattern.builder().pattern("/rock/demo/tenant").method(HttpMethod.POST).build());
		GatewayContext.URL_PATTERNS.add(UrlPattern.builder().pattern("/**").method(HttpMethod.DELETE).build());
	}

}
