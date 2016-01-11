package barqsoft.footballscores.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by hend on 1/11/16.
 */
public class ScoreWidgetIntentService extends IntentService {


    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;

    private String[] selectionArguments = new String[1];
//    private static final String[] SCORES_COLUMNS={
//
//    };
//
//    private static final int INDEX

    public ScoreWidgetIntentService() {
        super("ScoreWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ScoreWidgetProvider.class));

        Date fragmentdate = new Date(System.currentTimeMillis()+(0*86400000)); //Today
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        selectionArguments[0]= mformat.format(fragmentdate);

        Uri scoresUri=  DatabaseContract.scores_table.buildScoreWithDate();
        Cursor data =getContentResolver().query(scoresUri,null,null,selectionArguments, DatabaseContract.scores_table._ID + " ASC " + " LIMIT 1");

        if (data==null)
        {
            return;
        }
        if (!data.moveToFirst()){
            data.close();
            return;
        }

        String homeName= data.getString(COL_HOME);
        String awayName= data.getString(COL_AWAY);
        String scores = Utilies.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS));
        String time=data.getString(COL_MATCHTIME);

        int homeImage =Utilies.getTeamCrestByTeamName(data.getString(COL_HOME));
        int awayImage=Utilies.getTeamCrestByTeamName(data.getString(COL_AWAY));

        data.close();

        for (int appWidgetId: appWidgetIds)
        {
            int layoutId = R.layout.widget_score_small;
            RemoteViews views = new RemoteViews(this.getPackageName(),layoutId);
            views.setTextViewText(R.id.home_name,homeName);
            views.setTextViewText(R.id.away_name,awayName);
            views.setTextViewText(R.id.score_textview,scores);
            views.setTextViewText(R.id.data_textview,time);
            views.setImageViewResource(R.id.home_crest,homeImage);
            views.setImageViewResource(R.id.away_crest,awayImage);

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent= PendingIntent.getActivity(this,0,launchIntent,0);
            views.setOnClickPendingIntent(R.id.widget,pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }





    }
}
