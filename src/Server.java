import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Server {

    public Server(int port) throws IOException {
        ServerSocket sS = new ServerSocket(port);
        List<Thread> ts = new ArrayList<Thread>();

        while (true) {
            Thread t = new Thread(new ServerWorker(sS.accept(), bn));
            System.out.println("1");
            t.start();
            System.out.println("2");
            ts.add(t);

        }

    }

}
