package com.lzb.rock.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzb.rock.base.enums.RockEnum;
import com.lzb.rock.base.factory.PageFactory;
import com.lzb.rock.base.model.Result;
import com.lzb.rock.demo.entity.Pattern;
import com.lzb.rock.demo.service.IPatternService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 规则表 控制器
 * </p>
 *
 * @author lzb
 * @since 2020-12-22
 */
@RestController
@Api(tags = { "规则表控制器" })
@Slf4j
@RequestMapping("/rock/demo/pattern")
public class PatternController {

	@Autowired
	IPatternService patternService;

	@GetMapping("/list")
	@ResponseBody
	@ApiOperation(value = "获取规则表列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "limit", value = "每页条数"),
			@ApiImplicitParam(name = "page", value = "当前页") })
	public Result<Page<Pattern>> list(@RequestParam("limit") Integer limit, @RequestParam("page") Integer page) {

		Page<Pattern> patternPage = new PageFactory<Pattern>().defaultPage(limit, page);
		LambdaQueryWrapper<Pattern> queryWrapper = Wrappers.lambdaQuery();
		Page<Pattern> pageResp = patternService.page(patternPage, queryWrapper);
		return new Result<Page<Pattern>>(pageResp);
	}

	@PostMapping
	@ResponseBody
	@ApiOperation(value = "新增规则表")
	public Result<Pattern> add(@RequestBody Pattern pattern) {
		patternService.save(pattern);
		return new Result<Pattern>(pattern);
	}

	@DeleteMapping
	@ResponseBody
	@ApiOperation(value = "删除规则表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "patternId", value = "主键"), })
	public Result<Void> delete(@RequestParam(name = "patternId") Long patternId) {
//    	LambdaUpdateWrapper<Pattern> updateWrapper = Wrappers.lambdaUpdate();
//    	Pattern entity = new Pattern();
//		entity.setIsDel(1);
//		entity.setLastUser(null);
//		updateWrapper.eq(Pattern::getPatternId, patternId);
//		Integer count = patternService.update(entity,updateWrapper);

		LambdaQueryWrapper<Pattern> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(Pattern::getPatternId, patternId);
		Integer count = patternService.delete(queryWrapper);
		if (count > 0) {
			return new Result<Void>();
		} else {
			return new Result<Void>(RockEnum.DELETE_ERR);
		}
	}

	@PutMapping
	@ResponseBody
	@ApiOperation(value = "修改规则表")
	public Result<Void> update(@RequestBody Pattern pattern) {
		LambdaUpdateWrapper<Pattern> updateWrapper = Wrappers.lambdaUpdate();
		updateWrapper.eq(Pattern::getPatternId, pattern.getPatternId());
		pattern.setPatternId(null);
		Integer count = patternService.update(pattern, updateWrapper);
		if (count > 0) {
			return new Result<Void>();
		} else {
			return new Result<Void>(RockEnum.UPDATE_ERR);
		}
	}

	@GetMapping
	@ResponseBody
	@ApiOperation(value = "规则表详情")
	@ApiImplicitParams({ @ApiImplicitParam(name = "patternId", value = "主键") })
	public Result<Pattern> detail(@RequestParam(name = "patternId") Long patternId) {
		LambdaQueryWrapper<Pattern> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(Pattern::getPatternId, patternId);
		Pattern pattern = patternService.getOne(queryWrapper);
		return new Result<Pattern>(pattern);
	}
}
