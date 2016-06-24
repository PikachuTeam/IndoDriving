package tatteam.com.app_common.sqlite.v2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import tatteam.com.app_common.sqlite.AssetDatabaseOpenHelper;

/**
 * Created by ThanhNH-Mac on 6/24/16.
 */
public class DatabaseLoaderV2 {

    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private String databaseName;

    public DatabaseLoaderV2(Context context, String databaseName) {
        this.context = context;
        this.databaseName = databaseName;
    }

    public void initIfNeeded(){
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            AssetDatabaseOpenHelper assetDatabaseOpenHelper = new AssetDatabaseOpenHelper(context, databaseName);
            sqLiteDatabase = assetDatabaseOpenHelper.openDatabase();
        }
    }

    public SQLiteDatabase openConnection() {
        if (sqLiteDatabase == null) {
            initIfNeeded();
        }
        sqLiteDatabase.acquireReference();
        return sqLiteDatabase;
    }

    public void closeConnection() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.releaseReference();
        }
    }

    private void closeDatabase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    public void destroy() {
        closeDatabase();
    }
}
