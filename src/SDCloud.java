

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class SDCloud implements SDCloudInterface, Serializable {
    private Map<Integer,Music> library;
    //TEST
    private Map<Integer,String > users;

    public SDCloud(){
        library = new HashMap<Integer,Musica>();
        users = new HashMap<>();
    }

    public SDCloud(String relativePath) {
        try {
            ObjectInputStream ooj = new ObjectInputStream(new FileInputStream(relativePath+"lib.txt"));
            this.library = (Map<Integer, Music>) ooj.readObject();
            ooj.close();
            ooj = new ObjectInputStream(new FileInputStream(relativePath+"usr.txt"));
            this.users = (Map<Integer, User>) ooj.readObject();
            ooj.close();


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public SDCloud(SDCloud sd) {
        this.library = sd.getLib();
        this.users = sd.getUsers();
    }



}
