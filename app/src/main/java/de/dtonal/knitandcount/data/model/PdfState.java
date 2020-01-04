package de.dtonal.knitandcount.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "pdfstate",
        foreignKeys = @ForeignKey(entity = Project.class,
                parentColumns = "id",
                childColumns = "project_id",
                onDelete = CASCADE
        ), indices = {@Index("project_id")})
public class PdfState {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private float zoom;

    private float offsetX;

    private float offsetY;

    @ColumnInfo(name = "project_id")
    private int projectId;

    public PdfState(int projectId) {
        update(1f, 0f, 0f);
        this.projectId = projectId;
    }

    public void update(float zoom, float offsetX, float offsetY) {
        this.zoom = zoom;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
