package Client;
import Exceptions.PathIncorrectException;
import Server.SDNetwork;
import Server.ServerWriter;
import com.google.common.io.Files;
import com.googlecode.mp4parser.util.Path;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.math3.ml.neuralnet.Network;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;


public class ClientWriter implements Runnable
{
    /** Menu do cliente **/
    private Menu menu;
    /** Socket **/
    private Socket socket;
    /** BufferedReader **/
    private BufferedWriter out;

    /**
     * Construtor da classe ClientWriter parametrizado
     *
     * @param m         Menu de opções
     * @param s         Socket
     * @throws          IOException
     */
    public ClientWriter(Menu m, Socket s) throws IOException {
        this.menu = m;
        this.socket = s;
        this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    }

    /**
     * Método que executa a thread ClientWriter
     */
    @Override
    public void run() {
        int op;
            while ((op = menu.op()) != -1)
                try {
                parsing(op);
        } catch (IOException | PathIncorrectException e) { System.out.println(e.getMessage()); menu.showMenu(); }
    }

    /**
     * Método que faz o parse da opção do menu recebida
     *@param op                Opção recebida
     */
    private void parsing(Integer op) throws IOException, PathIncorrectException {
        switch (menu.getOpt()) {
            case 0:
                if (op == 0)
                    System.exit(0);
                if (op == 1)
                    login();
                 if (op == 2)
                    registration();
                 break;
            case 1:
                if(op == 0)
                    logout();
                 if(op == 1){
                    upload();
                    }
                  if(op == 2){
                    download();
                    }
                  if(op == 3){
                    search();
                    }
                 if(op == 4){
                    showLibrary();
                    }
                break;
        }
    }

    /**
     * Método que lê os valores de modo a efetuar o login
     * @throws IOException
     */
    private void login() throws IOException{
        String username = menu.readString("\033[1m\033[48;5;79m> Username:\033[0m\033[0m");
        String password = menu.readString("\033[1m\033[48;5;79m> Password:\033[0m\033[0m");
        String q = String.join(" ", "LOGIN", username, password);
        out.write(q);
        out.newLine();
        out.flush();

    }

    /**
     * Método que indica ao servidor que o utilizador pretende  terminar sessão
     * @throws IOException
     */
    private void logout() throws IOException {
        out.write("LOGOUT");
        out.newLine();
        out.flush();

    }

    /**
     * Método que lê os valores de modo a efetuar o registo de um utilizador
     * @throws IOException
     */
    private void registration() throws IOException{
        String username = menu.readString("\033[1m\033[48;5;79m> Username:\033[0m\033[0m");
        String password = menu.readString("\033[1m\033[48;5;79m> Password:\033[0m\033[0m");
        String q = String.join(" ", "REGISTER", username, password);
        out.write(q);
        out.newLine();
        out.flush();
    }

    /**
     * Método que indica ao servidor que o utilizador pretende fazer download de um ficheiro
     * @throws IOException
     */
    private void download() throws IOException{
        String id = menu.readString("Id: ");
        String q = String.join(" ", "DOWNLOAD", id);

        out.write(q);
        out.newLine();
        out.flush();

    }

    /**
     * Método que indica ao servidor que o utilizador pretende fazer upload de um ficheiro e os metadados do ficheiros
     * @throws IOException
     */
    private void upload() throws IOException, PathIncorrectException {
        String path = menu.readString("Path:");
        System.out.println("\033[1m\033[48;5;79m> Inserir Metadados\033[0m\033[0m");
        String y = menu.readString("Ano:");
        String t = menu.readString("Título:");
        String a = menu.readString("Artista:");
        System.out.println("\033[1m\033[48;5;79m> Inserir as etiquetas separadas por virgulas e sem espaços\033[0m\033[0m");
        String e = menu.readString("Etiquetas:");
        String q = String.join(" ", "UPLOAD", y, t, a, e);
        File tmp= new File(path);
        if(tmp.length()==0) throw new Exceptions.PathIncorrectException("\033[1m\033[48;5;79m>Path Incorreto!\033[0m\033[0m");
        out.write(q);
        out.newLine();
        int lastfrag = ((int) tmp.length()/ (SDNetwork.MAXSIZE) ) + ((int)tmp.length()%(SDNetwork.MAXSIZE) == 0 ? 0 : 1 );
        for(int i=0; i< lastfrag;i++) {
            String frag = SDNetwork.fragger(path,i,lastfrag,"UPLOAD");
            out.write(frag);
            out.newLine();
            out.flush();
        }

    }


    /**
     * Método que indica ao servidor que o utilizador pretende procurar uma música com determinado tag
     * @throws IOException
     */
    private void search() throws IOException{
        String tag = menu.readString("Etiqueta a pesquisar: ");
        String q = String.join(" ", "SEARCH", tag);
        out.write(q);
        out.newLine();
        out.flush();
    }

    /**
     * Método que indica ao servidor que o utilizador pretende aceder à biblioteca
     * @throws IOException
     */
    private void showLibrary() throws IOException {
        out.write("LIBRARY");
        out.newLine();
        out.flush();
    }
}
