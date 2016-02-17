package tatteam.com.app_common.sqlite;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ThanhNH-Mac on 2/10/16.
 */
public class BaseDataSource {

    public static SQLiteDatabase openConnection() {
        return DatabaseLoader.getInstance().openConnection();
    }

    public static void closeConnection() {
        DatabaseLoader.getInstance().closeConnection();
    }
}
