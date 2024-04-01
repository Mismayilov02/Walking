package com.imehemmed.walking.Room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "history")
public class RunHistory {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "startTime")
    private long startTime;
    @ColumnInfo(name = "endTime")
    private long endTime;

    public RunHistory(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
