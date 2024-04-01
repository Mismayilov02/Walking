package com.imehemmed.walking.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.imehemmed.walking.Constant;


@Database(entities = {RunHistory.class, RunHistoryDetail.class} , exportSchema = false , version = 1)
public abstract class MyroomDatabase extends RoomDatabase {
public abstract RunHistoryDao historyDao();
public abstract RunDetailHistoryDao historyDetailDao();

private  static MyroomDatabase INSTANCE;
public static MyroomDatabase getINSTANCE(Context context){

    if(INSTANCE == null){
        INSTANCE = Room.databaseBuilder(context.getApplicationContext() ,
               MyroomDatabase.class , Constant.tableName).allowMainThreadQueries().build();
    }
    return INSTANCE;
}

}
