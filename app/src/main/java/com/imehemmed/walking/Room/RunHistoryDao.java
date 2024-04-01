package com.imehemmed.walking.Room;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RunHistoryDao {

    @Insert
    void insertHistory(RunHistory history);

    @Query("select count(*) from history")
    int readHistoryCount();

    @Query("select max(id) from history")
    int getMaximumId();

    @Query("select startTime from history")
    Long[] readHistoryStartTime();

    @Query("update history set endTime =:endTame where id = (select max(id) from history)")
    void updateHistory(long endTame);
    @Query("select * from history where id =:id")
    RunHistory readHistory(int id);

    @Query("select * from history order by id desc")
    RunHistory[] readHistoryAll();

    @Query("delete from history")
    void deleteHistoryAll();




}
