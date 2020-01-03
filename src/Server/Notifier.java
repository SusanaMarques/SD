package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Notifier implements Runnable {

    /** Lista de mensagens **/
    List<MsgBuffer> list = new ArrayList<>();
    /** Queue de notificações **/
    List<String> queue = new ArrayList<>();
    /** Lock  da lisya de mensagens **/
    Lock listLock = new ReentrantLock();
    /** Lock da queue de notificações **/
    Lock queueLock = new ReentrantLock();
    /** Condition da queue **/
    Condition newMusic = queueLock.newCondition();

    @Override
    public void run() {
        queueLock.lock();
        while (queue.isEmpty()){
            try { newMusic.await(); } catch (InterruptedException e) {} finally { queueLock.unlock(); }
        }
        queueLock.lock();
        listLock.lock();
        for(String m : queue){
            for(MsgBuffer ms : list){
                ms.lock();
                String notif = "NOTIFY " + m;
                ms.write(notif);
                ms.unlock();
            }
            queue.remove(m);
        }
        queueLock.unlock();
        listLock.unlock();
    }

    /**
     * Método que adiciona uma nova notificação à queue
     * @param m         Notificação a enviar
     */
    public void added(String m){
        queueLock.lock();
        queue.add(m);
        newMusic.signal();
        queueLock.unlock();
    }

    /**
     * Método que adiciona uma nova mensagem ao buffer
     * @param m         Mensagem a adicionar ao buffer
     */
    public void addBuffer (MsgBuffer m){
        listLock.lock();
        list.add(m);
        listLock.unlock();
    }
}
