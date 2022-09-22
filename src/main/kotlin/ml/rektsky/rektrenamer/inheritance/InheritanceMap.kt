package ml.rektsky.rektrenamer.inheritance


class InheritanceMap {

    private val map = HashMap<String, Array<String>>()

    fun getParents(internalName: String): Array<String>? {
        return map[internalName]
    }

    operator fun set(internalName: String, array: Array<String>) {
        map[internalName] = array
    }

}