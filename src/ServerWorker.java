import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
/** TODO **/
public class ServerWorker implements Runnable {
    Socket sock;
    InterfaceBanco bn;
    public ServerWorker(Socket s, InterfaceBanco b){
        sock = s;
        bn = b;
    }
    @Override
    public void run()  { try{
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        PrintWriter out = new PrintWriter(sock.getOutputStream());
        String inString;

        while ((inString = in.readLine())!=null) {
            StringTokenizer strtok = new StringTokenizer(inString);
            String token;
            String ret = "";
            token=strtok.nextToken();
            switch (token){
                case "criarC" :{
                    int i =0;
                    double d=Double.parseDouble(strtok.nextToken());
                    i=bn.criarConta(d);
                    ret= "Ok " + i;
                    break;}

                case "cons":  try{
                    int i =Integer.parseInt(strtok.nextToken());
                    ret= "Ok " + bn.consultar(i);
                } catch (ContaInvalida e){
                    ret ="Err cI "+e.getMessage();
                }
                finally {
                    break;
                }
            }
            out.println(ret);
            out.flush();


        }

        sock.shutdownOutput();
        sock.shutdownInput();
        sock.close();

    }catch(IOException e){}}
}
