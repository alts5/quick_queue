package qq


fun main() {
    val docs = DocumentTypesService()
    for (elem in docs.get_all_document_types()) {
        println(elem)
    }
}

