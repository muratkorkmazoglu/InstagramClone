package com.muratkorkmazoglu.instagramkotlinclone.Model

class UserDetails {

    var follower: String? = null
    var following: String? = null
    var post: String? = null
    var profilePicture: String? = null
    var biography: String? = null
    var webSite: String? = null

    constructor()
    constructor(
        follower: String?,
        following: String?,
        post: String?,
        profilePicture: String?,
        biography: String?,
        webSite: String?
    ) {
        this.follower = follower
        this.following = following
        this.post = post
        this.profilePicture = profilePicture
        this.biography = biography
        this.webSite = webSite
    }

    override fun toString(): String {
        return "UserDetails(follower=$follower, following=$following, post=$post, profilePicture=$profilePicture, biography=$biography, webSite=$webSite)"
    }


}