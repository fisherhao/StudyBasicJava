package com.hao.yu.classinfo;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * 说明：
 *
 * @author： Witty·Kid Fisher
 * @date： 2022-04-30 16:52:07
 */
@Data
@Component
public class SimpleClassImpl implements SimpleClass {
    @Override
    public String getCurrentName() {
        return "nidaye";
    }

    @Override
    public Integer getCurrentNameLength() {
        return 3;
    }
}
