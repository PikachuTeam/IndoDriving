package tatteam.com.app_common.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import tatteam.com.app_common.util.AppLocalSharedPreferences;

/**
 * Created by ThanhNH on 10/1/2016.
 */
public class DatabaseLoader {

    private static DatabaseLoader instance;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private String databaseName;

    private DatabaseLoader() {
    }

    public static DatabaseLoader getInstance() {
        if (instance == null) {
            instance = new DatabaseLoader();
        }
        return instance;
    }

    private void initIfNeeded(Context context) {
        if (this.context == null || this.context != context) {
            this.context = context;
        }
    }

    public void restoreState(Context context) {
        initIfNeeded(context);
        this.databaseName = AppLocalSharedPreferences.getInstance().getDatabaseName();
    }

    public void createIfNeeded(Context context, String databaseName) {
        AppLocalSharedPreferences.getInstance().setDatabaseName(databaseName);
        this.databaseName = databaseName;
        initIfNeeded(context);
        this.openDatabase();
    }

    public SQLiteDatabase openConnection() {
        if (sqLiteDatabase == null) {
            openDatabase();
        }
        sqLiteDatabase.acquireReference();
        return sqLiteDatabase;
    }

    public void closeConnection() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.releaseReference();
        }
    }

    private void openDatabase() {
        if (sqLiteDatabase == null || !sqLiteDatabase.isOpen()) {
            AssetDatabaseOpenHelper assetDatabaseOpenHelper = new AssetDatabaseOpenHelper(context, databaseName);
            sqLiteDatabase = assetDatabaseOpenHelper.openDatabase();
        }
    }

    private void closeDatabase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    public void destroy() {
        closeDatabase();
        instance = null;
    }
}
