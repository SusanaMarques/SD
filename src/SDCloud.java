import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class SDCloud
{
    /** Utilizadores da cloud **/
    private Map<String,Server.User> users;
    /** Lock do utilizador */
    private Lock userLock;


    /**
     * Construtor da classe SDCloud sem parâmetros
     */
    public SDCloud(){
        this.users = new HashMap<>();
        this.userLock = new ReentrantLock();
    }

    /**
     * Método que efetua o registo um utilizador
     * @param email              Email do utilizador
     * @param password           Password do utilizador
     *
     * @throws Exceptions.UserExistsException
     */
    public void registration(String email, String password) throws  Exceptions.UserExistsException{
        userLock.lock();
        try {
            if (users.containsKey(email))
                throw new Exceptions.UserExistsException("O utilizador já existe");
            else
                users.put(email, new  Server.User(email, password));
        } finally { userLock.unlock(); }
    }

    /**
     * Método que efetua o inicio de sessão
     * @param email         Email inserido
     * @param password      Password inserida
     * @param msg           Buffer de mensagens
     * @return              Utilizador autenticado
     * @throws              Exceptions.InvalidRequestException
     */
    public Server.User iniciarSessao(String email, String password, Server.MsgBuffer msg) throws Exceptions.InvalidRequestException{
        Server.User u;
        userLock.lock();
        try {
            u = users.get(email);
            if (u == null || !u.verifyPassword(password)) throw new Exceptions.InvalidRequestException("Dados incorretos");
            else u.setNotificacoes(msg);
        } finally { userLock.unlock(); }
        return u;
    }





}
