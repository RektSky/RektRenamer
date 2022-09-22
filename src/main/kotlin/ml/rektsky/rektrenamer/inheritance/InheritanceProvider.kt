package ml.rektsky.rektrenamer.inheritance


interface InheritanceProvider {

    fun getParents(className: String): Array<String>?

}