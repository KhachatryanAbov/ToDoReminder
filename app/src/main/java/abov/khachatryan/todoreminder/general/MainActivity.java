package abov.khachatryan.todoreminder.general;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import abov.khachatryan.todoreminder.R;
import abov.khachatryan.todoreminder.database.DatabaseHelper;
import abov.khachatryan.todoreminder.fragments.DatePickerFragment;
import abov.khachatryan.todoreminder.fragments.TimePickerFragment;
import abov.khachatryan.todoreminder.models.ToDo;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final String ALARM_ACTION_SHOW_TODO = "showToDo";

    private final String SIMPLE_DATE_FORMAT = "dd/MM/YYYY";
    private final String SIMPLE_TIME_FORMAT = "HH:mm";

    private TextView mDateTxtView;
    private TextView mTimeTxtView;
    private TextView mChangeDateTxtView;
    private TextView mChangeTimeTxtView;
    private TextView mAddToDoTxtView;
    private EditText mDescriptionEdtTxtView;
    private RecyclerView mTodoRecyclerView;

    private DatabaseHelper mDatabaseHelper;
    private ToDoAdapter mToDoAdapter;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setBtnClicks();
        initParams();

        if (getIntent().getAction().equals(ALARM_ACTION_SHOW_TODO)) {
            showToDoDialog();
            mDatabaseHelper.removeOutdatedToDos();
            mToDoAdapter.updateToDos(mDatabaseHelper);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDateTxtView.setText(getModifiedDate(SIMPLE_DATE_FORMAT));
        mTimeTxtView.setText(getModifiedDate(SIMPLE_TIME_FORMAT));
    }

    private void initViews() {
        mDateTxtView = (TextView) findViewById(R.id.date_text_view);
        mTimeTxtView = (TextView) findViewById(R.id.time_text_view);
        mChangeDateTxtView = (TextView) findViewById(R.id.change_date_text_view);
        mChangeTimeTxtView = (TextView) findViewById(R.id.change_time_text_view);
        mAddToDoTxtView = (TextView) findViewById(R.id.add_todo_text_view);
        mDescriptionEdtTxtView = (EditText) findViewById(R.id.description_edit_text_view);
        mTodoRecyclerView = (RecyclerView) findViewById(R.id.todo_recycler_view);
    }

    private void setBtnClicks() {
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
                String description = mDescriptionEdtTxtView.getText().toString();
                if (description.matches("")) {
                    mDescriptionEdtTxtView.setError(getString(R.string.please_fill_out));
                } else {
                    if (mCalendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
                        mDescriptionEdtTxtView.setText("");
                        addToDoInDb(createToDoWithDescription(description));
                        createNewAlarm();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.invalid_date), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void initParams() {
        mDatabaseHelper = new DatabaseHelper(this);

        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);

        mTodoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mToDoAdapter = new ToDoAdapter();
        mTodoRecyclerView.setAdapter(mToDoAdapter);
        mToDoAdapter.updateToDos(mDatabaseHelper);
    }

    private void showToDoDialog() {
        List<ToDo> outdatedToDos = mDatabaseHelper.getAllOutdatedToDos();
        CharSequence[] descriptions = new CharSequence[outdatedToDos.size()];
        for (int i = 0; i < outdatedToDos.size(); ++i) {
            descriptions[i] = outdatedToDos.get(i).getmDescription();
        }
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setNegativeButton(getString(R.string.cancel), null)
                .setItems(descriptions, null)
                .show();
    }

    private String getModifiedDate(String dateFormat) {
        return new SimpleDateFormat(dateFormat, Locale.getDefault()).format(mCalendar.getTime());
    }

    private void addToDoInDb(ToDo toDo) {
        mDatabaseHelper.addToDo(toDo);
        mToDoAdapter.updateToDos(mDatabaseHelper);
    }

    private ToDo createToDoWithDescription(String description) {
        ToDo toDo = new ToDo();
        toDo.setmDescription(description);
        toDo.setmDate(getModifiedDate(SIMPLE_DATE_FORMAT));
        toDo.setmTime(getModifiedDate(SIMPLE_TIME_FORMAT));
        toDo.setmTimeInMillis(String.valueOf(mCalendar.getTimeInMillis()));
        return toDo;
    }

    private void createNewAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intentAlarm = new Intent(MainActivity.this, AlarmReceiver.class);
        intentAlarm.setAction(Long.toString(System.currentTimeMillis()));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,
                (int) System.currentTimeMillis(), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(year, month, dayOfMonth);
        mDateTxtView.setText(getModifiedDate(SIMPLE_DATE_FORMAT));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mTimeTxtView.setText(getModifiedDate(SIMPLE_TIME_FORMAT));
    }
}
