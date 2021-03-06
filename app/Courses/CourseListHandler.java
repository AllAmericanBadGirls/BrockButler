/**
 * CourseListHandler.java
 * Brock Butler
 * Creates a database table for a full list of offerings on the registrar's
 * timetable and allows the table to have inserts or be read
 * Created by James Grisdale on 2013-02-24
 * Copyright (c) 2013 Sea Addicts. All rights reserved.
**/

package edu.seaaddicts.brockbutler;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
public class CourseListHandler extends SQLiteOpenHelper {
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "Database";
 
    // Contacts table name
    private static final String TABLE_COURSES = "MasterList";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SUBJ = "subj";
    private static final String KEY_CODE = "code";
    private static final String KEY_DESC = "desc";
    private static final String KEY_TYPE = "type";
    private static final String KEY_SEC = "sec";
    private static final String KEY_DUR = "dur";
    private static final String KEY_DAYS = "days";
    private static final String KEY_TIME = "time";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_INSTRUCTOR = "instructor";
    Context context;
    public CourseListHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //creates the courses table for the master list of courses
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
                + KEY_ID + " TEXT," + KEY_SUBJ + " TEXT," + KEY_CODE + " TEXT," 
				+ KEY_DESC + " TEXT," + KEY_TYPE + " TEXT," + KEY_SEC + " TEXT," + KEY_DUR + " TEXT,"
                + KEY_DAYS + " TEXT,"+ KEY_TIME + " TEXT,"+ KEY_LOCATION + " TEXT,"
                + KEY_INSTRUCTOR + " TEXT"+ ")";
        db.execSQL(CREATE_COURSES_TABLE);
	}

	//upgrading the database will drop the table and recreate
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        // Create tables again
        onCreate(db);
	}
	
	//addCourse - adds information for a course into the database
	public void addCourse(){
		Brocku list = new Brocku();
		ArrayList<MasterCourse> course = new ArrayList<MasterCourse>();
		try{
		course = list.execute().get();
		SQLiteDatabase db = this.getWritableDatabase();
		//sets database for multiple line insert
		db.beginTransaction();
		for (int i=0; i<course.size(); i++){
			ContentValues values = new ContentValues();
			values.put(KEY_ID, course.get(i).id); // Course id
			values.put(KEY_SUBJ, course.get(i).subj); // subject code
			values.put(KEY_CODE, course.get(i).code);
			values.put(KEY_DESC, course.get(i).desc);
			values.put(KEY_TYPE, course.get(i).type);
			values.put(KEY_SEC, course.get(i).sec);
			values.put(KEY_DUR, course.get(i).dur);
			values.put(KEY_DAYS, course.get(i).days);
			values.put(KEY_TIME, course.get(i).time);
			values.put(KEY_LOCATION, course.get(i).location);
			values.put(KEY_INSTRUCTOR, course.get(i).instructor);	 
			// Inserting Row
			db.insert(TABLE_COURSES, null, values);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	    db.close(); // Closing database connection
		}catch(Exception e){};
	}
	
	//getCourses - returns a list of offerings for a particular subject and code
	public ArrayList<MasterCourse> getCourses(String subj, String code) {
	    SQLiteDatabase db = this.getReadableDatabase();
	    ArrayList<MasterCourse> courseList = new ArrayList<MasterCourse>();
	    courseList.ensureCapacity(50);
	    MasterCourse course;
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_COURSES + " where " + KEY_SUBJ + "= '" + subj + "' and " + KEY_CODE + " = '" + code + "'", null);
		//int i = 0;
		if (c != null){
			if (c.moveToFirst()){
				do{
					course = new MasterCourse();
					course.id = c.getString(c.getColumnIndex(KEY_ID));
					course.subj = c.getString(c.getColumnIndex(KEY_SUBJ));
					course.code = c.getString(c.getColumnIndex(KEY_CODE));
					course.desc = c.getString(c.getColumnIndex(KEY_DESC));
					course.type = c.getString(c.getColumnIndex(KEY_TYPE));
					course.sec = c.getString(c.getColumnIndex(KEY_SEC));
					course.dur = c.getString(c.getColumnIndex(KEY_DUR));
					course.days = c.getString(c.getColumnIndex(KEY_DAYS));
					course.time = c.getString(c.getColumnIndex(KEY_TIME));
					course.location = c.getString(c.getColumnIndex(KEY_LOCATION));
					course.instructor = c.getString(c.getColumnIndex(KEY_INSTRUCTOR));
					courseList.add(course);
				}while (c.moveToNext());
			}
		}
		c.close();
		return courseList;	    
	}
	
	//getSubjects - returns a list of subjects from the database
	public ArrayList<String> getSubjects(){
		//String subjects;
		ArrayList<String> subj = new ArrayList<String>();
		try{		
		SQLiteDatabase db = this.getReadableDatabase();		
		Cursor c = db.rawQuery("SELECT DISTINCT "+KEY_SUBJ+" FROM " + TABLE_COURSES, null);

		if (c != null){
			if (c.moveToFirst()){
				do{					
					subj.add(c.getString(c.getColumnIndex(KEY_SUBJ)));					
				}while (c.moveToNext());
			}
		}
		c.close();
		}catch(Exception e){subj.add(e.toString());}
		return subj;
	}
	
	//getCodes - returns a list of codes for a subject from the database
	public ArrayList<String> getCodes(String subj){
		ArrayList<String> codes = new ArrayList<String>();
		try{		
		SQLiteDatabase db = this.getReadableDatabase();		
		Cursor c = db.rawQuery("SELECT DISTINCT "+KEY_CODE+" FROM " + TABLE_COURSES+
				" WHERE "+KEY_SUBJ+"='"+subj+"'", null);
		if (c != null){
			if (c.moveToFirst()){
				do{					
					codes.add(c.getString(c.getColumnIndex(KEY_CODE)));					
				}while (c.moveToNext());
			}
		}
		c.close();
		}catch(Exception e){codes.add(e.toString());}
		return codes;
	}
}