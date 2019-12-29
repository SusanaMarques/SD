package Server;

import java.net.Socket;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;


public class ServerWriter implements Runnable
{
    private MsgBuffer msg;
    private BufferedWriter out;

    /**
     * Construtor da classe ServerWriter parametrizado
     * @param msg      Buffer de mensagens
     * @param s       Socket
     * @throws         IOException
     */
    ServerWriter(MsgBuffer msg, Socket s) throws IOException {
        this.msg = msg;
        this.out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
    }

    /**
     * Método que executa a thread ServerWriter
     */
    @Override
    public void run() {
        while (true) {
            try {
                String r = msg.read();
                out.write(r);
                out.newLine();
                out.flush();
            } catch (IOException | InterruptedException e) { e.printStackTrace(); }
        }
    }
}
