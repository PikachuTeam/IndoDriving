package tatteam.com.app_common.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by ThanhNH on 10/1/2016.
 */
public class DatabaseLoader {

    private static DatabaseLoader instance;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    private DatabaseLoader() {
    }

    public static DatabaseLoader getInstance() {
        if (instance == null) {
            instance = new DatabaseLoader();
        }
        return instance;
    }

    private void initIfNeeded(Context context) {
        if (this.context == null || this.context!=context) {
            this.context = context;
        }
    }

    public void createIfNeeded(Context context, String databaseName) {
        initIfNeeded(context);
        this.openDatabase(databaseName);
    }

    public SQLiteDatabase openConnection(){
        if(sqLiteDatabase!=null){
            sqLiteDatabase.acquireReference();
        }
        return sqLiteDatabase;
    }

    public void closeConnection(){
        if(sqLiteDatabase!=null){
            sqLiteDatabase.releaseReference();
        }
    }

    private void openDatabase(String databaseName) {
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
