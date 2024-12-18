package qq

import org.ktorm.schema.*

object Applicants : Table<Nothing>("applicants") {
    val applicantId = int("applicant_id").primaryKey()
    val hash = varchar("hash").primaryKey()
    val stat = varchar("stat")
}
object ApplicantsDocuments : Table<Nothing>("applicants_documents") {
    val applicantsDocumentsId = int("applicants_documents_id").primaryKey()
    val applicant = int("applicant")
    val documentType = int("document_type")
    val documentNumber = varchar("document_number")
    val documentOwner = varchar("document_owner")
    val stat = varchar("stat")
    val createDate = timestamp("create_date")
    val blockDate = timestamp("block_date")

}

object Categories : Table<Nothing>("categories") {
    val categoryId = int("category_id").primaryKey()
    val name = varchar("name")
    val description = varchar("description")
    val stat = varchar("stat")
}

object CategoriesServices : Table<Nothing>("categories_services") {
    val categoriesServicesId = int("categories_services_id").primaryKey()
    val category = int("category")
    val service = int("service")
    val stat = varchar("stat")
    val createDate = timestamp("create_date")
    val blockDate = timestamp("block_date")

}


object DocumentTypes : Table<Nothing>("documenttypes") {
    val documentTypeId = int("document_type_id").primaryKey()
    val label = varchar("label")
    val description = varchar("description")
    val stat = varchar("stat")
}


object Main : Table<Nothing>("main") {
    val applicantsWsId = int("applicants_ws_id").primaryKey()
    val applicant = int("applicant")
    val categoryService = int("category_service")
    val windowStaff = int("window_staff")
    val stat = varchar("stat")
    val createDate = timestamp("create_date")
    val blockDate = timestamp("block_date")

}


object Services : Table<Nothing>("services") {
    val serviceId = int("service_id").primaryKey()
    val name = varchar("name")
    val description = varchar("description")
    val stat = varchar("stat")
}


object Settings : Table<Nothing>("settings") {
    val settingId = int("setting_id").primaryKey()
    val name = varchar("name")
    val value = varchar("value")
}


object Staff : Table<Nothing>("staff") {
    val staffId = int("staff_id").primaryKey()
    val name = varchar("name")
    val login = varchar("login")
    val password = varchar("password")
    val token = varchar("token")
    val admin = varchar("admin")
    val stat = varchar("stat")
}


object Windows : Table<Nothing>("windows") {
    val windowId = int("window_id").primaryKey()
    val label = varchar("label")
    val stat = varchar("stat")
}


object WindowsStaffs : Table<Nothing>("windows_staffs") {
    val windowsStaffsId = int("windows_staffs_id").primaryKey()
    val window = int("window")
    val staff = int("staff")
    val stat = varchar("stat")
    val createDate = timestamp("create_date")
    val blockDate = timestamp("block_date")
}

object Setting : Table<Nothing>("settings") {
    val id = int("id").primaryKey()
    val setting = varchar("setting")
    val value = varchar("value")
}