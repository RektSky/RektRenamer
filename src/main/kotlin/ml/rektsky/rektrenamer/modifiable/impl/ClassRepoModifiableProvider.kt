package ml.rektsky.rektrenamer.modifiable.impl

import ml.rektsky.rektrenamer.ClassRepo
import ml.rektsky.rektrenamer.data.FieldInfo
import ml.rektsky.rektrenamer.data.MethodInfo
import ml.rektsky.rektrenamer.modifiable.ModifiableProvider

class ClassRepoModifiableProvider(val classRepo: ClassRepo, val modifiable: Boolean): ModifiableProvider {


    override fun isModifiable(className: String): Boolean? {
        if (className in classRepo) {
            return modifiable
        } else {
            return null
        }
    }

}