public class Music {

    private int id;
    private String path;
    private Metadata data;

    public Music()
    {
        this.id = 0;
        this.path = "N/D";
        this.data = new Metadata();
    }

    public Music(int id, String path, Metadata data)
    {
        this.id = id;
        this.path = path;
        this.data = data;
    }

    public Music(Music m)
    {
        this.id = m.getID();
        this.path = m.getPath();
        this.data = m.getMetadata();
    }

    public int getID(){return this.id;}
    public String getPath(){return this.path;}
    public Metadata getMetadata(){return this.data;}


    public void setID(int id){this.id = id;}
    public void setPath(String path){this.path = path;}
    public void setMetadata(Metadata data){this.data = data;}

    public Music clone() {return new Music(this);}

    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null && this.getClass() != o.getClass()) return false;
        Music m = (Music) o;
        return  this.id == m.getID()  && this.path.equals(m.getPath()) && this.data.equals(m.getMetadata());
    }

}
