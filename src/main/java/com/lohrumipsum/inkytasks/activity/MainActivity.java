package com.lohrumipsum.inkytasks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.lohrumipsum.inkytasks.R;
import com.lohrumipsum.inkytasks.adapter.TaskListAdapter;
import com.lohrumipsum.inkytasks.model.TaskList;
import com.lohrumipsum.inkytasks.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private TaskListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setupRecyclerView();
        observeTaskLists();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.task_lists_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskListAdapter(new TaskListAdapter.OnTaskListClickListener() {
            @Override
            public void onTaskListClick(TaskList taskList) {
                Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
                intent.putExtra("taskListId", taskList.getId());
                intent.putExtra("taskListName", taskList.getTitle());
                startActivity(intent);
            }

            @Override
            public void onTaskListLongClick(TaskList taskList) {
                showEditOrDeleteTaskListDialog(taskList);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void observeTaskLists() {
        mainViewModel.getTaskLists().observe(this, taskLists -> {
            if (taskLists != null) {
                adapter.setTaskLists(taskLists);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_task_list) {
            showAddTaskListDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddTaskListDialog() {
        showEditOrDeleteTaskListDialog(null);
    }

    private void showEditOrDeleteTaskListDialog(final TaskList taskListToEdit) {
        boolean isEditing = taskListToEdit != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEditing ? "Edit Task List" : "New Task List");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_new_task_list, null);
        builder.setView(customLayout);

        final EditText editText = customLayout.findViewById(R.id.edit_text_task_list_name);
        if (isEditing) {
            editText.setText(taskListToEdit.getTitle());
            editText.setSelection(editText.getText().length());
        }

        builder.setPositiveButton(isEditing ? "Save" : "Create", (dialog, which) -> {
            String newTitle = editText.getText().toString().trim();
            if (!newTitle.isEmpty()) {
                if (isEditing) {
                    taskListToEdit.setTitle(newTitle);
                    mainViewModel.updateTaskList(taskListToEdit);
                } else {
                    mainViewModel.addTaskList(newTitle);
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Add delete button only when editing an existing list
        if (isEditing) {
            builder.setNeutralButton("Delete", (dialog, which) -> {
                showDeleteConfirmationDialog(taskListToEdit);
            });
        }

        builder.show();
    }

    private void showDeleteConfirmationDialog(final TaskList taskListToDelete) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task List")
                .setMessage("Are you sure you want to delete this list and all its tasks? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    mainViewModel.deleteTaskList(taskListToDelete);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}

