package com.main.tapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.main.tapp.metadata.Job;
import com.main.tapp.util.FireBaseUserUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateJobFragment extends Fragment {

    private static final String TAG = "CreateJobFragment";
    private DatabaseHelper mDatabaseHelper;

    private FireBaseUserUtil mFireUtil;

    private Button mSubmitButton;
    private EditText mTitle;
    private EditText mDescription;
    private EditText mEstTime;
    private EditText mSalary;
    private TextView mSelectDateTV;
    private TextView mSelectTimeTV;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;


    private DateFormat mDateFormat = new SimpleDateFormat("dd/MM");


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_job, container, false);

        initializeViews(rootView);
        mFireUtil = new FireBaseUserUtil();

        mDatabaseHelper = new DatabaseHelper(getActivity());

        mSelectDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog(getActivity(),
                        R.style.MyDatePickerDialogTheme, mDateSetListener,
                        year, month, day);
                dateDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month +=1;
                mSelectDateTV.setText(dayOfMonth+"/"+month+"/"+year);
            }
        };

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                mSelectTimeTV.setText(hour+":"+minute);
            }
        };

        mSelectTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minute = cal.get(Calendar.MINUTE);

                TimePickerDialog timeDialog = new TimePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme,
                        mTimeSetListener, hour, minute, true);
                timeDialog.show();
            }
        });


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Job job = new Job().
                            title(mTitle.getText().toString()).
                            description(mDescription.getText().toString()).
                            estTime(Double.valueOf(mEstTime.getText().toString())).
                            salary(Integer.valueOf(mSalary.getText().toString())).
                            date(mSelectDateTV.getText().toString()).
                            dateTime(mSelectTimeTV.getText().toString()).
                            createdByUID(mFireUtil.getCurrentUser().getUid()).
                            createdByName(mFireUtil.getUserNameSynch(getActivity())).
                            createdDate(mDateFormat.format(new Date()));

                    addJobDb(job);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Could not create job. " + e.getMessage(), Toast.LENGTH_LONG);
                }
            }
        });

        return rootView;
    }

    private void initializeViews(View rootView) {
        mTitle = (EditText)rootView.findViewById(R.id.create_job_title_ET);
        mDescription = (EditText)rootView.findViewById(R.id.create_job_description_ET);
        mEstTime = (EditText)rootView.findViewById(R.id.create_job_est_time_ET);
        mSalary = (EditText)rootView.findViewById(R.id.create_job_salary_ET);
        mSelectDateTV = (TextView)rootView.findViewById(R.id.select_date_tv);
        mSelectTimeTV = (TextView)rootView.findViewById(R.id.select_time_tv);
        mSubmitButton = (Button)rootView.findViewById(R.id.submit_button);
    }

    private void clearInputs() {
        mTitle.getText().clear();
        mDescription.getText().clear();
        mEstTime.getText().clear();
        mSalary.getText().clear();
        mSelectDateTV.setText("");
        mSelectTimeTV.setText("");
    }

    private void addJobDb(Job job) {
        boolean success = mDatabaseHelper.addJob(job);
        if (success) {
            clearInputs();
            toastMessage("Successfully created job!");
        } else {
            toastMessage("Failed to create job.");
        }
    }


    private void toastMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }
}
