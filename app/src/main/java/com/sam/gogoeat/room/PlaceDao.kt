package com.sam.gogoeat.room

import androidx.room.*
import com.sam.gogoeat.data.GogoPlace

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(gogoPlace: GogoPlace) : Long

    @Insert
    fun insertList(list: List<GogoPlace>)

    @Update
    fun update(gogoPlace: GogoPlace)

    @Delete
    fun deleteOne(gogoPlace: GogoPlace)

    @Query("SELECT * FROM place_table")
    fun getAll(): List<GogoPlace>

}