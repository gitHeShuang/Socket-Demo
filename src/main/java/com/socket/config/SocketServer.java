package com.socket.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.socket.config.SocketUtil;
import com.socket.model.User;

import java.io.IOException;
import java.net.Socket;

/**
 * socket服务端
 */
public class SocketServer extends Thread {

    private Socket mSocket = null;

    public SocketServer(Socket socket) {
        this.mSocket = socket;
    }

    @Override
    public void run() {
        try {
            String resp = readSocket();
            User user1 = JSON.parseObject(resp,User.class);

            DealWithService dealWithService = new DealWithService();
            writeResponse(dealWithService.deal(resp));
        } catch (Exception e) {
        } finally {
            if (!mSocket.isClosed())
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private String readSocket() {
        if (null == mSocket)
            return "";
        String server_content = "";
        try {
            server_content = SocketUtil.Accept(mSocket);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return server_content;
    }

    private void writeResponse(String resp)
            throws Exception {
        SocketUtil.Send(resp, mSocket);
    }

}
