package com.mars.socket;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by kys-29 on 2016/9/21.
 */

/*
MainActivity代码：
    SocketServer server=new SocketServer ( port );
    server.beginListen ();

* */
public class SocketServer
{
    private ServerSocket server;
    private Socket socket;
    private InputStream in;
    private String str=null;
    private boolean isClint=false;
    public static Handler ServerHandler;

    /**
     * @steps bind();绑定端口号
     * @effect 初始化服务端
     * @param port 端口号
     * */
    public SocketServer(int port){
        try {
            server= new ServerSocket ( port );
            isClint=true;
        }catch (IOException e){
            e.printStackTrace ();
        }
       
    }

    /**
     * @steps listen();
     * @effect socket监听数据
     * */
    public void beginListen()
    {
        new Thread ( new Runnable ( )
        {
            @Override
            public void run()
            {
                try {
                    /**
                     * accept();
                     * 接受请求
                     * */
                    socket=server.accept ();
                    try {
                        /**得到输入流*/
                        in =socket.getInputStream();
                        /**
                         * 实现数据循环接收
                         * */
                        while (!socket.isClosed())
                        {
                            byte[] bt=new byte[50];
                            in.read ( bt );
                            str=new String ( bt,"UTF-8" );                  //编码方式  解决收到数据乱码
                            if (str!=null&&str!="exit")
                            {
                                returnMessage ( str );
                            }else if (str==null||str=="exit"){
                                break;                                     //跳出循环结束socket数据接收
                            }
                            System.out.println(str);
                        }
                    } catch (IOException e) {
                        e.printStackTrace ( );
                        socket.isClosed ();
                    }
                } catch (IOException e) {
                    e.printStackTrace ( );
                    socket.isClosed ();
                }
            }
        } ).start ();
    }

    /**
     * @steps write();
     * @effect socket服务端发送信息
     * */
    public void sendMessage(final String chat)
    {
        Thread thread=new Thread ( new Runnable ( )
        {
            @Override
            public void run()
            {
                try {
                    PrintWriter out=new PrintWriter ( socket.getOutputStream () );
                    out.print ( chat );
                    out.flush ();
                } catch (IOException e) {
                    e.printStackTrace ( );
                }
            }
        } );
        thread.start ();
    }

    /**
     * @steps read();
     * @effect socket服务端得到返回数据并发送到主界面
     * */
    public void returnMessage(String chat){
        Message msg=new Message ();
        msg.obj=chat;
        ServerHandler.sendMessage ( msg );
    }

}
