package com.lzb.rock.gateway.filte;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;

import com.lzb.rock.base.enums.RockEnum;
import com.lzb.rock.base.enums.RedisKey;
import com.lzb.rock.base.model.Result;
import com.lzb.rock.base.model.UrlPattern;
import com.lzb.rock.base.util.UtilJson;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.gateway.context.GatewayContext;
import com.lzb.rock.redis.mapper.RedisMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthFilte implements GlobalFilter, Ordered {

	@Value("${token.aes.encodeRules}")
	String encodeRules;

	@Value("${token.ref.expire:600}")
	Long refTokenExpire;

	@Autowired
	RedisMapper redisMapper;

	private static AntPathMatcher pathMatcher = new AntPathMatcher();

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest serverHttpRequest = exchange.getRequest();
		String uri = serverHttpRequest.getURI().getPath();
		HttpMethod method = serverHttpRequest.getMethod();

		Boolean isAuth = false;
		for (UrlPattern pattern : GatewayContext.URL_PATTERNS) {
			if (pathMatcher.match(pattern.getPattern(), uri)) {
				if (pattern.getMethod() == null || pattern.getMethod() == method) {
					isAuth = true;
					break;
				}
			}
		}
		if (!isAuth) {
			return chain.filter(exchange);
		}

		ServerHttpRequest request = exchange.getRequest();
		List<String> tokens = request.getHeaders().get("token");

		List<String> userId = request.getHeaders().get("userId");

		if (tokens == null || tokens.size() < 1 || UtilString.isBlank(tokens.get(0))) {
			log.info("token为空,非法登录,uri:{}", uri);
			Result<Void> result = new Result<Void>();
			result.error(RockEnum.WRONGFUL);
			return buildReturnMono(result, exchange);
		}
		String token = tokens.get(0);

		List<UrlPattern> urls = redisMapper.getList(RedisKey.TOKEN + token, UrlPattern.class);

		if (urls != null) {
			for (UrlPattern pattern : urls) {
				if (!pathMatcher.match(pattern.getPattern(), uri)) {
					continue;
				}
				if (pattern.getMethod() == null || pattern.getMethod() == method) {
					return chain.filter(exchange);
				}
			}
		}

		Result<Void> result = new Result<Void>();
		result.error(RockEnum.AUTH_URL_ERR);
		log.info("无访问权限,userId：{};uri:{}", userId, uri);
		return buildReturnMono(result, exchange);
	}

	@Override
	public int getOrder() {

		return Integer.MIN_VALUE + 1;
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
