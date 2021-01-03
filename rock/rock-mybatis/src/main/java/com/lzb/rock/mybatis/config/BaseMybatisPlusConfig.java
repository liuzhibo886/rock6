package com.lzb.rock.mybatis.config;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * 扫描dao或者是Mapper接口
 * 
 * @author lzb
 * @Date 2019年7月31日 下午5:03:28
 */
@Configuration
@MapperScan("com.lzb.rock.**.mapper.**")
public class BaseMybatisPlusConfig {
	/**
	 * mybatis-plus 分页插件
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		paginationInterceptor.setDialectType("mysql");
		
		
//        // 创建SQL解析器集合
//        List<ISqlParser> sqlParserList = new ArrayList<>();
// 
//        // 创建租户SQL解析器
//        TenantSqlParser tenantSqlParser = new TenantSqlParser();
// 
//        // 设置租户处理器
//        tenantSqlParser.setTenantHandler(new TenantHandler() {
//
//			@Override
//			public Expression getTenantId(boolean where) {
//                // 设置当前租户ID，实际情况你可以从cookie、或者缓存中拿都行
//				
//                return new StringValue("jiannan");			}
//
//			@Override
//			public String getTenantIdColumn() {
//                // 对应数据库租户ID的列名
//                return "tenant_id";
//			}
//
//			@Override
//			public boolean doTableFilter(String tableName) {
//                // 是否需要需要过滤某一张表
//              /*  List<String> tableNameList = Arrays.asList("sys_user");
//                if (tableNameList.contains(tableName)){
//                    return true;
//                }*/
//                return false;
//			}
//        	
//        });
// 
//        sqlParserList.add(tenantSqlParser);
//        paginationInterceptor.setSqlParserList(sqlParserList);
		
		return paginationInterceptor;
	}

	/**
	 * 乐观锁mybatis插件
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}

	/*
	 * 开启逻辑删除(3.1.1开始不再需要这一步)：
	 */
//	@Bean
//	public ISqlInjector sqlInjector() {
//		return new LogicSqlInjector();
//	}
}
