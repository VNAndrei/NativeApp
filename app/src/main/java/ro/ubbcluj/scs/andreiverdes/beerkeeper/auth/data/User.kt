package ro.ubbcluj.scs.andreiverdes.beerkeeper.auth.data

data class User(
    val token: String,
    val email: String,
    val displayName: String,
    val _id: String
)