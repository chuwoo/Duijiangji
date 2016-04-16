package com.test.zhuqunwu.duijiangji;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by zhuqunwu on 2016/4/9.
 */
public class AudPlay extends Thread {
    //private  DatagramPacket dp;
    private  DatagramSocket ds;
    private InetAddress ip;
    private String data;
    //private  int port;
    public static boolean Sendf=false;
    public static String resule=null;
    //private MyActivity handler;

    public AudPlay(DatagramSocket ds)  {
        this.ds=ds;
        //this.port= 8124;
        //this.Sendf=false;

    }

    public  void run() {

        //ds=new DatagramSocket(5555);

        while (true) {

            byte[] bte = new byte[1024];
            //DatagramSocket ds=new DatagramSocket(5555);


            DatagramPacket dp = new DatagramPacket(bte, bte.length);


            try {
                ds.receive(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            resule = new String(dp.getData(), dp.getOffset(), dp.getLength());
            //Message message=new Message();
            //message.what=MyActivity.UPDATA_TEXT;
            //handler.sendMessage(message);
            //Sendf=true;
            //MyActivity.trueFler = false;
        }

        //System.out.println(resule);


    }


}


