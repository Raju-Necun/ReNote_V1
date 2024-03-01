package com.example.cameraxapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.cameraxapp.utils.Constants.NOTE_TABLE

@Entity(tableName = NOTE_TABLE)

//@Entity(
//    tableName = NOTE_TABLE,
//    foreignKeys = [
//        ForeignKey(
//            entity = FolderEntity::class,
//            parentColumns = arrayOf("folderId"),
//            childColumns = arrayOf("folder_id"),
//            onDelete = ForeignKey.CASCADE
//        )
//    ]
//)

data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0,
    @ColumnInfo(name = "note_title")
    var noteTitle: String ="",
    @ColumnInfo(name = "file_uri")
    var fileUri: String ="",
    @ColumnInfo(name = "file_name")
    var fileName: String ="",
    @ColumnInfo(name = "is_favorite")
    var isFavorite:Boolean = false,
    @ColumnInfo(name = "time_stamp")
    var timeStamp:Long = 0,
    @ColumnInfo(name = "file_type")
    var fileType:String = "",
)


