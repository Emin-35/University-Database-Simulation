package com.example.final_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentFragment extends Fragment {

    DBHelper dbHelper;
    EditText studentID, name, lastName;
    ListView listView;
    RadioGroup gender;
    RadioButton male, female;
    Spinner faculty, department, lecturer;
    Button add_tab2, delete_tab2, update_tab2, search_tab2;
    long primaryFacultyID, primaryDepartmentID;

    ArrayAdapter<String> facultyAdapter,departmentAdapter,lecturerAdapter;
    ArrayList<String> databaseInfo  = new ArrayList<>();
    ArrayList<String> studentInfo = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.student_registration_tab, container, false);

        dbHelper = new DBHelper(getContext());

        // Initialize EditTexts
        studentID = view.findViewById(R.id.studentID);
        name = view.findViewById(R.id.name);
        lastName = view.findViewById(R.id.lastName);

        listView = view.findViewById(R.id.listView_tab2);

        // Initialize RadioGroup and RadioButtons
        gender = view.findViewById(R.id.gender);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);

        // Generate a 10-digit random number for student ID
        long randomID = giveRandomID();

        // Make it StudentID
        studentID.setText(String.valueOf(randomID));

        // Initialize Spinners
        faculty = view.findViewById(R.id.faculty);
        department = view.findViewById(R.id.department);
        lecturer = view.findViewById(R.id.lecturer);

        // Initialize Buttons
        add_tab2 = view.findViewById(R.id.add_tab2);
        delete_tab2 = view.findViewById(R.id.delete_tab2);
        update_tab2 = view.findViewById(R.id.update_tab2);
        search_tab2 = view.findViewById(R.id.search_tab2);

//-------------------------------------------------------------SPINNERS----------------------------------------------------------------
        // Populate Spinners with data from the database
        List<String> faculties = dbHelper.getFaculties(getContext());
        facultyAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, faculties);
        faculty.setAdapter(facultyAdapter);

        //Add Item Select Listener for faculty spinner
        faculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected faculty name
                String selectedFacultyName = parent.getItemAtPosition(position).toString();

                // Call a method to retrieve the faculty ID based on the selected faculty name
                primaryFacultyID = dbHelper.getFacultyId(selectedFacultyName);

                //Call the getDepartments with primaryFacultyID, so that we can only put the correct departments in the spinner
                List<String> departments = dbHelper.getDepartments(getContext(), primaryFacultyID);
                departmentAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, departments);
                department.setAdapter(departmentAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Add Item Select Listener for department spinner
        department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected department name
                String selectedDepartmentName = parent.getItemAtPosition(position).toString();

                // Call a method to retrieve the department ID based on the selected department name
                primaryDepartmentID = dbHelper.getDepartmentId(selectedDepartmentName, primaryFacultyID);

                //Call the getLecturers with primaryFacultyID, so that we can only put the correct departments in the spinner
                List<String> lecturers = dbHelper.getLecturers(getContext(),primaryDepartmentID);
                lecturerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lecturers);
                lecturer.setAdapter(lecturerAdapter);
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//-------------------------------------------------------------SPINNERS----------------------------------------------------------------

//-------------------------------------------------------ADD BUTTON FUNCTIONS----------------------------------------------------------

        add_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the values from tab
                String StudentID = studentID.getText().toString();
                String studentName = name.getText().toString();
                String studentLastName = lastName.getText().toString();

                String facultyName = faculty.getSelectedItem().toString();
                String departmentName = department.getSelectedItem().toString();
                String lecturerName = lecturer.getSelectedItem().toString();

                //If the studentID is same, change it
                if(!dbHelper.isStudentIDUnique(StudentID)) {
                    //Change the studentID
                    StudentID = String.valueOf(giveRandomID());
                    studentID.setText(StudentID);
                    //Set all the values to null
                    name.setText("");
                    lastName.setText("");
                    male.setChecked(true);

                    Toast.makeText(getContext(), "Student ID is not unique!", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getContext(), "Updating Student ID", Toast.LENGTH_SHORT).show();
                }

                else {
                    //If the studentID is unique
                    if(dbHelper.isStudentIDUnique(StudentID)) {
                        //If non of the fields are not empty
                        if(!studentName.isEmpty() && !studentLastName.isEmpty()
                                && !facultyName.isEmpty() && !departmentName.isEmpty() && !lecturerName.isEmpty()) {
                            //If male radio-button is checked
                            if(male.isChecked()) {
                                //Call the addStudent function
                                dbHelper.addStudent(StudentID,
                                        studentName,studentLastName,
                                        male.getText().toString(),
                                        facultyName,departmentName,lecturerName);
                                Toast.makeText(getContext(), "Male Student Added into Database", Toast.LENGTH_SHORT).show();
                            }
                            //Means female radio button is checked
                            else {
                                //Call the addStudent function
                                dbHelper.addStudent(StudentID,
                                        studentName,studentLastName,
                                        female.getText().toString(),
                                        facultyName,departmentName,lecturerName);
                                Toast.makeText(getContext(), "Female Student Added into Database", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getContext(), "Please fill the fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

//-------------------------------------------------------ADD BUTTON FUNCTIONS----------------------------------------------------------

//------------------------------------------------------DELETE BUTTON FUNCTIONS--------------------------------------------------------

        delete_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the studentID
                String selectedStudentID = studentID.getText().toString();
                if(!dbHelper.isStudentIDUnique(selectedStudentID)) {
                    //Delete student according to it's primary key
                    dbHelper.deleteStudent(selectedStudentID);
                    Toast.makeText(getContext(), "Deleted student: "+ selectedStudentID, Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(), "Student is not in the Database", Toast.LENGTH_SHORT).show();
            }
        });

//-----------------------------------------------------DELETE BUTTON FUNCTIONS---------------------------------------------------------

//-----------------------------------------------------UPDATE BUTTON FUNCTIONS---------------------------------------------------------

        update_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the values from the app
                String updateStudentID = studentID.getText().toString();
                String updateStudentName = name.getText().toString();
                String updateStudentLastName = lastName.getText().toString();

                String m_gender = male.getText().toString();
                String f_gender = female.getText().toString();

                String updateFacultyName = faculty.getSelectedItem().toString();
                String updateDepartmentName = department.getSelectedItem().toString();
                String updateLecturerName = lecturer.getSelectedItem().toString();

                //If the studentID is not unique and male radio button selected
                if(!dbHelper.isStudentIDUnique(updateStudentID) && male.isChecked()) {
                    dbHelper.updateStudentInfo(getContext(), updateStudentID,
                            updateStudentName,updateStudentLastName, m_gender,
                            updateFacultyName,updateDepartmentName,updateLecturerName);
                    Toast.makeText(getContext(), "Male student Updated", Toast.LENGTH_SHORT).show();
                    studentInfo = dbHelper.findStudentByID(getContext(),updateStudentID);
                }


                //Else if the student is female
                else if (!dbHelper.isStudentIDUnique(updateStudentID) && female.isChecked()) {
                    dbHelper.updateStudentInfo(getContext(), updateStudentID,
                            updateStudentName,updateStudentLastName, f_gender,
                            updateFacultyName,updateDepartmentName,updateLecturerName);
                    Toast.makeText(getContext(), "Female student Updated", Toast.LENGTH_SHORT).show();
                    studentInfo = dbHelper.findStudentByID(getContext(),updateStudentID);
                }
                //Else throw error
                else
                    Toast.makeText(getContext(), "This student is not in database", Toast.LENGTH_SHORT).show();
            }
        });

//-----------------------------------------------------UPDATE BUTTON FUNCTIONS---------------------------------------------------------

        search_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Call showEntireDatabase function, secure the arrayList and send a message for searching the database
                databaseInfo = dbHelper.showStudentDatabase(getContext(), listView);
                Toast.makeText(getContext(), "Printing the DATABASE", Toast.LENGTH_SHORT).show();
            }
        });

//-----------------------------------------------------SEARCH BUTTON FUNCTIONS---------------------------------------------------------

//----------------------------------------------------LIST VIEW CLICK LISTENERS--------------------------------------------------------

        //Add Item listener on listView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the position when clicked on the listview
                String selectedID = (String) parent.getItemAtPosition(position);

                //Edit the studentID segment
                studentID.setText(selectedID);

                //Get all the student information when clicked on the studentID
                studentInfo = dbHelper.findStudentByID(getContext(),selectedID);

                //For every info in studentInfo
                for (String info : studentInfo) {
                    // Split the current string using "--" as a delimiter
                    String[] parts = info.split("--");

                    String studentName = parts[0];
                    String studentLastName = parts[1];
                    String gender = parts[2];

                    String facultyName = parts[3];
                    String departmentName = parts[4];
                    String lecturerName = parts[5];

                    //set the text correctly
                    name.setText(studentName);
                    lastName.setText(studentLastName);
                    if(gender.toLowerCase().equals("male")) {
                        male.setChecked(true);
                    }
                    else if(gender.toLowerCase().equals("female")) {
                        female.setChecked(true);
                    }
                    //Find the faculty name in given student information array list and compare it with the faculty spinner
                    //Then set it to correct faculty
                    int f_position = facultyAdapter.getPosition(facultyName);
                    faculty.setSelection(f_position);

                    //Find the department name in given student information array list and compare it with the department spinner
                    //Then set it to correct department
                    int d_position = departmentAdapter.getPosition(departmentName);
                    department.setSelection(d_position);

                    //Find the lecturer name in given student information array list and compare it with the lecturer spinner
                    //Then set it to correct lecturer
                    int l_position = lecturerAdapter.getPosition(lecturerName);
                    lecturer.setSelection(l_position);
                }
            }
        });

//----------------------------------------------------LIST VIEW CLICK LISTENERS--------------------------------------------------------
        return view;
    }


    //Generate 10 digit random number for Student ID
    public long giveRandomID(){
        Random random = new Random();
        long randomID;
        // Generate a non-negative 10-digit random number
        do {
            randomID = 1000000000L + random.nextLong() % 9000000000L;
        } while (randomID < 0);

        // Ensure that the generated ID does not start with 0
        if (String.valueOf(randomID).startsWith("0")) {
            // Increment the ID to make sure it doesn't start with 0
            randomID++;
        }
        return randomID;
    }
}