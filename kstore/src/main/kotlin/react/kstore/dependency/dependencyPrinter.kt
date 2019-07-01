package react.kstore.dependency

fun dependencyPrinter(root: Any, dependencyType: String, dependencies: Array<out Any>): String {
    return "${root::class.simpleName} $dependencyType:\n${dependencies.joinToString(
            separator = "\n",
            transform = { " - $it" })}"
}
