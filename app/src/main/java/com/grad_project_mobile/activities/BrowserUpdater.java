package com.grad_project_mobile.activities;

import com.grad_project_mobile.client.models.models.FileRowData;

public interface BrowserUpdater {
    public abstract void update(FileRowData[] files, boolean isSuccessful);
    public abstract void update(String pathToUpdate);
    public abstract void makeMessages(String msg);
}
