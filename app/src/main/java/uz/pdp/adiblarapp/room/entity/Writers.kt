package uz.pdp.adiblarapp.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Writers : Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var uid: String? = null
    var name: String? = null
    var image: String? = null
    var life_sicle: String? = null
    var type: String? = null
    var dict: String? = null

    constructor(
        uid: String?,
        name: String?,
        image: String?,
        life_sicle: String?,
        type: String?,
        dict: String?
    ) {
        this.uid = uid
        this.name = name
        this.image = image
        this.life_sicle = life_sicle
        this.type = type
        this.dict = dict
    }
    constructor()

    constructor(
        id: Int,
        uid: String?,
        name: String?,
        image: String?,
        life_sicle: String?,
        type: String?,
        dict: String?
    ) {
        this.id = id
        this.uid = uid
        this.name = name
        this.image = image
        this.life_sicle = life_sicle
        this.type = type
        this.dict = dict
    }


}