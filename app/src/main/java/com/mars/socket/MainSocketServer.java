package com.mars.socket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainSocketServer extends AppCompatActivity
{
    private TextView txt;
    private EditText edit;
    private Button btn;
    private EditText edit_server;
    private EditText edit_ip;
    private Button server_OK;
    private LinearLayout mLinearLayout;
    private LinearLayout mLinearLayout_client;

    /**启动服务端端口
     * 服务端IP为手机IP
     * */
    private int pite;
    private SocketServer server;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        txt = (TextView) findViewById ( R.id.textView );
        edit = (EditText) findViewById ( R.id.edit );
        btn = (Button) findViewById ( R.id.btn );
        edit_server=(EditText)findViewById ( R.id.editText_server );
        edit_ip=(EditText)findViewById ( R.id.client_ip );
        server_OK=(Button)findViewById ( R.id.server_OK );
        mLinearLayout=(LinearLayout)findViewById ( R.id.lin_1 ) ;
        mLinearLayout_client=(LinearLayout)findViewById ( R.id.lin_ip ) ;

        mLinearLayout_client.setVisibility ( View.GONE );

        server_OK.setOnClickListener ( new View.OnClickListener ( )
        {
            @Override
            public void onClick(View v)
            {
                mLinearLayout.setVisibility ( View.GONE );
                try {
                    pite= Integer.parseInt(edit_server.getText ().toString ());

                    server=new SocketServer ( pite );
                    /**socket服务端开始监听*/
                    server.beginListen ( );

                }catch (Exception e){
                    Toast.makeText ( MainSocketServer.this,"请输入数字", Toast.LENGTH_SHORT ).show ();
                    mLinearLayout.setVisibility ( View.VISIBLE );
                    e.printStackTrace ();
                }

            }
        } );


        btn.setOnClickListener ( new View.OnClickListener ( )
        {
            @Override
            public void onClick(View v)
            {
                /**socket发送数据*/
                server.sendMessage ( edit.getText ().toString () );
            }
        } );

        /**socket收到消息线程*/
        SocketServer.ServerHandler=new Handler (  ){
            @Override
            public void handleMessage(Message msg)
            {
                txt.setText ( msg.obj.toString ());
            }
        };

    }

}
