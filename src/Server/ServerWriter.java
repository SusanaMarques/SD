package Server;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class ServerWriter implements Runnable
{
    private Notifier notifier;
    private MsgBuffer msg;
    private BufferedWriter out;
    private SDCloud sdCloud;


    /**
     * Construtor da classe ServerWriter parametrizado
     * @param msg      Buffer de mensagens
     * @param s       Socket
     * @param nots
     * @throws         IOException
     */
    ServerWriter(MsgBuffer msg, Socket s, SDCloud sdCloud, Notifier nots) throws IOException {
        this.msg = msg;
        this.out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
        this.sdCloud=sdCloud;
        this.notifier = nots;
    }

    /**
     * Método que executa a thread ServerWriter
     */
    @Override
    public  void run() {
        String last="";
        String[] ss= new String[2];
        while (true) {
            try {
                String r = msg.read();
                if(last.equals("DOWNLAST")){
                    sdCloud.finishedDownloading();
                }
                if(last.equals("UPLOAD")){
                    notifier.added(ss[1]);
                }
                out.write(r);
                out.newLine();
                out.flush();
                ss = r.split(" ", 2);
                last = ss[0];
            } catch (IOException | InterruptedException e) { e.printStackTrace(); }

        }
    }
}
