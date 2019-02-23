package com.yutasuz.photo.api.response

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
) {

    val imageUrl: String?
        get() {
            if (id == null || farm == null || server == null || secret == null) return null
            return "https://farm$farm.staticflickr.com/$server/${id}_${secret}_c.jpg"
        }
}

