package abov.khachatryan.todoreminder.models;

import java.util.Calendar;

/* Created by abov on 9/15/17.*/

public class ToDo {

    private int mId;
    private String mDescription;
    private String mDate;
    private String mTime;
    private String mTimeInMillis;

    public ToDo() {
    }

    public ToDo(int id, String description, String date, String time, String timeInMillis) {
        mId = id;
        mDescription = description;
        mDate = date;
        mTime = time;
        mTimeInMillis = timeInMillis;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmTimeInMillis() {
        return mTimeInMillis;
    }

    public void setmTimeInMillis(String mTimeInMillis) {
        this.mTimeInMillis = mTimeInMillis;
    }

    public boolean isOutdated() {
        return Long.valueOf(getmTimeInMillis()) <= Calendar.getInstance().getTimeInMillis();
    }
}
