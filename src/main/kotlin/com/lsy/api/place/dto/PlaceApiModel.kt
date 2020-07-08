package com.lsy.api.place.dto

class KakaoPlaceApiResultDto(
        var meta: KakaoPlaceApiMetaDto?,
        var documents: List<KakaoPlaceApiDetailDto>?
)

class KakaoPlaceApiMetaDto(
        var pageable_count: Int,
        var is_end: Boolean
)
class KakaoPlaceApiDetailDto(
        var id: String,
        var place_name: String,
        var phone: String,
        var address_name: String,
        var road_address_name: String,
        var place_url: String
)

data class PlaceDetail(
        var placeId: String,
        var placeName: String,
        var phone: String,
        var addressName: String,
        var roadAddressName: String,
        var placeUrl: String
)

data class KeywordSearchCountDto (
        var keyword: String,
        var count: Int
): java.io.Serializable