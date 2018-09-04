package com.grad_project_mobile.client.models.models;

import com.grad_project_mobile.shared.Constants;
import com.grad_project_mobile.shared.Methods;
import com.grad_project_mobile.shared.models.BasicFileData;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FileRowData extends BasicFileData {

    /**
     * Constructor for the FileRowData object
     *
     * @param file File object to be changed (transformed)
     */
    public FileRowData(File file) {
        super(file);
    }

    /**
     * Method used to get the size of the file
     *
     * @return the size of file in bytes, K-bytes, M-bytes
     */
    public String getSizeInfo() {
        /*
        Check if file is a directory
         */
        if (isDirectory())
            return "";

        else {
            Object[] sizeInfo = Methods.getInstance().reduceSize((double) super.getSize());

            return Methods.getInstance().round((double) sizeInfo[0], 2) + " " + sizeInfo[1];
        }
    }

    /**
     * Method used to extract the extension of the file
     *
     * @return The extension of the file (EXE, PDF, ...)
     */
    private String getExtension() {
        String extension = "";

        int i = getPath().lastIndexOf('.');
        int p = getPath().lastIndexOf(Constants.BACKWARD_DASH);

        if (i > p && i > 0) {
            extension = getPath().substring(i + 1);
        }

        return extension;
    }

    /**
     * Method used to modify the format of the date
     *
     * @return The reformatted date for the last edit done on the file
     */
    public String getModifiedDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(this.getLastModified());
    }

    /**
     * Method used to extract the file name from the file path
     *
     * @return The name of the file
     */
    public String getName() {
        int i = getPath().lastIndexOf(Constants.BACKWARD_DASH);

        return getPath().substring(i + 1);
    }

    /**
     * Method used to extract the parent of the file from the file path
     *
     * @return The path for the parent of the file
     */
    public String getParent() {
        int i = getPath().lastIndexOf(Constants.BACKWARD_DASH);

        return getPath().substring(0, i);
    }

    /**
     * Method used to obtain parent directory of the file
     *
     * @return The path for the directory at which the parent exists
     */
    public String getPreviousDirectory() {
        String parent = getParent();

        int i = parent.lastIndexOf(Constants.BACKWARD_DASH);

        return parent.substring(0, i + 1);
    }
}
