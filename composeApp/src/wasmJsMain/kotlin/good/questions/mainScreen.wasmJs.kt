package good.questions

actual fun openUrlwasm(url: String) {
    js("window.open(url, '_blank')")

}