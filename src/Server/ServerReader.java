package Server;


import Client.ClientWriter;
import Exceptions.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.endpoint.Server;

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
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
        } catch (IOException e) { System.out.println("Não foi possível ler novas mensagens"); }
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
                return this.download(p[1]);
            case "UPLOAD":
                return this.upload(p[1]);
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
        String[] p = in.split(" ");
        if (p.length != 2) throw new InvalidRequestException("Credenciais Erradas!");
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
            throw new InvalidRequestException("Acesso negado");
        if (!status && user != null)
            throw new InvalidRequestException("Já existe um utilizador autenticado");
    }

    /**
     * Método que efetua um download
     * @param in       Linha lida do BufferedReader
     * @return         String
     */
    private String download(String in) throws MusicDoesntExistException, IOException {
        int id = Integer.parseInt(in);
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
        String b64 = ClientWriter.packager(path);
        String send ="DOWNLOAD "+meta.getYear()+" "+meta.getTitle()+" "+meta.getArtist()+" ";
        m.unlock();

        for(String tag: meta.getTags())
            send+=tag+",";

        send+=" "+b64+"\n";
        return send;
    }

    /**
     * Método que efetua um upload
     * @param payload       Linha lida do BufferedReader
     * @return              String
     */
    private String upload(String payload) throws IOException {
        String[] s = payload.split(" ",5);
        String title = (s[1].isEmpty() ? "" + new Random().nextInt() : s[1]);
        int ano=0;
        try{ano = Integer.parseInt(s[0]);} catch (Exception e){}
        String artist = s[2];
        String tags = s[3];
        int id = sdCloud.upload(ano,title,artist,tags);
        String path = "Biblioteca/"+id+".mp3";
        unpackager(path,s[4]);

        return "UPLOAD";
    }

    /**
     * Método que ....
     * @param path
     * @param data64
     * @throws IOException
     */
    public static void unpackager(String path, String data64) throws IOException {
        System.out.println("unpackager1");
        byte[] ba = Base64.decodeBase64(data64);
        System.out.println("unpackager1");
        Path pt = Paths.get(path);
        System.out.println("SERver reader unpackagerPt: " + pt.toString());
        Files.createDirectories(pt.getParent());
        Files.write(pt, ba, StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.CREATE_NEW);
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
        return sdCloud.search(p[0]);
    }

    /**
     * Método que mostra a biblioteca de músicas da cloud
     * @return         String
     */
    private String showLibrary() throws EmptyLibraryException, InterruptedException { return sdCloud.showLibrary(); }

}
