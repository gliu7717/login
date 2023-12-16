package com.example.login.models

class User {
    var _id: String = ""
    var name: String = ""
    var email: String = ""
    var image: String = ""
    var contacts: ArrayList<UserContact> = ArrayList()
    var createdAt: String = ""
}