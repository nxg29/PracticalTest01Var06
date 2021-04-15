package ro.pub.cs.systems.eim.practicaltest01var06;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import static ro.pub.cs.systems.eim.practicaltest01var06.Constants.*;

public class PracticalTest01Var06Service extends Service {
    ProcessingThread processingThread = null;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int scor = intent.getIntExtra(SCOR, 0);
        processingThread = new ProcessingThread(this, scor);
        processingThread.start();
        return Service.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        processingThread.stopThread();
    }
}
