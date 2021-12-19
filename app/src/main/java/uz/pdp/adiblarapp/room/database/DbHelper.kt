package uz.pdp.adiblarapp.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.pdp.adiblarapp.room.dao.WritersDao
import uz.pdp.adiblarapp.room.entity.Writers

@Database(entities = [Writers::class], version = 1)
abstract class DbHelper: RoomDatabase() {

    abstract fun funDb(): WritersDao

    companion object {

        private var dbHelper: DbHelper? = null

        @Synchronized
        fun getInstance(context: Context): DbHelper {
            if (dbHelper == null) {
                dbHelper = Room.databaseBuilder(context, DbHelper::class.java, "my_db")
                    .allowMainThreadQueries()
                    .build()
            }

            return dbHelper!!
        }

    }
}