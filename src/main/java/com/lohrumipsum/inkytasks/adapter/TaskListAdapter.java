package com.lohrumipsum.inkytasks.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lohrumipsum.inkytasks.R;
import com.lohrumipsum.inkytasks.model.TaskList;
import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private List<TaskList> taskLists = new ArrayList<>();
    private final OnTaskListClickListener listener;

    public interface OnTaskListClickListener {
        void onTaskListClick(TaskList taskList);
        void onTaskListLongClick(TaskList taskList);
    }

    public TaskListAdapter(OnTaskListClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TaskList currentList = taskLists.get(position);
        holder.bind(currentList, listener);
    }

    @Override
    public int getItemCount() {
        return taskLists.size();
    }

    public void setTaskLists(List<TaskList> taskLists) {
        this.taskLists = taskLists;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskListTitle;

        ViewHolder(View itemView) {
            super(itemView);
            taskListTitle = itemView.findViewById(R.id.task_list_title);
        }

        void bind(final TaskList taskList, final OnTaskListClickListener listener) {
            taskListTitle.setText(taskList.getTitle());
            itemView.setOnClickListener(v -> listener.onTaskListClick(taskList));
            itemView.setOnLongClickListener(v -> {
                listener.onTaskListLongClick(taskList);
                return true;
            });
        }
    }
}

