package Client;
import Server.ServerWriter;
import com.google.common.io.Files;
import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.net.Socket;


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
        try {
            while ((op = menu.op()) != -1)
                parsing(op);
        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * Método que faz o parse da opção do menu recebida
     *@param op                Opção recebida
     */
    private void parsing(Integer op) throws IOException{
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
        String username = menu.readString("Username: ");
        String password = menu.readString("Password: ");
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
        String username = menu.readString("Username: ");
        String password = menu.readString("Password: ");
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

        System.out.println("@clientwriter download");
        out.write(q);
        out.newLine();
        out.flush();

    }

    /**
     * Método que indica ao servidor que o utilizador pretende fazer upload de um ficheiro e os metadados do ficheiros
     * @throws IOException
     */
    private void upload() throws IOException{
        String path = menu.readString("Path:");
        System.out.println("> Inserir metadados");
        String y = menu.readString("Ano:");
        String t = menu.readString("Título:");
        String a = menu.readString("Artista:");
        System.out.println("> Inserir as etiquetas separadas por virgulas e sem espaços");
        String e = menu.readString("Etiquetas:");
        String q = String.join(" ", "UPLOAD", y, t, a, e,packager(path));

        String frags = fragger(packager(path));
        out.write(frags);
        out.write(q);
        out.newLine();
        out.flush();
    }

    private String fragger(String pack) {
        String ret="";
        int allfrags =  (pack.length()/ServerWriter.MAXSIZE) +
                (((pack.length()%ServerWriter.MAXSIZE))==0 ? 0 : 1);

        if(pack.length()> ServerWriter.MAXSIZE) {
            for(int i=0,fragno=0;i<pack.length();fragno++) {
                ret+="UPLFRAG " + fragno + " " + allfrags +" ";
                ret += pack.substring(i,i+=ServerWriter.MAXSIZE);
                ret+="\n";
            }
        }
        return ret;
    }

    /** Método de codificação do ficheiro para transmitir
     * @param path Path do fString artist = s[2];icheiro a enviar
    **/
    public static String packager(String path) throws IOException {
        File f = new File(path);
        byte[] ba = Files.toByteArray(f);
        String ret = Base64.encodeBase64String(ba);
        return ret;
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
