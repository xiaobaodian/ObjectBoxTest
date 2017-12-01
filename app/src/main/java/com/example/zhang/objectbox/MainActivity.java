package com.example.zhang.objectbox;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

public class MainActivity extends Activity {

    private EditText editText;
    private View addTaskButton;

    private Box<Task> tasksBox;
    private Query<Task> tasksQuery;
    private TasksAdapter tasksAdapter;

    private Box<CheckItem> checkItemsBox;
    private Query<CheckItem> checkItemsQuery;
    private CheckListAdapter checkListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setUpViews();

        BoxStore boxStore = ((App) getApplication()).getBoxStore();

        tasksBox = boxStore.boxFor(Task.class);
        checkItemsBox = boxStore.boxFor(CheckItem.class);

        // query all notes, sorted a-z by their title (http://greenrobot.org/objectbox/documentation/queries/)
        tasksQuery = tasksBox.query().order(Task_.title).build();
        checkItemsQuery = checkItemsBox.query().order(CheckItem_.title).build();
        updateTasks();
    }

    /** Manual trigger to re-query and update the UI. For a reactive alternative check {@link ReactiveNoteActivity}. */
    private void updateTasks() {
        List<Task> tasks = tasksQuery.find();
        tasksAdapter.setTasks(tasks);
    }
    private void updatecheck(){
        List<CheckItem> checkItems = checkItemsQuery.find();
        checkListAdapter.setCheckList(checkItems);
    }
    private void updateCheckList(Task task){
        List<CheckItem> checkList = task.checkList;
        if (checkList != null){
            checkListAdapter.setCheckList(checkList);
        }
    }


    protected void setUpViews() {
        ListView listViewTask = findViewById(R.id.listViewNotes);
        listViewTask.setOnItemClickListener(noteClickListener);

        tasksAdapter = new TasksAdapter();
        listViewTask.setAdapter(tasksAdapter);

        ListView listViewCheckList = findViewById(R.id.listViewCheckList);
        listViewCheckList.setOnItemClickListener(checkClickListener);

        checkListAdapter = new CheckListAdapter();
        listViewCheckList.setAdapter(checkListAdapter);

        addTaskButton = findViewById(R.id.buttonAdd);
        addTaskButton.setEnabled(false);

        editText = findViewById(R.id.editTextNote);
        editText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addTask();
                    return true;
                }
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addTaskButton.setEnabled(enable);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void onAddButtonClick(View view) {
        addTask();
    }

    private void addTask() {
        String noteText = editText.getText().toString();
        editText.setText("");

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        Task task = new Task();
        task.setTitle(noteText);
        task.setComment(comment);
        task.setDate(new Date());
        CheckItem checkItem;
        checkItem = new CheckItem();
        checkItem.setTitle(noteText+"1");
        task.checkList.add(checkItem);
        checkItem = new CheckItem();
        checkItem.setTitle(noteText+"2");
        task.checkList.add(checkItem);
        checkItem = new CheckItem();
        checkItem.setTitle(noteText+"3");
        task.checkList.add(checkItem);
        long taskid = tasksBox.put(task);
        Log.d(App.TAG, "Inserted new task, ID: " + task.getId());

        updateTasks();
    }

    OnItemClickListener noteClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Task task = tasksAdapter.getItem(position);
            //tasksBox.remove(task);
            //Log.d(App.TAG, "Deleted task, ID: " + task.getId());
            //updateTasks();
            updateCheckList(task);
        }
    };

    OnItemClickListener checkClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            CheckItem item = checkListAdapter.getItem(position);
            Task t = null;
            Task task = item.task.getTarget();
            long oid = item.task.getTargetId();
            editText.setText(task.getTitle());
        }
    };

}