package com.mehemmed_i.walking.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mehemmed_i.walking.Static;


@Database(entities = {History.class} , exportSchema = false , version = 1)
public abstract class MyroomDatabase extends RoomDatabase {
public abstract History_Dao history_dao();

private  static MyroomDatabase INSTANCE;
public static MyroomDatabase getINSTANCE(Context context){

    if(INSTANCE == null){
        INSTANCE = Room.databaseBuilder(context.getApplicationContext() ,
               MyroomDatabase.class , Static.tableName).allowMainThreadQueries().build();
    }
    return INSTANCE;
}

}
