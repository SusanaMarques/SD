package Server;


import Client.ClientWriter;
import Exceptions.*;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class ServerReader implements Runnable
{
    /** Utilizador autenticado **/
    private User user;
    /** Socket **/
    private Socket socket;
    /** ServerCloud **/
    private SDCloud sdCloud;
    /** Buffer de mensagens **/
    private MsgBuffer msg;
    /** BufferedReader **/
    private BufferedReader in;

    private String UPpath;

    /**
     * Construtor da classe ServerReader parametrizado
     * @param socket            Socket
     * @param sdCloud           sdCloud
     * @param msg               Buffer
     * @throws                  IOException
     */
    public ServerReader(Socket socket, SDCloud sdCloud, MsgBuffer msg) throws IOException {
        this.user = null;
        this.socket = socket;
        this.sdCloud = sdCloud;
        this.msg = msg;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        String r;
        while ((r = readLine()) != null) {
            try { msg.write(parsing(r)); } catch (IndexOutOfBoundsException e) { msg.write("WRONG"); e.printStackTrace(); }
            catch (InvalidRequestException | InvalidTagsException | EmptyLibraryException | UserExistsException | InterruptedException | MusicDoesntExistException| IOException e) { msg.write(e.getMessage()); }
        }
        // endConnection();
        if (this.user == null) {
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException e) { e.printStackTrace(); }
        }

    }

    /**
     * Método que lê linhas do BufferedReader
     * @return            String lida
     */
    private String readLine() {
        String l = null;
        try { l = in.readLine();
        } catch (IOException e) { System.out.println("\033[1m\033[48;5;79m> Não foi possivel ler novas mensagens! \033[0m\033[0m"); }
        return l;
    }

    /**
     * Método que faz parse das strings lidas pelo BufferedReader
     * @return String
     * @throws Exceptions.UserExistsException
     * @throws InterruptedException
     */
    private  String parsing(String r) throws UserExistsException, InterruptedException, EmptyLibraryException, InvalidRequestException, InvalidTagsException, EmptyLibraryException, IOException, MusicDoesntExistException {
        System.out.println("ENTROU");
        String[] p = r.split(" ", 2);
        switch (p[0].toUpperCase()) {
            case "LOGIN":
                autentication(false);
                return this.login(p[1]);
            case "LOGOUT":
                autentication(true);
                return this.logout();
            case "REGISTER":
                autentication(false);
                return this.registration(p[1]);
            case "DOWNLOAD":
                System.out.println("parsing server reader");
                 String[] ss= this.download(p[1]).split(" ",2);
                 msg.write(ss[1]);
                 downfrag(ss[0]);
                return "DOWNLAST";
            case "UPLOAD":
                System.out.println("BUG?: "+p[1]);
                return this.upload(p[1]);
            case "UPFRAG":
                return this.uploadFrag(p[1]);
            case "SEARCH":
                return this.search(p[1]);
            case "LIBRARY":
                System.out.println("RECEBEU");
                return this.showLibrary();
            default:
                return "ERRO!";
        }
    }



    /**
     * Método que efetua o login
     * @param in      Linha lida do BufferedReader
     * @return        String
     * @throws        Exceptions.InvalidRequestException
     */
    private String login(String in) throws InvalidRequestException {
        String[] p = in.split(" ");
        if (p.length != 2) throw new InvalidRequestException("\033[1m\033[48;5;79m> Credenciais Erradas!\033[0m\033[0m");
        this.user = sdCloud.login(p[0], p[1],msg);
        return "AUTENTICATED";
    }

    /**
     * Método que termina uma sessão
     * @return        String
     * @throws        Exceptions.InvalidRequestException
     */
    private String logout() {
        this.user = null;
        return "LOGOUT";
    }

    /**
     * Método que efetua o registo de um utilizador
     * @param in      Linha lida do BufferedReader
     * @return        String
     * @throws        InvalidRequestException
     * @throws        Exceptions.UserExistsException
     */
    private String registration(String in) throws InvalidRequestException, UserExistsException {
        String[] p = in.split(" ");
        if (p.length != 2) throw new InvalidRequestException("\033[1m\033[48;5;79m> Credenciais Erradas!\033[0m\033[0m");
        sdCloud.registration(p[0], p[1]);
        return "REGISTER";
    }

    /**
     * Método que verifica a autenticação de um utilizador
     * @param status   Estado da sessão
     * @throws         InvalidRequestException
     */
    private void autentication(Boolean status) throws InvalidRequestException {
        if (status && user == null)
            throw new InvalidRequestException("\033[1m\033[48;5;79m> Acesso negado!\033[0m\033[0m");
        if (!status && user != null)
            throw new InvalidRequestException("\033[1m\033[48;5;79m> Já existe um utilizador autenticado!\033[0m\033[0m");
    }

    /**
     * Método que efetua um download
     * @param in       Linha lida do BufferedReader
     * @return         String
     */
    private String download(String in) throws MusicDoesntExistException, IOException {
        int id = 0;
        try { id = Integer.parseInt(in); } catch (NumberFormatException e){System.out.println("\033[1m\033[48;5;79m>Id Inválido!\033[0m\033[0m");}
        System.out.println("@serverwriter download sd locking");
        sdCloud.lock();
        System.out.println("@serverwriter download sd locked");
        Music m = sdCloud.download(id);
        m.lock();
        System.out.println("@serverwriter download lim locked");
        sdCloud.unlock();
        sdCloud.startingDownload();
        Metadata meta = m.getMetadata();
        String path = "Biblioteca/" +id+".mp3";


        String send =path+" DOWNLOAD "+meta.getYear()+" "+meta.getTitle()+" "+meta.getArtist()+" ";
        for(String tag: meta.getTags())
            send+=tag+",";

        m.unlock();

      //  send+="\n";
        return send;
    }
    public void downfrag(String path) throws IOException {
        File tmp= new File(path);
        int lastfrag = ((int) tmp.length()/ (SDNetwork.MAXSIZE) ) + ((int)tmp.length()%(SDNetwork.MAXSIZE) == 0 ? 0 : 1 );
        for(int i=0; i< lastfrag;i++) {
            String frag = SDNetwork.fragger(path,i,lastfrag,"DOWNLOAD");
            msg.write(frag);
        }
    }

    /**
     * Método que efetua um upload
     * @param payload       Linha lida do BufferedReader
     * @return              String
     */
    private String upload(String payload) throws IOException {
        String[] s = payload.split(" ",4);
        String title = (s[1].isEmpty() ? "" + new Random().nextInt() : s[1]);
        int ano=0;
        try{ano = Integer.parseInt(s[0]);} catch (Exception e){}
        String artist = s[2];
        String tags = s[3];
        int id = sdCloud.upload(ano,title,artist,tags);
        UPpath = "Biblioteca/"+id+".mp3";

        return "UPLOAD "+title+" "+artist;
    }
    private String uploadFrag(String s) throws IOException {
        return "UPLFRAGNO "+ SDNetwork.unfragger(s, UPpath);
    }



    /**
     * Método que procura músicas consoante as etiquetas recebidas
     * @param in       Linha lida do BufferedReader
     * @return         String
     */
    private String search(String in) throws Exceptions.InvalidTagsException {
        String[] p = in.split(" ");
        if (p.length > 1)
             throw new Exceptions.InvalidTagsException("\033[1m\033[48;5;79m> Etiqueta inválida!\033[0m\033[0m");
        return sdCloud.search(p[0]);
    }

    /**
     * Método que mostra a biblioteca de músicas da cloud
     * @return         String
     */
    private String showLibrary() throws EmptyLibraryException, InterruptedException { return sdCloud.showLibrary(); }

}
