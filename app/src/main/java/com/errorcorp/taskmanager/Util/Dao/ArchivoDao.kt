package com.errorcorp.taskmanager.Util.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.errorcorp.taskmanager.Model.Archivo

@Dao
interface ArchivoDao {
    @Insert
    fun insertArchivo(archivo: Archivo)

    @Update
    fun updateArchivo(archivo: Archivo)

    @Delete
    fun deleteArchivo(archivo: Archivo)

    @Query("SELECT * FROM archivo")
    fun getAllArchivos(): List<Archivo>

    @Query("SELECT * FROM archivo WHERE nombre = :nombre")
    fun getArchivoByNombre(nombre: String): Archivo?

    @Transaction
    @Query("SELECT COUNT(*) FROM Archivo")
    fun getCount(): Int
}