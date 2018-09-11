package com.grad_project_mobile.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.grad_project_mobile.R;
import com.grad_project_mobile.client.models.models.ServerRowInfo;

import java.util.ArrayList;

/**
 * This class is used to turn an ArrayList of ServerRowInfo object to an adapter of a RecycleView
 */
public class ServerInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ServerInfoClickListener mListener;
    private ArrayList<ServerRowInfo> servers;
    private Context context;


    /**
     * Constructor for ServerInfoAdapter
     * @param context Context of the host UI (view)
     * @param servers The list of available server
     */
    public ServerInfoAdapter(Context context, ArrayList<ServerRowInfo> servers){
        this.context = context;
        this.servers = servers;
    }

    /**
     * This class is used to initialize items information and components
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView serverIPTextView;
        Button selectServerButton;

        ViewHolder(View itemView) {
            super(itemView);

            serverIPTextView = itemView.findViewById(R.id.server_info_item_ip_text_view);
            selectServerButton = itemView.findViewById(R.id.server_info_item_select_button);

            selectServerButton.setOnClickListener(this);

            context.databaseList();
        }

        @Override
        public void onClick(View view) {
            /*
            Check which element was clicked
             */
            switch (view.getId()) {

                /*
                Check if button was clicked
                 */
                case R.id.server_info_item_select_button:
                    mListener.onServerSelected(servers.get(getAdapterPosition()), view);
                    break;
                default:
                    break;

            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.server_info_item, parent, false);

        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ServerInfoAdapter.ViewHolder)holder).serverIPTextView.setText(servers.get(position).getIp());

    }

    @Override
    public int getItemCount() {
        return servers.size();
    }

    /**
     * This interface is used to link the item UI elements with methods in other activities
     */
    public interface ServerInfoClickListener {
        void onServerSelected(ServerRowInfo server, View v);
    }

    /**
     * set method for mListener
     * @param mListener activity that contains the functions to be executed
     */
    public void setListener(ServerInfoClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * Set method for servers
     * @param servers The new list of servers
     */
    public void setServers(ArrayList<ServerRowInfo> servers) {
        this.servers.clear();
        this.servers.addAll(servers);
    }
}
