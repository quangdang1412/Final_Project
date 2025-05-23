package com.example.hcmute.quangvaphong.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.hcmute.quangvaphong.R;
import com.example.hcmute.quangvaphong.models.IrregularVerb;
import com.example.hcmute.quangvaphong.models.Vocabulary;
import com.example.hcmute.quangvaphong.models.VocabularyIelts;
import com.example.hcmute.quangvaphong.models.VocabularyOxford;
import com.example.hcmute.quangvaphong.models.VocabularyToefl;
import com.example.hcmute.quangvaphong.repository.VocabularyRepository;
import com.example.hcmute.quangvaphong.views.viewModel.VocabularyViewModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;

public class NotificationReceiver extends BroadcastReceiver {
    private VocabularyRepository repository;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotificationReceiver", "onReceive: Notification triggered");

        repository = new VocabularyRepository(context);

        int random = LocalDate.now().getDayOfMonth() % 4;
        switch (random) {
            case 0:
                Executors.newSingleThreadExecutor().execute(() -> {
                    List<VocabularyOxford> vocabularyList = repository.getAllVocabularyOxfordSync();
                    if (vocabularyList != null && !vocabularyList.isEmpty()) {
                        Vocabulary vocab = vocabularyList.get(new Random().nextInt(vocabularyList.size()));

                        NotificationManager notificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        String channelId = "scheduled_channel";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(
                                    channelId,
                                    "Scheduled Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                            );
                            notificationManager.createNotificationChannel(channel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                                .setSmallIcon(R.drawable.baseline_notifications_24)
                                .setContentTitle("New day, new word")
                                .setContentText(String.format("%s %s: %s", vocab.getWord(), vocab.getPronunciation(), vocab.getMeaning()))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        notificationManager.notify(100, builder.build());
                    }
                });
                break;
            case 1:
                Executors.newSingleThreadExecutor().execute(() -> {
                    List<VocabularyToefl> vocabularyList = repository.getAllVocabularyToeflSync();
                    if (vocabularyList != null && !vocabularyList.isEmpty()) {
                        Vocabulary vocab = vocabularyList.get(new Random().nextInt(vocabularyList.size()));

                        NotificationManager notificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        String channelId = "scheduled_channel";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(
                                    channelId,
                                    "Scheduled Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                            );
                            notificationManager.createNotificationChannel(channel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                                .setSmallIcon(R.drawable.baseline_notifications_24)
                                .setContentTitle("New day, new word")
                                .setContentText(String.format("%s %s: %s", vocab.getWord(), vocab.getPronunciation(), vocab.getMeaning()))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        notificationManager.notify(100, builder.build());
                    }
                });
                break;
            case 2:
                Executors.newSingleThreadExecutor().execute(() -> {
                    List<VocabularyIelts> vocabularyList = repository.getAllVocabularyIeltsSync();
                    if (vocabularyList != null && !vocabularyList.isEmpty()) {
                        Vocabulary vocab = vocabularyList.get(new Random().nextInt(vocabularyList.size()));

                        NotificationManager notificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        String channelId = "scheduled_channel";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(
                                    channelId,
                                    "Scheduled Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                            );
                            notificationManager.createNotificationChannel(channel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                                .setSmallIcon(R.drawable.baseline_notifications_24)
                                .setContentTitle("New day, new word")
                                .setContentText(String.format("%s %s: %s", vocab.getWord(), vocab.getPronunciation(), vocab.getMeaning()))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        notificationManager.notify(100, builder.build());
                    }
                });
                break;
            case 3:
                Executors.newSingleThreadExecutor().execute(() -> {
                    List<IrregularVerb> vocabularyList = repository.getAllVocabularyIrregularSync();
                    if (vocabularyList != null && !vocabularyList.isEmpty()) {
                        Vocabulary vocab = vocabularyList.get(new Random().nextInt(vocabularyList.size()));

                        NotificationManager notificationManager =
                                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                        String channelId = "scheduled_channel";
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel channel = new NotificationChannel(
                                    channelId,
                                    "Scheduled Notifications",
                                    NotificationManager.IMPORTANCE_HIGH
                            );
                            notificationManager.createNotificationChannel(channel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                                .setSmallIcon(R.drawable.baseline_notifications_24)
                                .setContentTitle("New day, new word")
                                .setContentText(String.format("%s %s: %s", vocab.getWord(), vocab.getPronunciation(), vocab.getMeaning()))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setAutoCancel(true);

                        notificationManager.notify(100, builder.build());
                    }
                });
                break;
        }
    }
}
