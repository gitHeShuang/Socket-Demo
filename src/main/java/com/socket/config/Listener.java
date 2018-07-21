package com.socket.config;

import com.socket.model.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Listener extends Thread {

    private static Listener mListener = null;
    private List<User> userList = new ArrayList<>();

    public static Listener getInstance() {
        if (mListener == null) {
            synchronized (Listener.class) {
                if (mListener == null)
                    mListener = new Listener();
            }
        }

        return mListener;
    }

    // 线程池，用于支持并发。
    private ExecutorService mThreadPool;

    // 关闭标志
    private volatile boolean mStopFlag;

    private ServerSocket mServerSocket;

    /**
     * 只能存在单实例
     */
    private Listener() {
        mThreadPool = Executors.newCachedThreadPool();
        mStopFlag = false;
        mServerSocket = null;
    }

    /**
     * stop
     */
    public void setStopFlag() {
        mStopFlag = true;
    }

    @Override
    public void run() {
        try {
            mServerSocket = new ServerSocket(10222);
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }

        System.out.println("服务器已经启动，等待连接");
        while (!mStopFlag) {
            try {
                Socket socket = null;
                socket = mServerSocket.accept();// which is connected to the
                User user = new User(UUID.randomUUID().toString().replaceAll("-","").toLowerCase(),socket);

                // socket from client
                // break;
                if (!mStopFlag) {
                    mThreadPool.execute(new SocketServer(socket));
                } else {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("已经跳出循环了");
        try {
            mThreadPool.shutdown();
            mThreadPool.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Listener准备退出");
        return;

    }
}
