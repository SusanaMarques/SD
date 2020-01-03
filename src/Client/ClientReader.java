package Client;

import Server.SDNetwork;

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

    private String path;

    /**
     * Construtor da classe ClientReader parametrizado
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
        } catch (IOException | InterruptedException e) { e.printStackTrace(); }
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
                this.path= this.download(p[1]);
                menu.showMenu();
                break;
            case "UPLFRAGNO":
                break;
            case "DOWNLAST":
                break;
            case "LAST":
                break;
                case "MORE":
                break;
            case "DOWNFRAG":
                downfrag(p[1]);
                break;
            case "SEARCH":
                menu.setOpt(1);
                repl(c);
                menu.showMenu();
                break;
            case "LIBRARY":
                menu.setOpt(1);
                repl(c);
                menu.showMenu();
                break;
            default:
                System.out.println(c);
                menu.showMenu();
                break;
        }
    }

    private void downfrag(String s) throws IOException {
        SDNetwork.unfragger(s, this.path);

    }

    private String download(String p) throws IOException {
        String[] s = p.split(" ", 5);
        System.out.println("download client reader");
        //long var10000 = Thread.currentThread().getId();
        System.out.println("CLIENT reader: "+path);
        System.out.flush();
        return "Download/" + s[1] + ".mp3";
    }

    private void repl(String c){
        String[] a= c.split(" ",2);
        String b = a[1].replace("[]","\n");
        System.out.println(b);

    }



}

