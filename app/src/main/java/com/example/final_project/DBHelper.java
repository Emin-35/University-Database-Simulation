package com.example.final_project;

// DBHelper.java
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BilgiUniversityDB";
    private static final int DATABASE_VERSION = 2;
    //----------------------------------------TABLES----------------------------------------
    // Table names
    private static final String TABLE_FACULTY = "faculty";
    private static final String TABLE_DEPARTMENT = "department";
    private static final String TABLE_LECTURER = "lecturer";
    private static final String TABLE_STUDENT = "student";
    private static final String TABLE_REGISTERED = "registered_student";
    //--------------------------------------VALUES--------------------------------------
    // Define table and column names
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FACULTY_NAME = "faculty_name";

    // Columns for department table
    public static final String COLUMN_DEPARTMENT_NAME = "department_name";
    private static final String COLUMN_FACULTY_ID = "faculty_id";

    // Columns for lecturer table
    public static final String COLUMN_LECTURER_NAME = "lecturer_name";
    private static final String COLUMN_DEPARTMENT_ID = "department_id";

    //-----------------------------------------------------------------------------------------------
    // Columns for Student table
    public static final String COLUMN_STUDENT_ID = "studentID";
    public static final String COLUMN_STUDENT_NAME = "name";
    public static final String COLUMN_STUDENT_LASTNAME = "lastName";
    public static final String COLUMN_STUDENT_GENDER = "gender";
    public static final String COLUMN_STUDENT_FACULTY = "faculty";
    public static final String COLUMN_STUDENT_DEPARTMENT = "department";
    public static final String COLUMN_STUDENT_LECTURER = "lecturer";

    // Create TABLE_FACULTY
    private static final String CREATE_TABLE_FACULTY =
            "CREATE TABLE IF NOT EXISTS " + TABLE_FACULTY + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FACULTY_NAME + " TEXT)";

    // Create TABLE_DEPARTMENT
    private static final String CREATE_TABLE_DEPARTMENT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_DEPARTMENT + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_DEPARTMENT_NAME + " TEXT," +
                    COLUMN_FACULTY_ID + " INTEGER REFERENCES " + TABLE_FACULTY + "(" + COLUMN_ID + "))";

    // Create TABLE_LECTURER
    private static final String CREATE_TABLE_LECTURER =
            "CREATE TABLE IF NOT EXISTS " + TABLE_LECTURER + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_LECTURER_NAME + " TEXT," +
                    COLUMN_DEPARTMENT_ID + " INTEGER REFERENCES " + TABLE_DEPARTMENT + "(" + COLUMN_ID + "))";

    // Create TABLE_STUDENT
    private static final String CREATE_TABLE_STUDENT =
            "CREATE TABLE IF NOT EXISTS " + TABLE_STUDENT + "(" +
                    COLUMN_STUDENT_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_STUDENT_NAME + " TEXT, " +
                    COLUMN_STUDENT_LASTNAME + " TEXT, " +
                    COLUMN_STUDENT_GENDER + " TEXT, " +
                    COLUMN_STUDENT_FACULTY + " TEXT, " +
                    COLUMN_STUDENT_DEPARTMENT + " TEXT, " +
                    COLUMN_STUDENT_LECTURER + " TEXT)";
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the administrator table
        db.execSQL(CREATE_TABLE_FACULTY);
        db.execSQL(CREATE_TABLE_DEPARTMENT);
        db.execSQL(CREATE_TABLE_LECTURER);
        // Create the student table
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade logic here if needed
    }

//-------------------------------------------------------HELPER FUNCTIONS-----------------------------------------------------------

//-------------------------------------------------------ADMINISTRATION FUNCTIONS-------------------------------------------------------

    //-------------------------------------------------------FACULTY FUNCTIONS------------------------------------------------------

    // Method to add faculty
    public void addFaculty(String facultyName) {
        // Open a writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a ContentValues object to store the lecturer information
        ContentValues values = new ContentValues();
        // Put the faculty name into the ContentValues
        values.put(COLUMN_FACULTY_NAME, facultyName);
        // Insert the new row (faculty) into the TABLE_FACULTY
        db.insert(TABLE_FACULTY, null, values);
        // Close database
        db.close();
    }

    // Method to check if faculty exists and return its id
    public long getFacultyId(String facultyName) {
        // Open a readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Define the columns to be retrieved from the database
        String[] columns = {COLUMN_ID};
        // Specify the selection criteria (WHERE clause)
        String selection = COLUMN_FACULTY_NAME + "=?";
        // Specify the values for the selection criteria
        String[] selectionArgs = {facultyName};

        // Execute a query on the database to retrieve data
        Cursor cursor = db.query(TABLE_FACULTY, columns, selection, selectionArgs, null, null, null);
        long facultyId = 1; // Default value if not found

        // Check if the cursor contains any rows (records) matching the query
        if (cursor.moveToFirst()) {
            // Retrieve the index of the COLUMN_ID column
            int columnIndex = cursor.getColumnIndex(COLUMN_ID);
            // Check if the COLUMN_ID column index is valid
            if (columnIndex != -1) {
                // Retrieve the facultyId from the cursor
                facultyId = cursor.getLong(columnIndex);
            }
        }
        // Close the cursor to release its resources
        cursor.close();
        // Close the database
        db.close();
        // Return the retrieved or default facultyId
        return facultyId;
    }

    // Method to check if faculty exists
    public boolean isFacultyExists(String facultyName) {
        // Open a readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Define the columns to be retrieved from the database
        String[] columns = {COLUMN_ID};
        // Specify the selection criteria (WHERE clause)
        String selection = COLUMN_FACULTY_NAME + "=?";
        // Specify the values for the selection criteria
        String[] selectionArgs = {facultyName};

        /*
            Execute a query on the database to retrieve data
            Table name, Columns to retrieve, Selection criteria, Values for selection criteria,
            Group by (not used in this case), Having (not used in this case), Order by (not used in this case)
         */
        Cursor cursor = db.query(TABLE_FACULTY, columns, selection, selectionArgs, null, null, null);
        // Check if the cursor contains any rows (records) matching the query
        boolean exists = cursor.getCount() > 0;
        // Close the cursor to release its resources
        cursor.close();
        // Close the database
        db.close();
        // Return whether the faculty exists in the database
        return exists;
    }

    // Method to check if the given faculty name is already in the table or not
    public boolean isFacultyNameUnique(String facultyName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_FACULTY_NAME + "=?";
        String[] selectionArgs = {facultyName};

        Cursor cursor = db.query(TABLE_FACULTY, columns, selection, selectionArgs, null, null, null);

        boolean isUnique = cursor.getCount() == 0; // If count is 0, it's unique

        cursor.close();
        db.close();

        return isUnique;
    }

    //-------------------------------------------------------DEPARTMENT FUNCTIONS---------------------------------------------------

    // Method to add department under a faculty
    public void addDepartment(String departmentName, long facultyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Put the department name and faculty ID into the ContentValues
        values.put(COLUMN_DEPARTMENT_NAME, departmentName);
        values.put(COLUMN_FACULTY_ID, facultyId);
        // Insert the new row (department name and faculty ID) into the TABLE_DEPARTMENT
        db.insert(TABLE_DEPARTMENT, null, values);
        db.close();
    }

    public long getDepartmentId(String departmentName, long facultyId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns to be retrieved from the database
        String[] columns = {COLUMN_ID};

        // Specify the selection criteria (WHERE clause)
        String selection = COLUMN_DEPARTMENT_NAME + "=? AND " + COLUMN_FACULTY_ID + "=?";

        // Specify the values for the selection criteria
        String[] selectionArgs = {departmentName, String.valueOf(facultyId)};

        // Execute a query on the database to retrieve data
        Cursor cursor = db.query(TABLE_DEPARTMENT, columns, selection, selectionArgs, null, null, null);

        long departmentID = -1; // Default value if not found

        // Check if the cursor contains any rows (records) matching the query
        if (cursor.moveToFirst()) {
            // Retrieve the index of the COLUMN_ID column
            int columnIndex = cursor.getColumnIndex(COLUMN_ID);
            if (columnIndex != -1) { // Check if the COLUMN_ID column index is valid
                departmentID = cursor.getLong(columnIndex);
            }
        }

        cursor.close();
        db.close();

        // Return the retrieved or default departmentID
        return departmentID;
    }

    // Method to check if a department exists in the SQLite database
    public boolean isDepartmentExists(String departmentName) {
        // Open a readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Define the columns to be retrieved from the database
        String[] columns = {COLUMN_ID};
        // Specify the selection criteria (WHERE clause)
        String selection = COLUMN_DEPARTMENT_NAME + "=?";
        // Specify the values for the selection criteria
        String[] selectionArgs = {departmentName};

        //Execute a query on the database to retrieve data
        Cursor cursor = db.query(TABLE_DEPARTMENT, columns, selection, selectionArgs, null, null, null);
        // Check if the cursor contains any rows (records) matching the query
        boolean exists = cursor.getCount() > 0;
        // Close the cursor to release its resources
        cursor.close();
        // Close the database
        db.close();
        // Return whether the department exists in the database
        return exists;
    }

    // Method to check if the given department name is already in the table or not
    public boolean isDepartmentNameUnique(String departmentName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_DEPARTMENT_NAME + "=?";
        String[] selectionArgs = {departmentName};

        Cursor cursor = db.query(TABLE_DEPARTMENT, columns, selection, selectionArgs, null, null, null);

        boolean isUnique = cursor.getCount() == 0; // If count is 0, it's unique

        cursor.close();
        db.close();

        return isUnique;
    }

    //-------------------------------------------------------LECTURER FUNCTIONS-----------------------------------------------------

    // Method to add lecturer under a department
    public long addLecturer(String lecturerName, long departmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // Put the lecturer name and department ID into the ContentValues
        values.put(COLUMN_LECTURER_NAME, lecturerName);
        values.put(COLUMN_DEPARTMENT_ID, departmentId);
        // Insert the new row (lecturer) into the TABLE_LECTURER
        long newRowId = db.insert(TABLE_LECTURER, null, values);
        db.close();
        return newRowId;
    }

    // Method to find given Lecturer's ID by his name and departmentID
    public long findLecturerId(String lecturerName, long departmentID) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Define the columns to be retrieved from the database
        String[] columns = {COLUMN_ID};
        // Specify the selection criteria (AND clause)
        String selection = COLUMN_DEPARTMENT_ID + "=? AND " + COLUMN_LECTURER_NAME + "=?";
        // Specify the values for the selection criteria
        String[] selectionArgs = {String.valueOf(departmentID), lecturerName};

        // Execute a query on the database to retrieve data
        Cursor cursor = db.query(TABLE_LECTURER, columns, selection, selectionArgs, null, null, null);

        long foundLecturerId = 1; // Default value if not found

        // Check if the cursor contains any rows (records) matching the query
        if (cursor.moveToFirst()) { // Retrieve the index of the COLUMN_ID column
            int columnIndex = cursor.getColumnIndex(COLUMN_ID);
            if (columnIndex != -1) { // Check if the COLUMN_ID column index is valid
                foundLecturerId = cursor.getLong(columnIndex);
            }
        }
        cursor.close();
        db.close();

        return foundLecturerId;
    }

    // Method to check if lecturer exists
    public boolean isLecturerExists(String lecturerName) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Define the columns to be retrieved from the database
        String[] columns = {COLUMN_ID};
        // Specify the selection criteria (WHERE clause)
        String selection = COLUMN_LECTURER_NAME + "=?";
        // Specify the values for the selection criteria
        String[] selectionArgs = {lecturerName};
        // Execute a query on the database to retrieve data
        Cursor cursor = db.query(TABLE_LECTURER, columns, selection, selectionArgs, null, null, null);
        // Check if the cursor contains any rows (records) matching the query
        boolean exists = cursor.getCount() > 0;
        // Close the cursor to release its resources
        cursor.close();
        // Close the database
        db.close();
        // Return whether the lecturer exists in the database
        return exists;
    }

    // Method to check if the given lecturer name is already in the table or not
    public boolean isLecturerNameUnique(String lectureName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_LECTURER_NAME + "=?";
        String[] selectionArgs = {lectureName};

        Cursor cursor = db.query(TABLE_LECTURER, columns, selection, selectionArgs, null, null, null);

        boolean isUnique = cursor.getCount() == 0; // If count is 0, it's unique

        cursor.close();
        db.close();

        return isUnique;
    }

    //-------------------------------------------------------COUNT FUNCTIONS--------------------------------------------------------

    // Method to retrieve the count of lecturers in a department from the SQLite database
    public int getLecturerCountInDepartment(long departmentId) {
        // Open a readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // Define the columns to be retrieved from the database
        String[] columns = {COLUMN_ID};
        // Specify the selection criteria (WHERE clause)
        String selection = COLUMN_DEPARTMENT_ID + "=?";
        // Specify the values for the selection criteria
        String[] selectionArgs = {String.valueOf(departmentId)};

        // Execute a query on the database to retrieve data
        Cursor cursor = db.query(TABLE_LECTURER, columns, selection, selectionArgs, null, null, null);
        // Get the count of lecturers from the cursor
        int lecturerCount = cursor.getCount();

        cursor.close();
        db.close();
        return lecturerCount;
    }

    public int getDepartmentCountInFaculty(long facultyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Define the columns to be retrieved from the database
        String[] columns = {COLUMN_ID};
        // Specify the selection criteria (WHERE clause)
        String selection = COLUMN_FACULTY_ID + "=?";
        // Specify the values for the selection criteria
        String[] selectionArgs = {String.valueOf(facultyId)};

        // Execute a query on the database to retrieve data
        Cursor cursor = db.query(TABLE_DEPARTMENT, columns, selection, selectionArgs, null, null, null);
        // Get the count of lecturers from the cursor
        int departmentCount = cursor.getCount();

        cursor.close();
        db.close();
        return departmentCount;
    }

    //-------------------------------------------------------DELETE FUNCTIONS-------------------------------------------------------
    public void deleteLecturer(long primaryDepartmentID, long lecturerID) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the lecturer from given table
        db.delete(TABLE_LECTURER, COLUMN_DEPARTMENT_ID + "=? AND " + COLUMN_ID + "=?", new String[]{String.valueOf(primaryDepartmentID), String.valueOf(lecturerID)});
    }

    public void deleteDepartment(long primaryFacultyID, long primaryDepartmentID) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete department
        db.delete(TABLE_DEPARTMENT, COLUMN_FACULTY_ID + "=? AND " + COLUMN_ID + "=?", new String[]{String.valueOf(primaryFacultyID), String.valueOf(primaryDepartmentID)});
    }

    public void deleteFaculty(long primaryFacultyID) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete faculty
        db.delete(TABLE_FACULTY,  COLUMN_ID + "=?", new String[]{String.valueOf(primaryFacultyID)});
    }

    //-------------------------------------------------------UPDATE FUNCTIONS-------------------------------------------------------

    // Update Lecturer
    public void updateLecturer(long primaryDepartmentID, long lecturerID, String newLecturerName) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a ContentValues object to store the updated values
        ContentValues values = new ContentValues();

        // Put the new lecturer name into the ContentValues
        values.put(COLUMN_LECTURER_NAME, newLecturerName);

        // Specify the update criteria (WHERE clause)
        String selection = COLUMN_DEPARTMENT_ID + "=? AND " + COLUMN_ID + "=?";

        // Specify the values for the update criteria
        String[] selectionArgs = {String.valueOf(primaryDepartmentID), String.valueOf(lecturerID)};

        // Update the row (lecturer) in the TABLE_LECTURER with the new values
        db.update(TABLE_LECTURER, values, selection, selectionArgs);

        // Close the database to release its resources
        db.close();
    }

    // Update Department
    public void updateDepartment(long primaryFacultyID, long departmentID, String newDepartmentName) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a ContentValues object to store the updated values
        ContentValues values = new ContentValues();

        // Put the new department name into the ContentValues
        values.put(COLUMN_DEPARTMENT_NAME, newDepartmentName);

        // Specify the update criteria (WHERE clause)
        String selection = COLUMN_FACULTY_ID + "=? AND " + COLUMN_ID + "=?";

        // Specify the values for the update criteria
        String[] selectionArgs = {String.valueOf(primaryFacultyID), String.valueOf(departmentID)};

        // Update the row (lecturer) in the TABLE_DEPARTMENT with the new values
        db.update(TABLE_DEPARTMENT, values, selection, selectionArgs);

        // Close the database to release its resources
        db.close();
    }

    // Update Faculty
    public void updateFaculty(long primaryFacultyID, String newFacultyName) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a ContentValues object to store the updated values
        ContentValues values = new ContentValues();

        // Put the new faculty name into the ContentValues
        values.put(COLUMN_FACULTY_NAME, newFacultyName);

        // Specify the update criteria (WHERE clause)
        String selection = COLUMN_ID + "=?";

        // Specify the values for the update criteria
        String[] selectionArgs = {String.valueOf(primaryFacultyID)};

        // Update the row (lecturer) in the TABLE_FACULTY with the new values
        db.update(TABLE_FACULTY, values, selection, selectionArgs);

        // Close the database to release its resources
        db.close();
    }

    //-------------------------------------------------------UPDATE FUNCTIONS-------------------------------------------------------

    //-------------------------------------------------------SEARCH FUNCTION--------------------------------------------------------

    // Method for showing the entire database also return an ArrayList<String> so that we can modify it.
    public ArrayList<String> showAdminDatabase(Context context, ListView listView) {

        // Query to join Lecturer, Department, and Faculty tables (INNER JOIN)
        String query = "SELECT " +
                TABLE_LECTURER + "." + COLUMN_LECTURER_NAME + "," +  //-- Lecturer table: lecturer name
                TABLE_DEPARTMENT + "." + COLUMN_DEPARTMENT_NAME + "," + //-- Department table: department name
                TABLE_FACULTY + "." + COLUMN_FACULTY_NAME + //-- Faculty table: faculty name
                " FROM " + TABLE_LECTURER + //-- From clause specifying the tables involved in the query
                " INNER JOIN " + TABLE_DEPARTMENT + //-- Inner join with the DEPARTMENT table based on the matching department IDs
                " ON " + TABLE_LECTURER + "." + COLUMN_DEPARTMENT_ID + " = " + TABLE_DEPARTMENT + "." + COLUMN_ID +
                " INNER JOIN " + TABLE_FACULTY + //-- Inner join with the FACULTY table based on the matching faculty IDs
                " ON " + TABLE_DEPARTMENT + "." + COLUMN_FACULTY_ID + " = " + TABLE_FACULTY + "." + COLUMN_ID;

        //Create a db instance for reading only
        SQLiteDatabase db = this.getReadableDatabase();
        //Create a cursor
        Cursor cursor;

        //Create an arrayList the put the data's from database
        ArrayList<String> lecturerInfoList  = new ArrayList<>();
        //Create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lecturerInfoList );

        //Check if database is not null
        if (db != null) {
            //Store the data in cursor
            cursor = db.rawQuery(query,null);

            while (cursor.moveToNext()){
                //Get all the values from the query we created lecturerName,departmentName,facultyName
                String lecturerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LECTURER_NAME));
                String departmentName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTMENT_NAME));
                String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACULTY_NAME));
                //We put "--" between values so that we can separated once more when needed
                String result = lecturerName + "--" + departmentName + "--" + facultyName;
                //Add it to arrayList
                lecturerInfoList.add(result);
            }

            cursor.close(); // Close the cursor after use
        }
        //If database is empty
        else {
            Toast.makeText(context, "Database is empty", Toast.LENGTH_SHORT).show();
        }
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        db.close();

        //Return the lecturerInfoList arrayList
        return lecturerInfoList;
    }

    //-------------------------------------------------------SEARCH FUNCTION--------------------------------------------------------

//-------------------------------------------------------ADMINISTRATION FUNCTIONS-------------------------------------------------------

//-------------------------------------------------------REGISTRATION FUNCTIONS---------------------------------------------------------

    //---------------------------------------------------GET FACULTIES FUNCTION-----------------------------------------------------
    public ArrayList<String> getFaculties(Context context) {
        //Create a db instance for reading only
        SQLiteDatabase db = this.getReadableDatabase();
        //Create a cursor
        Cursor cursor;
        //Create ArrayList for all the faculty names
        ArrayList<String> allFaculties = new ArrayList<>();

        //Select all the faculty names from faculty table
        String query = "SELECT faculty_name FROM "+ TABLE_FACULTY;

        //Create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, allFaculties);

        //Check if database is not null
        if (db != null) {
            //Store the data in cursor
            cursor = db.rawQuery(query,null);

            //While there is still data in database
            while (cursor.moveToNext()) {
                String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACULTY_NAME));
                //We put all the faculty names in database
                String result = facultyName + "";
                //Add it to arrayList
                allFaculties.add(result);
            }

            cursor.close(); // Close the cursor after use
        }
        //If database is empty
        else {
            Toast.makeText(context, "Database is empty", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        db.close();

        return allFaculties;
    }
    //---------------------------------------------------GET FACULTIES FUNCTION-----------------------------------------------------

    //--------------------------------------------------GET DEPARTMENTS FUNCTION----------------------------------------------------
    public ArrayList<String> getDepartments(Context context, long facultyID) {
        //Create a db instance for reading only
        SQLiteDatabase db = this.getReadableDatabase();
        //Create a cursor
        Cursor cursor;
        //Create ArrayList for all the department names
        ArrayList<String> allDepartments = new ArrayList<>();

        //Select all the correct department names(JOIN) from department table that is COLUMN_FACULTY_ID = facultyID
        String query = "SELECT " + TABLE_DEPARTMENT + "." + COLUMN_DEPARTMENT_NAME + // Department table: department name
                " FROM " + TABLE_DEPARTMENT +
                " JOIN " + TABLE_FACULTY +
                " ON " + TABLE_DEPARTMENT + "." + COLUMN_FACULTY_ID + " = " + TABLE_FACULTY + "." + COLUMN_ID +
                " WHERE " + TABLE_FACULTY + "." + COLUMN_ID + " = " + facultyID;

        //Create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, allDepartments);

        //Check if database is not null
        if (db != null) {
            //Store the data in cursor
            cursor = db.rawQuery(query,null);

            //While there is still data in database
            while (cursor.moveToNext()) {
                String departmentName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DEPARTMENT_NAME));
                //We put all the faculty names in database
                String result = departmentName + "";
                //Add it to arrayList
                allDepartments.add(result);
            }

            cursor.close(); // Close the cursor after use
        }
        //If database is empty
        else {
            Toast.makeText(context, "Database is empty", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        db.close();

        return allDepartments;
    }
    //--------------------------------------------------GET DEPARTMENTS FUNCTION----------------------------------------------------

    //--------------------------------------------------GET LECTURER FUNCTION-------------------------------------------------------
    public ArrayList<String> getLecturers(Context context, long departmentID) {
        //Create a db instance for reading only
        SQLiteDatabase db = this.getReadableDatabase();
        //Create a cursor
        Cursor cursor;
        //Create ArrayList for all the department names
        ArrayList<String> allLecturers = new ArrayList<>();

        //Select all the correct department names(JOIN) from department table that is COLUMN_FACULTY_ID = facultyID
        String query = "SELECT " + TABLE_LECTURER + "." + COLUMN_LECTURER_NAME + // Department table: department name
                " FROM " + TABLE_LECTURER +
                " JOIN " + TABLE_DEPARTMENT +
                " ON " + TABLE_LECTURER + "." + COLUMN_DEPARTMENT_ID + " = " + TABLE_DEPARTMENT + "." + COLUMN_ID +
                " WHERE " + TABLE_DEPARTMENT + "." + COLUMN_ID + " = " + departmentID;

        //Create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, allLecturers);

        //Check if database is not null
        if (db != null) {
            //Store the data in cursor
            cursor = db.rawQuery(query,null);

            //While there is still data in database
            while (cursor.moveToNext()) {
                String LecturerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LECTURER_NAME));
                //We put all the faculty names in database
                String result = LecturerName + "";
                //Add it to arrayList
                allLecturers.add(result);
            }

            cursor.close(); // Close the cursor after use
        }
        //If database is empty
        else {
            Toast.makeText(context, "Database is empty", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        db.close();

        return allLecturers;
    }

    //--------------------------------------------------GET LECTURER FUNCTION-------------------------------------------------------

    //--------------------------------------------------STUDENT FUNCTIONS-------------------------------------------------------

    //Check if the given studentID is unique
    public boolean isStudentIDUnique(String studentID) {
        SQLiteDatabase db = this.getReadableDatabase();
        //Search in all the student IDs
        String[] columns = {COLUMN_STUDENT_ID};
        String selection = COLUMN_STUDENT_ID + "=?";
        String[] selectionArgs = {studentID};

        Cursor cursor = db.query(TABLE_STUDENT, columns, selection, selectionArgs, null, null, null);

        boolean isUnique = cursor.getCount() == 0; // If count is 0, it's unique

        cursor.close();
        db.close();

        return isUnique;
    }

    public ArrayList<String> findStudentByID(Context context, String studentID) {
        //Create a db instance for reading only
        SQLiteDatabase db = this.getReadableDatabase();
        //Create a cursor
        Cursor cursor;
        // Select student information with lecturer name based on student ID
        String query = "SELECT " +
                COLUMN_STUDENT_NAME + ", " +
                COLUMN_STUDENT_LASTNAME + ", " +
                COLUMN_STUDENT_GENDER + ", " +
                COLUMN_STUDENT_FACULTY + ", " +
                COLUMN_STUDENT_DEPARTMENT + ", " +
                COLUMN_STUDENT_LECTURER +
                " FROM " + TABLE_STUDENT +
                " WHERE " + COLUMN_STUDENT_ID + " = " + studentID;

        //Create an String array for all the student Information
        ArrayList<String> studentInfo = new ArrayList<>();

        //Create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, studentInfo);

        //Check if database is not null
        if (db != null) {
            //Store the data in cursor
            cursor = db.rawQuery(query,null);

            //While there is still data in database
            while (cursor.moveToNext()) {
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME));
                String studentLastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_LASTNAME));
                String studentGender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_GENDER));
                String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_FACULTY));
                String departmentName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_DEPARTMENT));
                String lecturerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_LECTURER));
                //We put all the student information in result
                String result = studentName + "--" + studentLastName + "--" + studentGender + "--" +
                        facultyName + "--" + departmentName + "--" + lecturerName;
                //Add it to arrayList
                studentInfo.add(result);
            }

            cursor.close(); // Close the cursor after use
        }
        //If database is empty
        else {
            Toast.makeText(context, "Student database is empty", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        db.close();

        return studentInfo;
    }

    //Add student to database
    public void addStudent(String studentID,
                           String name, String lastName,
                           String gender,
                           String faculty, String department, String lecturer) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Add all the given information to STUDENT TABLE
        values.put(COLUMN_STUDENT_ID, studentID);
        values.put(COLUMN_STUDENT_NAME, name);
        values.put(COLUMN_STUDENT_LASTNAME, lastName);
        values.put(COLUMN_STUDENT_GENDER, gender);
        values.put(COLUMN_STUDENT_FACULTY, faculty);
        values.put(COLUMN_STUDENT_DEPARTMENT, department);
        values.put(COLUMN_STUDENT_LECTURER, lecturer);

        db.insert(TABLE_STUDENT, null, values);
        db.close();
    }

    //Delete student from Student Table
    public void deleteStudent(String studentID) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the student from Student Table according to it's Primary Key equal to given StudentID
        db.delete(TABLE_STUDENT, COLUMN_STUDENT_ID + " =? ", new String[]{studentID});
        db.close();
    }

    //Update student by given new info
    public void updateStudentInfo(Context context, String studentID,
                                  String updateStudentName, String updateStudentLastName, String updateStudentGender,
                                  String updateFacultyName, String updateDepartmentName, String updateLecturerName) {
        //Connect to the database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //If the student male
        if(updateStudentGender.toLowerCase().equals("male")) {
            // Update the values based on the provided parameters
            values.put(COLUMN_STUDENT_NAME, updateStudentName);
            values.put(COLUMN_STUDENT_LASTNAME, updateStudentLastName);
            values.put(COLUMN_STUDENT_GENDER, "Male");
            values.put(COLUMN_STUDENT_FACULTY, updateFacultyName);
            values.put(COLUMN_STUDENT_DEPARTMENT, updateDepartmentName);
            values.put(COLUMN_STUDENT_LECTURER, updateLecturerName);

            // Update the row where COLUMN_STUDENT_ID is equal to the given studentID
            db.update(TABLE_STUDENT, values, COLUMN_STUDENT_ID + " =? ", new String[]{studentID});
        }
        //If the student female
        else if (updateStudentGender.toLowerCase().equals("female")) {
            // Update the values based on the provided parameters
            values.put(COLUMN_STUDENT_NAME, updateStudentName);
            values.put(COLUMN_STUDENT_LASTNAME, updateStudentLastName);
            values.put(COLUMN_STUDENT_GENDER, "Female");
            values.put(COLUMN_STUDENT_FACULTY, updateFacultyName);
            values.put(COLUMN_STUDENT_DEPARTMENT, updateDepartmentName);
            values.put(COLUMN_STUDENT_LECTURER, updateLecturerName);

            // Update the row where COLUMN_STUDENT_ID is equal to the given studentID
            db.update(TABLE_STUDENT, values, COLUMN_STUDENT_ID + " =? ", new String[]{studentID});
        }
        else
            Toast.makeText(context, "This student is not exists", Toast.LENGTH_SHORT).show();

        // Close the database
        db.close();
    }

    //Print the student database
    public ArrayList<String> showStudentDatabase(Context context, ListView listView) {
        // Query to select Student IDs from TABLE_STUDENT
        String query = "SELECT " + COLUMN_STUDENT_ID + " FROM " + TABLE_STUDENT;

        //Create a db instance for reading only
        SQLiteDatabase db = this.getReadableDatabase();
        //Create a cursor
        Cursor cursor;

        //Create an arrayList the put the data's from database
        ArrayList<String> studentInfoList  = new ArrayList<>();
        //Create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, studentInfoList);

        //Check if database is not null
        if (db != null) {
            //Store the data in cursor
            cursor = db.rawQuery(query,null);

            while (cursor.moveToNext()){
                // Secure all the StudentIDs
                String studentID = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID));

                //Add them to result
                String result = studentID;
                //Add it to arrayList
                studentInfoList.add(result);
            }

            cursor.close(); // Close the cursor after use
        }
        //If database is empty
        else {
            Toast.makeText(context, "Database is empty", Toast.LENGTH_SHORT).show();
        }

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        db.close();

        //Return the lecturerInfoList arrayList
        return studentInfoList;
    }
    //--------------------------------------------------STUDENT FUNCTIONS-------------------------------------------------------

    //-------------------------------------------------REGISTERED FUNCTIONS-----------------------------------------------------
    public ArrayList<String> showStudentByName (Context context, ListView listView) {
        // Query to select Student all columns from TABLE_STUDENT
        String query = "SELECT " + COLUMN_STUDENT_ID +","+ COLUMN_STUDENT_NAME +"," + COLUMN_STUDENT_LASTNAME + " FROM " + TABLE_STUDENT;

        //Create a db instance for reading only
        SQLiteDatabase db = this.getReadableDatabase();
        //Create a cursor
        Cursor cursor;

        //Create an arrayList the put the data's from database
        ArrayList<String> studentInfoList2  = new ArrayList<>();
        //Create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, studentInfoList2);

        //Check if database is not null
        if (db != null) {
            //Store the data in cursor
            cursor = db.rawQuery(query,null);

            while (cursor.moveToNext()){
                // Secure all the StudentIDs
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME));
                String studentLastname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_LASTNAME));
                String studentID = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID));
                //Add them to result
                String result = studentName +" "+ studentLastname+" - " + studentID;
                //Add it to arrayList
                studentInfoList2.add(result);
            }

            cursor.close(); // Close the cursor after use
        }
        //If database is empty
        else {
            Toast.makeText(context, "Database is empty", Toast.LENGTH_SHORT).show();
        }

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        db.close();

        //Return the lecturerInfoList arrayList
        return studentInfoList2;
    }
    public ArrayList<String> StudentInfoToStringByID(Context context, String studentID) {
        //Create a db instance for reading only
        SQLiteDatabase db = this.getReadableDatabase();
        //Create a cursor
        Cursor cursor;
        // Select student information with lecturer name based on student ID
        String query = "SELECT " +
                COLUMN_STUDENT_NAME + ", " +
                COLUMN_STUDENT_LASTNAME + ", " +
                COLUMN_STUDENT_GENDER + ", " +
                COLUMN_STUDENT_FACULTY + ", " +
                COLUMN_STUDENT_DEPARTMENT + ", " +
                COLUMN_STUDENT_LECTURER + ", " +
                COLUMN_STUDENT_ID +
                " FROM " + TABLE_STUDENT +
                " WHERE " + COLUMN_STUDENT_ID + " = " + studentID;

        //Create an String array for all the student Information
        ArrayList<String> studentInfo = new ArrayList<>();

        //Create an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, studentInfo);

        //Check if database is not null
        if (db != null) {
            //Store the data in cursor
            cursor = db.rawQuery(query,null);

            while (cursor.moveToNext()) {
                String studentIDNumber = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID));
                String studentName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME));
                String studentLastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_LASTNAME));
                String studentGender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_GENDER));
                String facultyName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_FACULTY));
                String departmentName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_DEPARTMENT));
                String lecturerName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_LECTURER));
                //We put all the student information in result message box format
                String result = "Id: "+studentIDNumber+"\nName: " + studentName + "\nSurname: " + studentLastName + "\nGender: " + studentGender + "\nFaculty: " +
                        facultyName + "\nDepartment: " + departmentName + "\nLecturer: " + lecturerName;
                //Add it to arrayList
                studentInfo.add(result);
            }

            cursor.close(); // Close the cursor after use
        }
        adapter.notifyDataSetChanged();
        db.close();

        return studentInfo;
    }
}

