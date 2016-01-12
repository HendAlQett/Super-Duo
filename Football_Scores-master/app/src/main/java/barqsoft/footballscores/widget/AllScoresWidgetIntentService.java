package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by hend on 1/12/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AllScoresWidgetIntentService extends RemoteViewsService {

    private String[] selectionArguments = new String[1];
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DATE = 1;
    public static final int COL_LEAGUE = 5;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_ID = 8;
    public static final int COL_MATCHTIME = 2;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new RemoteViewsFactory() {
            private Cursor data = null;
            String homeName = getString(R.string.empty_text);
            String awayName = getString(R.string.empty_text);

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }






















                final long identityToken = Binder.clearCallingIdentity();

                Uri scoresUri = DatabaseContract.scores_table.buildScoreWithDate();
                Date fragmentdate = new Date(System.currentTimeMillis() + (0 * 86400000)); //Today
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                selectionArguments[0] = mformat.format(fragmentdate);
                data = getContentResolver().query(scoresUri, null, null, selectionArguments, DatabaseContract.scores_table._ID + " ASC ");

                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_scores_list_item);

                homeName = data.getString(COL_HOME);
                awayName = data.getString(COL_AWAY);
                String scores = Utilies.getScores(data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS));
                String time = data.getString(COL_MATCHTIME);

                int homeImage = Utilies.getTeamCrestByTeamName(data.getString(COL_HOME));
                int awayImage = Utilies.getTeamCrestByTeamName(data.getString(COL_AWAY));


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    setRemoteContentDescription(views);
                }
                views.setTextViewText(R.id.home_name, homeName);
                views.setTextViewText(R.id.away_name, awayName);
                views.setTextViewText(R.id.score_textview, scores);
                views.setTextViewText(R.id.data_textview, time);
                views.setImageViewResource(R.id.home_crest, homeImage);
                views.setImageViewResource(R.id.away_crest, awayImage);


                Intent launchIntent = new Intent(AllScoresWidgetIntentService.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(AllScoresWidgetIntentService.this, 0, launchIntent, 0);
                views.setOnClickPendingIntent(R.id.widget, pendingIntent);
                return views;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views) {
                views.setContentDescription(R.id.home_crest, homeName);
                views.setContentDescription(R.id.away_crest, awayName);
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_scores_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getLong(COL_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };


    }
}
