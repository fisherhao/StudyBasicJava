package com.hao.yu.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 说明：
 *
 * @author： Witty·Kid Fisher
 * @date： 2022-01-08 19:30:23
 */
@Mapper
public interface TestDao {

    /**
     * @param stuId
     *
     * @return
     */
    String getName(@Param("stuId") Long stuId);

    /**
     * 获取为List
     *
     * @return
     */
    List<Map<String, Object>> listMapDao();
}
