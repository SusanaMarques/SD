package Server;

import java.io.*;
import java.nio.file.Paths;
import java.util.Base64;

public class SDNetwork {

    public static final int MAXSIZE = 1000;


    public static String fragger(String path,int fragindex,int lastfrag,String command) throws IOException {
        String op;
        if(command.equals("UPLOAD")) op = "UPFRAG"; else op= "DOWNFRAG";
        File f = new File(Paths.get(path).toString());
        RandomAccessFile raf = new RandomAccessFile(new File(path), "r");
        byte[] ba = new byte[MAXSIZE];
        if(fragindex==lastfrag) ba = new byte[(int)raf.length()-(fragindex-1)*MAXSIZE];
        raf.seek(fragindex * MAXSIZE);
        raf.read(ba);
        raf.close();
        String ret = op + " " + fragindex+" " + lastfrag+" ";
        return ret+encode(ba)+"\n";
    }

    public static String unfragger(String payload, String path) throws IOException {
        byte[] ba;
        String[] ss = payload.split(" ", 3);
        int fragindex = Integer.parseInt(ss[0]);
        ba = decode(ss[2]);
        System.out.println("unfragger path: "+ path);
        File f = new File(Paths.get(path).toString());
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        raf.seek(fragindex * MAXSIZE);
        raf.write(ba);
        raf.close();
        if(fragindex == Integer.parseInt(ss[1])) return "LAST";
        else return "MORE";
    }







/*
        int allfrags =  (pack.length()/ ServerWriter.MAXSIZE) +
                (((pack.length()%ServerWriter.MAXSIZE))==0 ? 0 : 1);

        if(pack.length()> ServerWriter.MAXSIZE) {
            for(int i=0,fragno=0;i<pack.length();fragno++) {
                ret+=op+ fragno + " " + allfrags +" ";
                ret += pack.substring(i,i+=ServerWriter.MAXSIZE);
                ret+="\n";
            }
        }
        return ret;
        */



    private static String encode(byte[] ba) {
        return Base64.getEncoder().encodeToString(ba);
    }

    private static byte[] decode(String b64) {
        return Base64.getDecoder().decode(b64);
    }


}
