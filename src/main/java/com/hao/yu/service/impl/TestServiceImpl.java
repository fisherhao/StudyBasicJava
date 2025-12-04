package com.hao.yu.service.impl;

import com.hao.yu.dao.TestDao;
import com.hao.yu.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 说明：
 *
 * @author： Witty·Kid Fisher
 * @date： 2022-01-09 14:59:26
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestDao testDao;

    @Override
    public List<Map<String, Object>> mapList() {
        return testDao.listMapDao();
    }

    public static void main(String[] args) {
        System.out.println("Hello World! ");
    }
}
