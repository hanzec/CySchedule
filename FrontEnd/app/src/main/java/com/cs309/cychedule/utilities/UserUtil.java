package com.cs309.cychedule.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import com.cs309.cychedule.R;
import com.cs309.cychedule.activities.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The UserUtil contains the utility functions that we will use in this app
 */
public class UserUtil {
	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		return isNum.matches();
	}
	
	public static boolean hasSpecialChar(String str) {
		String regEx = "[ _`~!#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	public static void noti_normal(Context context, int NotificationID, String title, String text){
		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			String CHANNEL_ID = "test_channel_01";
			CharSequence name = "test_channel";
			String Description = "This is cychedule test channel.";
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(Description);
			channel.enableLights(true);
			channel.setLightColor(Color.RED);
			channel.enableVibration(true);
			channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
			channel.setShowBadge(false);
			notificationManager.createNotificationChannel(channel);
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "test_channel_01")
				.setSmallIcon(R.mipmap.ic_launcher)
				.setContentTitle(title)
				.setContentText(text);

		Intent resultIntent = new Intent(context, context.getClass());
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent
				= stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultPendingIntent);
		notificationManager.notify(NotificationID, builder.build());
	}
	
	public static void noti_invite(Context context, int NotificationID, String title, String text){
		
		Intent intent = new Intent(context, Main3Activity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addParentStack(Main3Activity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationManager notificationManager =
				(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			String CHANNEL_ID = "invite_channel_01";
			CharSequence name = "invite_channel";
			String Description = "This is cychedule invite channel.";
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
			channel.setDescription(Description);
			channel.enableLights(true);
			channel.setLightColor(Color.RED);
			channel.enableVibration(true);
			channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
			channel.setShowBadge(false);
			notificationManager.createNotificationChannel(channel);
		}
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "invite_channel_01")
				.setSmallIcon(R.drawable.gitcat)
				// .setLargeIcon(R.drawable.gitcat)
				.setContentTitle(title)
				.setContentText(text)
				.setContentIntent(resultPendingIntent);
		
		Intent resultIntent = new Intent(context, context.getClass());
		notificationManager.notify(NotificationID, builder.build());
	}
	
}
