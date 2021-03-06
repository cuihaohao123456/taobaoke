package com.example.order_assess;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.adapter.MyRecyclerAdapter;
import com.example.module_user_mine.R;
import com.example.mvp.BasePresenter;
import com.example.order_assess.adapter.OrderAssessAdapter;
import com.example.utils.OnChangeHeaderListener;
import com.example.utils.PopUtils;
import com.example.utils.UIHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrderAssessPresenter extends BasePresenter<OrderAssessView> {
    private List<Uri> uriList = new ArrayList<>();
    private OrderAssessAdapter adapter;
    private Uri fileUri;
    private String filePath = Environment.getExternalStorageDirectory() + "/fltk/image";

    public OrderAssessPresenter(Context context) {
        super(context);
    }

    @Override
    protected void onViewDestroy() {

    }

    public void loadData() {
        adapter = new OrderAssessAdapter(mContext, uriList, R.layout.rv_order_assess);
        if (getView() != null) {
            getView().loadRv(adapter);
        }

        adapter.setViewOnClickListener(new MyRecyclerAdapter.ViewOnClickListener() {
            @Override
            public void ViewOnClick(View view, final int index) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uriList.remove(index);
                        adapter.notifyDataSetChanged();
                        isSHowAdd();
                    }
                });
            }
        });

        adapter.setOnItemClick(new MyRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                UIHelper.seeBigImg(mContext, uriList.get(position));
            }
        });
    }


    public void addPic() {
        PopUtils.changeHeader(mContext, new OnChangeHeaderListener() {
            @Override
            public void setOnChangeHeader(final PopupWindow pop, TextView camera, TextView album) {
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCamera();
                        pop.dismiss();
                    }
                });

                album.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openPhotoAlbum();
                        pop.dismiss();
                    }
                });
            }
        });
    }

    private void openCamera() {
        File file0 = new File(filePath);
        if (!file0.exists()) {
            file0.mkdirs();
        }
        File file = new File(filePath, System.currentTimeMillis() + ".jpg");

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(mContext.getApplicationContext(), "com.lxy.taobaoke.provider", file);
            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            fileUri = Uri.fromFile(file);
        }
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        getView().takePhoto(captureIntent);
    }

    private void openPhotoAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        getView().photoAlbum(intent);
    }

    public void parseUri(Intent intent) {
        fileUri = intent.getData();
        String type = intent.getType();
        if (fileUri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = fileUri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = mContext.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        fileUri = uri_temp;
                    }
                }
            }
        }
        updateList();
    }

    public void updateList() {
        uriList.add(fileUri);
        fileUri = null;
        adapter.notifyDataSetChanged();
        isSHowAdd();
    }

    private void isSHowAdd() {
        if (uriList.size() <= 0) {
            getView().hideRv();
        } else {
            getView().showRv();
            if (uriList.size() >= 3) {
                getView().hideAdd();
            } else {
                getView().showAdd();
            }
        }
    }
}
