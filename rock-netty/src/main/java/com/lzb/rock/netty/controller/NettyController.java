package com.lzb.rock.netty.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lzb.rock.base.facade.IEnum;
import com.lzb.rock.base.model.Result;
import com.lzb.rock.base.util.UtilString;
import com.lzb.rock.netty.context.DelayContext;
import com.lzb.rock.netty.context.MyNettyContext;
import com.lzb.rock.netty.dto.MyDelayed;
import com.lzb.rock.netty.dto.NettyMsg;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RequestMapping("/tell/netty")
@RestController
@Api(tags = { "netty控制器" })
public class NettyController {

	@PostMapping("/push")
	@ApiOperation(value = "发送消息")
	// @Log(before = false, end = false)
	public Result<Void> push(@RequestBody @Valid NettyMsg msg) {

		if (UtilString.isBlank(msg.getMsgId())) {
			msg.setMsgId(UUID.randomUUID().toString().replace("-", ""));
		}
		IEnum enums = MyNettyContext.writeAndFlush(msg);
		MyDelayed myDelayed = new MyDelayed(msg);
		DelayContext.offer(myDelayed);

		return Result.newInstance(enums);
	}
}
