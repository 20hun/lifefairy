package com.danny.lifefairy.form

import kotlin.collections.ArrayList

data class HTTP_GET_Model(
    var something : String? =null ,
    var users : ArrayList<UserModel>? =null,
    var spaceYn : String?=null
)

data class NudgeModel(
    var data : NudgeDataModel?= null
)

data class NudgeDataModel(
    var mainMsg : String?=null,
    var feedback : FeedbackDataModel? =null
)
data class FeedbackDataModel(
    var id : Int?=null,
    var emoji: String?=null,
    var message: String?=null,
    var type: String?=null
)

data class UserModel(
    var idx : Int? =null ,
    var id : String?=null,
    var nick : String? =null
)

data class PostEmailCheckModel(
    var email : String? =null
)

data class PostLoginModel(
    var email : String? =null,
    var password : String?=null
)

data class PostDeviceTokenModel(
    var deviceToken : String? =null
)

data class PostGoogleModel(
    val grant_type: String?=null,
    val client_id: String?=null,
    val client_secret: String?=null,
    val redirect_uri: String?=null,
    val code: String?=null,
)

data class PostModel(
    var email : String? =null ,
    var password : String?=null,
    var name : String? =null,
    var emoji : String? =null
)

data class PostEmailAuthModel(
    var email : String?=null,
    val confirmCode : String?=null
)

data class TokenResult(
    var error : String? =null ,
    var message : String?=null,
    var access_token : String?=null,
    var refresh_token: String?=null,
    var userId : Int?=null,
    var name : String?=null
)

data class GoogleData(
    val access_token: String?=null
)

data class TokenData(
    val access_token: String?=null,
    val message: String?=null,
)

data class InvitationData(
    val invitationCode: String?=null
)

data class DevicePostData(
    val message: String?=null,
)

data class PostGetGo(
    var result:String? = null
)
