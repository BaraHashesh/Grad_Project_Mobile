package com.grad_project_mobile.shared;

import com.grad_project_mobile.client.models.models.ServerRowInfo;
import com.grad_project_mobile.shared.models.BasicFileData;

import java.io.File;
import java.util.ArrayList;

/**
 * ObjectParser class is used to cast objects, that have no direct relationship between them
 * from a given model to another [No Inheritance](File -> BasicFileData)
 */
public class ObjectParser {
    private static ObjectParser instance = new ObjectParser();

    /**
     * Empty constructor
     */
    private ObjectParser() {
    }

    /**
     * Get method for Instance
     *
     * @return An instance of the ObjectParser object
     */
    public static ObjectParser getInstance() {
        return instance;
    }

    /**
     * Method used to convert File objects into BasicFileData objects
     *
     * @param fileList Is a list of File objects
     * @return A list of BasicFileData objects
     */
    public BasicFileData[] fileToBasicFileData(File... fileList) {
        BasicFileData[] newList = new BasicFileData[fileList.length];

        /*
        for loop used to iterate over file objects
         */
        for (int i = 0; i < fileList.length; i++)
            newList[i] = new BasicFileData(fileList[i]);

        return newList;
    }

    /**
     * Method used to convert an ArrayList object that contains
     * servers IPs into an array of ServerInfo Objects
     *
     * @param primitiveServerInfo An ArrayList of strings strings that contains the IPs of ther servers
     * @return An array of ServerRowInfo Objects
     */
    public ServerRowInfo[] constructServerInfo(ArrayList<String> primitiveServerInfo) {
        ServerRowInfo[] serverInfos = new ServerRowInfo[primitiveServerInfo.size()];

        /*
        For loop to iterate over ArrayList
         */
        for (int i = 0; i < primitiveServerInfo.size(); i++) {
            serverInfos[i] = new ServerRowInfo(primitiveServerInfo.get(i));
        }

        return serverInfos;
    }

}
