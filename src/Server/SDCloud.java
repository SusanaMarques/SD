package Server;

import Exceptions.InvalidTagsException;
import Exceptions.MusicDoesntExistException;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SDCloud
{
    /** Utilizadores da cloud **/
    private Map<String, User> users;
    /** Lock do utilizador */
    private Lock userLock;
    /** Biblioteca de musicas da cloud  **/
    private Map<Integer, Music> library;
    /** Lock da biblioteca de músicas da cloud */
    private Lock libraryLock;


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
     * Método que efetua uma pesquisa através das etiquetas passadas como parametro UNFINISHED
     * */
    public void search(String tag) throws InvalidTagsException {
        libraryLock.lock();
        try
        {
            ArrayList<Music> l = (ArrayList<Music>) this.library.values();
            ArrayList<Music> c = new ArrayList<>();
            for(Music m : l)
            {
                Metadata data = m.getMetadata();
                ArrayList<String> tags = data.getTags();
                if(tags.contains(tag))
                    c.add(m);
            }
        } finally { libraryLock.unlock(); }

    }

    /**
     * Método que mostra as músicas disponiveis na biblioteca da cloud
     * */
    public void showLibrary() throws Exceptions.EmptyLibraryException
    {
        libraryLock.lock();
        System.out.println("here");
        if(library.size() < 1)
            throw new  Exceptions.EmptyLibraryException("Biblioteca Vazia!");
        try
        {
            for(Map.Entry<Integer,Music> e : library.entrySet())
            {
                String title = e.getValue().getMetadata().getTitle();
                System.out.print("Id: " + e.getKey() + " " + "Titulo: " + title);
            }
        } finally { libraryLock.unlock(); }

    }
}
