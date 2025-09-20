package com.mipa.readerandroid.repository.nao

class Domain {
    companion object{

        const val publicPrefix = "/api/public"
        const val privatePrefix = "/api/private"

        const val authPublic = "${publicPrefix}/auth"
        const val authPrivate = "${privatePrefix}/auth"

        const val bookPublic = "${publicPrefix}/book"
        const val bookPivate = "${privatePrefix}/book"

        const val chapterPublic = "${publicPrefix}/chapter"
        const val chapterPrivate = "${privatePrefix}/chapter"

        const val LibraryPublic = "${publicPrefix}/library"
        const val LibraryPrivate = "${privatePrefix}/library"

        const val searchPublic = "${publicPrefix}/search"
        const val searchPrivate = "${privatePrefix}/search"


    }
}