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
                users.put(username, new User(username, password));
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
            if (u == null || !u.verifyPassword(password)) throw new Exceptions.InvalidRequestException("Dados incorretos");
            else u.setNotificacoes(msg);
        } finally { userLock.unlock(); }
        return u;
    }





}
