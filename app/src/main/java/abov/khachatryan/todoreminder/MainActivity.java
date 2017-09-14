package abov.khachatryan.todoreminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import abov.khachatryan.todoreminder.fragments.DatePickerFragment;
import abov.khachatryan.todoreminder.fragments.TimePickerFragment;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    @SuppressWarnings("FieldCanBeLocal")
    private final String SIMPLE_DATE_FORMAT = "dd/MM/YY";
    @SuppressWarnings("FieldCanBeLocal")
    private final String SIMPLE_TIME_FORMAT = "HH:mm";

    private TextView mDateTxtView;
    private TextView mTimeTxtView;
    private TextView mChangeDateTxtView;
    private TextView mChangeTimeTxtView;
    private TextView mAddToDoTxtView;

    private EditText mDescriptionEdtTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initBtnClicks();
        updateViewDate(mDateTxtView, Calendar.getInstance(), SIMPLE_DATE_FORMAT);
        updateViewDate(mTimeTxtView, Calendar.getInstance(), SIMPLE_TIME_FORMAT);
    }

    private void initViews() {
        mDateTxtView = (TextView) findViewById(R.id.date_text_view);
        mTimeTxtView = (TextView) findViewById(R.id.time_text_view);
        mChangeDateTxtView = (TextView) findViewById(R.id.change_date_text_view);
        mChangeTimeTxtView = (TextView) findViewById(R.id.change_time_text_view);
        mAddToDoTxtView = (TextView) findViewById(R.id.add_todo_text_view);

        mDescriptionEdtTxtView = (EditText) findViewById(R.id.description_edit_text_view);
    }

    private void initBtnClicks() {

        mChangeDateTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerFragment().show(getSupportFragmentManager(),
                        DialogFragment.class.getSimpleName());
            }
        });

        mChangeTimeTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerFragment().show(getSupportFragmentManager(),
                        TimePickerFragment.class.getSimpleName());
            }
        });

        mAddToDoTxtView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDescriptionEdtTxtView.getText().toString().matches("")) {
                    mDescriptionEdtTxtView.setError(getString(R.string.please_fill_out));
                } else {
                    //todo save params
                }
            }
        });

        mDescriptionEdtTxtView = (EditText) findViewById(R.id.description_edit_text_view);
    }

    private void updateViewDate(TextView textView, Calendar calendar, String dateFormat) {
        textView.setText(new SimpleDateFormat(dateFormat, Locale.getDefault()).format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        updateViewDate(mDateTxtView, c, SIMPLE_DATE_FORMAT);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        c.set(HOUR_OF_DAY, hourOfDay);
        c.set(MINUTE, minute);
        updateViewDate(mTimeTxtView, c, SIMPLE_TIME_FORMAT);
    }
}
