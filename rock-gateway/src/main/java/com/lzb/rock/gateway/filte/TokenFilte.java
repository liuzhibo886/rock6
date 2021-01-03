package com.lzb.rock.gateway.filte;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

import com.lzb.rock.base.enums.RockEnum;
import com.lzb.rock.base.enums.RedisKey;
import com.lzb.rock.base.model.Header;
import com.lzb.rock.base.model.Result;
import com.lzb.rock.base.model.UrlPattern;
import com.lzb.rock.base.util.UtilAES;
import com.lzb.rock.base.util.UtilJson;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.redis.mapper.RedisMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TokenFilte implements GlobalFilter, Ordered {

	@Value("${token.aes.encodeRules}")
	String encodeRules;

	@Value("${token.ref.expire:600}")
	Long refTokenExpire;

	@Autowired
	RedisMapper redisMapper;

	private static AntPathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 白名单规则，可配置文件或者基础数据
	 */
	private static List<UrlPattern> anons = new ArrayList<UrlPattern>();

	private static List<UrlPattern> authc = new ArrayList<UrlPattern>();

	/**
	 * 临时初始化
	 */
	static {
		anons.add(UrlPattern.builder().pattern("/**/swagger**").build());
		anons.add(UrlPattern.builder().pattern("/**/**swagger**/**").build());
		anons.add(UrlPattern.builder().pattern("/**/v2/api-docs/**").build());
		anons.add(UrlPattern.builder().pattern("/**/csrf/**").build());
		anons.add(UrlPattern.builder().pattern("/rock/demo/user/logon").method(HttpMethod.GET).build());
		anons.add(UrlPattern.builder().pattern("/rock/demo/user").method(HttpMethod.POST).build());

		authc.add(UrlPattern.builder().pattern("/authc/**").build());
		authc.add(UrlPattern.builder().pattern("/**").method(HttpMethod.DELETE).build());
		authc.add(UrlPattern.builder().pattern("/**").method(HttpMethod.POST).build());
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

		ServerHttpRequest serverHttpRequest = exchange.getRequest();
		String uri = serverHttpRequest.getURI().getPath();
		log.info("uri:{}", uri);

		HttpMethod method = serverHttpRequest.getMethod();

		// 白名单
		for (UrlPattern pattern : anons) {
			if (pathMatcher.match(pattern.getPattern(), uri)) {
				if (pattern.getMethod() == null || pattern.getMethod() == method) {
					return chain.filter(exchange);
				}
			}
		}

		HttpHeaders headers = serverHttpRequest.getHeaders();
		List<String> tokens = headers.get("aes-token");

		if (tokens == null || tokens.size() < 1 || UtilString.isBlank(tokens.get(0))) {
			Result<Void> result = new Result<Void>();
			result.error(RockEnum.WRONGFUL);
			return buildReturnMono(result, exchange);
		}
		String aesToken = tokens.get(0);
		String jsonStr = UtilAES.Base64AESDncode(encodeRules, aesToken);

		Header header = UtilJson.getJavaBean(jsonStr, Header.class);

		// 解密失败，token不合法
		if (header == null || UtilString.isBlank(header.getUserId()) || UtilString.isBlank(header.getToken())) {
			Result<Void> result = new Result<Void>();
			result.error(RockEnum.WRONGFUL);
			return buildReturnMono(result, exchange);
		}

		// 认证,token 合法且token有效
		for (UrlPattern pattern : authc) {
			if (!pathMatcher.match(pattern.getPattern(), uri)) {
				continue;
			}
			if (pattern.getMethod() == null || pattern.getMethod() == method) {
				String token = header.getToken();
				if (!redisMapper.hasKey(RedisKey.TOKEN + token)) {
					// 此处应该查数据库，当前用户token是否失效
					Result<Void> result = new Result<Void>();
					result.error(RockEnum.TOKEN_ERR);
					return buildReturnMono(result, exchange);
				}
			}
		}

		// 解密后的数据，写入请求头，带入到后面服务
		ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> httpHeaders.remove("userId"))
				.headers(httpHeaders -> httpHeaders.remove("tenantId"))
				.headers(httpHeaders -> httpHeaders.remove("platform"))
				.headers(httpHeaders -> httpHeaders.remove("aes-token")).header("userId", header.getUserId())
				.header("tenantId", header.getTenantId()).header("token", header.getToken())
				.header("platform", header.getPlatform()).build();
		ServerWebExchange serverWebExchange = exchange.mutate().request(request).build();
		return chain.filter(serverWebExchange);
	}

	@Override
	public int getOrder() {

		return Integer.MIN_VALUE;
	}

	private Mono<Void> buildReturnMono(Result<?> result, ServerWebExchange exchange) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		// 指定编码，否则在浏览器中会中文乱码
		response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");

		if (result != null) {
			byte[] bits = UtilJson.getStr(result).getBytes(StandardCharsets.UTF_8);
			DataBuffer buffer = response.bufferFactory().wrap(bits);
			return response.writeWith(Mono.just(buffer));
		}

		return response.writeWith(Mono.just(null));
	}
}
