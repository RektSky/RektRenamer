package ml.rektsky.rektrenamer.modifiable

import ml.rektsky.rektrenamer.data.ClassInfo

interface ModifiableProvider {

    fun isModifiable(className: String): Boolean?

}