package Server;

import Client.ClientWriter;
import Exceptions.InvalidTagsException;
import Exceptions.MusicDoesntExistException;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SDCloud
{
    /** Número maximo de downloads **/
    private static final int MAXDOWN = 2 ;
    /** Utilizadores da cloud **/
    private Map<String, User> users;
    /** Lock do utilizador */
    private Lock userLock= new ReentrantLock();
    /** Biblioteca de musicas da cloud  **/
    private Map<Integer, Music> library;
    /** Lock da biblioteca de músicas da cloud */
    private Lock libraryLock = new ReentrantLock();
    /** Lock da cloud **/
    private Lock sdCloudlock = new ReentrantLock();
    /** Número de clientes a fazer um download em determinado momento **/
    private int downloading = 0;
    /** Lock do download **/
    private Lock downlock = new ReentrantLock();
    /** Condição do download **/
    private Condition cd = downlock.newCondition();


    /**
     * Construtor da classe SDCloud sem parâmetros
     */
    public SDCloud(){
        this.users = new HashMap<>();
        this.library= new HashMap<>();
    }

    /**
     * Método que faz o lock da cloud
     */
    public void lock(){
        sdCloudlock.lock();
    }

    /**
     * Método que faz o unlock da cloud
     */
    public void unlock(){ sdCloudlock.unlock(); }

    /**
     * Método que efetua o registo um utilizador
     * @param username           Username do utilizador
     * @param password           Password do utilizador
     * @throws Exceptions.UserExistsException
     */
    public void registration(String username, String password) throws  Exceptions.UserExistsException{
        userLock.lock();
        try {
            if (users.containsKey(username))
                throw new Exceptions.UserExistsException("\033[1m\033[48;5;30m> O utilizador já existe!\033[0m\033[0m");
            else
                users.put(username, new User(username,password));
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
            if (u == null || !u.verifyPassword(password)) throw new Exceptions.InvalidRequestException("\033[1m\033[48;5;30m>Credenciais de Login Inválidas!\033[0m\033[0m");
            else u.setNotificacoes(msg);
        } finally { userLock.unlock(); }
        return u;
    }

    /**
     * Método que efetua um download
     * @param id       Id da música a descarregar
     * @return         Música a descarregar
     * @throws Exceptions.MusicDoesntExistException, IOException
     */
    public Music download(int id) throws Exceptions.MusicDoesntExistException, IOException {
        libraryLock.lock();
        Music m;
        try{
            if(!library.containsKey(id)) throw new MusicDoesntExistException("\033[1m\033[48;5;30m >Id Inválido!\033[0m\033[0m");
            else
            {
                m = library.get(id);
                m.lock();
                m.setnDownloads( m.getnDownloads() + 1 );
                m.unlock();
                library.replace(id,m);
            }
        }
        finally {libraryLock.unlock(); }
        return m;
    }

    /**
     * Método que efetua um upload
     * @param year        Ano de lançamento da música
     * @param title       Titulo da música
     * @param artist      Arista da música
     * @param tags        Etiquetas da música
     * @return            Id da música
     */
    public int upload(int year, String title, String artist, String tags){
        String[] ts = tags.split(",");
        ArrayList<String> t = new ArrayList<String>(Arrays.asList(ts));
        Metadata data = new Metadata(year, title, artist, t);
        Music musica =  new Music(data,0);
        libraryLock.lock();
        int id = musica.getID();
        library.put(id, musica);
        libraryLock.unlock();
        return id;
    }

    /**
     * Método que efetua uma pesquisa através das etiquetas passadas como parametro
     */
    public String search(String tag) throws InvalidTagsException {
        libraryLock.lock();
        String t="";
        try
        {
            Collection<Music> l = this.library.values();
            for(Music m : l)
            {
                m.lock();
                Metadata data = m.getMetadata();
                ArrayList<String> tags = data.getTags();
                if(tags.contains(tag)) {
                    t = t + "\033[1m Id: \033[0m"+ m.getID()+
                            "\033[1m Titulo: \033[0m"+data.getTitle()+
                            "\033[1m   Artista: \033[0m"+ data.getArtist()+
                            "\033[1m   Ano: \033[0m"+data.getYear()+
                            "\033[1m Tags: \033[0m"+ data.getTags().toString()+
                            "\033[1m Número de downloads: \033[0m"+ m.getnDownloads()+"[]";
                }
                m.unlock();
            }
        }
        finally { libraryLock.unlock(); }
        if(t == "") throw new Exceptions.InvalidTagsException("\033[1m\033[48;5;30m> A etiqueta inserida não existe! \033[0m\033[0m");
        return ("SEARCH " + t);
    }

    /**
     * Método que mostra as músicas disponiveis na biblioteca da cloud
     */
    public String showLibrary() throws Exceptions.EmptyLibraryException {
        libraryLock.lock();
        String t= "\033[1m\033[48;5;30m" + " Biblioteca Da Cloud" + "\033[0m\033[0m" + "[]";
        try {
            for (Map.Entry<Integer, Music> e : library.entrySet()) {
                e.getValue().lock();
                String title = e.getValue().getMetadata().getTitle();
                t = t + "Id: " + e.getKey() + "    Title: " + title + "[]";
                e.getValue().unlock();
            }
        } finally { libraryLock.unlock(); }
        if (library.size() < 1) throw new Exceptions.EmptyLibraryException("\033[1m\033[48;5;30" +
                "m> Biblioteca Vazia!\033[0m\033[0m");
        return ("LIBRARY " + t);
    }


    /**
     * Método que inicia um download, caso o número máximo de downloads simultaneos não esteja no limite
     */
    public void startingDownload() {
        downlock.lock();
        try {
            while (downloading >= MAXDOWN)
                cd.await();
            downloading++;
        } catch (InterruptedException e) { e.printStackTrace();} finally {downlock.unlock(); }
    }

    /**
     * Método que termina um download
     */
    public void finishedDownloading() {
        downlock.lock();
        downloading--;
        cd.signal();
        downlock.unlock();
    }
}
