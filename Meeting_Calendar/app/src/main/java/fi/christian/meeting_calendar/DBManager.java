package fi.christian.meeting_calendar;

import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class DBManager {
    private static DBAdapter dbAdapter;

    public static DBAdapter getAdapter(Context context) {
        if (dbAdapter == null) {
            String dbName = context.getString(R.string.db_name);
            String dirName = context.getString(R.string.db_dir_name);
            String dbPath = Objects.requireNonNull(context.getExternalFilesDir(dirName)).getAbsolutePath() + File.separator;
            
            copyDBFileFromAssets(context, dbPath, dbName);
            
            dbAdapter = new DBAdapter(context.getApplicationContext(), dbPath, dbName, 
                    context.getString(R.string.meetings_table_name), 
                    context.getString(R.string.participants_table_name));
        }
        return dbAdapter;
    }

    private static void copyDBFileFromAssets(Context context, String dbPath, String dbName) {
        File dbFile = new File(dbPath + dbName);
        if (!dbFile.exists()) {
            new File(dbPath).mkdirs();
            try {
                InputStream inputStream = context.getAssets().open(dbName);
                OutputStream outputStream = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
