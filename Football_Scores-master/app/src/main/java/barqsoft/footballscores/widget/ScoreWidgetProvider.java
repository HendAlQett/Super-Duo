package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by hend on 1/11/16.
 */
public class ScoreWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        for (int appWidgetId:appWidgetIds)
//        {
//            int layoutId = R.layout.widget_score_small;
//            RemoteViews views = new RemoteViews(context.getPackageName(),layoutId);
//            Intent launchIntent = new Intent(context, MainActivity.class);
//            PendingIntent pendingIntent= PendingIntent.getActivity(context,0,launchIntent,0);
//            views.setOnClickPendingIntent(R.id.widget,pendingIntent);
//            appWidgetManager.updateAppWidget(appWidgetId,views);
//        }

        context.startService(new Intent(context,ScoreWidgetIntentService.class));



    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        context.startService(new Intent(context,ScoreWidgetIntentService.class));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}
