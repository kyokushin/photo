package com.yutasuz.photo.api.response

import android.os.Parcel
import android.os.Parcelable

data class FlickrPhotosResultResponse(
    var photos: FlickrPhotosResponse?
)

class FlickrPhotosResponse(
    var page: Int?,
    var pages: Int?,
    var parpage: Int?,
    var total: Int?,
    var photo: List<FlickrPhotoResponse?>?
)

class FlickrPhotoResponse(
    var id: Long?,
    var owner: String?,
    var secret: String?,
    var server: String?,
    var farm: Int?,
    var title: String?,
    var ispublic: Int?,
    var isfriend: Int?,
    var isfamily: Int?
) : Parcelable {

    val imageUrl: String?
        get() {
            return createImageUrl("c")
        }

    val imageUrlLarge: String?
        get() {
            return createImageUrl("k")
        }

    private fun isInvalid() = (id == null || farm == null || server == null || secret == null)

    private fun createImageUrl(sizeSuffix: String): String? {
        if (isInvalid()) return null
        return "https://farm$farm.staticflickr.com/$server/${id}_${secret}_$sizeSuffix.jpg"
    }

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(owner)
        writeString(secret)
        writeString(server)
        writeValue(farm)
        writeString(title)
        writeValue(ispublic)
        writeValue(isfriend)
        writeValue(isfamily)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FlickrPhotoResponse> = object : Parcelable.Creator<FlickrPhotoResponse> {
            override fun createFromParcel(source: Parcel): FlickrPhotoResponse = FlickrPhotoResponse(source)
            override fun newArray(size: Int): Array<FlickrPhotoResponse?> = arrayOfNulls(size)
        }
    }
}

