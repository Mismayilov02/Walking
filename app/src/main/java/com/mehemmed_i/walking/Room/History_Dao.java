package com.mehemmed_i.walking.Room;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface History_Dao {

    @Insert
    void  insert_history(History history);

    @Query("select * from History where id =:id")
    History read_history(int id);

    @Query("select * from History")
    History[] read_history_all();
//    @Query("update history set id =:id where is_sync= :sync")
//    void update_history(int id ,  boolean sync );

    @Query("delete from history")
    void delete_history_all();

//    @Query("DELETE FROM history WHERE is_sync = :sync")
//     void delete_history(boolean sync);



}
