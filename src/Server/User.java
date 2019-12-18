package Server;

public class User
{
    private String username;
    private String password;

    /**
     * Construtor da classe Server.Server.User com parâmetros
     * @param username     Username do utilizador
     * @param password     Password do utilizador
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * método que devolve o username do utilizador
     * @return           username do utilizador
     */
    public String getUsername() { return this.username; }

    /**
     * Método que devolve a password do utilizador
     * @return           password do utilizador
     */
    public String getPassword() { return this.password; }

    /**
     * Função equals da classe Server.Server.User
     * @param o           objecto
     * @return            boolean
     */
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || (this.getClass() != o.getClass())) return false;
        User u = (User) o;
        return username.equals(u.username);
    }
}
