package ml.rektsky.rektrenamer.modifiable.impl

import ml.rektsky.rektrenamer.modifiable.ModifiableProvider

class JointModifiableProvider: ModifiableProvider {

    private val providers = ArrayList<ModifiableProvider>()

    fun addChildren(modifiableProvider: ModifiableProvider) {
        providers.add(modifiableProvider)
    }

    override fun isModifiable(className: String): Boolean? {
        for (provider in providers) {
            val modifiable = provider.isModifiable(className)
            if (modifiable != null) {
                return modifiable
            }
        }
        return null
    }

}