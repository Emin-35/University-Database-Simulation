package com.example.final_project;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdministrationFragment extends Fragment {
    EditText faculties,departments,lecturer;
    ListView listView;
    Button add_tab1,delete_tab1,update_tab1,search_tab1;
    DBHelper dbHelper;
    ArrayList<String> databaseInfo  = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the administration_tab layout for this fragment
        View view = inflater.inflate(R.layout.administration_tab, container, false);
        //Find the IDs
        faculties = view.findViewById(R.id.faculties);
        departments = view.findViewById(R.id.departments);
        lecturer = view.findViewById(R.id.lecturer);

        listView = view.findViewById(R.id.listView_tab1);

        add_tab1 = view.findViewById(R.id.add_tab1);
        delete_tab1 = view.findViewById(R.id.delete_tab1);
        update_tab1 = view.findViewById(R.id.update_tab1);
        search_tab1 = view.findViewById(R.id.search_tab1);

        dbHelper = new DBHelper(getContext());

//-------------------------------------------------------ADD BUTTON FUNCTIONS----------------------------------------------------------

        add_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String faculty = faculties.getText().toString();
                String department = departments.getText().toString();
                String lecturerName = lecturer.getText().toString();

                //If all the sections are full, add the data values to correct tables
                if (!faculty.isEmpty() && !department.isEmpty() && !lecturerName.isEmpty()) {
                    //If the given faculty, department and lecturer is not in the database means it's a new value
                    if(!dbHelper.isFacultyExists(faculty) && !dbHelper.isDepartmentExists(department) && !dbHelper.isLecturerExists(faculty)){
                        //First add the faculty to database
                        dbHelper.addFaculty(faculty);

                        //Then find the faculty ID that we added
                        long primaryFacultyID = dbHelper.getFacultyId(faculty);
                        //Add the correct department to faculty
                        dbHelper.addDepartment(department,primaryFacultyID);

                        //Find departmentID that we added
                        long primaryDepartmentID = dbHelper.getDepartmentId(department,primaryFacultyID);

                        //Add the correct lecturer to department
                        dbHelper.addLecturer(lecturerName,primaryDepartmentID);
                        Toast.makeText(getContext(), "Lecturer successfully added", Toast.LENGTH_SHORT).show();
                    }

                    //Else-if faculty is exist and we're trying to add a new department with lecturer
                    else if(dbHelper.isFacultyExists(faculty) && !dbHelper.isDepartmentExists(department)) {
                        //Find the faculty ID that is exist
                        long primaryFacultyID = dbHelper.getFacultyId(faculty);

                        //Add the correct department to faculty
                        dbHelper.addDepartment(department,primaryFacultyID);

                        //Find departmentID that we added
                        long primaryDepartmentID = dbHelper.getDepartmentId(department,primaryFacultyID);

                        //Add the correct lecturer to department
                        dbHelper.addLecturer(lecturerName,primaryDepartmentID);
                        Toast.makeText(getContext(), "Lecturer successfully added", Toast.LENGTH_SHORT).show();
                    }

                    //Else-if the given faculty and department is exist
                    else if(dbHelper.isFacultyExists(faculty) && dbHelper.isDepartmentExists(department)) {
                        //Find the faculty ID that is exist
                        long primaryFacultyID = dbHelper.getFacultyId(faculty);

                        //Find the correct department ID
                        long primaryDepartmentID = dbHelper.getDepartmentId(department,primaryFacultyID);

                        //If we're trying to add the lecturer in the same faculty and department again
                        if(dbHelper.isLecturerExists(lecturerName)) {
                            Toast.makeText(getContext(), "Lecturer already exists in given Department", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //Add lecturer to lecturer table. Notice same lecturer can give multiple classes
                            long newRowId = dbHelper.addLecturer(lecturerName,primaryDepartmentID);
                            Toast.makeText(getContext(), "Lecturer successfully added", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //If the correct Faculty and Department is not in the database means we can't add lecturer to database
                    else {
                        Toast.makeText(getContext(), "Department is already exists in different Faculty", Toast.LENGTH_SHORT).show();
                    }
                }

                //Else if faculty section and department is filled and the lecturer name is empty
                else if (!faculty.isEmpty() && !department.isEmpty() && lecturerName.isEmpty()) {
                    //Check if the given faculty is already in the database,
                    if(dbHelper.isFacultyExists(faculty)) {
                        //Find the correct faculty ID
                        long primaryFacultyID = dbHelper.getFacultyId(faculty);

                        //If the given department is already in the database throw error
                        if(dbHelper.isDepartmentExists(department)) {
                            Toast.makeText(getContext(), "Department is already in the Database", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //Else add the data to department table
                            dbHelper.addDepartment(department,primaryFacultyID);
                            Toast.makeText(getContext(), "Department successfully added", Toast.LENGTH_SHORT).show();
                        }
                    }

                    //Else-If faculty is not in the database, means it's a new value
                    else if (!dbHelper.isFacultyExists(faculty)) {
                        //First add the faculty to database
                        dbHelper.addFaculty(faculty);

                        //Then find the faculty ID that we added
                        long primaryFacultyID = dbHelper.getFacultyId(faculty);

                        //Add the correct department to faculty
                        dbHelper.addDepartment(department,primaryFacultyID);
                        Toast.makeText(getContext(), "Faculty successfully added", Toast.LENGTH_SHORT).show();
                    }
                }

                //If only faculty section is filled and the rest of the table is empty
                else if(!faculty.isEmpty() && department.isEmpty() && lecturerName.isEmpty()) {
                    //Check if the given faculty is already in the database or not, If it is throw error
                    if(dbHelper.isFacultyExists(faculty)) {
                        Toast.makeText(getContext(), "This Faculty already in the database", Toast.LENGTH_SHORT).show();
                    }
                    //If not add it to database
                    else {
                        dbHelper.addFaculty(faculty);
                        Toast.makeText(getContext(), "Faculty successfully added", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(getContext(), "Please fill the fields correctly", Toast.LENGTH_SHORT).show();
                }
            }
        }); //add_tab1.setOnClickListener

//-------------------------------------------------------ADD BUTTON FUNCTIONS----------------------------------------------------------

//-------------------------------------------------------DELETE BUTTON FUNCTIONS-------------------------------------------------------

        delete_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String faculty = faculties.getText().toString();
                String department = departments.getText().toString();
                String lecturerName = lecturer.getText().toString();

                //If all 3 EditView is filled
                if(!faculty.isEmpty() && !department.isEmpty() && !lecturerName.isEmpty()) {
                    //If given faculty, department and Lecturer is in the database
                    if(dbHelper.isFacultyExists(faculty) && dbHelper.isDepartmentExists(department) && dbHelper.isLecturerExists(lecturerName)) {
                        //Find the faculty ID that is exist
                        long primaryFacultyID = dbHelper.getFacultyId(faculty);
                        //Delete lecturer from given database
                        long primaryDepartmentID = dbHelper.getDepartmentId(department,primaryFacultyID);
                        long LecturerID = dbHelper.findLecturerId(lecturerName,primaryDepartmentID);
                        dbHelper.deleteLecturer(primaryDepartmentID, LecturerID);
                        Toast.makeText(getContext(), "Lecturer Deleted", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getContext(), "Lecturer is not in the database", Toast.LENGTH_SHORT).show();
                }

                //Else if we're trying to delete the department
                else if (!faculty.isEmpty() && !department.isEmpty() && lecturerName.isEmpty()) {
                    //If given faculty and department is in the database
                    if(dbHelper.isFacultyExists(faculty) && dbHelper.isDepartmentExists(department)) {
                        //Find the faculty and department IDs for deletion
                        long primaryFacultyID = dbHelper.getFacultyId(faculty);
                        long primaryDepartmentID = dbHelper.getDepartmentId(department,primaryFacultyID);

                        //First check if there are lecturers in the given department table
                        if(dbHelper.getLecturerCountInDepartment(primaryDepartmentID) <= 0) {
                            //Delete Department
                            dbHelper.deleteDepartment(primaryFacultyID,primaryDepartmentID);
                            Toast.makeText(getContext(), "Department Deleted", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), "There are Lecturers in given Department", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(getContext(), "Given Department or Faculty is not exists", Toast.LENGTH_SHORT).show();
                }

                //Else if we're trying to delete the faculty
                else if (!faculty.isEmpty() && department.isEmpty() && lecturerName.isEmpty()) {
                    //If the given faculty is in the database
                    if(dbHelper.isFacultyExists(faculty)) {
                        //Find the faculty ID for deletion
                        long primaryFacultyID = dbHelper.getFacultyId(faculty);
                        //First check if there are departments in the given faculty table
                        if(dbHelper.getDepartmentCountInFaculty(primaryFacultyID) <= 0) {
                            //Delete Faculty
                            dbHelper.deleteFaculty(primaryFacultyID);
                            Toast.makeText(getContext(), "Faculty Deleted", Toast.LENGTH_SHORT).show();
                        }
                        //Else there must be departments in given faculty
                        else {
                            Toast.makeText(getContext(), "There are Departments in given Faculty", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(getContext(), "Given Faculty is not exists", Toast.LENGTH_SHORT).show();
                }

                //Else throw error
                else {
                    Toast.makeText(getContext(), "Given Faculty,Department or Lecturer is not exists", Toast.LENGTH_SHORT).show();
                }
            }
        }); //delete_tab1.setOnClickListener

//-------------------------------------------------------DELETE BUTTON FUNCTIONS-------------------------------------------------------

//-------------------------------------------------------UPDATE BUTTON FUNCTIONS-------------------------------------------------------

        update_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get values from EditText fields
                String faculty = faculties.getText().toString();
                String department = departments.getText().toString();
                String lecturerName = lecturer.getText().toString();

                //If all 3 EditView is filled and we're trying to update lecturer
                if(!faculty.isEmpty() && !department.isEmpty() && !lecturerName.isEmpty()) {
                    //If given faculty, department and Lecturer is in the database
                    if(dbHelper.isFacultyExists(faculty) && dbHelper.isDepartmentExists(department) && dbHelper.isLecturerExists(lecturerName)) {
                        // Prompt the user to enter a new lecturer name
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Update Lecturer");
                        builder.setMessage("Enter the new lecturer name:");

                        // Get user input
                        final EditText input = new EditText(getContext());
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        //Set positive-button
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newLecturerName = input.getText().toString();
                                // If the new lecturer name is not empty also must be unique
                                if (!newLecturerName.isEmpty() && dbHelper.isLecturerNameUnique(newLecturerName)) {
                                    //Find the faculty ID that is exist
                                    long primaryFacultyID = dbHelper.getFacultyId(faculty);
                                    // Get the primaryDepartmentID and LecturerID
                                    long primaryDepartmentID = dbHelper.getDepartmentId(department,primaryFacultyID);
                                    long lecturerID = dbHelper.findLecturerId(lecturerName, primaryDepartmentID);

                                    // Update the lecturer with the new name
                                    dbHelper.updateLecturer(primaryDepartmentID, lecturerID, newLecturerName);
                                    //Success message
                                    Toast.makeText(getContext(), "Lecturer Updated", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), "Lecturer name must be unique or not empty", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        //If clicked cancel close the window
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                    //If either the given faculty does not contain the department or the department does not have lecturer for given name
                    else
                        Toast.makeText(getContext(), "Given Lecturer does not in given Department or Faculty", Toast.LENGTH_SHORT).show();
                }

                //Else if we're trying to update the department
                else if (!faculty.isEmpty() && !department.isEmpty() && lecturerName.isEmpty()) {
                    //If given faculty and department is in the database
                    if(dbHelper.isFacultyExists(faculty) && dbHelper.isDepartmentExists(department)) {
                        // Prompt the user to enter a new department name
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Update Department");
                        builder.setMessage("Enter the new department name:");

                        // Get user input
                        final EditText input = new EditText(getContext());
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        //Set positive-button
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newDepartmentName = input.getText().toString();
                                // If the new department name is not empty also must be unique
                                if (!newDepartmentName.isEmpty() && dbHelper.isDepartmentNameUnique(newDepartmentName)) {
                                    //Find the faculty ID that is exist
                                    long primaryFacultyID = dbHelper.getFacultyId(faculty);
                                    // Get the primaryDepartmentID and LecturerID
                                    long primaryDepartmentID = dbHelper.getDepartmentId(department,primaryFacultyID);

                                    // Update the lecturer with the new name
                                    dbHelper.updateDepartment(primaryFacultyID, primaryDepartmentID, newDepartmentName);
                                    //Success message
                                    Toast.makeText(getContext(), "Department Updated", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), "Please enter a new department name", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        //If clicked cancel close the window
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                    //If either the given faculty does not contain the department or the department does not have lecturer for given name
                    else
                        Toast.makeText(getContext(), "Given Department does not in given Faculty", Toast.LENGTH_SHORT).show();
                }

                //Else if we're trying to update the faculty
                else if (!faculty.isEmpty() && department.isEmpty() && lecturerName.isEmpty()) {
                    //If the given faculty is in the database
                    if (dbHelper.isFacultyExists(faculty)) {
                        // Prompt the user to enter a new department name
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Update Faculty");
                        builder.setMessage("Enter the new faculty name:");

                        // Get user input
                        final EditText input = new EditText(getContext());
                        input.setInputType(InputType.TYPE_CLASS_TEXT);
                        builder.setView(input);

                        //Set positive-button
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newFacultyName = input.getText().toString();
                                // If the new department name is not empty also must be unique
                                if (!newFacultyName.isEmpty() && dbHelper.isFacultyNameUnique(newFacultyName)) {
                                    //Find the faculty ID that is exist
                                    long primaryFacultyID = dbHelper.getFacultyId(faculty);

                                    // Update the lecturer with the new name
                                    dbHelper.updateFaculty(primaryFacultyID, newFacultyName);
                                    //Success message
                                    Toast.makeText(getContext(), "Faculty Updated", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), "Please enter a new faculty name", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        //If clicked cancel close the window
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                    //If either the given faculty does not contain the department or the department does not have lecturer for given name
                    else
                        Toast.makeText(getContext(), "Given Faculty does not exists", Toast.LENGTH_SHORT).show();
                }
                //Else throw error
                else {
                    Toast.makeText(getContext(), "Given Faculty,Department or Lecturer is not exists", Toast.LENGTH_SHORT).show();
                }
            }
        }); //update_tab1.setOnClickListener

//-------------------------------------------------------UPDATE BUTTON FUNCTIONS-------------------------------------------------------

        search_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call showEntireDatabase function, secure the arrayList and send a message for searching the database
                databaseInfo = dbHelper.showAdminDatabase(getContext(), listView);
                Toast.makeText(getContext(), "Printing the DATABASE", Toast.LENGTH_SHORT).show();
            }
        }); //search_tab1.setOnClickListener

//-------------------------------------------------------UPDATE BUTTON FUNCTIONS-------------------------------------------------------

//-------------------------------------------------------LISTVIEW BUTTON FUNCTIONS-----------------------------------------------------

        // Add click listener to listView so when we click the data it can fill the EditText fields
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Get the position when clicked on the listview
                String selectedLecturer = databaseInfo.get(position);

                // Split the original string using "--" as a delimiter
                String[] parts = selectedLecturer.split("--");

                // Ensure that the split resulted in exactly three parts
                // LecturerName,DepartmentName,FacultyName
                if (parts.length == 3) {
                    String LecturerName = parts[0];
                    String DepartmentName = parts[1];
                    String FacultyName = parts[2];

                    // Now we can put the correct values into EditText areas
                    faculties.setText(FacultyName);
                    departments.setText(DepartmentName);
                    lecturer.setText(LecturerName);
                }
                else {
                    // Handle the case where the split did not result in three parts
                    System.out.println("Invalid input format");
                }
            }
        }); //listView.setOnClickListener

//-------------------------------------------------------LISTVIEW BUTTON FUNCTIONS-----------------------------------------------------

        return view;
    }
}