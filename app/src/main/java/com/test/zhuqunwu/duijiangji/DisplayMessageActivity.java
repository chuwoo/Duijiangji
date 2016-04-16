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
        Intent intent=getIntent();      //得到intent并赋值给本地变量
        String message=intent.getStringExtra(MyActivity.EXTRA_MESSAGE); //提取从调用activit传递过来的值

        TextView textView=new TextView(this);       //创建一个textview对象
        textView.setTextSize(40);
        textView.setText(message);
        setContentView(textView);                   //把textview作为activity布局的根视图
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
