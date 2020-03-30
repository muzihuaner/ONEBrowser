package com.huangenet.onebrowser;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText et_url;


    private Button btn_visit;

    private WebView web;

    private Button btn_home,btn_left,btn_right,btn_exit;

    private boolean isExit;

    private String homeStr = "cn.bing.com";


    //-------------------------------------------
    /**
     * 基本数据初始化
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void init(){

        isExit = false;
        et_url = (EditText)findViewById(R.id.et_url);
        et_url.setText(homeStr);
        btn_visit = (Button)findViewById(R.id.btn_visit);

        web = (WebView)findViewById(R.id.web);
        WebSettings ws = web.getSettings();
        //是否允许脚本支持

        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSaveFormData(true);
        ws.setSavePassword(true);
        ws.setAppCacheEnabled(true);
        ws.setAppCacheMaxSize(10240);
//		ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //是否允许缩放
        //ws.setBuiltInZoomControls(true);
        web.setWebViewClient(wvc);
        web.setWebChromeClient(wcc);

        btn_home = (Button)findViewById(R.id.btn_home);
        btn_left = (Button)findViewById(R.id.btn_left);
        btn_right = (Button)findViewById(R.id.btn_right);
        btn_exit = (Button)findViewById(R.id.btn_exit);

        btn_visit.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
        btn_exit.setOnClickListener(this);

        web.setOnTouchListener(touchListener);

        conn(et_url.getText().toString());

    }

    //-------------------------------------------
    /**
     * 触摸监听
     */
    View.OnTouchListener touchListener = new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            switch(v.getId()){
                case R.id.web:
                    web.requestFocus();
                    break;
            }
            return false;
        }
    };
    //-------------------------------------------
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }


    //-------------------------------------------

    WebViewClient wvc = new WebViewClient(){

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            et_url.setText(url);

            return true;
        };
    };


    //-------------------------------------------

    WebChromeClient wcc = new WebChromeClient(){

        public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
            view.requestFocus();

        }


    };



    //-------------------------------------------
    /**
     * 访问url
     * @param urlStr
     */
    private void conn(String urlStr){
        String url = "";
        if(urlStr.contains("http://")){
            url = urlStr;
        }else{
            url = "http://"+urlStr;
        }
        web.loadUrl(url);
    }

    //-------------------------------------------
    /**
     * 后退
     */
    private void goBack(){
        if(web.canGoBack()){
            web.goBack();
        }else{
            Toast.makeText(this, "已经是第一页了哦！",Toast.LENGTH_SHORT).show();
        }
    }

    //-------------------------------------------
    /**
     * 前进
     */
    private void goForward(){
        if(web.canGoForward()){
            web.goForward();
        }else{
            Toast.makeText(this, "已经是最后一页了哦！",Toast.LENGTH_SHORT).show();
        }
    }
    //-------------------------------------------
    /**
     * 退出
     */
    private void exit(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("退出")
                .setMessage("主人你要离开我吗？")
                .setPositiveButton("确定", dialogListener)
                .setNegativeButton("取消", dialogListener)
                .create()
                .show();

    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;

                default:
                    dialog.cancel();
                    break;
            }
        }
    };

    //-------------------------------------------

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK||web.canGoBack()){
            web.goBack();
            if(!web.canGoBack()){
                if(isExit){
                    return super.onKeyDown(keyCode, event);
                }
                isExit = true;
                Toast.makeText(this, "再按一次才能离开我哦！", Toast.LENGTH_SHORT).show();
            }else{
                isExit = false;
            }
        }
        return true;

    }


    //-------------------------------------------
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_visit:

                conn(et_url.getText().toString());

                break;
            case R.id.btn_home:


                conn(homeStr);
                break;
            case R.id.btn_left:
                goBack();
                break;
            case R.id.btn_right:
                goForward();
                break;
            case R.id.btn_exit:
                exit();
                break;

        }
    }


}