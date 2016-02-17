package tatteam.com.app_common.util;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

/**
 * Created by ThanhNH-Mac on 10/23/15.
 */
public class AppSpeaker {

    private static AppSpeaker instance;

    private TextToSpeech textToSpeech;

    private AppSpeaker() {
    }

    public static AppSpeaker getInstance() {
        if (instance == null) {
            instance = new AppSpeaker();
        }
        return instance;
    }

    public void initIfNeeded(final Context context, final Locale loc) {
        if (textToSpeech == null) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (instance) {
                        try {
                            Thread.sleep(100);
                            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status != TextToSpeech.ERROR) {
                                        try {
                                            textToSpeech.setLanguage(loc);
                                            textToSpeech.setSpeechRate(AppConstant.TEXT_TO_SPEECH_RATE);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        }
    }

    public boolean ready() {
        return textToSpeech != null;
    }

    public void speak(String message) {
        if (ready()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    public void stop() {
        if (ready()) {
            textToSpeech.stop();
        }
    }


    public void destroy() {
        if (ready()) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        instance = null;
    }

}
