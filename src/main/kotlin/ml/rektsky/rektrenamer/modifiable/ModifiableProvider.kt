package ml.rektsky.rektrenamer.modifiable


interface ModifiableProvider {

    fun isModifiable(className: String): Boolean?

}