package Server;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;


public class ServerWriter implements Runnable
{
    private MsgBuffer msg;
    private BufferedWriter out;
    private SDCloud sdCloud;

    /**
     * Construtor da classe ServerWriter parametrizado
     * @param msg      Buffer de mensagens
     * @param s       Socket
     * @throws         IOException
     */
    ServerWriter(MsgBuffer msg, Socket s, SDCloud sdCloud) throws IOException {
        this.msg = msg;
        this.out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        this.sdCloud=sdCloud;
    }

    /**
     * Método que executa a thread ServerWriter
     */
    @Override
    public void run() {
        String last="";
        while (true) {
            try {
                String r = msg.read();
                System.out.println("MAYBE NOT EMPTY:"+last+"/S");
                if(last.equals("DOWNLOAD")){
                    System.out.println("Actual serverwriteer lim locking");
                    sdCloud.finishedDownloading();
                    System.out.println("ActualServerwritter lim locked");
                }
                out.write(r);
                out.newLine();
                out.flush();
                last = r.split(" ",2)[0];
            } catch (IOException | InterruptedException e) { e.printStackTrace(); }
        }
    }
}
