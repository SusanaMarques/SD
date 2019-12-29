package Server;

import java.util.ArrayList;

public class Metadata
{
    /** Ano de lançamento da música **/
    private int year;
    /** Titulo da música **/
    private String title;
    /** Nome do artista da música **/
    private String artist;
    /** Lista de etiquetas da música **/
    private ArrayList<String> tags;

    /**
     * Construtor da classe Server.Metadata sem parametros
     */
    public Metadata()
    {
        this.year = 0;
        this.title = "N/D";
        this.artist = "N/D";
        this.tags = new ArrayList<String>();
    }

    /**
     * Construtor da classe Musica parametrizado
     * @param year       Ano de lançamento da música
     * @param title      Titulo da música
     * @param artist     Nome do artista da música
     * @param tags       Lista de etiquetas da música
     */
    public Metadata(int year, String title, String artist, ArrayList tags)
    {
        this.year = year;
        this.title = title;
        this.artist = artist;
        this.tags = tags;
    }

    /**
     * Método que devolve o ano de lançamento da musica
     * @return           Ano de lançamento da musica
     */
    public int getYear(){return this.year;}

    /**
     * Método que devolve o titulo o da musica
     * @return           Titulo da musica
     */
    public String getTitle(){return this.title;}

    /**
     * Método que devolve o nome do artista da musica
     * @return           Nome do artista da musica
     */
    public String getArtist(){return this.artist;}

    /**
     * Método que devolve a lista de etiquetas da musica
     * @return           Lista de etiquetas da musica
     */
    public ArrayList getTags(){return this.tags;}


    /**
     * Método que atualiza o ano de lançamento da música
     * @param y           Novo ano de lançamento da música
     */
    public void setYear(int y){this.year = y;}

    /**
     * Método que atualiza o título da música
     * @param t           Novo título da música
     */
    public void setTitle(String t){this.title = t;}

    /**
     * Método que atualiza a artista da música
     * @param a           Novo artista da música
     */
    public void setArtist(String a){this.artist = a;}

    /**
     * Método que atualiza a lista de etiquetas da música
     * @param a           Nova lista de etiquetas da música
     */
    public void setTags(ArrayList<String> t) {this.tags = t;}


    /**
     * Função equals da classe Server.Metadata
     * @param o           Objecto
     * @return            Boolean
     */
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null && this.getClass() != o.getClass()) return false;
        Metadata m = (Metadata) o;
        return  this.year == m.getYear() &&
                this.title.equals(m.getTitle()) &&
                this.artist.equals(m.getArtist()); //FALTA A LISTA
    }
}
