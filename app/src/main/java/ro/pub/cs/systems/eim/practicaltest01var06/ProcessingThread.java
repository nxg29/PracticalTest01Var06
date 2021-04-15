package ro.pub.cs.systems.eim.practicaltest01var06;

import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.Date;

public class ProcessingThread extends Thread {
    private Context context = null;
    private boolean isRunning = true;
    private int scor;

    public ProcessingThread(Context context, int scor) {
        this.context = context;
        this.scor = scor;
    }

    @Override
    public void run() {
        sleep();
        sendMessage();
    }

    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction(Constants.INTENT_FILTER);
        intent.putExtra(Constants.BROADCAST_RECEIVER_EXTRA,
                new Date(System.currentTimeMillis()) + " "  + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + " SCOR: " + scor);
        context.sendBroadcast(intent);
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }

    public void stopThread() {
        isRunning = false;
    }
}
