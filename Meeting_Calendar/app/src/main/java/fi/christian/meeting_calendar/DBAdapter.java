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
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class DBAdapter {
    private static String[] meetingColumnNames, participantColumnNames;
    private static String TAG;
    private static String meetingsTable, participantsTable;
    private static final int DATABASE_VERSION = 1;
    private static String MEETINGS_CREATE_QUERY;
    private static String PARTICIPANTS_CREATE_QUERY;
    private static String MEETINGS_DELETE_QUERY;
    private static String PARTICIPANTS_DELETE_QUERY;

    private Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqlLiteDb;
    private String dbPath;
    private static String dbName;

    public DBAdapter(Context context, String dbPath, String dbName, String meetingsTable, String participantsTable) {
        this.context = context;
        this.dbPath = dbPath;
        DBAdapter.dbName = dbName;
        DBAdapter.meetingsTable = meetingsTable;
        DBAdapter.participantsTable = participantsTable;

        meetingColumnNames = context.getResources().getStringArray(R.array.meetings_column_names);
        participantColumnNames = context.getResources().getStringArray(R.array.participants_column_names);
        TAG = context.getString(R.string.db_class_tag);

        MEETINGS_CREATE_QUERY = "CREATE TABLE " + meetingsTable + " (" +
                meetingColumnNames[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                meetingColumnNames[1] + " TEXT, " +
                meetingColumnNames[2] + " TEXT, " +
                meetingColumnNames[3] + " TEXT, " +
                meetingColumnNames[4] + " TEXT);";

        PARTICIPANTS_CREATE_QUERY = "CREATE TABLE " + participantsTable + " (" +
                participantColumnNames[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                participantColumnNames[1] + " INTEGER, " +
                participantColumnNames[2] + " TEXT, " +
                participantColumnNames[3] + " BLOB);";

        MEETINGS_DELETE_QUERY = "DROP TABLE IF EXISTS " + meetingsTable;
        PARTICIPANTS_DELETE_QUERY = "DROP TABLE IF EXISTS " + participantsTable;

        dbHelper = new DatabaseHelper(context);
    }

    public DBAdapter() {}

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, dbName, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(MEETINGS_CREATE_QUERY);
                db.execSQL(PARTICIPANTS_CREATE_QUERY);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ". All old data will be deleted!");
            db.execSQL(PARTICIPANTS_DELETE_QUERY);
            db.execSQL(MEETINGS_DELETE_QUERY);
            onCreate(db);
        }
    }

    public DBAdapter openDBConnection() {
        try {
            sqlLiteDb = SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            sqlLiteDb = dbHelper.getWritableDatabase();
        }
        return this;
    }

    public void closeDBConnection() {
        if (sqlLiteDb != null && sqlLiteDb.isOpen()) {
            sqlLiteDb.close();
        }
        dbHelper.close();
    }

    public long addMeeting(Meeting meeting) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(meetingColumnNames[1], meeting.getTitle());
        initialValues.put(meetingColumnNames[2], meeting.getPlace());
        initialValues.put(meetingColumnNames[3], meeting.getDate());
        initialValues.put(meetingColumnNames[4], meeting.getTime());

        openDBConnection();
        long meetingId = sqlLiteDb.insert(meetingsTable, null, initialValues);

        if (meetingId != -1) {
            for (Participant participant : meeting.getParticipants()) {
                ContentValues pValues = new ContentValues();
                pValues.put(participantColumnNames[1], meetingId);
                pValues.put(participantColumnNames[2], participant.getName());

                if (participant.getImage() != null) {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    participant.getImage().compress(CompressFormat.PNG, 50, outputStream);
                    pValues.put(participantColumnNames[3], outputStream.toByteArray());
                }
                sqlLiteDb.insert(participantsTable, null, pValues);
            }
        }
        closeDBConnection();
        return meetingId;
    }

    public void deleteMeeting(long rowID) {
        openDBConnection();
        sqlLiteDb.delete(participantsTable, participantColumnNames[1] + "=" + rowID, null);
        sqlLiteDb.delete(meetingsTable, meetingColumnNames[0] + "=" + rowID, null);
        closeDBConnection();
    }

    public ArrayList<Meeting> getAllMeetings() {
        ArrayList<Meeting> meetingsList = new ArrayList<>();
        openDBConnection();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeDBConnection();
        return meetingsList;
    }

    private ArrayList<Participant> getParticipantsForMeeting(long meetingId) {
        ArrayList<Participant> participants = new ArrayList<>();
        Cursor cursor = sqlLiteDb.query(participantsTable, participantColumnNames,
                participantColumnNames[1] + "=" + meetingId, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(2);
                byte[] imgByte = cursor.getBlob(3);
                Bitmap image = null;
                if (imgByte != null) {
                    image = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                }
                participants.add(new Participant(name, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return participants;
    }
}
