package Server;

import java.util.ArrayList;

public class MsgBuffer
{
    /** Mensagens guardadas no buffer **/
    private ArrayList<String> msg;
    /** Número de mensagens guardadas no buffer **/
    private int n;

    /**
     * Construtor da classe MsgBuffer sem parâmetros
     */
    public MsgBuffer() {
        msg = new ArrayList<>();
        n = 0;
    }

    /**
     * Método que verifica se o buffer está vazio
     * @return           True caso o buffer esteja vazio, false caso contrário
     */
    synchronized public boolean isEmpty() { return msg.size() == n; }

    /**
     * Método que escreve uma nova mensagem no buffer
     * @param message         Mensagem a adicionar ao buffer
     */
    synchronized public void write(String message) {
        msg.add(message);
        notifyAll();
    }

    /**
     * Método que lê a última mensagem escrita no buffer
     * @return           Última mensagem escrita no buffer
     * @throws           InterruptedException
     */
    synchronized public String read() throws InterruptedException {
        while(isEmpty())
            wait();
        String message = msg.get(n);
        n += 1;
        return message;
    }
}
