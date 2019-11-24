package de.dtonal.knitandcount.de.dtonal.knitandcount.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = Project.class,
        parentColumns = "id",
        childColumns = "project_id",
        onDelete = CASCADE
))
public class Counter {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="counter_id")
    private int id;
    private String name;
    @ColumnInfo(name="creation_date")
    private Date creationDate;
    private int value;
    @ColumnInfo(name="reset_value")
    private int resetValue;
    @ColumnInfo(name="project_id")
    private int projectId;

    public Counter(String name, int projectId){
        this.name = name;
        this.creationDate = new Date();
        this.value = 0;
        this.resetValue = -1;
        this.projectId = projectId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getResetValue() {
        return resetValue;
    }

    public void setResetValue(int resetValue) {
        this.resetValue = resetValue;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
