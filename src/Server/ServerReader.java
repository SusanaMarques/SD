package Server;


import Exceptions.InvalidRequestException;
import Exceptions.UserExistsException;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.IOException;

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
    private String parse(String r) throws UserExistsException, InterruptedException, InvalidRequestException {
        String[] p = r.split(" ", 2);
        switch (p[0].toUpperCase()) {
            case "LOGIN":
                autentication(false);
                return this.login(p[1]);
            case "LOGOUT":
                autentication(true);
                return this.logout();
            case "REGISTAR":
                autentication(false);
                return this.registration(p[1]);
            default:
                return "ERRO";
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
        if (p.length != 2) throw new InvalidRequestException("Dados errados!");
        this.user = sdCloud.login(p[0], p[1],msg);
        return "AUTENTICADO";
    }

    /**
     * Método que termina uma sessão
     * @return        String
     * @throws        Exceptions.InvalidRequestException
     */
    private String logout() {
        this.user = null;
        return "SESSAOTERMINADA";
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
        if (p.length != 2) throw new InvalidRequestException("Dados errados!");
        sdCloud.registration(p[0], p[1]);
        return "REGISTADO";
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
            throw new InvalidRequestException("Já existe um utilizador autentidaco");
    }

}
