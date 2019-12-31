package Server;


import Exceptions.InvalidRequestException;
import Exceptions.InvalidTagsException;
import Exceptions.UserExistsException;

import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.*;

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
            try { msg.write(parsing(r)); } catch (IndexOutOfBoundsException e) { msg.write("WRONG"); }
            catch (InvalidRequestException | InvalidTagsException | UserExistsException | InterruptedException | IOException e) { msg.write(e.getMessage()); }
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
        } catch (IOException e) { System.out.println("Não foi possível ler novas mensagens"); }
        return l;
    }

    /**
     * Método que faz parse das strings lidas pelo BufferedReader
     * @return String
     * @throws Exceptions.UserExistsException
     * @throws InterruptedException
     */
    private String parsing(String r) throws UserExistsException, InterruptedException, InvalidRequestException, InvalidTagsException, IOException {
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
                //return this.download(p[1]);
            case "UPLOAD":
                String[] s = p[1].split(" ",5);
               // String byteReady = s[4].replace(" [REGEXN] ", "\n").replace(" [REGEXR] ", "\r");
                byte[] ba = Base64.decodeBase64(s[4]);
                String path = "Biblioteca/"+s[1];
                Path pt = Paths.get(path);
                Files.createDirectories(pt.getParent());
                Files.write(pt, ba, StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.CREATE_NEW);
                //System.out.println("ServerREader path :"+byteReady.length());
                //System.out.println("ServerREader balen : "+ba.length);
                return "WEE";
            case "SEARCH":
                return this.search(p[1]);
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
        if (p.length != 2) throw new InvalidRequestException("Credenciais Erradas!");
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
        return "ENDSESSION";
    }

    /**
     * Método que efetua o registo de um utilizador
     * @param in      Linha lida do BufferedReader
     * @return        String
     * @throws        InvalidRequestException
     * @throws        Exceptions.UserExistsException
     */
    private String registration(String in) throws InvalidRequestException, UserExistsException {
        System.out.print("3");
        String[] p = in.split(" ");
        System.out.print("4");
        if (p.length != 2) throw new InvalidRequestException("Credenciais Erradas!");
        System.out.print("1");
        sdCloud.registration(p[0], p[1]);
        System.out.print("2");
        return "REGISTER";
    }

    /**
     * Método que verifica a autenticação de um utilizador
     * @param status   Estado da sessão
     * @throws         InvalidRequestException
     */
    private void autentication(Boolean status) throws InvalidRequestException {
        if (status && user == null)
            throw new InvalidRequestException("Acesso negado");
        if (!status && user != null)
            throw new InvalidRequestException("Já existe um utilizador autenticado");
    }

    /**
     * Método que efetua um download
     * @param in       Linha lida do BufferedReader
     * @return         String

    private String download(String in) {
        int id = Integer.parseInt(in);
        ....
        sdCloud.download();
        return "DOWNLOAD";
    }  */

    /**
     * Método que efetua um upload
     * @param in       Linha lida do BufferedReader
     * @return         String
     */
    private String upload(String in) {
        String[] p = in.split(" ");
        if (p.length < 6) //maybe mandar exception tem de receber os metadados todos e pelo menos 1 etiqueta
            sdCloud.upload(p[0], Integer.parseInt(p[1]), p[2], p[3], p[4]);
        return "UPLOAD";
    }

    /**
     * Método que procura músicas consoante as etiquetas recebidas
     * @param in       Linha lida do BufferedReader
     * @return         String
     */
    private String search(String in) throws Exceptions.InvalidTagsException {
        String[] p = in.split(" ");
        if (p.length > 1)
             throw new Exceptions.InvalidTagsException("Etiqueta Inválida");
        sdCloud.search(p[0]);
        return "SEARCH";
    }

}
