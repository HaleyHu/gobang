package org.hbin.game.gobang.component;

import org.hbin.game.gobang.client.GobangClient;

public class TimerThread extends Thread {
    private GobangClient client;
    private int myTime;
    private int opTime;
    
    public TimerThread(GobangClient client, int totalTime) {
        this.client = client;
        myTime = totalTime;
        opTime = totalTime;
    }
    
    @Override
    public void run() {
        client.getTimingPanel().setMyTime(myTime);
        client.getTimingPanel().setOpTime(opTime);
        
        while(client.getBoard().isGamming()) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            if(client.getBoard().isTurn()) {
                myTime --;
                client.getTimingPanel().setMyTime(myTime);
                if(myTime <= 0) {
                    client.getC().giveup();
                    break;
                }
            } else {
                opTime --;
                client.getTimingPanel().setOpTime(opTime);
                if(opTime <= 0) {
                    break;
                }
            }
        }
    }
}
