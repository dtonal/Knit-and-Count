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

    public Project(String name){
        this.name = name;
        this.creationDate = new Date();
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

    //TODO: Nadelstärke, Lauflänge m / 100grm, Größe, Maschenprobe 10 x 10cm nass, Maschenprobe 10x10 trocken, Notizen
}
