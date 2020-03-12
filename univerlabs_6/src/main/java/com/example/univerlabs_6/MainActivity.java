package com.example.univerlabs_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
TODO попробовать
Совет. Если необходимо пользовательское диалоговое окно, можно отображать Activity в качестве диалога вместо API Dialog. Нужно создать операцию и установить тему Theme.Holo.Dialog в элементе манифеста&lt;операция&gt;:

<activity android:theme="@android:style/Theme.Holo.Dialog" >

Готово. Операция теперь отображается в диалоговом окне, а не в полноэкранном режиме.
* */
public class MainActivity extends AppCompatActivity {

    RecyclerView remindersListView;
    public List<Remainder> remindersListData;
    public DataAdapter remindersListAdapter;

    //TODO int page - on GetPage in DAO

    //TODO Schema export directory is not provided to the annotation processor so we cannot export the schema. You can either provide `room.schemaLocation` annotation processor argument OR set exportSchema to false.
    //TODO DB provider
    //TODO Add Dagger 2 for code in App.java

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        remindersListView = findViewById(R.id.remindersList);
        remindersListData = new ArrayList<Remainder>();
        remindersListAdapter = new DataAdapter(this, remindersListData);
        remindersListView.setAdapter(remindersListAdapter);


        //TODO alarm and notification + customise notify
        //TODO анимировать тектовые поля: при нажатии текст отплывает вверх
        //TODO make fragment + rotate + off anim after rotate
        //TODO check outdated and complited
        //TODO LiveData https://codelabs.developers.google.com/codelabs/android-room-with-a-view/#5
        //TODO MVVM + LiveData

        ShowReminds task = new ShowReminds(this);
        task.execute();
    }

    public void DeleteRemindHandler(View view) {
        //TODO Del from DB
        //TODO del by long touch
        //TODO del by swipe
        //TODO delete Dialog
        //TODO Del from View
        //TODO del from Schedule
    }

    public void NEWRemindHandler(View view) {
        DialogCreateRemind dialogCreateRemind = new DialogCreateRemind();
        dialogCreateRemind.show(getSupportFragmentManager(), "btn1");
        /*
        //TODO Weak ref | static and nested class
        Remainder remainder = new Remainder("t123", "note", new Date());
        remindersListData.add(remainder);
        remindersListAdapter.notifyDataSetChanged();
        //TODO dialog window
        //TODO add and remove with animation
        AddRemind task = new AddRemind();// new AddRemind(this);
        task.execute(remainder);*/
    }

    //TODO when tap on notify or alarm, delete
    //TODO if outdate show red color
    //TODO if outdate checked delete
}

class AddRemind extends AsyncTask<Remainder, Void, Void> {
/*
    Activity activity;

    AddRemind(Activity activity) {
        this.activity = activity;
    }
*/
    @Override
    protected Void doInBackground(Remainder... remainders) {
        App.getDatabase().remainderDAO().insert(remainders[0]);
        //TODO Add to Schedule
        return null;
    }
}

class ShowReminds extends AsyncTask<Void, Void, Void> {

    MainActivity activity;

    ShowReminds(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void[] objects) {
        List<Remainder> list = App.getDatabase().remainderDAO().getPage();
        activity.remindersListData.addAll(list);
        //TODO check schedules
        //TODO off schedules if deleted
        publishProgress();
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        activity.remindersListAdapter.notifyDataSetChanged();
        super.onProgressUpdate(values);
    }
}

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Remainder> remainders;

    DataAdapter(Context context, List<Remainder> remainders){
        inflater = LayoutInflater.from(context);
        this.remainders = remainders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.remind_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Remainder remainder = remainders.get(position);
        holder.tvTitle.setText(remainder.getTitle());
        holder.tvText.setText(remainder.getText());
        holder.tvDate.setText(remainder.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return remainders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle, tvText, tvDate;
        ViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tvTitle);
            tvText = view.findViewById(R.id.tvText);
            tvDate = view.findViewById(R.id.tvDate);
        }
    }
}