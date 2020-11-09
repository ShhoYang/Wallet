package com.highstreet.wallet.task;

public interface ProgressTaskListener {
    public abstract void onTaskResponse(TaskResult result);

    public abstract void onTaskProgress(Integer progress);
}
