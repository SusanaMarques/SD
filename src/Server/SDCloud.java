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
    /** **/
    private int downloading = 0;
    /** Lock do download **/
    private Lock downlock = new ReentrantLock();
    /** **/
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
                throw new Exceptions.UserExistsException("O utilizador já existe");
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
            if (u == null || !u.verifyPassword(password)) throw new Exceptions.InvalidRequestException("Credenciais de Login Inválidas!");
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
            m = library.get(id);
            m.lock();
            m.setnDownloads( m.getnDownloads()+1 );
            m.unlock();
            library.replace(id,m);
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
     * Método que efetua uma pesquisa através das etiquetas passadas como parametro UNFINISHED
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
                    //ret.append("         ").append("Titulo: ").append(data.getTitle()).append(" Artista: ").append(data.getArtist()).append(" Ano: ").append(data.getYear()).append("\n");
                    t=t+"Id: "+ m.getID()+" Titulo: "+data.getTitle()+"   Artista: "+ data.getArtist()+"   Ano: "+data.getYear()+ " Tags: "+ data.getTags().toString()+ " Número de downloads: "+ m.getnDownloads()+"[]";
                }
                m.unlock();
            }
        }
        finally { libraryLock.unlock(); }
        if(t == "") throw new Exceptions.InvalidTagsException("Tag não existe!");

        return ("SEARCH " + t);
    }

    /**
     * Método que mostra as músicas disponiveis na biblioteca da cloud
     */
    public String showLibrary() throws Exceptions.EmptyLibraryException {
        libraryLock.lock();
        String t="";
        try {

            for (Map.Entry<Integer, Music> e : library.entrySet()) {
                e.getValue().lock();
                String title = e.getValue().getMetadata().getTitle();

                t = t + "Id: " + e.getKey() + "    Title: " + title + "[]";
                e.getValue().unlock();
            }
        } finally { libraryLock.unlock(); }
        if (library.size() < 1) throw new Exceptions.EmptyLibraryException("Biblioteca Vazia!");
        return ("LIBRARY " + t);
    }


    /**
     * Método que ....
     */
    public void startingDownload() {
        downlock.lock();
        try {
        while (downloading >= MAXDOWN) {
            cd.await();
        }
            downloading++;
        } catch (InterruptedException e) { e.printStackTrace();}
        finally {downlock.unlock(); }

    }

    /**
     * Método que ....
     */
    public void finishedDownloading() {
        downlock.lock();
        downloading--;
        cd.signal();
        downlock.unlock();
    }
}
