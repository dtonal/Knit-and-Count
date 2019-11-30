package de.dtonal.knitandcount.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(indices = {@Index("id")})
public class Project {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    @ColumnInfo(name="creation_date")
    private Date creationDate;

    @ColumnInfo(defaultValue = "")
    private String needleSize;

    @ColumnInfo(defaultValue = "")
    private String yardage;

    @ColumnInfo(defaultValue = "")
    private String size;

    @ColumnInfo(defaultValue = "")
    private String gauge_wet;

    @ColumnInfo(defaultValue = "")
    private String gauge_dry;

    @ColumnInfo(defaultValue = "")
    private String notes;

    public Project(String name){
        this.name = name;
        this.creationDate = new Date();
        needleSize = "";
        yardage = "";
        size = "";
        gauge_wet = "";
        gauge_dry = "";
        notes = "";
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

    public String getNeedleSize() {
        return needleSize;
    }

    public void setNeedleSize(String needleSize) {
        this.needleSize = needleSize;
    }

    public String getYardage() {
        return yardage;
    }

    public void setYardage(String yardage) {
        this.yardage = yardage;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getGauge_wet() {
        return gauge_wet;
    }

    public void setGauge_wet(String gauge_wet) {
        this.gauge_wet = gauge_wet;
    }

    public String getGauge_dry() {
        return gauge_dry;
    }

    public void setGauge_dry(String gauge_dry) {
        this.gauge_dry = gauge_dry;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    //TODO: Nadelstärke, Lauflänge m / 100grm, Größe, Maschenprobe 10 x 10cm nass, Maschenprobe 10x10 trocken, Notizen
}
