package com.test.zhuqunwu.duijiangji;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.KeyEvent;
//import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by zhuqunwu on 2016/4/14.
 */
public class MyService extends Service{
    private DatagramSocket socketUDP=null;
    private JSONObject obj;
    public  AudPlay ap =null;
    @Override
    public void onCreate() {
        obj=new JSONObject();
        try {
            socketUDP=new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        ap=new AudPlay(socketUDP);
        ap.start();
        System.out.println("ϵͳ����");
        super.onCreate();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public boolean onKeyDown(int keyCode,KeyEvent event){       //�����������¿�ʼ¼��
        if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            onClickRec();

            return true;
        }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            onClickRec();

            return true;
        }else  if(keyCode==KeyEvent.KEYCODE_BACK) {
            //trueFlers=false;
            this.finish();
        }

        return  super.onKeyDown(keyCode, event);
    }

    private void onClickRec() {
        try {
            //obj.put("name", "test");
            obj.put("content","zhuqunwu" );
            obj.put("action", "chat");
        } catch (JSONException e) {
            e.printStackTrace();
        }//����json����
        try {
            new UdpSend(socketUDP,obj.toString()).start();        //��json��udp���͵�����
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TextView tv=(TextView)findViewById(R.id.textView);
        //tv.setText("����һ������");
        //System.out.println("����һ������");
    }

}
