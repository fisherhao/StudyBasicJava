package com.hao.yu.controller;

import com.hao.yu.service.TestService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 说明：
 *
 * @author： Fisher.Hao
 * @date： 2022-01-08 19:31:19
 */
@RestController
@RequestMapping(value = "/study")
public class TestController {

	@Autowired
	private TestService testService;

	@GetMapping(value = "/listmap")
	@ResponseBody
	public List<Map<String, Object>> list(String id) {
		System.out.println("id：" + id);
		return testService.mapList();
	}
}
