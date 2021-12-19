package uz.pdp.adiblarapp.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Flowable
import uz.pdp.adiblarapp.room.entity.Writers

@Dao
interface WritersDao {

    @Insert
    fun addWriters(writers: Writers)

    @Delete
    fun deleteWriter(writers: Writers)

    @Query("select * from Writers")
    fun getAllWriters():Flowable<List<Writers>>
}