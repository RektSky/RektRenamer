package ml.rektsky.rektrenamer.modifiable


class ModifiableMap {

    private val map = HashMap<String, Boolean>()

    fun isModifiable(internalName: String): Boolean? {
        return map[internalName]
    }

    operator fun set(internalName: String, data: Boolean) {
        map[internalName] = data
    }

}