package ml.rektsky.rektrenamer.inheritance.impl

import ml.rektsky.rektrenamer.inheritance.InheritanceProvider

class JointInheritanceProvider: InheritanceProvider {

    private val providers = ArrayList<InheritanceProvider>()

    fun addChildren(inheritanceProvider: InheritanceProvider) {
        providers.add(inheritanceProvider)
    }

    override fun getParents(className: String): Array<String>? {
        for (provider in providers) {
            val parents = provider.getParents(className)
            if (parents != null) {
                return parents
            }
        }
        return null
    }
}