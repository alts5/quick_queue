package qq
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.util.UUID
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Date
import java.text.SimpleDateFormat


open class BaseService {
    val database = Database.connect(
        url = "jdbc:mysql://"
                + System.getenv("DB_HOST") + ":"
                + System.getenv("DB_PORT") + "/"
                + System.getenv("DB_NAME") +
                "?useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow&useSSL=false",
        driver = "com.mysql.jdbc.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD"),
    )
    public fun md5(text: String): String {
        val crypt = MessageDigest.getInstance("MD5");
        crypt.update(text.toByteArray());
        return BigInteger(1, crypt.digest()).toString(16)
    }
}

class DocumentTypesDAO(): BaseDAO() {
    public fun insert_document_type(label: String, description: String) {
        if (!label.equals("") && !description.equals("")) {
            database.insert(DocumentTypes) {
                set(it.label, label)
                set(it.description, description)
            }
        }
    }

    public fun delete_document_type(id: Int) {
        if (id > 0) {
            database.update(DocumentTypes) {
                set(it.stat, "Заблокировано")
                where {
                    it.documentTypeId eq id
                }
            }
        }
    }

    public fun get_document_type(id: Int): List<Map<String, String?>>? {
        if (id > 0) {
            return database.from(DocumentTypes)
                .select(DocumentTypes.label, DocumentTypes.description, DocumentTypes.documentTypeId)
                .where {
                    (DocumentTypes.documentTypeId eq id) and (DocumentTypes.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[DocumentTypes.documentTypeId].toString(),
                        "label" to row[DocumentTypes.label],
                        "description" to row[DocumentTypes.description]
                    )
                }
        }
        return null
    }

    public fun get_all_document_types(): List<Map<String, String?>> {
        return database.from(DocumentTypes)
            .select(DocumentTypes.label, DocumentTypes.description, DocumentTypes.documentTypeId)
            .where {
                (DocumentTypes.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[DocumentTypes.documentTypeId].toString(),
                    "description" to row[DocumentTypes.description],
                    "description" to row[DocumentTypes.description]
                )
            }
    }
}

class CategoriesDAO(): BaseDAO() {
    public fun insert_category(name: String, description: String) {
        if (!name.equals("") && !description.equals("")) {
            database.insert(Categories) {
                set(it.name, name)
                set(it.description, description)
            }
        }
    }
    public fun delete_category(id: Int) {
        if (id > 0) {
            database.update(Categories) {
                set(it.stat, "Заблокировано")
                where {
                    it.categoryId eq id
                }
            }
        }
    }
    public fun get_category(id: Int): List<Map<String, String?>>? {
        if (id > 0) {
            return database.from(Categories)
                .select(Categories.name, Categories.description, Categories.categoryId)
                .where {
                    (Categories.categoryId eq id) and (Categories.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[Categories.categoryId].toString(),
                        "name" to row[Categories.name],
                        "description" to row[Categories.description]
                    )
                }
        }
        return null
    }

    public fun get_all_categories(): List<Map<String, String?>> {
        return database.from(Categories)
            .select(Categories.name, Categories.description, Categories.categoryId)
            .where {
                (Categories.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[Categories.categoryId].toString(),
                    "name" to row[Categories.name],
                    "description" to row[Categories.description]
                )
            }
    }
}

class ServicesDAO(): BaseDAO() {
    public fun insert_service(name: String, description: String) {
        if (!name.equals("") && !description.equals("")) {
            database.insert(Services) {
                set(it.name, name)
                set(it.description, description)
            }
        }
    }
    public fun delete_service(id: Int) {
        if (id > 0) {
            database.update(Services) {
                set(it.stat, "Заблокировано")
                where {
                    it.serviceId eq id
                }
            }
        }
    }
    public fun get_service(id: Int): List<Map<String, String?>>? {
        if (id > 0) {
            return database.from(Services)
                .select(Services.name, Services.description, Services.serviceId)
                .where {
                    (Services.serviceId eq id) and (Services.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[Services.serviceId].toString(),
                        "name" to row[Services.name],
                        "description" to row[Services.description]
                    )
                }
        }
        return null
    }

    public fun get_all_services(): List<Map<String, String?>> {
        return database.from(Services)
            .select(Services.name, Services.description)
            .where {
                (Services.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[Services.serviceId].toString(),
                    "name" to row[Services.name],
                    "description" to row[Services.description]
                )
            }
    }
}

class WindowsDAO(): BaseDAO() {
    public fun insert_window(label: String) {
        if (!label.equals("")) {
            database.insert(Windows) {
                set(it.label, label)
            }
        }
    }
    public fun delete_window(id: Int) {
        if (id > 0) {
            database.update(Windows) {
                set(it.stat, "Заблокировано")
                where {
                    it.windowId eq id
                }
            }
        }
    }
    public fun get_window(id: Int): List<Map<String, String?>>? {
        if (id > 0) {
            return database.from(Windows)
                .select(Windows.label)
                .where {
                    (Windows.windowId eq id) and (Windows.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[Windows.windowId].toString(),
                        "label" to row[Windows.label]
                    )
                }
        }
        return null
    }

    public fun get_all_windows(): List<Map<String, String?>> {
        return database.from(Windows)
            .select(Windows.label)
            .where {
                (Windows.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[Windows.windowId].toString(),
                    "label" to row[Windows.label]
                )
            }
    }
}

class ApplicantsDAO(): BaseDAO() {
    public fun insert_applicant() {
        database.insert(Applicants) {
            set(it.hash, UUID.randomUUID())
        }
    }
    public fun delete_applicant(id: Int) {
        if (id > 0) {
            database.update(Applicants) {
                set(it.stat, "Заблокирован")
                where {
                    it.applicantId eq id
                }
            }
        }
    }
    public fun get_applicant(id: Int): List<Map<String, String?>>? {
        if (id > 0) {
            return database.from(Applicants)
                .select(Applicants.hash, Applicants.applicantId)
                .where {
                    (Applicants.applicantId eq id) and (Applicants.stat notEq "Заблокирован")
                }
                .map { row ->
                    mapOf(
                        "id" to row[Applicants.applicantId].toString(),
                        "hash" to row[Applicants.hash],
                    )
                }
        }
        return null
    }

    public fun get_all_applicants(): List<Map<String, String?>> {
        return database.from(Applicants)
            .select(Applicants.hash, Applicants.applicantId)
            .where {
                (Applicants.stat notEq "Заблокирован")
            }
            .map { row ->
                mapOf(
                    "id" to row[Applicants.applicantId].toString(),
                    "hash" to row[Applicants.hash],
                )
            }
    }
}

class StaffDAO(): BaseDAO() {
    public fun insert_staff(name: String, login: String, password: String) {
        if (!name.equals("") && !login.equals("") && !password.equals("")) {
            database.insert(Staff) {
                set(it.name, name)
                set(it.login, login)
                set(it.password, UUID.fromString(password))
            }
        }
    }
    public fun delete_staff(id: Int) {
        if (id > 0) {
            database.update(Staff) {
                set(it.stat, "Заблокирован")
                where {
                    it.staffId eq id
                }
            }
        }
    }
    public fun get_staff(id: Int): List<Map<String, String?>>? {
        if (id > 0) {
            return database.from(Staff)
                .select(Staff.name, Staff.login, Staff.password, Staff.staffId)
                .where {
                    (Staff.staffId eq id) and (Staff.stat notEq "Заблокирован")
                }
                .map { row ->
                    mapOf(
                        "id" to row[Staff.staffId].toString(),
                        "login" to row[Staff.name],
                        "password" to row[Staff.login],
                        "name" to row[Staff.password],
                    )
                }
        }
        return null
    }

    public fun get_all_staffs(): List<Map<String, String?>> {
        return database.from(Staff)
            .select(Staff.name, Staff.login, Staff.password, Staff.staffId)
            .where {
                (Staff.stat notEq "Заблокирован")
            }
            .map { row ->
                mapOf(
                    "id" to row[Staff.staffId].toString(),
                    "login" to row[Staff.name],
                    "password" to row[Staff.login],
                    "name" to row[Staff.password],
                )
            }
    }
}


class CategoriesServicesDAO(): BaseDAO() {
    public fun insert_category_service(category: Int, service: Int) {
        if (category > 0 && service > 0) {
            database.insert(CategoriesServices) {
                set(it.category, category)
                set(it.service, service)
            }
        }
    }
    public fun delete_category_service(id: Int) {
        if (id > 0) {
            database.update(CategoriesServices) {
                set(it.stat, "Заблокирован")
                set(it.blockDate, SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Date()))
                where {
                    it.categoriesServicesId eq id
                }
            }
        }
    }
    public fun get_category_service(id: Int): List<Map<String, Any>>? {
        if (id > 0) {
            return database.from(CategoriesServices)
                .select(CategoriesServices.category, CategoriesServices.service, CategoriesServices.categoriesServicesId)
                .where {
                    (CategoriesServices.categoriesServicesId eq id) and (CategoriesServices.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[CategoriesServices.categoriesServicesId].toString(),
                        "category" to row[CategoriesServices.category].toString(),
                        "service" to row[CategoriesServices.service].toString(),
                    )
                }
        }
        return null
    }
    public fun get_all_categories_services(): List<Map<String, String>> {
        return database.from(CategoriesServices)
            .select(CategoriesServices.category, CategoriesServices.service, CategoriesServices.categoriesServicesId)
            .where {
                (CategoriesServices.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[CategoriesServices.categoriesServicesId].toString(),
                    "category" to row[CategoriesServices.category].toString(),
                    "service" to row[CategoriesServices.service].toString(),
                )
            }
    }
}

class WindowsStaffDAO(): BaseDAO() {
    public fun insert_window_staff(window: Int, staff: Int) {
        if (window > 0 && staff > 0) {
            database.insert(WindowsStaffs) {
                set(it.window, window)
                set(it.staff, staff)
            }
        }
    }
    public fun delete_window_staff(id: Int) {
        if (id > 0) {
            database.update(WindowsStaffs) {
                set(it.stat, "Заблокирован")
                set(it.blockDate, SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Date()))
                where {
                    it.windowsStaffsId eq id
                }

            }
        }
    }
    public fun get_window_staff(id: Int): List<Map<String, String>>? {
        if (id > 0) {
            return database.from(WindowsStaffs)
                .select(WindowsStaffs.window, WindowsStaffs.staff, WindowsStaffs.windowsStaffsId)
                .where {
                    (WindowsStaffs.windowsStaffsId eq id) and (CategoriesServices.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[WindowsStaffs.windowsStaffsId].toString(),
                        "window" to row[WindowsStaffs.window].toString(),
                        "staff" to row[WindowsStaffs.staff].toString(),
                    )
                }

        }
        return null
    }
    public fun get_all_windows_staff(): List<Map<String, String>> {
        return database.from(WindowsStaffs)
            .select(WindowsStaffs.window, WindowsStaffs.staff, WindowsStaffs.windowsStaffsId)
            .where {
                (WindowsStaffs.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[WindowsStaffs.windowsStaffsId].toString(),
                    "window" to row[WindowsStaffs.window].toString(),
                    "staff" to row[WindowsStaffs.staff].toString(),
                )
            }
    }
}

class ApplicantsDocumentsDAO(): BaseDAO() {
    public fun insert_applicant_service(applicant: Int, documentType: Int, documentNumber: String, documentOwner: String) {
        if (applicant > 0 && documentType > 0 && !documentNumber.equals("") && !documentOwner.equals("")) {
            database.insert(ApplicantsDocuments) {
                set(it.applicant, applicant)
                set(it.documentType, documentType)
                set(it.documentNumber, documentNumber)
                set(it.documentOwner, documentOwner)
            }
        }
    }
    public fun delete_applicant_service(id: Int) {
        if (id > 0) {
            database.update(ApplicantsDocuments) {
                set(it.stat, "Заблокирован")
                set(it.blockDate, SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Date()))
                where {
                    it.applicantsDocumentsId eq id
                }
            }
        }
    }
    public fun get_applicant_service(id: Int): List<Map<String, String?>>? {
        if (id > 0) {
            return database.from(ApplicantsDocuments)
                .select(ApplicantsDocuments.applicant, ApplicantsDocuments.documentType, ApplicantsDocuments.documentNumber, ApplicantsDocuments.documentOwner, ApplicantsDocuments.applicantsDocumentsId)
                .where {
                    (ApplicantsDocuments.applicantsDocumentsId eq id) and (ApplicantsDocuments.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[ApplicantsDocuments.applicantsDocumentsId].toString(),
                        "applicant" to row[ApplicantsDocuments.applicant].toString(),
                        "documentType" to row[ApplicantsDocuments.documentType].toString(),
                        "documentNumber" to row[ApplicantsDocuments.documentNumber],
                        "documentOwner" to row[ApplicantsDocuments.documentOwner]
                    )
                }
        }
        return null
    }
    public fun get_all_applicants_services(): List<Map<String, String?>> {
        return database.from(ApplicantsDocuments)
            .select(ApplicantsDocuments.applicant, ApplicantsDocuments.documentType, ApplicantsDocuments.documentNumber, ApplicantsDocuments.documentOwner, ApplicantsDocuments.applicantsDocumentsId)
            .where {
                (ApplicantsDocuments.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[ApplicantsDocuments.applicantsDocumentsId].toString(),
                    "applicant" to row[ApplicantsDocuments.applicant].toString(),
                    "documentType" to row[ApplicantsDocuments.documentType].toString(),
                    "documentNumber" to row[ApplicantsDocuments.documentNumber],
                    "documentOwner" to row[ApplicantsDocuments.documentOwner]
                )
            }
    }
}

class ApplicantsCategoriesWindowsDAO(): BaseDAO() {
    public fun insert_applicant_category_window(applicant: Int, categoryService: Int, windowStaff: Int) {
        if (applicant > 0 && categoryService > 0 && windowStaff > 0) {
            database.insert(Main) {
                set(it.applicant, applicant)
                set(it.categoryService, categoryService)
                set(it.windowStaff, windowStaff)
            }
        }
    }
    public fun delete_applicant_category_window(id: Int) {
        if (id > 0) {
            database.update(Main) {
                set(it.stat, "Заблокирован")
                set(it.blockDate, SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Date()))
                where {
                    it.applicantsWsId eq id
                }
            }
        }
    }
    public fun get_applicant_category_window(id: Int): List<Map<String, String>>? {
        if (id > 0) {
            return database.from(Main)
                .select(Main.applicant, Main.categoryService, Main.windowStaff, Main.applicantsWsId)
                .where {
                    (Main.applicantsWsId eq id) and (Main.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[Main.applicantsWsId].toString(),
                        "applicant" to row[Main.applicant].toString(),
                        "categoryService" to row[Main.categoryService].toString(),
                        "windowStaff" to row[Main.windowStaff].toString()
                    )
                }
        }
        return null
    }
    public fun get_all_applicants_categories_windows(): List<Map<String, String>> {
        return database.from(Main)
            .select(Main.applicant, Main.categoryService, Main.windowStaff)
            .where {
                (Main.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[Main.applicantsWsId].toString(),
                    "applicant" to row[Main.applicant].toString(),
                    "categoryService" to row[Main.categoryService].toString(),
                    "windowStaff" to row[Main.windowStaff].toString()
                )
            }
    }
}
