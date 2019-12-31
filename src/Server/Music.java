package Server;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Music {
    /**ID livre **/
    private static int ID = 1;
    /** Id da música **/
    private int id;
    /** Metadados do ficheiro **/
    private Metadata data;
    /** Número de downloads da música **/
    private int nDownloads;

    private Lock l;

    /**
     * Construtor da classe Musica sem parametros
     */
    public Music()
    {
        this.id = 0;
        this.data = new Metadata();
        this.nDownloads = 0;
    }

    /**
     * Construtor da classe Musica parametrizado

     * @param data          Metadados do ficheiro
     * @param nDownloads    Número de downloads da música
     */
    public Music(Metadata data, int nDownloads)
    {
        this.id = ID++;
        this.data = data;
        this.nDownloads = nDownloads;
        this.l= new ReentrantLock();
    }

    /**
     * Método que devolve o id da musica
     * @return           Id da musica
     */
    public int getID(){return this.id;}

    /**
     * Método que devolve os metadados da musica
     * @return           Metadados da musica
     */
    public Metadata getMetadata(){return this.data;}

    /**
     * Método que devolve o número de downloads da musica
     * @return           Número de downloads da musica
     */
    public int getnDownloads(){return this.nDownloads;}

    /**
     * Método que atualiza o id da música
     * @param id           Novo id da música
     */
    public void setID(int id){this.id = id;}

    /**
     * Método que atualiza os metadados da música
     * @param data           Novos metadados da música
     */
    public void setMetadata(Metadata data){this.data = data;}

    /**
     * Método que atualiza o número de downloads da música
     * @param nd           Novo número de downloads da música
     */
    public void setnDownloads(int nd){this.nDownloads = nd;}

    /**
     * Função equals da classe Server.Music
     * @param o           Objecto
     * @return            Boolean
     */
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null && this.getClass() != o.getClass()) return false;
        Music m = (Music) o;
        return  this.data.equals(m.getMetadata()) &&
                this.nDownloads == m.getnDownloads();
    }

    public void lock(){l.lock();}

    public void unlock(){l.unlock();}

}
