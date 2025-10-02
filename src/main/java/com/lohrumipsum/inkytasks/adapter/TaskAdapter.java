package com.lohrumipsum.inkytasks.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lohrumipsum.inkytasks.R;
import com.lohrumipsum.inkytasks.model.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> tasks = new ArrayList<>();
    private final OnTaskInteractionListener listener;

    public interface OnTaskInteractionListener {
        void onTaskClick(Task task);
        void onTaskLongClick(Task task);
        void onMoveUpClick(int position);
        void onMoveDownClick(int position);
    }

    public TaskAdapter(OnTaskInteractionListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task currentTask = tasks.get(position);
        holder.bind(currentTask, listener, position, tasks.size());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskTitle;
        private final ImageButton moveUpButton;
        private final ImageButton moveDownButton;

        ViewHolder(View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.task_title);
            moveUpButton = itemView.findViewById(R.id.move_up_button);
            moveDownButton = itemView.findViewById(R.id.move_down_button);
        }

        void bind(final Task task, final OnTaskInteractionListener listener, final int position, final int listSize) {
            taskTitle.setText(task.getTitle());

            if ("completed".equals(task.getStatus())) {
                taskTitle.setPaintFlags(taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                taskTitle.setPaintFlags(taskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            itemView.setOnClickListener(v -> listener.onTaskClick(task));
            itemView.setOnLongClickListener(v -> {
                listener.onTaskLongClick(task);
                return true;
            });

            moveUpButton.setVisibility(position > 0 ? View.VISIBLE : View.INVISIBLE);
            moveDownButton.setVisibility(position < listSize - 1 ? View.VISIBLE : View.INVISIBLE);

            moveUpButton.setOnClickListener(v -> listener.onMoveUpClick(getAdapterPosition()));
            moveDownButton.setOnClickListener(v -> listener.onMoveDownClick(getAdapterPosition()));
        }
    }
}

