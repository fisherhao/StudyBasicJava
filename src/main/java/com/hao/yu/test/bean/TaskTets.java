package com.hao.yu.test.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 说明：
 *
 * @author： Witty·Kid Fisher
 * @date： 2022-01-15 18:19:53
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    private String taskName;

    private String taskDesc;
}
