package com.grad_project_mobile.client.models.connection;

import com.grad_project_mobile.client.models.models.FileRowData;

import com.grad_project_mobile.shared.ConnectionBuilder;
import com.grad_project_mobile.shared.JsonParser;
import com.grad_project_mobile.shared.models.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

/**
 * BrowseClient class is used to browse the storage device and to delete files if required
 */
public class BrowsingClient {
    private String IP;

    /**
     * Constructor for the BrowsingClient object for a specific storage device
     *
     * @param hostIP Is the IP of the storage device
     */
    public BrowsingClient(String hostIP) {
        this.IP = hostIP;
    }

    /**
     * Method used to fetch the information of files under a certain directory
     *
     * @param path Is the path to the directory
     * @return Information about the files in the directory if it exists
     */
    public String browserRequest(String path) {
        Message request, response;
        try {
            Socket clientSocket = ConnectionBuilder.getInstance().buildClientSocket(this.IP);

            DataOutputStream dataOutputStream = ConnectionBuilder.getInstance()
                    .buildOutputStream(clientSocket);

            DataInputStream dataInputStream = ConnectionBuilder.getInstance()
                    .buildInputStream(clientSocket);

            request = new Message();
            request.createBrowseMessage(path);

            dataOutputStream.writeUTF(JsonParser.getInstance().toJson(request));
            dataOutputStream.flush();

            response = JsonParser.getInstance().fromJson(dataInputStream.readUTF(), Message.class);

            clientSocket.close();
            dataInputStream.close();
            dataOutputStream.close();

            /*
             Check if operation was a success
              */
            if (response.isSuccessMessage()) {
                return response.getMessageInfo();
            } else {

                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * Method used to delete a file/directory on the storage device
     *
     * @param path Is the path to the file/directory the storage device
     */
    public void deleteRequest(String path) {
        Message request, response;
        try {
            Socket clientSocket = ConnectionBuilder.getInstance().buildClientSocket(this.IP);

            DataOutputStream dataOutputStream = ConnectionBuilder.getInstance()
                    .buildOutputStream(clientSocket);

            DataInputStream dataInputStream = ConnectionBuilder.getInstance()
                    .buildInputStream(clientSocket);

            request = new Message();
            request.createDeleteMessage(path);

            dataOutputStream.writeUTF(JsonParser.getInstance().toJson(request));
            dataOutputStream.flush();

            response = JsonParser.getInstance().fromJson(dataInputStream.readUTF(), Message.class);

            clientSocket.close();
            dataInputStream.close();
            dataOutputStream.close();

            // check if operation is possible
            if (response.isErrorMessage()) {
                /*
                 pop up code here
                 */

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
