package com.imehemmed.walking.Room;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RunDetailHistoryDao {

    @Insert
    void insertHistory(RunHistoryDetail history);

    @Query("select * from historydetail where id =:id")
    RunHistoryDetail readHistory(int id);

    @Query("select count(*) from historydetail where parentId =:parentId")
    int readHistorySizebyParentId(int parentId);

    @Query("select * from historydetail where parentId =:parentId")
    RunHistoryDetail[] readHistorybyParentId(int parentId);

    @Query("select * from historydetail")
    RunHistoryDetail[] readHistoryAll();

    @Query("delete from historydetail")
    void deleteHistoryAll();




}
