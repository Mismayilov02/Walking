package com.mehemmed_i.walking.Room;


import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "history")
public class History {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

   private  Double longtitute;
   private Double laitute;

    public History(Double longtitute, Double laitute) {
        this.longtitute = longtitute;
        this.laitute = laitute;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
