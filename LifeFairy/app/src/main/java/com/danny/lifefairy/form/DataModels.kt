package com.danny.lifefairy.form

data class HTTP_GET_Model(
    var something : String? =null ,
    var users : ArrayList<UserModel>? =null
)


data class UserModel(
    var idx : Int? =null ,
    var id : String?=null,
    var nick : String? =null
)

data class PostEmailCheckModel(
    var email : String? =null
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

data class PostResult(
    var error : String? =null ,
    var message : String?=null,
)

data class PostGetGo(
    var result:String? = null
)
