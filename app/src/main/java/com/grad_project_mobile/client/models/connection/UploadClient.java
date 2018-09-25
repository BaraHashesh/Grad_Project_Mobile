package com.grad_project_mobile.client.models.connection;

import com.grad_project_mobile.client.models.models.FileRowData;
import com.grad_project_mobile.shared.Constants;
import com.grad_project_mobile.shared.FileTransfer;
import com.grad_project_mobile.shared.JsonParser;
import com.grad_project_mobile.shared.Methods;
import com.grad_project_mobile.shared.models.Message;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.File;
import java.net.URI;
import java.util.concurrent.TimeUnit;


/**
 * Class responsible for creating the web socket responsible for handling upload operations
 */
class UploadWebSocket extends WebSocketClient {

    private File fileToUpload;

    /**
     * Constructor for the {@link UploadWebSocket}
     *
     * @param serverUri Is the server URI
     */
    UploadWebSocket(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
        Message responseMessage = JsonParser.getInstance().fromJson(s, Message.class);

        /*
        Check if message is a success message
         */
        if (responseMessage.isSuccessMessage()) {
            FileTransfer fileTransfer = new FileTransfer();

            new Thread(()->{
                fileTransfer.send(this, this.fileToUpload);

                responseMessage.createStreamEndMessage("");

                send(JsonParser.getInstance().toJson(responseMessage));
            }).start();
        }
        /*
        Check if error message
         */
        else if (responseMessage.isErrorMessage()) {

        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {

    }

    /**
     * set method for fileToUpload
     *
     * @param fileToUpload Is the file/folder to upload
     */
    void setFileToUpload(File fileToUpload) {
        this.fileToUpload = fileToUpload;
    }
}

/**
 * Class responsible for handling upload operations
 */
public class UploadClient {
    private UploadWebSocket uploadWebSocket;

    /**
     * Constructor for the {@link UploadClient}
     *
     * @param serverIP Is the server IP
     */
    public UploadClient(String serverIP) {
        try {
            this.uploadWebSocket = new UploadWebSocket(
                    new URI("wss://" + serverIP + ":" + Constants.TCP_PORT)
            );

            this.uploadWebSocket.setSocket(Methods.getInstance().buildFactory().createSocket());
            this.uploadWebSocket.connectBlocking(2000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Method used to start upload operation
     *
     * @param fileToUpload   Is the file/folder to upload
     * @param locationToSave Is where to save the file/folder on the storage device
     */
    public void upload(File fileToUpload, String locationToSave) {
        this.uploadWebSocket.setFileToUpload(fileToUpload);

        Message requestMessage = new Message();
        requestMessage.createUploadMessage(locationToSave);

        this.uploadWebSocket.send(JsonParser.getInstance().toJson(requestMessage));
    }
}
