package de.dtonal.knitandcount.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.dtonal.knitandcount.R;
import de.dtonal.knitandcount.data.model.Project;
import de.dtonal.knitandcount.listener.project.OnProjectClickListener;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {


    private final List<Project> projects;
    private final OnProjectClickListener onProjectClickListener;

   public ProjectAdapter(List<Project> projects, OnProjectClickListener onProjectClickListener) {
        this.projects = projects;
        this.onProjectClickListener = onProjectClickListener;
    }

    @NonNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, int position) {
        holder.textViewProjectName.setText(projects.get(position).getName());
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public void setData(List<Project> projects) {
        this.projects.clear();
        this.projects.addAll(projects);
        this.notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewProjectName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewProjectName = itemView.findViewById(R.id.textViewProjectName);
        }

        void bind(final int position) {
            this.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    onProjectClickListener.onProjectClicked(projects.get(position));
                }
            });
        }
    }
}
