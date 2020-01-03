package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServer {

    public static void main(String[] args) throws IOException {
        ServerSocket s = new ServerSocket(12345);
        SDCloud sdCloud= new SDCloud();
        Notifier nots = new Notifier();
        Thread nts =  new Thread(nots);
        nts.start();
        while (true) {
            MsgBuffer msg = new MsgBuffer();
            Socket socket = s.accept();
            nots.addBuffer(msg);
            ServerReader sr =  new ServerReader(socket,sdCloud,msg);
            ServerWriter sw = new ServerWriter(msg,socket,sdCloud, nots);
            Thread tw = new Thread(sw);
            Thread tr = new Thread(sr);
            tw.start();
            tr.start();
        }
    }
}
