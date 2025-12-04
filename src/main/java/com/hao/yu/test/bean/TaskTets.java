package com.hao.yu.test.bean;

/**
 * 说明：
 *
 * @author： Witty·Kid Fisher
 * @date： 2022-01-15 18:19:53
 */
public class TaskTets {

    private String taskName;

    private String taskDesc;

    public TaskTets(String taskName, String taskDesc) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
    }

    public TaskTets() {

    }

    /**
     * Getter method for property <tt>taskName</tt>.
     *
     * @return property value of taskName
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Setter method for property <tt>taskName</tt>.
     *
     * @param taskName
     *         value to be assigned to property
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Getter method for property <tt>taskDesc</tt>.
     *
     * @return property value of taskDesc
     */
    public String getTaskDesc() {
        return taskDesc;
    }

    /**
     * Setter method for property <tt>taskDesc</tt>.
     *
     * @param taskDesc
     *         value to be assigned to property
     */
    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }
}
