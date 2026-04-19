package fi.christian.meeting_calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBAdapter {
    private static String[] meetingColumnNames, participantColumnNames;
    private static String meetingsTable, participantsTable;
    private static final int DATABASE_VERSION = 1;
    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqlLiteDb;
    private String dbPath;
    private static String dbName;

    public DBAdapter(Context context) {
        this.context = context;
        dbName = context.getString(R.string.db_name);
        dbPath = context.getFilesDir().getPath();
        meetingsTable = context.getString(R.string.meetings_table_name);
        participantsTable = context.getString(R.string.participants_table_name);
        meetingColumnNames = context.getResources().getStringArray(R.array.meetings_column_names);
        participantColumnNames = context.getResources().getStringArray(R.array.participants_column_names);

        dbHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, dbName, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + meetingsTable + " (" + meetingColumnNames[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + meetingColumnNames[1] + " TEXT NOT NULL, " + meetingColumnNames[2] + " TEXT NOT NULL, " + meetingColumnNames[3] + " TEXT NOT NULL, " + meetingColumnNames[4] + " TEXT NOT NULL);");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + participantsTable + " (" + participantColumnNames[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + participantColumnNames[1] + " INTEGER, " + participantColumnNames[2] + " TEXT NOT NULL, " + participantColumnNames[3] + " BLOB, FOREIGN KEY(" + participantColumnNames[1] + ") REFERENCES " + meetingsTable + "(" + meetingColumnNames[0] + ") ON DELETE CASCADE);");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + participantsTable);
            db.execSQL("DROP TABLE IF EXISTS " + meetingsTable);
            onCreate(db);
        }
    }

    public DBAdapter openDBConnection() {
        copyDBFile();
        File dbFile = new File(dbPath, dbName);
        sqlLiteDb = SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
        return this;
    }

    private void copyDBFile() {
        File dbFile = new File(dbPath, dbName);
        if (!dbFile.exists()) {
            new File(dbPath).mkdirs();
            try {
                InputStream inputStream = context.getAssets().open(dbName);
                OutputStream outputStream = new FileOutputStream(dbFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) outputStream.write(buffer, 0, length);
                outputStream.close();
                inputStream.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void closeDBConnection() {
        dbHelper.close();
    }

    public long addMeeting(Meeting meeting) {
        openDBConnection();
        ContentValues initialValues = new ContentValues();
        initialValues.put(meetingColumnNames[1], meeting.getTitle());
        initialValues.put(meetingColumnNames[2], meeting.getPlace());
        initialValues.put(meetingColumnNames[3], meeting.getDate());
        initialValues.put(meetingColumnNames[4], meeting.getTime());
        long meetingId = sqlLiteDb.insert(meetingsTable, null, initialValues);
        for (Participant participant : meeting.getParticipants()) {
            ContentValues pValues = new ContentValues();
            pValues.put(participantColumnNames[1], meetingId);
            pValues.put(participantColumnNames[2], participant.getName());
            byte[] imgData = bitmapToByteArray(participant.getImage());
            if (imgData != null) pValues.put(participantColumnNames[3], imgData);
            sqlLiteDb.insert(participantsTable, null, pValues);
        }
        closeDBConnection();
        return meetingId;
    }

    public boolean updateMeeting(Meeting meeting) {
        long meetingId = meeting.getId();
        openDBConnection();
        ContentValues initialValues = new ContentValues();
        initialValues.put(meetingColumnNames[1], meeting.getTitle());
        initialValues.put(meetingColumnNames[2], meeting.getPlace());
        initialValues.put(meetingColumnNames[3], meeting.getDate());
        initialValues.put(meetingColumnNames[4], meeting.getTime());
        int updatedRows = sqlLiteDb.update(meetingsTable, initialValues, meetingColumnNames[0] + "=" + meetingId, null);
        sqlLiteDb.delete(participantsTable, participantColumnNames[1] + "=" + meetingId, null);
        for (Participant participant : meeting.getParticipants()) {
            ContentValues pValues = new ContentValues();
            pValues.put(participantColumnNames[1], meetingId);
            pValues.put(participantColumnNames[2], participant.getName());
            byte[] imgData = bitmapToByteArray(participant.getImage());
            if (imgData != null) pValues.put(participantColumnNames[3], imgData);
            sqlLiteDb.insert(participantsTable, null, pValues);
        }
        closeDBConnection();
        return updatedRows > 0;
    }

    public void deleteMeeting(long rowID) {
        openDBConnection();
        sqlLiteDb.delete(meetingsTable, meetingColumnNames[0] + "=" + rowID, null);
        closeDBConnection();
    }

    public ArrayList<Meeting> getAllMeetings() {
        ArrayList<Meeting> meetingsList = new ArrayList<>();
        openDBConnection();
        Cursor cursor = sqlLiteDb.query(meetingsTable, meetingColumnNames, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String place = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                ArrayList<Participant> participants = getParticipantsForMeeting(id);
                meetingsList.add(new Meeting(id, title, place, participants, date, time));
            } while (cursor.moveToNext());
        }
        cursor.close();
        closeDBConnection();
        return meetingsList;
    }

    private ArrayList<Participant> getParticipantsForMeeting(long meetingId) {
        ArrayList<Participant> participants = new ArrayList<>();
        Cursor cursor = sqlLiteDb.query(participantsTable, participantColumnNames, participantColumnNames[1] + "=" + meetingId, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String name = cursor.getString(2);
                byte[] imgByte = cursor.getBlob(3);
                Bitmap image = (imgByte != null) ? BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length) : null;
                participants.add(new Participant(id, name, image));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        return participants;
    }

    public ArrayList<Participant> getAllParticipants() {
        ArrayList<Participant> participants = new ArrayList<>();
        openDBConnection();
        Cursor cursor = sqlLiteDb.query(participantsTable, participantColumnNames, null, null, null, null, participantColumnNames[2] + " ASC");
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String name = cursor.getString(2);
                byte[] imgByte = cursor.getBlob(3);
                Bitmap image = (imgByte != null) ? BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length) : null;
                participants.add(new Participant(id, name, image));
            } while (cursor.moveToNext());
        }
        if (cursor != null) cursor.close();
        closeDBConnection();
        return participants;
    }

    public boolean updateParticipant(Participant participant) {
        openDBConnection();
        ContentValues values = new ContentValues();
        values.put(participantColumnNames[2], participant.getName());
        byte[] imgData = bitmapToByteArray(participant.getImage());
        if (imgData != null) {
            values.put(participantColumnNames[3], imgData);
        } else {
            values.putNull(participantColumnNames[3]);
        }
        int rows = sqlLiteDb.update(participantsTable, values, participantColumnNames[0] + "=" + participant.getId(), null);
        closeDBConnection();
        return rows > 0;
    }

    public boolean deleteParticipant(long id) {
        openDBConnection();
        int rows = sqlLiteDb.delete(participantsTable, participantColumnNames[0] + "=" + id, null);
        closeDBConnection();
        return rows > 0;
    }

    private byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) return null;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int maxSize = 512;

        if (width > maxSize || height > maxSize) {
            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.JPEG, 70, outputStream);
        return outputStream.toByteArray();
    }
}
