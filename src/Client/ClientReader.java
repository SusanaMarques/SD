package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientReader  implements Runnable
{
    /** Menu do cliente **/
    private Menu menu;
    /** Socket **/
    private Socket socket;
    /** BufferedReader **/
    private BufferedReader in;

    /**
     * Construtor da classe ClientReader parametrizado
     *
     * @param m         Menu de opções
     * @param s         Socket
     * @throws          IOException
     */
    public ClientReader(Menu m, Socket s) throws IOException {
        this.menu = m;
        this.socket = s;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Método que executa a thread ClientReader
     */
    @Override
    public void run() {
        try {
            String command;
            while ((command = in.readLine()) != null)
                parsing(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que faz o parse do comando recebido
     * @param c Comando recebido
     */
    private synchronized void parsing(String c) {
        String[] p = c.split(" ", 2);
        switch (p[0].toUpperCase()) {
            case "AUTENTICADO":
                menu.setOpt(1);
                menu.showMenu();
                break;
            case "REGISTADO":
                menu.setOpt(0);
                menu.showMenu();
                break;
            case "SESSAOTERMINADA":
                menu.setOpt(0);
                menu.showMenu();
                break;
        }
    }
}

