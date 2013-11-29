package cl.telematica.databaseexample.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseClass extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EarthQuakeDB";
    
    private String sqlString = "CREATE TABLE 'alumnos' ('id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
    												  "'title' TEXT, " +
    												  "'magnitude' TEXT, " +
    												  "'location' TEXT, " +
    												  "'depth' TEXT, " +
    												  "'latitude' TEXT, " +
    												  "'longitude' TEXT, " +
    												  "'datetime' TEXT, " +
    												  "'link' TEXT)";
    
    public DataBaseClass(Context ctx){
    	super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sqlString);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS 'alumnos'");
		onCreate(db);
	}

}
