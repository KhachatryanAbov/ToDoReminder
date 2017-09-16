package abov.khachatryan.todoreminder.general;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* Created by abov on 9/15/17.*/

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setAction(MainActivity.ALARM_ACTION_SHOW_TODO);
        context.startActivity(i);
    }
}