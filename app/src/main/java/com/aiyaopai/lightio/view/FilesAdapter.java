package com.aiyaopai.lightio.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
@Deprecated
public class FilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

//    private Context mContext;
//    private List<UsbFile> mData = new ArrayList<>();
//
//    public FilesAdapter(Context context) {
//        this.mContext = context;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(mContext).inflate(R.layout.file_item, parent, false);
//        return new ViewHolder(itemView);
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ViewHolder viewHolder = (ViewHolder) holder;
//        viewHolder.tvName.setText(mData.get(position).getName());
//        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mData.get(position).isDirectory()) {
//                    showFileDir(mData.get(position));
//                }
//            }
//        });
//    }
//
//    public void setData(List<UsbFile> mData) {
//        this.mData = mData;
//    }
//
//    public List<UsbFile> getData() {
//        return mData;
//    }
//
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        TextView tvName;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            tvName = itemView.findViewById(R.id.tv_name);
//        }
//    }
//
//
//    /**
//     * 扫描显示文件列表
//     *
//     * @param usbFile
//     */
//    private void showFileDir(UsbFile usbFile) {
//        UsbFile[] files;
//        try {
//            files = usbFile.listFiles();
//            List<UsbFile> usbFilesList = new ArrayList<>();
//            Collections.addAll(usbFilesList, files);
//            setData(usbFilesList);
//            notifyDataSetChanged();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
}
