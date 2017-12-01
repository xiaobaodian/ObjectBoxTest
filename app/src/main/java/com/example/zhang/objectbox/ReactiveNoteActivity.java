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
import io.objectbox.android.AndroidScheduler;
import io.objectbox.query.Query;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscriptionList;

/** An alternative to {@link MainActivity} using a reactive query (without RxJava, just plain ObjectBox API). */
public class ReactiveNoteActivity extends Activity {

    private EditText editText;
    private View addNoteButton;

    private Box<Task> notesBox;
    private Query<Task> notesQuery;
    private TasksAdapter tasksAdapter;
    private DataSubscriptionList subscriptions = new DataSubscriptionList();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setUpViews();

        notesBox = ((App) getApplication()).getBoxStore().boxFor(Task.class);

        // query all notes, sorted a-z by their title (http://greenrobot.org/objectbox/documentation/queries/)
        notesQuery = notesBox.query().order(Task_.title).build();

        // Reactive query (http://greenrobot.org/objectbox/documentation/data-observers-reactive-extensions/)
        notesQuery.subscribe(subscriptions).on(AndroidScheduler.mainThread())
                .observer(new DataObserver<List<Task>>() {
                    @Override
                    public void onData(List<Task> tasks) {
                        tasksAdapter.setTasks(tasks);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        subscriptions.cancel();
        super.onDestroy();
    }

    protected void setUpViews() {
        ListView listView = (ListView) findViewById(R.id.listViewNotes);
        listView.setOnItemClickListener(noteClickListener);

        tasksAdapter = new TasksAdapter();
        listView.setAdapter(tasksAdapter);

        addNoteButton = findViewById(R.id.buttonAdd);
        addNoteButton.setEnabled(false);

        editText = (EditText) findViewById(R.id.editTextNote);
        editText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addNote();
                    return true;
                }
                return false;
            }
        });
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean enable = s.length() != 0;
                addNoteButton.setEnabled(enable);
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
        addNote();
    }

    private void addNote() {
        String noteText = editText.getText().toString();
        editText.setText("");

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
        String comment = "Added on " + df.format(new Date());

        Task task = new Task();
        task.setTitle(noteText);
        task.setComment(comment);
        task.setDate(new Date());
        notesBox.put(task);
        Log.d(App.TAG, "Inserted new task, ID: " + task.getId());
    }

    OnItemClickListener noteClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Task task = tasksAdapter.getItem(position);
            notesBox.remove(task);
            Log.d(App.TAG, "Deleted task, ID: " + task.getId());
        }
    };
}