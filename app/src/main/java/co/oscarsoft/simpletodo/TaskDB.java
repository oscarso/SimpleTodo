package co.oscarsoft.simpletodo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;




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
        db.execSQL(CREATE_TASK_TABLE);
    }


    public void onRecreate() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);

        // Create tables again
        onCreate(db);
    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
 
        // Create tables again
        onCreate(db);
    }


    public ArrayList<Task> getTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                DB_TABLE, //table name
                new String[]{TASK_NAME}, //columns
                null, //selection
                null, //selectionArgs
                null, //groupBy
                null, //having
                null, //orderBy
                null  //limit
        );

        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }

        ArrayList<Task> tasks = new ArrayList<Task>();
        while ((cursor != null) &&
        	cursor.moveToNext()) {
            Task task = new Task(cursor.getString(0));
            tasks.add(task);
        }
        db.close();
        return tasks;
    }


    public void setTasks(ArrayList<Task> tasks) {
        if (tasks.size() == 0) {
            return; //do nothing
        }

        SQLiteDatabase db = this.getWritableDatabase();

        for (int i=0; i < tasks.size(); i++) {
            ContentValues values = new ContentValues();
            Task task = tasks.get(i);
            values.put(TASK_NAME, task.getTaskName());
            // Inserting Row
            db.insert(DB_TABLE, null, values);
        }
        db.close(); // Closing database connection
    }
}
