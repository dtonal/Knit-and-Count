package de.dtonal.knitandcount.de.dtonal.knitandcount.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Project {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
}
