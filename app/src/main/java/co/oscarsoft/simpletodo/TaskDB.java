package co.oscarsoft.simpletodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TaskDB extends SQLiteOpenHelper {
	private static TaskDB mInstance = null;

    private static final String DB_NAME = "simpletodotask";
    private static final String DB_TABLE = "ud_task";
    private static final int DB_VERSION = 1;

    private Context mCtx;

    // Contacts Table Columns names
    private static final String TASK_NAME = "task_name";

    public static TaskDB getInstance(Context ctx) {
    	if (mInstance == null) {
    		mInstance = new TaskDB(ctx.getApplicationContext());
    	}
    	return mInstance;
    }

    private TaskDB(Context ctx) {
        super(ctx, DB_NAME, null, DB_VERSION);
        this.mCtx = ctx;
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE =
        		"CREATE TABLE " + DB_TABLE +
        		"("
        		+ TASK_NAME + " TEXT PRIMARY KEY"
        		+ ")";
        //Log.d(DebugInfo.TAG, "TaskDB: onCreate: sql=" + CREATE_INFO_TABLE);
        db.execSQL(CREATE_TASK_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
 
        // Create tables again
        onCreate(db);
    }

    public Task getTask() {
    	//Log.d(DebugInfo.TAG, "getTask");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
        		DB_TABLE, //table name
        		new String[] { TASK_NAME }, //columns
        		null, //selection
                null, //selectionArgs
                null, //groupBy
                null, //having
                null, //orderBy
                "1"   //limit
        		);
        if ((cursor != null) &&
        	cursor.moveToFirst()) {
        	//Log.d(DebugInfo.TAG, "getUser:    ID: " + cursor.getString(0));
        	//Log.d(DebugInfo.TAG, "getUser: level: " + cursor.getInt(1));
            Task task = new Task(cursor.getString(0));
            db.close();
            return task;
        } else {
        	db.close();
        	return null;
        }
    }

    public void setTask(Task task) {
    	//Log.d(DebugInfo.TAG, "setUser");
    	if (getTask() != null) {
    		// if User has already been set, overwrite is not allowed
    		//Log.d(DebugInfo.TAG, "setUser: User has already been set");
    		return;
    	}

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TASK_NAME, task.getTaskName());
        //values.put(KEY_LEVEL, user.getLevel());
        //Log.d(DebugInfo.TAG, "setUser:    ID: " + user.getId());
        //Log.d(DebugInfo.TAG, "setUser: level: " + user.getLevel());
     
        // Inserting Row
        db.insert(DB_TABLE, null, values);
        db.close(); // Closing database connection
    }
}
