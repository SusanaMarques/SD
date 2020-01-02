package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que faz o parse do comando recebido
     * @param c Comando recebido
     */
    private void parsing(String c) throws IOException, InterruptedException {
        String[] p = c.split(" ", 2);
        switch (p[0].toUpperCase()) {
            case "AUTENTICATED":
                menu.setOpt(1);
                menu.showMenu();
                break;
            case "REGISTER":
                menu.setOpt(0);
                menu.showMenu();
                break;
            case "ENDSESSION":
                menu.setOpt(0);
                menu.showMenu();
                break;
            case "UPLOAD":
                menu.setOpt(1);
                menu.showMenu();
                break;
            case "DOWNLOAD":
                menu.setOpt(1);
                break;
            case "SEARCH":
                menu.setOpt(1);
                System.out.println(c);
                break;
            case "LIBRARY":
                menu.setOpt(1);
                System.out.println(c);
                break;
            default:
                System.out.println(c);
                menu.showMenu();
                break;


        }
    }


}

