package com.grad_project_mobile.client.models.connection;

import com.grad_project_mobile.shared.ConnectionBuilder;
import com.grad_project_mobile.shared.FileTransfer;
import com.grad_project_mobile.shared.JsonParser;
import com.grad_project_mobile.shared.models.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.net.Socket;

/**
 * UploadClient class is used to upload files to storage device on a separate thread
 */
public class UploadClient implements Runnable {
    private File file;
    private String locationToSave;
    private String IP;
    private Thread thread;

    /**
     * Constructor for the UploadClient object for a specific storage device
     *
     * @param hostIP Is the IP of the storage device
     */
    public UploadClient(String hostIP) {
        this.IP = hostIP;
    }

    /**
     * Initialize method for the variables
     * also start thread operations
     *
     * @param file           Is file to be uploaded
     * @param locationToSave Is the location to save file under within the storage device
     */
    public void start(File file, String locationToSave) {
        this.file = file;
        this.locationToSave = locationToSave;

        /*
        Check if the thread is not running
         */
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    /**
     * Upload operations are done here
     */
    @Override
    public void run() {
        Message request, response;
        try {
            Socket clientSocket = ConnectionBuilder.getInstance().buildClientSocket(this.IP);

            DataOutputStream dataOutputStream = ConnectionBuilder.getInstance()
                    .buildOutputStream(clientSocket);

            DataInputStream dataInputStream = ConnectionBuilder.getInstance()
                    .buildInputStream(clientSocket);

            request = new Message();
            request.createUploadMessage(locationToSave);

            dataOutputStream.writeUTF(JsonParser.getInstance().toJson(request));
            dataOutputStream.flush();

            response = JsonParser.getInstance().fromJson(dataInputStream.readUTF(), Message.class);

            /*
            Check if operation was possible
             */
            if (response.isErrorMessage()) {

                /*
                pop up code here
                 */

            } else {
                String parent = this.file.getParent();

                FileTransfer fileTransfer = new FileTransfer();

                fileTransfer.sendFiles(dataOutputStream, this.file, parent);

                Message streamEndMessage = new Message();
                streamEndMessage.createStreamEndMessage("");

                dataOutputStream.writeUTF(JsonParser.getInstance().toJson(streamEndMessage));
                dataOutputStream.flush();
            }

            clientSocket.close();
            dataInputStream.close();
            dataOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
