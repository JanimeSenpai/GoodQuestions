package good.questions

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform