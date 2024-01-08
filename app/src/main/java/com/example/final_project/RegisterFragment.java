package com.example.final_project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {

    DBHelper dbHelper;
    ListView listView;
    Button btnSearch;
    ArrayList<String> databaseInfo  = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.registered_student_tab, container, false);
        dbHelper = new DBHelper(getContext());
        listView = view.findViewById(R.id.registeredListView);
        btnSearch = view.findViewById(R.id.btnSearch);

        //Set onClickListener to search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When clicked call showStudentByName function
                databaseInfo = dbHelper.showStudentByName(getContext(), listView);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the position when clicked on the listview
                String selectedStudent = (String) parent.getItemAtPosition(position);
                String selectedID = (String) selectedStudent.substring(selectedStudent.length() - 10);

                //Call the StudentInfoToStringByID and get the clicked student's info
                List message = dbHelper.StudentInfoToStringByID(getContext(),selectedID);

                //Create an Alert Dialog and show the info there as a pop-up screen
                AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                alertDialog.setTitle("Student Information");
                alertDialog.setMessage(message.toString());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog,int which){
                                dialog.dismiss();
                            }
                        }) ;
                alertDialog.show();
            }});

        return view;
    }
}