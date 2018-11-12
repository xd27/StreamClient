package com.orbbec.streamclient;


import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Observable;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
public class Receiver {
    private Context mContext;
    private static DatagramSocket socket;
    private int port = 12666;
  //  private String ip = "10.10.6.17";
 //   private InetAddress address;
    private DatagramPacket packet;
    private byte[] data;
    public Receiver(){
        try{
          //  address = InetAddress.getByName(ip);
            socket = new DatagramSocket(port);
            data = new byte[300 * 1024];
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static DatagramSocket getSocket(){
        return socket;
    }

    public byte[] receive() {
        try {
            if (data != null) {
                packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                data = packet.getData();
           //     Log.e("Received data:",data[0]+","+data[1]+","+data[2]+","+data[3]);
                return data;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;

    }

    public void destroy(){
        if(socket!=null){
            socket.close();
        }
    }
}
