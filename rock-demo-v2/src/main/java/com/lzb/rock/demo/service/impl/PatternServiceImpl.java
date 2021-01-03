package com.lzb.rock.demo.service.impl;

import org.springframework.stereotype.Service;

import com.lzb.rock.base.facade.impl.ServiceImpl;
import com.lzb.rock.demo.entity.Pattern;
import com.lzb.rock.demo.mapper.PatternMapper;
import com.lzb.rock.demo.service.IPatternService;

/**
 * <p>
 * 规则表 服务实现类
 * </p>
 *
 * @author lzb
 * @since 2020-12-22
 */
@Service
public class PatternServiceImpl extends ServiceImpl<PatternMapper, Pattern> implements IPatternService {

}
