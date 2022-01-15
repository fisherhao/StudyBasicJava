package com.hao.yu;

import com.alibaba.fastjson.JSON;
import com.hao.yu.dao.TestDao;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StudyJavaApplicationTests {

	@Autowired
	private TestDao testDao;

	@Test
	public void setTestDao() {
		String name11 = testDao.getName(2L);
		System.out.println("***********************************");
		System.out.println(name11);
		System.out.println("***********************************");
	}

	@Test
	public void setTestDao111() {
		List<Map<String, Object>> maps = testDao.listMapDao();
		System.out.println("***********************************");
		System.out.println(maps);
		System.out.println(JSON.toJSONString(maps));
		System.out.println("***********************************");
	}

}
