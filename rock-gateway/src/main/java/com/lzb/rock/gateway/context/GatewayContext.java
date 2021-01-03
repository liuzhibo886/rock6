package com.lzb.rock.gateway.context;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import com.lzb.rock.base.model.UrlPattern;

/**
 * 网关上下文对象
 * 
 * @author lzb
 *
 *         2020年12月21日 上午9:37:49
 *
 */
public class GatewayContext {

	/**
	 * 所有需要授权的url，可通过MQ广播 增加或者减少，也可以定时刷新，启动时全部加载，先默认写死，待业务优化
	 */
	public static Set<UrlPattern> URL_PATTERNS = new CopyOnWriteArraySet<UrlPattern>();

}
