public class Metadata
{
    private int year;
    private String title;
    private String artist;
    private int nDownloads;
    //private list<String> tags;

    public Metadata()
    {
        this.year = 0;
        this.title = "N/D";
        this.artist = "N/D";
        this.nDownloads = 0;
        //list
    }

    public Metadata(int year, String title, String artist, int nDownloads)
    {
        this.year = year;
        this.title = title;
        this.artist = artist;
        this.nDownloads = nDownloads;
    }

    public Metadata(Metadata m)
    {
        this.year = m.getYear();
        this.title = m.getTitle();
        this.artist = m.getArtist();
        this.nDownloads = m.getnDownloads();
    }


    public int getYear(){return this.year;}
    public String getTitle(){return this.title;}
    public String getArtist(){return this.artist;}
    public int getnDownloads(){return this.nDownloads;}
    //LIST

    public void setYear(int y){this.year = y;}
    public void setTitle(String t){this.title = t;}
    public void setArtist(String a){this.artist = a;}
    public void setnDownloads(int nd){this.nDownloads = nd;}
    //LIST

    public Metadata clone() {return new Metadata(this);}

    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null && this.getClass() != o.getClass()) return false;
        Metadata m = (Metadata) o;
        return  this.year == m.getYear() &&
                this.title.equals(m.getTitle()) &&
                this.artist.equals(m.getArtist()) &&
                this.nDownloads == m.getnDownloads(); //FALTA A LISTA
    }
}
