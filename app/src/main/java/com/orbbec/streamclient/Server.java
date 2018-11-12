package com.orbbec.streamclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class Server {
    private static Server server;
    private static ServerSocket mServerSocket;
    private Socket socket;
    /* 服务器端口 */
    private final static int SERVER_HOST_PORT = 12580;
    private boolean release;
    private static final String TAG = "Receiver";

    public boolean hasRelease() {
        return release;
    }

    public void setRelease(boolean release) {
        this.release = release;
    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    private static ServerSocket getServerSocket() {
        if (mServerSocket == null) {
            try {
                mServerSocket = new ServerSocket(SERVER_HOST_PORT);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return mServerSocket;
    }

    public void connect() {
        // TODO Auto-generated method stub
        try {
            mServerSocket = getServerSocket();
            Log.i(TAG, "socket已开启,等待连接");
            while (true) {
                socket = mServerSocket.accept();
                if (socket.isConnected()) {
                    Log.e(TAG, "socket连接成功");
                    break;
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void disconnect() {
        // TODO Auto-generated method stub
        try {
            if (socket != null && socket.isConnected()) {
                socket.shutdownInput();
                socket.close();
                mServerSocket.close();
                Log.i(TAG, "socket断开了");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public byte[] readLength() {
        try {
            if (!socket.isConnected()) {
                socket = mServerSocket.accept();
            }
            InputStream is = socket.getInputStream();
            return readBytes(is, 4);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public byte[] readSPSPPS(int length) {
        // TODO Auto-generated method stub
        try {
            if (!socket.isConnected()) {
                socket = mServerSocket.accept();
            }
            InputStream is = socket.getInputStream();
            return readBytes(is, length);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

//    public Frame readFrame(int frameLength) {
//        // TODO Auto-generated method stub
//        try {
//            if (!socket.isConnected()) {
//                socket = mServerSocket.accept();
//            }
//            InputStream is = socket.getInputStream();
//            Frame frame = new Frame(readBytes(is, frameLength), 0, frameLength);
//            return frame;
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return null;
//    }

    /**
     * 从socket读byte数组
     *
     * @param in
     * @param length
     * @return
     */
    public static byte[] readBytes(InputStream in, long length) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while (read < length) {
            int cur = 0;
            try {
                cur = in.read(buffer, 0, (int) Math.min(1024, length - read));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (cur < 0) {
                break;
            }
            read += cur;
            baos.write(buffer, 0, cur);
        }
        return baos.toByteArray();
    }


    public InputStream getInputStream(){
        try {
            return socket.getInputStream();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
