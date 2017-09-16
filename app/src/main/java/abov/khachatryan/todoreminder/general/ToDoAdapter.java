package abov.khachatryan.todoreminder.general;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import abov.khachatryan.todoreminder.R;
import abov.khachatryan.todoreminder.database.DatabaseHelper;
import abov.khachatryan.todoreminder.models.ToDo;

/* Created by abov on 9/15/17.*/

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDo> toDos;

    public ToDoAdapter() {
    }

    public void updateToDos(final DatabaseHelper databaseHelper) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                ToDoAdapter.this.toDos = databaseHelper.getAllToDos();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.recycler_view_item_todo, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final ToDo todo = toDos.get(position);
        viewHolder.mDescriptionTxtView.setText(todo.getmDescription());
        viewHolder.mDateTextView.setText(todo.getmDate());
        viewHolder.mTimeTextView.setText(todo.getmTime());
    }

    @Override
    public int getItemCount() {
        return toDos != null ? toDos.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDescriptionTxtView;
        private TextView mDateTextView;
        private TextView mTimeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mDescriptionTxtView = (TextView) itemView.findViewById(R.id.todo_item_description_txt_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.todo_item_date_txt_view);
            mTimeTextView = (TextView) itemView.findViewById(R.id.todo_item_time_txt_view);
        }
    }

}
