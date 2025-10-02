package com.lohrumipsum.inkytasks.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lohrumipsum.inkytasks.R;
import com.lohrumipsum.inkytasks.adapter.TaskAdapter;
import com.lohrumipsum.inkytasks.model.Task;
import com.lohrumipsum.inkytasks.viewmodel.TaskListViewModel;
import com.lohrumipsum.inkytasks.viewmodel.TaskListViewModelFactory;

public class TaskListActivity extends AppCompatActivity {

    private TaskListViewModel taskListViewModel;
    private TaskAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        long taskListId = getIntent().getLongExtra("taskListId", -1);
        String taskListName = getIntent().getStringExtra("taskListName");

        if (taskListId == -1) {
            finish();
            return;
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(taskListName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TaskListViewModelFactory factory = new TaskListViewModelFactory(getApplication(), taskListId);
        taskListViewModel = new ViewModelProvider(this, factory).get(TaskListViewModel.class);

        setupRecyclerView();
        observeTasks();
        setupClearButton();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.tasks_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(new TaskAdapter.OnTaskInteractionListener() {
            @Override
            public void onTaskClick(Task task) {
                String newStatus = "completed".equals(task.getStatus()) ? "needsAction" : "completed";
                task.setStatus(newStatus);
                taskListViewModel.updateTask(task);
            }

            @Override
            public void onTaskLongClick(Task task) {
                showEditOrDeleteTaskDialog(task);
            }

            @Override
            public void onMoveUpClick(int position) {
                taskListViewModel.moveTask(adapter.getTasks(), position, position - 1);
            }

            @Override
            public void onMoveDownClick(int position) {
                taskListViewModel.moveTask(adapter.getTasks(), position, position + 1);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void observeTasks() {
        taskListViewModel.getTasks().observe(this, tasks -> {
            if (tasks != null) {
                adapter.setTasks(tasks);
            }
        });
    }

    private void setupClearButton() {
        Button clearButton = findViewById(R.id.clear_completed_button);
        clearButton.setOnClickListener(v -> taskListViewModel.clearCompletedTasks());
    }

    private void showAddTaskDialog() {
        showEditOrDeleteTaskDialog(null);
    }

    private void showEditOrDeleteTaskDialog(final Task taskToEdit) {
        boolean isEditing = taskToEdit != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEditing ? "Edit Task" : "New Task");

        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_new_task, null);
        builder.setView(customLayout);

        final EditText editText = customLayout.findViewById(R.id.edit_text_task_name);
        if (isEditing) {
            editText.setText(taskToEdit.getTitle());
            editText.setSelection(editText.getText().length());
        }

        builder.setPositiveButton(isEditing ? "Save" : "Create", (dialog, which) -> {
            String newTitle = editText.getText().toString().trim();
            if (!newTitle.isEmpty()) {
                if (isEditing) {
                    taskToEdit.setTitle(newTitle);
                    taskListViewModel.updateTask(taskToEdit);
                } else {
                    taskListViewModel.addTask(newTitle);
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        if (isEditing) {
            builder.setNeutralButton("Delete", (dialog, which) -> {
                showDeleteConfirmationDialog(taskToEdit);
            });
        }

        builder.show();
    }

    private void showDeleteConfirmationDialog(final Task taskToDelete) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Task")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    taskListViewModel.deleteTask(taskToDelete);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        // Rename the action item to be more specific for this context
        MenuItem item = menu.findItem(R.id.action_add_task_list);
        if (item != null) {
            item.setTitle("Add Task");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // Handle the back arrow click
            onBackPressed();
            return true;
        } else if (id == R.id.action_add_task_list) {
            // Handle the "Add Task" click
            showAddTaskDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

