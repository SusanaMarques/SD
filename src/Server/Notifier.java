package Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Notifier implements Runnable {
  List<MsgBuffer> list = new ArrayList<>();
  List<String> queue = new ArrayList<>();
  Lock listLock = new ReentrantLock();
  Lock queueLock = new ReentrantLock();
  Condition newMusic = queueLock.newCondition();

    @Override
    public void run() {
         queueLock.lock();
        while (queue.isEmpty()){
            try {
                newMusic.await();
            } catch (InterruptedException e) {} finally {
                queueLock.unlock();
            }
        }
                queueLock.lock();
                listLock.lock();
                for(String m : queue){

                    for(MsgBuffer ms : list){
                        ms.lock();
                        String notif = "NOTIFY " +m;
                        ms.write(notif);
                        System.out.println("1Notificacao");

                        ms.unlock();
                    }
                   queue.remove(m);
                }
                queueLock.unlock();
                listLock.unlock();

    }

    public void added(String m){
        queueLock.lock();
        queue.add(m);
        System.out.println("notifier: "+m);
        newMusic.signal();
        queueLock.unlock();
    }
    public void addBuffer (MsgBuffer m){
        listLock.lock();
        list.add(m);
        listLock.unlock();
    }


}
