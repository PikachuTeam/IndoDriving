package tatteam.com.app_common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import tatteam.com.app_common.entity.MyExtraAppsEntity;

/**
 * Created by ThanhNH-Mac on 10/3/15.
 */
public class AppParseUtil {
    public static MyExtraAppsEntity parseExtraApps(String json) {
        Gson gson = new GsonBuilder().create();
        MyExtraAppsEntity extraApps = gson.fromJson(json, MyExtraAppsEntity.class);
        return extraApps;
    }
}
