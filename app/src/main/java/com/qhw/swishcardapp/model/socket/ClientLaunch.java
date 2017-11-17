package com.qhw.swishcardapp.model.socket;

import com.qhw.swishcardapp.model.callback.NoneValueOperationListener;
import com.qhw.swishcardapp.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientLaunch {
    // 这个客户端连接到地址为xxx.xxx.xxx.xxx的服务器，端口为10020，并发送16进制到服务器，然后接受服务器的返回信息，最后结束会话。
    // 客户端，使用Socket对网络上某一个服务器的某一个端口发出连接请求，一旦连接成功，打开会话；会话完成后，关闭Socket。客户端不需要指定打开的端口，通常临时的、动态的分配一个1024以上的端口。
    public String Client(String strInput, NoneValueOperationListener listener) {
        String strOutput = "";
        Socket socket = null;
        InputStream socketReader = null;
        try {
            socket = new Socket();

            SocketAddress socAddress = new InetSocketAddress("192.168.0.141", 8575);
            socket.connect(socAddress, 5000);
            if (socket.isConnected()) {
                // 客户端输出流作为服务器的输入
                OutputStream socketWriter = socket.getOutputStream();
                socketWriter.write(Utils.str2Bcd(strInput));
                socketWriter.flush();
                Thread.sleep(1000);
                // 服务器的输出即为客户端的输入，这里主要是为了把服务器输出的字节流报文转化成字符串，方便进行解析，最终测试报文的正确性
                socketReader = socket.getInputStream();

                int count = 0;
                while (count == 0) {
                    count = socketReader.available();
                }
                // 因为我测试的报文包含报文头和报文体，这里的字节数组长度37为报文头长度
                byte[] temp = new byte[count];
                int bytes = 0;
                bytes = socketReader.read(temp);
                strOutput += Utils.bcd2Str(temp);
                socketReader.close();
                socket.close();
            } else {
                listener.responseEventCallBack();
            }
        } catch (SocketTimeoutException se) {//超时
            listener.responseEventCallBack();
        } catch (SocketException se) {//中断连接
            listener.responseEventCallBack();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socketReader != null) {
                    socketReader.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strOutput;
    }
}
