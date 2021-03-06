/**
 * CourseHandler.java
 * Brock Butler
 * A class to allow easy access to database functions
 * Created by James Grisdale on 2013-02-24
 * Copyright (c) 2013 Sea Addicts. All rights reserved.
**/

package edu.seaaddicts.brockbutler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;

public class CourseHandler{
	//Context context;
	CurrentCoursesHandler CH;
	CourseListHandler courseList;

	public CourseHandler(Context context){
		//this.context = context;
		CH = new CurrentCoursesHandler(context);
		courseList = new CourseListHandler(context);
	}
	
	//getAllCourses - grabs course data from the registrar's timetable and
	//inserts data into the masterlist table
	public void getAllCourses(){
		courseList.addCourse();
	}
	
	//getCourse - gets all information for a given course subject and code
	public Course getCourse(final String subj, final String code){	
		return CH.getCourse(subj, code);
	}
	
	//updateCourse - updates all the information for a given course
	public void updateCourse(Course course){
		CH.addCourse(course);
	}
	
	//getSubjects - gets a list of subjects available from the master list
	public ArrayList<String> getSubjects() throws Exception{	
		return courseList.getSubjects();
	}
	
	//getCodes - gets a list of codes for a given subject from the master list
	public ArrayList<String> getCodes(String subj){
		return courseList.getCodes(subj);		
	}
	
	//getCourseOfferings - returns all offerings offered for a given course
	public ArrayList<MasterCourse> getCourseOfferings(String subj, String code){
		return courseList.getCourses(subj, code);
	}
	
	//addCourse - adds information for a course into the database
	public int addCourse(Course course) throws Exception{		
		try{
			CH.addCourse(course);
			return 0;
		}
		catch(Exception e){return 1;}	
	}
	
	//removeCourse - deletes all information from the database for a course
	public int removeCourse(Course course){
		try{
			CH.deleteCourse(course);
			return 0;
		}catch(Exception e){return 1;}		
	}
	
	/*getRegisteredCourses - returns all information for all courses in the 
	* current courses database
	*/
	public ArrayList<Course> getRegisteredCourses(){
		return CH.getRegCourses();
	}
	
	//getOfferings - get all offerings for a certain course
	public ArrayList<Offering> getOfferings(String subj, String code){
		return CH.getOfferings(subj, code);
	}
	
	//getTasks - gets all tasks from the database
	public ArrayList<Task> getTasks(){
		return CH.getTasks();
	}
	
	//addTask - adds a given task to the task table in the database
	public int addTask(Task task){
		try{
		CH.addTasks(task);
		return 0;
		}catch(Exception e){return 1;}
	}
	
	//addTask - adds the tasks for a given course to the task table in the database
	public int addTask(Course course){
		try{
		CH.addTasks(course);
		return 0;
		}catch(Exception e){return 1;}
	}
	
	//removeTask - deletes task information from the database for a given task
	public int removeTask(Task task){
		try{
		CH.removeTask(task);
		return 0;
		}catch(Exception e){return 1;}
	}
	
	//getMark - returns the calculated progress mark for a course
	public float getMark(Course course){
		float mark=0;
		for(int i=0; i<course.mTasks.size(); i++) 
			mark+=(course.mTasks.get(i).mMark / course.mTasks.get(i).mBase) * 
			course.mTasks.get(i).mWeight;
		return mark;
	}
	
	//Query - returns a cursor with results for a custom query
	public Cursor Query(String query){
		return CH.Query(query);
	}
}
