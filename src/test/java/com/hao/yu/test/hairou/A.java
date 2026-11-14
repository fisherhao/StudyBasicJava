package com.hao.yu.test.hairou;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 说明：
 *
 * @author Witty·Kid Fisher
 * @version v 0.1 2026年10月16日 星期五 18:31
 */
public class A {
    /**
     * 要求：
     * <p>
     * 1. null/非法数据过滤；
     * 2. key=sessionId:toolCallId；
     * 3. 同一个 toolCall 可能因为 retry 有多次执行；
     * 4. SUCCESS > RUNNING > FAILED；
     * 5. status 相同，attempt 更大的代表更新的一次；
     * 6. attempt 相同，endTime 更新的优先；
     * 7. 输出按 sessionId、startTime 排序；
     * 8. 要求 O(n log n) 以内。
     *
     * @param execution
     *
     * @return
     */

    public Map<String, ToolExecution> mergeExecutions(List<ToolExecution> execution) {

        if (execution == null || execution.isEmpty()) {
            throw new IllegalArgumentException("Invalid execution data");
        }
        execution = execution.stream()
            .filter(Objects::nonNull)
            .filter(e -> e.getSessionId() != null && e.getToolCallId() != null)
            .sorted(Comparator.comparing(ToolExecution::getStatus)
                .thenComparing(ToolExecution::getAttempt)
                .thenComparing(ToolExecution::getEndTime))
            .collect(Collectors.toList());

        //结果排序
        //treemap
        Map<String, List<ToolExecution>> collect = execution.stream()
            .collect(
                Collectors.groupingBy(e -> e.getSessionId() + ":" + e.getToolCallId()));

        Map<String, ToolExecution> result = new TreeMap<>();

        for (Map.Entry<String, List<ToolExecution>> entry : collect.entrySet()) {
            String key = entry.getKey();
            ToolExecution last = getLast(entry.getValue());
            
            result.put(key, last);
        }

        return result;
    }

    private ToolExecution getLast(List<ToolExecution> executions) {
        if (executions == null || executions.isEmpty()) {
            return null;
        }
        return executions.get(executions.size() - 1);
    }
}

@Setter
@Getter
class ToolExecution {
    String sessionId;
    String toolCallId;
    String toolName;
    String status;
    int attempt;
    long startTime;
    long endTime;
}
