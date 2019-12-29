package Client;

import java.io.IOException;
import java.net.Socket;

public class MainClient
{

    public static void main(String[] args){
        Socket s = null;
        try {
            s = new Socket("127.0.0.1", 12345);
            Menu m =  new Menu();
            ClientWriter cw = new ClientWriter(m,s);
            ClientReader cr = new ClientReader(m,s);
            Thread reader = new Thread((Runnable) cr);
            Thread writer = new Thread((Runnable) cw);
            reader.start();
            writer.start();
        } catch(IOException e){System.out.println(e.getMessage());}
    }
}

