package com.test.zhuqunwu.duijiangji;

/**
 * Created by zhuqunwu on 2016/4/1.
 */
public class AmrAudioEncoder {
    /*****
    private static  AmrAudioEncoder amrAudioEncoder=null;
    private Activity activity;
    private boolean isAudioRecording;
    private LocalServerSocket lss;
    //private LoclSocket receiver;
    private  LocalSocket sender,receiver;
    private MediaRecorder audioRecorder;
    private static final String TAG="ArmAudioEncoder";

    public static AmrAudioEncoder getArmAudioEncoderInstance(){
        if(amrAudioEncoder==null){
            synchronized (AmrAudioEncoder.class){
                if(amrAudioEncoder==null){
                    amrAudioEncoder=new AmrAudioEncoder();
                }
            }
        }
        return amrAudioEncoder;
    }

    public void initArmAudioEncoder(Activity activity){
        this.activity=activity;
        isAudioRecording=false;
    }

    public  void  start(){
        if(activity==null){
            showToastTest("



     ��Ƶ������δ��ʼ��������ִ��init����");
            return;
        }
        if(isAudioRecording){
            showToastTest("��Ƶ�Ѿ���ʼ���룬�����ٴα���");
            return;
        }
        if(!initLocalSocket()){
            showToastTest("���������ʧ��");
            releaseAll();
            return;
        }
        if(!initAudioRecorder()){
            showToastTest("��Ƶ��������ʼ��ʧ��");
            releaseAll();
            return;
        }
        this.isAudioRecording=true;
        startAudioRecording();
    }


    private void startAudioRecording() {
        new Thread(new AudioCaptureAndSendThread()).start();

    }
    private boolean initAudioRecorder() {
        if(audioRecorder !=null){
            audioRecorder.reset();
            audioRecorder.release();
        }
        audioRecorder=new MediaRecorder();
        audioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        audioRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
        final  int mono=1;
        audioRecorder.setAudioChannels(mono);
        audioRecorder.setAudioSamplingRate(8000);
        audioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        audioRecorder.setOutputFile(sender.getFileDescriptor());

        boolean ret=true;
        try {
            audioRecorder.prepare();
            audioRecorder.start();
        }catch (Exception e){
            releaseMediaRecorder();
            showToastTest("�ֻ���֧��¼��");
            ret =false;
        }
        return ret;
    }

    private void releaseAll() {
        releaseMediaRecorder();
        releaseLocalSocker();
        amrAudioEncoder=null;
    }

    private void releaseMediaRecorder() {
        try{
            if(audioRecorder==null){
                return;
            }
            if(isAudioRecording){
                audioRecorder.stop();
                isAudioRecording=false;
            }
            audioRecorder.reset();
            audioRecorder.release();
            audioRecorder=null;
        }catch (Exception err){
            Log.d(TAG, err.toString());
        }
    }

    private boolean initLocalSocket() {
        boolean ret=true;
        try {
            releaseLocalSocker();
            String serverName="armAudioServer";
            final int bufSize=1024;
            lss=new LocalServerSocket(serverName);
            receiver=new LocalSocket();
            receiver.connect(new LocalSocketAddress(serverName));
            receiver.setReceiveBufferSize(bufSize);
            receiver.setSendBufferSize(bufSize);
            sender=lss.accept();
            sender.setReceiveBufferSize(bufSize);
            sender.setSendBufferSize(bufSize);
        }catch (IOException e){
            ret=false;
        }
        return ret;
    }


    private void releaseLocalSocker() {
        try{
            if(sender !=null){
                sender.close();
            }
            if(receiver !=null){
                receiver.close();
            }
            if(lss !=null){
                lss.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        sender=null;
        receiver=null;
        lss=null;
    }
    private void showToastTest(String msg) {
        Toast.makeText(activity,msg,Toast.LENGTH_SHORT).show();
    }


    private class AudioCaptureAndSendThread implements Runnable {
        public  void run(){
            try{
                sendAmrAudio();
            }catch (Exception e){
                Log.e(TAG,"sendAmrAudio() ����");
            }
        }

        private void sendAmrAudio() throws Exception{
            DatagramSocket udpSocket=new DatagramSocket();
            DataInputStream dataInput=new DataInputStream(receiver.getInputStream());
            skipAmrHead(dataInput);
            final int SEND_FRAME_COUNT_ONE_TIME=10;
            final int BLOCK_SIZE[]={12,13,15,17,19,20,26,31,5,0,0,0,0,0,0,0};
            byte[] sendBuffer=new byte[1024];
            while (isAudioRecording()){
                int offset=0;
                for (int index=0;index<SEND_FRAME_COUNT_ONE_TIME;++index){
                    if(!isAudioRecording()){
                        break;
                    }
                    dataInput.read(sendBuffer,offset,1);
                    int blockIndex=(int)(sendBuffer[offset]>>3)&0x0f;
                    int frameLength=BLOCK_SIZE[blockIndex];
                    readSomeData(sendBuffer,offset+1,frameLength,dataInput);
                    offset+=frameLength+1;
                }
                udpSend(udpSocket,sendBuffer,offset);
            }
            udpSocket.close();
            dataInput.close();
            releaseAll();
        }
        private void skipAmrHead(DataInputStream dataInput) {
            final byte[] AMR_HEAD = new byte[] { 0x23, 0x21, 0x41, 0x4D, 0x52, 0x0A };
            int result = -1;
            int state = 0;
            try {
                while (-1 != (result = dataInput.readByte())) {
                    if (AMR_HEAD[0] == result) {
                        state = (0 == state) ? 1 : 0;
                    } else if (AMR_HEAD[1] == result) {
                        state = (1 == state) ? 2 : 0;
                    } else if (AMR_HEAD[2] == result) {
                        state = (2 == state) ? 3 : 0;
                    } else if (AMR_HEAD[3] == result) {
                        state = (3 == state) ? 4 : 0;
                    } else if (AMR_HEAD[4] == result) {
                        state = (4 == state) ? 5 : 0;
                    } else if (AMR_HEAD[5] == result) {
                        state = (5 == state) ? 6 : 0;
                    }

                    if (6 == state) {
                        break;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "read mdat error...");
            }
        }
        private void readSomeData(byte[] buffer, int offset, int length, DataInputStream dataInput) {
            int numOfRead = -1;
            while (true) {
                try {
                    numOfRead = dataInput.read(buffer, offset, length);
                    if (numOfRead == -1) {
                        Log.d(TAG, "amr...no data get wait for data coming.....");
                        Thread.sleep(100);
                    } else {
                        offset += numOfRead;
                        length -= numOfRead;
                        if (length <= 0) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "amr..error readSomeData");
                    break;
                }
            }
        }

        private void udpSend(DatagramSocket udpSocket, byte[] buffer, int sendLength) {
            try {
                InetAddress ip = CommonConfig.SERVER_IP_ADDRESS;
                int port = CommonConfig.SERVER_PORT;

                byte[] sendBuffer = new byte[sendLength];
                System.arraycopy(buffer, 0, sendBuffer, 0, sendLength);

                DatagramPacket packet = new DatagramPacket(sendBuffer, sendLength);
                packet.setAddress(ip);
                packet.setPort(port);
                udpSocket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isAudioRecording() {
        return isAudioRecording;

    }
     *****/
}
