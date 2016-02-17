package tatteam.com.app_common.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ThanhNH on 10/1/2016.
 */
public class AssetDatabaseOpenHelper {
    private Context context;
    private String databaseName;

    public AssetDatabaseOpenHelper(Context context, String databaseName) {
        this.context = context;
        this.databaseName = databaseName;
    }

    public SQLiteDatabase openDatabase() {
        try {
            String path = "data/data/" + context.getPackageName() + "/databases/";
            File dbPath = new File(path);
            File dbFile = new File(dbPath + databaseName);
            if (!dbPath.exists()) {
                dbPath.mkdir();
                dbFile.createNewFile();
                cloneFromAssets(dbFile);
            } else {
                if (!dbFile.exists()) {
                    cloneFromAssets(dbFile);
                }
            }
            return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void cloneFromAssets(File dbFile) throws IOException {
        InputStream is = context.getAssets().open(databaseName);
        OutputStream os = new FileOutputStream(dbFile);

        byte[] buffer = new byte[1024];
        while (is.read(buffer) > 0) {
            os.write(buffer);
        }
        os.flush();
        os.close();
        is.close();
    }
}
