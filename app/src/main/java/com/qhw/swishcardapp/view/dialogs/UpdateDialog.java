package com.qhw.swishcardapp.view.dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.qhw.swishcardapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Hello_world on 2016/12/7.
 */

public class UpdateDialog extends Dialog implements View.OnClickListener {

    private RelativeLayout rl_comdit;
    private LinearLayout ll_value;
    private Context context;
    private String path;
    private ProgressDialog dialog;
    private RelativeLayout rl_back;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    dialog.setIndeterminate(false);
                    dialog.setProgress(msg.arg1);
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }
        }
    };

    public UpdateDialog(Context context, String path) {
        super(context);
        this.context = context;
        this.path = path;
        dialog = new ProgressDialog(context);
        dialog.setMax(100);
        dialog.setTitle("下载");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);
        init();
    }

    private void init() {
        ll_value = (LinearLayout) findViewById(R.id.ll_dialog_value);
        rl_comdit = (RelativeLayout) findViewById(R.id.rl_comdit);
        rl_back = (RelativeLayout) findViewById(R.id.rl_back);
        rl_comdit.setOnClickListener(this);
        rl_back.setOnClickListener(this);
        this.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_comdit:
                dialog.show();
                update(path);
                dismiss();
                break;
            case R.id.rl_back:
                dismiss();
                break;
        }
    }

    public void update(final String mPath) {
        try {
            Runnable r = new Runnable() {
                public void run() {
                    doDownloadTheFile(mPath);
                }
            };
            new Thread(r).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        intent.setDataAndType(Uri.fromFile(f), type);
        context.startActivity(intent);
    }

    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    private void doDownloadTheFile(String strPath) {
        URL myURL = null;
        try {
            myURL = new URL(strPath);
            HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept-Encoding", "identity"); // 添加这行代码
            conn.connect();
            InputStream is = conn.getInputStream();
            File myTempFile = new File(
                    Environment.getExternalStorageDirectory(),
                    "万店通刷.apk");
            FileOutputStream fos = new FileOutputStream(myTempFile);
            int fileLength = conn.getContentLength();
            byte data[] = new byte[128];
            long total = 0;
            int count;
            while ((count = is.read(data)) != -1) {
                if (count <= 0) {
                    break;
                }
                total += count;
                if (fileLength > 0) {
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.arg1 = (int) (total * 100 / fileLength);
                    handler.sendMessage(message);
                }
                fos.write(data, 0, count);
            }
            handler.sendEmptyMessage(2);
            openFile(myTempFile);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "连接服务器错误", Toast.LENGTH_SHORT).show();
        }
    }
}
