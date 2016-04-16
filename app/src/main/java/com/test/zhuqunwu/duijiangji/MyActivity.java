package com.test.zhuqunwu.duijiangji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class MyActivity extends Activity {
    public  final static String EXTRA_MESSAGE ="com.test.zhuqunwu.duijiangji";
    //private AmrAudioEncoder amrEncoder;
    //public static boolean trueFler=true;

    public  AudPlay ap =null;
    //public  UdpSend us=new Udp    Send("bc");
    //private AudRec ar = null;
    private boolean flag=true;
    private DatagramSocket socketUDP=null;
    //private Thread thread=null;
    //public String message;
    //private List<UseData> dataList;
    private JSONObject obj;
    private TextView text;

    //@Override

    protected void onCreate(Bundle savedInstanceState) {
        obj=new JSONObject();
        try {
            socketUDP=new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        //StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);
        //WifiManager manager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        //WifiManager.MulticastLock lock= manager.createMulticastLock("test wifi");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        //ap.init(new byte[640]);
        Intent startIntent=new Intent(this,MyService.class);
        startService(startIntent);


        ap=new AudPlay(socketUDP);
        ap.start();
        System.out.println("系统测试");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void sendMessage(View view) throws Exception{         //按钮按下的执行函数

        //Intent intent=new Intent(this,DisplayMessageActivity.class);    //构建一个intent

        EditText editTest = (EditText) findViewById(R.id.edit_message);
        String message = editTest.getText().toString();
        //if(flag) {


            try {
                obj.put("name", message);
                obj.put("content", "");
                obj.put("action", "online");
            } catch (JSONException e) {
                e.printStackTrace();
            }//创建json对象
            //flag=false;
        //}
        //String useSting=obj.toString();
        new UdpSend(socketUDP,obj.toString()).start();        //把json用udp发送到网络
        //TextView tv=(TextView)findViewById(R.id.textView);
        //tv.setText("这是一个测试");
        //System.out.println("这是一个测试");

        //ap.Send(obj.toString());
        //while (trueFler);
        //ap.start();
        //while (AudPlay.Sendf);

        //intent.putExtra(EXTRA_MESSAGE, AudPlay.resule);             //把用户内容传给intert
        //startActivity(intent);
    }

    private void startEncodeAudio(){
        //amrEncoder=AmrAudioEncoder.getArmAudioEncoderInstance();
        //amrEncoder.initArmAudioEncoder(this);
        //amrEncoder.start();
    }

    public boolean onKeyDown(int keyCode,KeyEvent event){       //音量键被按下开始录音
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
        }//创建json对象
        try {
            new UdpSend(socketUDP,obj.toString()).start();        //把json用udp发送到网络
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView tv=(TextView)findViewById(R.id.textView);
        tv.setText("这是一个测试");
        System.out.println("这是一个测试");
    }


    public boolean onKeyUp(int keyCode,KeyEvent event){         //音量键被释放，停止录音

        if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            //onClickUP();

            return true;
        }else  if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN){
            //cnClickUP();
            return  true;
        }
        return  super.onKeyUp(keyCode, event);
    }


    private class MyHandler extends Handler {
        public void handleMessage(Message msg){

        }

        @Override
        public void close() {

        }

        @Override
        public void flush() {

        }

        @Override
        public void publish(LogRecord record) {

        }
    }
}
