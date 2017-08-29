package com.stt.uavos.model;


/**
 * @ description: 任务类
 * @ time: 2017/8/24.
 * @ author: peiyun.feng
 * @ email: fengpy@aliyun.com
 */

public class Task {
    private String taskId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务模式
     */
    private String taskMode;
    /**
     * 探头类型
     */
    private String probeType;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskMode() {
        return taskMode;
    }

    public void setTaskMode(String taskMode) {
        this.taskMode = taskMode;
    }

    public String getProbeType() {
        return probeType;
    }

    public void setProbeType(String probeType) {
        this.probeType = probeType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
