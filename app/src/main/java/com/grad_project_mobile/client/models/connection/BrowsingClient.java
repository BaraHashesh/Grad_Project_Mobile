package com.grad_project_mobile.client.models.connection;

import android.util.Log;

import com.grad_project_mobile.BrowserUpdater;
import com.grad_project_mobile.client.models.models.FileRowData;
import com.grad_project_mobile.shared.Constants;
import com.grad_project_mobile.shared.JsonParser;
import com.grad_project_mobile.shared.Methods;
import com.grad_project_mobile.shared.models.Message;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.Socket;
import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Class responsible for creating the web socket responsible for handling browsing operations
 */
class BrowserWebSocket extends WebSocketClient {
    private BrowserUpdater browserUpdater;

    private BrowserWebSocket(URI serverUri) {
        super(serverUri);
    }

    BrowserWebSocket(BrowserUpdater browserUpdater, URI serverUri) {
        this(serverUri);
        this.browserUpdater = browserUpdater;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override
    public void onMessage(String s) {
        Message replyMessage = JsonParser.getInstance().fromJson(s, Message.class);

        /*
        Check if replay is a success message
         */
        if (replyMessage.isSuccessMessage()) {
            FileRowData[] files = JsonParser.getInstance()
                    .fromJson(replyMessage.getMessageInfo(), FileRowData[].class);

            this.browserUpdater.update(files, true);
        }
        /*
        Check if replay message is an update message
         */
        if (replyMessage.isUpdateMessage()) {
            this.browserUpdater.update(replyMessage.getMessageInfo());
        }

        /*
        Check if replay message is an error message
         */
        else if (replyMessage.isErrorMessage()) {
            this.browserUpdater.update(null, false);
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {

    }

    @Override
    public void onError(Exception e) {
        System.out.println(e);
        e.printStackTrace();
    }
}

/**
 * Class responsible for handling browsing operations
 */
public class BrowsingClient {
    private BrowserWebSocket browserWebSocket;

    /**
     * Constructor for the BrowsingClient
     *
     * @param serverIP is the ip of the server
     */
    public BrowsingClient(BrowserUpdater browserUpdater, String serverIP) {

        try {
            browserWebSocket = new BrowserWebSocket(browserUpdater, new URI("wss://" +
                    serverIP + ":" + Constants.TCP_PORT));

            browserWebSocket.setSocket(Methods.getInstance().buildFactory().createSocket());
//            browserWebSocket.setSocket(new Socket());
            browserWebSocket.connectBlocking(2000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method used to send a browsing request to the server
     *
     * @param filePath Is the path to the directory to browse
     */
    public void browse(String filePath) {
        Message browseMessage = new Message();
        browseMessage.createBrowseMessage(filePath);

        this.browserWebSocket.send(JsonParser.getInstance().toJson(browseMessage));
    }


    /**
     * Method used to send a delete request to the server
     *
     * @param filePath Is the path of the file to be deleted
     */
    public void delete(String filePath) {
        Message browseMessage = new Message();
        browseMessage.createDeleteMessage(filePath);

        this.browserWebSocket.send(JsonParser.getInstance().toJson(browseMessage));
    }
}
