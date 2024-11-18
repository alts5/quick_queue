package qq
import org.ktorm.dsl.forEach


fun main() {
    val docs = DocumentTypesService()
    print(docs.get_all_document_types().forEach { row -> println(row[DocumentTypes.label]) })
}

