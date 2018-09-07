package com.grad_project_mobile.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grad_project_mobile.R;
import com.grad_project_mobile.client.models.models.FileRowData;

import java.util.ArrayList;

public class FileInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private FileInfoAdapter.FileInfoClickListener mListener;
    private ArrayList<FileRowData> files;
    private Context context;


    /**
     * Constructor for FileInfoAdapter
     * @param context Context of the host UI (view)
     * @param files The list of files
     */
    public FileInfoAdapter(Context context, ArrayList<FileRowData> files){
        this.context = context;
        this.files = files;
    }

    /**
     * This class is used to initialize items information and components
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView fileIcon;
        LinearLayout fileDescriptions;
        TextView fileName;
        TextView fileSize;
        Button fileDeleteButton;
        Button fileDownloadButton;

        ViewHolder(View itemView) {
            super(itemView);

            fileIcon = itemView.findViewById(R.id.file_info_item_icon);

            fileDescriptions = itemView.findViewById(R.id.file_info_item_description);
            fileName = itemView.findViewById(R.id.file_info_item_name);
            fileSize = itemView.findViewById(R.id.file_info_item_size);

            fileDeleteButton = itemView.findViewById(R.id.file_info_item_delete_button);
            fileDownloadButton = itemView.findViewById(R.id.file_info_item_download_button);

            fileIcon.setOnClickListener(this);
            fileDeleteButton.setOnClickListener(this);
            fileDownloadButton.setOnClickListener(this);

            context.databaseList();
        }

        @Override
        public void onClick(View view) {
            /*
            Check which element was clicked
             */
            switch (view.getId()) {
                case R.id.file_info_item_delete_button:
                    mListener.onFileDelete(files.get(getAdapterPosition()), view);
                    break;
                case R.id.file_info_item_download_button:
                    mListener.onFileDownload(files.get(getAdapterPosition()), view);
                    break;
                case R.id.file_info_item_icon:
                    mListener.onFolderBrowse(files.get(getAdapterPosition()), view);
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
                .inflate(R.layout.file_info_item, parent, false);

        return new FileInfoAdapter.ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder)holder).fileName.setText(files.get(position).getName());
        ((ViewHolder)holder).fileSize.setText(files.get(position).getSizeInfo());

        /*
        Check if file is directory
         */
        if(files.get(position).isDirectory()){
            ((ViewHolder)holder).fileIcon.setBackgroundResource(R.drawable.folder);
        } else {
            ((ViewHolder)holder).fileIcon.setBackgroundResource(R.drawable.file);
        }
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    /**
     * This interface is used to link the item UI elements with methods in other activities
     */
    public interface FileInfoClickListener {
        void onFileDelete(FileRowData file, View v);
        void onFileDownload(FileRowData file, View v);
        void onFolderBrowse(FileRowData file, View v);
    }

    /**
     * set method for mListener
     * @param mListener activity that contains the functions to be executed
     */
    public void setListener(FileInfoClickListener mListener) {
        this.mListener = mListener;
    }
}
