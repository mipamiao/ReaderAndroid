package com.mipa.readerandroid.repository.nao

class Domain {
    companion object{

        const val publicPrefix = "/api/public"
        const val privatePrefix = "/api/private"

        const val authPublic = "${publicPrefix}/auth/"
        const val authPrivate = "${privatePrefix}/auth/"
    }
}