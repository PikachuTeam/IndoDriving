package tatteam.com.app_common.util;

/**
 * Created by ThanhNH-Mac on 10/31/15.
 */
public interface AppConstant {

    //text to speech
    float TEXT_TO_SPEECH_RATE = 0.8f;

    //ads
    String DEFAULT_ADS_URL = "https://www.dropbox.com/s/2fy36ay5po304eo/my_ads.txt?dl=1";
    String DEFAULT_MORE_APP_URL = "https://www.dropbox.com/s/faa5s1wzl0izcg1/my_extra_apps.txt?dl=1";

    int RE_SYNC_ADS_LAUNCH_TIME_INTERVAL = 5;

    enum AdsType {
        //test banner
        SMALL_BANNER_TEST("small_banner_test"),
        BIG_BANNER_TEST("big_banner_test"),

        //game
        SMALL_BANNER_GAME("small_banner_game"),
        BIG_BANNER_GAME("big_banner_game"),

        //language
        SMALL_BANNER_LANGUAGE_LEARNING("small_banner_language_learning"),
        BIG_BANNER_LANGUAGE_LEARNING("big_banner_language_learning"),

        //driving
        SMALL_BANNER_DRIVING_TEST("small_banner_driving_test"),
        BIG_BANNER_DRIVING_TEST("big_banner_driving_test");


        private String type;

        private AdsType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

    }
}
