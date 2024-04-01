package com.imehemmed.walking.Room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "historydetail")
public class RunHistoryDetail {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "parentId")
    private long parentId;
    @ColumnInfo(name = "longtitute")
    private Double longtitute;
    @ColumnInfo(name = "laitute")
    private Double laitute;

    public RunHistoryDetail(long parentId, Double longtitute, Double laitute) {
        this.parentId = parentId;
        this.longtitute = longtitute;
        this.laitute = laitute;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public Double getLongtitute() {
        return longtitute;
    }

    public void setLongtitute(Double longtitute) {
        this.longtitute = longtitute;
    }

    public Double getLaitute() {
        return laitute;
    }

    public void setLaitute(Double laitute) {
        this.laitute = laitute;
    }
}
