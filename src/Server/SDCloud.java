package Server;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SDCloud
{
    /** Utilizadores da cloud **/
    private Map<String, User> users;
    /** Lock do utilizador */
    private Lock userLock;
    /** Musicas da cloud  **/
    private Map<Integer, Music> library;


    /**
     * Construtor da classe Server.SDCloud sem parâmetros
     */
    public SDCloud(){
        this.users = new HashMap<>();
        this.userLock = new ReentrantLock();
    }

    /**
     * Método que efetua o registo um utilizador
     * @param username           Username do utilizador
     * @param password           Password do utilizador
     *
     * @throws Exceptions.UserExistsException
     */
    public void registration(String username, String password) throws  Exceptions.UserExistsException{
        userLock.lock();
        try {
            System.out.println("1");
            if (users.containsKey(username))
                throw new Exceptions.UserExistsException("O utilizador já existe");
            else
                System.out.println("2");
                User u = new User(username,password);
                users.put(username, u);
                System.out.println("3");
        } finally { userLock.unlock(); }
    }

    /**
     * Método que efetua o inicio de sessão
     * @param username      Username inserido
     * @param password      Password inserida
     * @param msg           Buffer de mensagens
     * @return              Utilizador autenticado
     * @throws              Exceptions.InvalidRequestException
     */
    public User login(String username, String password, MsgBuffer msg) throws Exceptions.InvalidRequestException{
        User u;
        userLock.lock();
        try {
            u = users.get(username);
            if (u == null || !u.verifyPassword(password)) throw new Exceptions.InvalidRequestException("Credenciais de Login Inválidas!");
            else u.setNotificacoes(msg);
        } finally { userLock.unlock(); }
        return u;
    }

    /**
     * Método que efetua um download
     * */
    public void download(int id) throws Exceptions.MusicDoesntExistException {
    }

    /**
     * Método que efetua um upload
     * */
    public void upload(String path, int year, String title, String artist, String tags){
        //IR BUSCAR O FICHEIRO PELO PATH

        String[] ts = tags.split(",");
        ArrayList<String> t = new ArrayList<String>(Arrays.asList(ts));
        Metadata data = new Metadata(year, title, artist, t);
        // gerar um id novo para a musica
        //Music m = new Music(id, path, data, 1);
        //library.put(id,m);

    }

    /**
     * Método que efetua uma pesquisa através das etiquetas passadas como parametro
     * */
    public void search(){}
}
