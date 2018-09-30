package com.grad_project_mobile;

import com.grad_project_mobile.client.models.models.FileRowData;

public interface BrowserUpdater {
    public abstract void update(FileRowData[] files, boolean isSuccessful);
    public abstract void update(String pathToUpdate);
}
