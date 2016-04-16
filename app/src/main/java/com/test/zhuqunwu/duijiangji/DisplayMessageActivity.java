package com.test.zhuqunwu.duijiangji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayMessageActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();      //�õ�intent����ֵ�����ر���
        String message=intent.getStringExtra(MyActivity.EXTRA_MESSAGE); //��ȡ�ӵ���activit���ݹ�����ֵ

        TextView textView=new TextView(this);       //����һ��textview����
        textView.setTextSize(40);
        textView.setText(message);
        setContentView(textView);                   //��textview��Ϊactivity���ֵĸ���ͼ
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
}
