package qq


fun main() {
    val docs = DocumentTypesDAO()
    for (elem in docs.get_all_document_types()) {
        println(elem)
    }
}

