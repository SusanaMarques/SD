package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServer {

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(12345);
        SDCloud sdCloud= new SDCloud();
        while (true) {
            MsgBuffer msg = new MsgBuffer();
            Socket socket = s.accept();
            //ServerReader sr =
            //ServerWriter sw =
            //Thread tw = new Thread(sw);
            //Thread tr = new Thread(sr);
            //tw.start();
            //tr.start();
        }
    }
}
