package Server;

public class Music {

    /** Id da música **/
    private int id;
    /** Path para o ficheiro **/
    private String path;
    /** Metadados do ficheiro **/
    private Metadata data;
    /** Número de downloads da música **/
    private int nDownloads;

    /**
     * Construtor da classe Musica sem parametros
     */
    public Music()
    {
        this.id = 0;
        this.path = "N/D";
        this.data = new Metadata();
        this.nDownloads = 0;
    }

    /**
     * Construtor da classe Musica parametrizado
     * @param id            Id da música
     * @param path          Path para o ficheiro
     * @param data          Metadados do ficheiro
     * @param nDownloads    Número de downloads da música
     */
    public Music(int id, String path, Metadata data, int nDownloads)
    {
        this.id = id;
        this.path = path;
        this.data = data;
        this.nDownloads = nDownloads;
    }

    /**
     * Método que devolve o id da musica
     * @return           Id da musica
     */
    public int getID(){return this.id;}

    /**
     * Método que devolve o path da musica
     * @return           Path da musica
     */
    public String getPath(){return this.path;}

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
     * Método que atualiza o path do ficheiro
     * @param path           Novo path do ficheiro
     */
    public void setPath(String path){this.path = path;}

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
        return  this.id == m.getID()  && this.path.equals(m.getPath()) && this.data.equals(m.getMetadata()) &&
                this.nDownloads == m.getnDownloads();
    }

}
