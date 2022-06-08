package andlima.hafizhfy.noteroomch8.local.room.notetable

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Note(
    @PrimaryKey(autoGenerate = true)
    var id : Int?,

    @ColumnInfo(name = "ownerId")
    var ownerId : String?,

    @ColumnInfo(name = "title")
    var title : String?,

    @ColumnInfo(name = "description")
    var description : String?
) : Parcelable
