/**
 * CurrentCoursesHandler.java
 * Brock Butler
 * Handles database table creation and queries for course information
 * Created by James Grisdale on 2013-02-24
 * Copyright (c) 2013 Sea Addicts. All rights reserved.
**/

package edu.seaaddicts.brockbutler;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CurrentCoursesHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;	 
    // Database Name
    private static final String DATABASE_NAME = "Database";
    // table names
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_TASKS = "tasks";
    private static final String TABLE_OFFERINGS = "offerings";
    private static final String TABLE_OFFERING_TIMES = "offering_times";
    private static final String TABLE_CONTACTS = "contacts"; 
    //field names
    private static final String KEY_SUBJ = "subj";
    private static final String KEY_CODE = "code";
    private static final String KEY_DESC = "desc";
    private static final String KEY_INSTRUCTOR = "instructor";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_SEC = "sec";
    private static final String KEY_DAY = "day";
    private static final String KEY_TIMES = "time_start";
    private static final String KEY_TIMEE = "time_end";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_DUR = "dur";
    private static final String KEY_ASSIGN = "assign";
    private static final String KEY_NAME = "name";
    private static final String KEY_MARK = "mark";
    private static final String KEY_BASE = "base";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_DUE = "due";
    private static final String KEY_CREATE_DATE = "create_date";
    private static final String KEY_CID = "cid";
    private static final String KEY_FNAME = "fname";
    private static final String KEY_LNAME = "lname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_INSTREMAIL = "insructor_email";
 
    /*Constructor for the database helper*/
    public CurrentCoursesHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* Create tables for courses, tasks, offerings, offering times, and contacts
     * in the database if they do not exist when the database
     * helper is first called
     */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_COURSES = "CREATE TABLE " + TABLE_COURSES + "("
                + KEY_SUBJ + " TEXT,"
                + KEY_CODE + " TEXT," + KEY_DESC + " TEXT,"
                + KEY_INSTRUCTOR + " TEXT," + KEY_INSTREMAIL + " TEXT," + "PRIMARY KEY("+KEY_SUBJ+","+KEY_CODE+")"+ ")";
		
		String CREATE_TASKS = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_SUBJ + " TEXT,"
                + KEY_CODE + " TEXT," + KEY_ASSIGN + " INTEGER,"
                + KEY_NAME + " TEXT," + KEY_MARK + " INTEGER," + KEY_BASE + " INTEGER," 
                + KEY_WEIGHT + " REAL," + KEY_DUE + " TEXT," + KEY_CREATE_DATE + " TEXT,"
                + KEY_PRIORITY + " INTEGER,"
                + "PRIMARY KEY("+KEY_SUBJ+","+KEY_CODE+","+KEY_ASSIGN+"),"
                + "FOREIGN KEY("+KEY_SUBJ+") REFERENCES "+ TABLE_COURSES +"("+KEY_SUBJ+"),"
                + "FOREIGN KEY("+KEY_CODE+") REFERENCES "+ TABLE_COURSES +"("+KEY_CODE+")"
                + ")";
		
		String CREATE_OFFERINGS = "CREATE TABLE " + TABLE_OFFERINGS + "("
                + KEY_ID + " INTEGER," + KEY_SUBJ + " TEXT ,"
                + KEY_CODE + " TEXT ," + KEY_TYPE + " TEXT,"
                + KEY_SEC + " INTEGER,"+ "PRIMARY KEY("+KEY_ID+"),"
                + "FOREIGN KEY("+KEY_SUBJ+") REFERENCES "+ TABLE_COURSES +"("+KEY_SUBJ+"),"
                + "FOREIGN KEY("+KEY_CODE+") REFERENCES "+ TABLE_COURSES +"("+KEY_CODE+")"
                + ")";
		
		String CREATE_OFFERING_TIMES = "CREATE TABLE " + TABLE_OFFERING_TIMES + "("
                + KEY_ID + " INTEGER," + KEY_DAY + " TEXT,"
                + KEY_TIMES + " TEXT ," + KEY_TIMEE + " TEXT,"
                + KEY_LOCATION + " TEXT,"+ "PRIMARY KEY("+KEY_ID+","+KEY_DAY+"),"
                + "FOREIGN KEY("+KEY_ID+") REFERENCES "+ TABLE_OFFERINGS +"("+KEY_ID+")"
                + ")";
		
		String CREATE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_SUBJ + " TEXT," + KEY_CODE + " TEXT,"
                + KEY_CID + " INTEGER," + KEY_FNAME + " TEXT,"
                + KEY_LNAME + " TEXT," + KEY_EMAIL + " TEXT,"+ "PRIMARY KEY("+KEY_CID+"),"
                + "FOREIGN KEY("+KEY_SUBJ+") REFERENCES "+ TABLE_COURSES +"("+KEY_SUBJ+"),"
                + "FOREIGN KEY("+KEY_CODE+") REFERENCES "+ TABLE_COURSES +"("+KEY_CODE+")"
                + ")";	
		
        db.execSQL(CREATE_COURSES);
        db.execSQL(CREATE_TASKS);
        db.execSQL(CREATE_OFFERINGS);
        db.execSQL(CREATE_OFFERING_TIMES);
        db.execSQL(CREATE_CONTACTS);
        
	}

	/* on an upgrade drop tables and recreate*/
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERING_TIMES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);
	}
	
	/* addCourse - adds all information for a course to the database
	 * adding course, offerings, tasks and contacts information, if 
	 * information exists for the course then an update is done, otherwise
	 * and insert is done
	 */
	public void addCourse(Course course){
		SQLiteDatabase db = this.getWritableDatabase();
	    ContentValues values = new ContentValues();
	    long num=0;
	    boolean update = false;
	    num=DatabaseUtils.queryNumEntries(db, TABLE_COURSES, KEY_SUBJ+" ='"+course.mSubject+"' AND "+KEY_CODE+"='"+course.mCode+"'");
	    if (num>0) update = true;
	    
	    //values.put(KEY_ID, course.mId); // Course id
	    values.put(KEY_SUBJ, course.mSubject); // subject code
	    values.put(KEY_CODE, course.mCode);
	    values.put(KEY_DESC, course.mDesc);
	    values.put(KEY_INSTRUCTOR, course.mInstructor);
	    values.put(KEY_INSTREMAIL, course.mInstructor_email);
	    // Inserting Row
	    if (update) db.update(TABLE_COURSES, values, KEY_SUBJ+" ='"+course.mSubject+"' AND "+KEY_CODE+"='"+course.mCode+"'", null);
	    else db.insert(TABLE_COURSES, null, values);
	    db.close(); // Closing database connection
	    values.clear();
	    addOfferings(course);
	    addTasks(course);	
	    addContacts(course.mContacts);
	    db.close();    
	}
	
	/* deleteCourse - removes all information for the given course from the databse*/
	public void deleteCourse(Course course){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COURSES, KEY_SUBJ + "='" + course.mSubject + "' AND " + KEY_CODE + "='" + course.mCode+"'", null);
		db.close();
	}
	
	/* getCourse - retreives all information for the given course */
	public Course getCourse(String subj, String code){
		SQLiteDatabase db = this.getReadableDatabase();
		Course course = new Course();
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_COURSES + " where " + KEY_SUBJ + "= '" + subj + "' and " + KEY_CODE + " = '" + code + "'", null);
		if (c != null){
			if (c.moveToFirst()){
				do{
					course.mSubject = c.getString(c.getColumnIndex(KEY_SUBJ));
					course.mCode = c.getString(c.getColumnIndex(KEY_CODE));
					course.mDesc = c.getString(c.getColumnIndex(KEY_DESC));
					course.mInstructor = c.getString(c.getColumnIndex(KEY_INSTRUCTOR));
					course.mInstructor_email = c.getString(c.getColumnIndex(KEY_INSTREMAIL));
					course.mOfferings = getOfferings(course.mSubject, course.mCode);
					course.mTasks = getTasks(course);
					course.mContacts = getContacts(course);
				}while (c.moveToNext());
			}
		}
		c.close();
		db.close();
		return course;
	}
	
	/*addOfferings - adds all offerings offered by a particular course
	 * as well as their offering times
	*/
	public void addOfferings(Course course){
		Offering offering;
		OfferingTime offeringtime;
		ContentValues values = new ContentValues();
		SQLiteDatabase db = this.getWritableDatabase();
		long num=0;
	    boolean update = false;
		for (int i = 0; i < course.mOfferings.size(); i++){
	    	offering = course.mOfferings.get(i);	
	    	num=0;
		    update = false;
		    num=DatabaseUtils.queryNumEntries(db, TABLE_OFFERINGS, KEY_SUBJ+" ='"+offering.mSubj+"' AND "+KEY_CODE+"='"+offering.mCode+"' AND "+KEY_TYPE+"='"+offering.mType+"' AND "+KEY_SEC+"="+offering.mSection);
		    if (num>0) update = true;
		    //values.put(KEY_ID,null); 
		    values.put(KEY_SUBJ, course.mSubject);
		    values.put(KEY_CODE, course.mCode);
		    values.put(KEY_TYPE, offering.mType);
		    values.put(KEY_SEC, offering.mSection);
		    if (update) db.update(TABLE_OFFERINGS, values, KEY_SUBJ+" ='"+offering.mSubj+"' AND "+KEY_CODE+"='"+offering.mCode+"' AND "+KEY_TYPE+"='"+offering.mType+"' AND "+KEY_SEC+"="+offering.mSection, null);
		    else db.insert(TABLE_OFFERINGS, null, values);
		    values.clear();
		    for (int j = 0; j < offering.mOfferingTimes.size(); j++){
		    	offeringtime = offering.mOfferingTimes.get(j);		    	
		    	num=0;
			    update = false;
			    num=DatabaseUtils.queryNumEntries(db, TABLE_OFFERING_TIMES, KEY_ID+" ="+offering.mId);
			    if (num>1) update = true;
			    
			    SQLiteDatabase rdb = this.getReadableDatabase();
			    Cursor c=rdb.rawQuery("SELECT "+KEY_ID+" FROM "+TABLE_OFFERINGS+" WHERE "+KEY_SUBJ+"='"+offering.mSubj+"' AND "+KEY_CODE+"='"+offering.mCode+"' AND "+KEY_TYPE+"='"+offering.mType+"' AND "+KEY_SEC+"="+offering.mSection, null);
			    c.moveToFirst();
			    offering.mId = c.getInt(c.getColumnIndex(KEY_ID));
		    	values.put(KEY_ID, offering.mId); 
			    values.put(KEY_DAY, offeringtime.mDay);
			    values.put(KEY_TIMES, offeringtime.mStartTime);
			    values.put(KEY_TIMEE, offeringtime.mEndTime);
			    values.put(KEY_LOCATION, offeringtime.mLocation);
			    if (update) db.update(TABLE_OFFERING_TIMES, values, KEY_ID+" ="+offering.mId, null);
			    else db.insert(TABLE_OFFERING_TIMES, null, values);
			    values.clear();
			    c.close();
			    rdb.close();
		    }
	    }
		db.close();
	}
	
	/* deleteOffering - removes all information from the databse for the
	 * given offering
	 */
	public void deleteOffering(Offering offering){
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_OFFERINGS, KEY_ID + "=" + offering.mId, null);
		db.close();
	}
	
	/* addTasks - adds all tasks associated with a given course*/
	public void addTasks(Course course){
		Task task;
		Contacts contact;
		ContentValues values = new ContentValues();
		SQLiteDatabase db = this.getWritableDatabase();
		long num=0;
	    boolean update = false;
		for (int i = 0; i < course.mTasks.size(); i++){
			task=course.mTasks.get(i);
			num=0;
		    update = false;
		    num=DatabaseUtils.queryNumEntries(db, TABLE_TASKS, KEY_ASSIGN+" ='"+task.mAssign+"' AND "+KEY_SUBJ+" ='"+task.mSubj+"' AND "+KEY_CODE+"='"+task.mCode+"'");
		    if (num>0) update = true;
		    values.put(KEY_SUBJ, task.mSubj);
		    values.put(KEY_CODE, task.mCode);
		    values.put(KEY_ASSIGN, task.mAssign);
		    values.put(KEY_NAME, task.mName);
		    values.put(KEY_MARK, task.mMark); 
		    values.put(KEY_BASE, task.mBase);
		    values.put(KEY_WEIGHT, task.mWeight);
		    values.put(KEY_DUE, task.mDueDate);
		    values.put(KEY_CREATE_DATE, task.mCreationDate);
		    values.put(KEY_PRIORITY, task.mPriority);
		    if (update) db.update(TABLE_TASKS, values, KEY_ASSIGN+" ="+task.mAssign+" AND "+KEY_SUBJ+" ='"+task.mSubj+"' AND "+KEY_CODE+"='"+task.mCode+"'", null);
		    else db.insert(TABLE_TASKS, null, values);
		    values.clear();
		    addContacts(task.mContacts);
	    }
		db.close();
	}
	
	/* addContacts - add contacts to the contacts table in the database
	 * for the given list of contacts
	 */
	public void addContacts(ArrayList<Contacts> contacts){
		Contacts contact;
		ContentValues values = new ContentValues();
		SQLiteDatabase db = this.getWritableDatabase();
		long num=0;
	    boolean update = false;
	    for (int j = 0; j < contacts.size(); j++){
	    	contact = contacts.get(j);
	    	num=0;
		    update = false;
		    num=DatabaseUtils.queryNumEntries(db, TABLE_CONTACTS, KEY_SUBJ+" ='"+contact.mSubj+"' AND "+KEY_CODE+"='"+contact.mCode+"'");
		    if (num>0) update = true;
	    	values.put(KEY_SUBJ, contact.mSubj);
		    values.put(KEY_CODE, contact.mCode);
		    //values.put(KEY_CID, contact.mId);
		    values.put(KEY_FNAME, contact.mFirstName);
		    values.put(KEY_LNAME, contact.mLastName); 
		    values.put(KEY_EMAIL, contact.mEmail);
		    if (update) db.update(TABLE_CONTACTS, values, KEY_SUBJ+" ='"+contact.mSubj+"' AND "+KEY_CODE+"='"+contact.mCode+"' AND "+KEY_FNAME+"='"+contact.mFirstName+"' AND "+KEY_LNAME+"='"+contact.mLastName+"' AND "+KEY_EMAIL+"='"+contact.mEmail+"'", null);
		    else db.insert(TABLE_CONTACTS, null, values);
		    values.clear();		    	
	    }
	    db.close();
	}
	
	/* addTasks - adds a task for a certian course*/
	public void addTasks(Task task){
		Course course = new Course();
		course.mTasks.add(task);
		addTasks(course);
	}
	
	/*getOfferings - gets all offerings for a given subject and code*/
	public ArrayList<Offering> getOfferings(String subj, String code){
		ArrayList<Offering> offerings = new ArrayList<Offering>();
		ArrayList<OfferingTime> offtimes = new ArrayList<OfferingTime>();
		Offering offering;
		OfferingTime otime;
		SQLiteDatabase db = this.getReadableDatabase();		
		Cursor c = db.rawQuery("SELECT *  FROM " + TABLE_OFFERINGS+" WHERE "+
		KEY_SUBJ+"='"+subj+"' and "+KEY_CODE+"='"+code+"'", null);
		try{
		if (c != null){
			if (c.moveToFirst()){
				do{	
					offering = new Offering();
					offering.mId = c.getInt(c.getColumnIndex(KEY_ID));
					offering.mSubj = c.getString(c.getColumnIndex(KEY_SUBJ));
					offering.mCode = c.getString(c.getColumnIndex(KEY_CODE));
					offering.mType = c.getString(c.getColumnIndex(KEY_TYPE));
					offering.mSection = c.getInt(c.getColumnIndex(KEY_SEC));					
					Cursor o = db.rawQuery("SELECT *  FROM " + TABLE_OFFERING_TIMES+" WHERE "+
							KEY_ID+"="+offering.mId, null);
					if(o != null){
						if(o.moveToFirst()){
							do{
								otime = new OfferingTime();
								otime.mOid = o.getInt(o.getColumnIndex(KEY_ID));
								otime.mDay = o.getString(o.getColumnIndex(KEY_DAY));
								otime.mStartTime = o.getString(o.getColumnIndex(KEY_TIMES));
								otime.mEndTime = o.getString(o.getColumnIndex(KEY_TIMEE));
								otime.mLocation = o.getString(o.getColumnIndex(KEY_LOCATION));
								offtimes.add(otime);
							}while (o.moveToNext());							
						}
					}
					offering.mOfferingTimes = offtimes;
					offerings.add(offering);
					o.close();				
				}while (c.moveToNext());
			}
		}
		c.close();
		db.close();
		}catch(Exception e){}
		return offerings;
	}
	
	/*getTasks - gets all tasks a person may have from the database*/
	public ArrayList<Task> getTasks(){
		ArrayList<Task> tasks = new ArrayList<Task>();
		SQLiteDatabase db = this.getReadableDatabase();
		Task task;
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TASKS, null);
		if (c != null){
			if (c.moveToFirst()){
				do{
					task = new Task();
					task.mSubj = c.getString(c.getColumnIndex(KEY_SUBJ));
					task.mCode = c.getString(c.getColumnIndex(KEY_CODE));
					task.mAssign = c.getInt(c.getColumnIndex(KEY_ASSIGN));
					task.mName = c.getString(c.getColumnIndex(KEY_NAME));
					task.mMark = c.getInt(c.getColumnIndex(KEY_MARK));
					task.mBase = c.getInt(c.getColumnIndex(KEY_BASE));
					task.mWeight = c.getFloat(c.getColumnIndex(KEY_WEIGHT));
					task.mDueDate = c.getString(c.getColumnIndex(KEY_DUE));
					task.mCreationDate = c.getString(c.getColumnIndex(KEY_CREATE_DATE));
					task.mPriority = c.getInt(c.getColumnIndex(KEY_PRIORITY));
					tasks.add(task);
				}while (c.moveToNext());
			}
		}
		c.close();
		db.close();
		return tasks;
	}
	
	/* getTasks - gets all tasks for a particular course*/
	private ArrayList<Task> getTasks(Course course){
		ArrayList<Task> tasks = new ArrayList<Task>();
		SQLiteDatabase db = this.getReadableDatabase();
		Task task;
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE "
				+KEY_SUBJ+"='"+course.mSubject+"' AND "+KEY_CODE+"='"+course.mCode+"'", null);
		if (c != null){
			if (c.moveToFirst()){
				do{
					task = new Task();
					task.mSubj = c.getString(c.getColumnIndex(KEY_SUBJ));
					task.mCode = c.getString(c.getColumnIndex(KEY_CODE));
					task.mAssign = c.getInt(c.getColumnIndex(KEY_ASSIGN));
					task.mName = c.getString(c.getColumnIndex(KEY_NAME));
					task.mMark = c.getInt(c.getColumnIndex(KEY_MARK));
					task.mBase = c.getInt(c.getColumnIndex(KEY_BASE));
					task.mWeight = c.getFloat(c.getColumnIndex(KEY_WEIGHT));
					task.mDueDate = c.getString(c.getColumnIndex(KEY_DUE));
					task.mCreationDate = c.getString(c.getColumnIndex(KEY_CREATE_DATE));
					task.mPriority = c.getInt(c.getColumnIndex(KEY_PRIORITY));
					task.mContacts = getContacts(course);
					tasks.add(task);					
				}while (c.moveToNext());
			}
		}
		c.close();	
		db.close();
		return tasks;
	}
	
	/* getContacts - get all contacts for a specified course*/
	private ArrayList<Contacts> getContacts(Course course){
		ArrayList<Contacts> contacts = new ArrayList<Contacts>();
		SQLiteDatabase db = this.getReadableDatabase();
		Contacts contact;
		Cursor c = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS + " WHERE "
				+KEY_SUBJ+"='"+course.mSubject+"' AND "+KEY_CODE+"='"+course.mCode+"'", null);
		if (c != null){
			if (c.moveToFirst()){
				do{
					contact = new Contacts();
					contact.mSubj = c.getString(c.getColumnIndex(KEY_SUBJ));
					contact.mCode = c.getString(c.getColumnIndex(KEY_CODE));
					contact.mId = c.getInt(c.getColumnIndex(KEY_CID));
					contact.mFirstName = c.getString(c.getColumnIndex(KEY_FNAME));
					contact.mLastName = c.getString(c.getColumnIndex(KEY_LNAME));
					contact.mEmail = c.getString(c.getColumnIndex(KEY_EMAIL));
					contacts.add(contact);
				}while (c.moveToNext());
			}
		}
		c.close();
		db.close();
		return contacts;
	}
	/* removeTask - deletes a given task from the tasks table of the database*/
	public void removeTask(Task task){
		SQLiteDatabase db = this.getWritableDatabase();
		db.rawQuery("DELETE * FROM "+TABLE_TASKS+" WHERE "+KEY_SUBJ+"='"+task.mSubj+
				"' AND "+KEY_CODE+"='"+task.mCode+"' AND "+KEY_ASSIGN+"="+task.mAssign,null);
		db.close();
	}
	
	/* getRegCourses - gets all courses added to the courses table of the database
	 * and all of it's components
	 */
	public ArrayList<Course> getRegCourses(){
		ArrayList<Course> courses = new ArrayList<Course>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT * FROM "+TABLE_COURSES, null);		
		if (c != null){
			if (c.moveToFirst()){
				do{
					courses.add(getCourse(c.getString(c.getColumnIndex(KEY_SUBJ)),c.getString(c.getColumnIndex(KEY_CODE))));
				}while (c.moveToNext());
			}
		}
		c.close();
		db.close();
		return courses;
		
	}
	
	/*Query - a general method to allow a query to be done that has not been
	 * specified. it returns a cursor to allow the data to be read
	 */
	public Cursor Query(String s){
		SQLiteDatabase db = this.getReadableDatabase();	
		Cursor c = db.rawQuery(s, null);
		db.close();
		return c;
	}
	
	
}
