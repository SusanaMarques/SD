package Server;

import java.io.*;
import java.nio.file.Paths;
import java.util.Base64;

public class SDNetwork {

    /** Tamanho máximo de tranferencia de um ficheiro **/
    public static final int MAXSIZE = 1000;


    /**
     * Método que fragmenta um ficheiro para que possa ser transmitido
     * @param path              Path do ficheiro a carregar
     * @param fragindex         Index do fragmento da transferencia
     * @param lastfrag          Ultimo fragmento da transferencia
     * @param command           Comando recebido -> Upload ou Download
     * @return                  Fragmento encoded
     * @throws IOException
     */
    public static String fragger(String path,int fragindex,int lastfrag,String command) throws IOException {
        String op;
        if(command.equals("UPLOAD"))
            op = "UPFRAG";
        else
            op= "DOWNFRAG";
        File f = new File(Paths.get(path).toString());
        RandomAccessFile raf = new RandomAccessFile(new File(path), "r");
        byte[] ba = new byte[MAXSIZE];
        if(fragindex==lastfrag)
            ba = new byte[(int)raf.length()-(fragindex-1)*MAXSIZE];
        raf.seek(fragindex * MAXSIZE);
        raf.read(ba);
        raf.close();
        String ret = op + " " + fragindex+" " + lastfrag+" ";
        return ret+encode(ba);
    }

    /**
     * Método que junta os fragmentos da transmissão de um ficheiro
     * @param payload      Fragmento transmitido
     * @param path         Path do ficheiro
     * @return
     * @throws IOException
     */
    public static String unfragger(String payload, String path) throws IOException {
        byte[] ba;
        String[] ss = payload.split(" ", 3);
        int fragindex = Integer.parseInt(ss[0]);
        ba = decode(ss[2]);
        File f = new File(Paths.get(path).toString());
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        raf.seek(fragindex * MAXSIZE);
        raf.write(ba);
        raf.close();
        if(fragindex == Integer.parseInt(ss[1]))
            return "LAST";
        else
            return "MORE";
    }

    /** Método que faz o encode do ficheiro de bytes para uma string **/
    private static String encode(byte[] ba) { return Base64.getEncoder().encodeToString(ba); }
    /** Método que faz o decode do ficheiro em string para um array de bytes **/
    private static byte[] decode(String b64) { return Base64.getDecoder().decode(b64); }


}
