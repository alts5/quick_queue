package qq

import io.ktor.http.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.math.BigInteger
import java.security.MessageDigest
//import java.security.Timestamp
import java.util.Date
import java.text.SimpleDateFormat

/**
 * Базовый класс DAO, обеспечивающий подключение к базе данных и служебные методы.
 */


open class BaseDAO {
    /**
     * Устанавливает соединение с базой данных MySQL.
     */
    val database = Database.connect(
        url = "jdbc:mysql://"
                + System.getenv("DB_HOST") + ":"
                + System.getenv("DB_PORT") + "/"
                + System.getenv("DB_NAME") +
                "?allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=utf8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow&useSSL=false",
        driver = "com.mysql.jdbc.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD"),
    )

    /**
     * Генерирует MD5-хэш входного текста.
     *
     * @param text - хэшируемый входной текст.
     * @return MD5-хэш входного текста в виде строки.
     */
    public fun md5(text: String): String {
        val crypt = MessageDigest.getInstance("MD5");
        crypt.update(text.toByteArray());
        return BigInteger(1, crypt.digest()).toString(16)
    }
}

/**
 * Объект доступа к данным для управления типами документов.
 */
class DocumentTypesDAO() : BaseDAO() {
    /**
     * Вставляет новый тип документа в базу данных.
     *
     * @param label - Метка типа документа.
     * @param description - Описание типа документа.
     */
    public fun insert_document_type(label: String?, description: String?): Boolean {
        if (!label.equals("")) {
            database.insert(DocumentTypes) {
                set(it.label, label)
                set(it.description, description)
            }
            return true
        }
        return false
    }

    /**
     * Помечает тип документа как удаленный.
     *
     * @param id - Идентификатор типа документа, который должен быть помечен как удаленный.
     */
    public fun delete_document_type(id: Int): Boolean {
        if (id > 0) {
            database.delete(DocumentTypes) {
                it.documentTypeId eq id
            }
            return true
        }
        return false
    }

    public fun set_stat_field(id: Int, stat: String): Boolean {
        if (id > 0) {
            database.update(DocumentTypes) {
                set(it.stat, stat)
                where {
                    it.documentTypeId eq id
                }
            }
            return true
        }
        return false
    }

    /**
     * Извлекает тип документа по его идентификатору.
     *
     * @param id - идентификатор типа документа для извлечения.
     * @return Карту, содержащую информацию о полученном типе документа, или значение null, если оно не найдено.
     */
    public fun get_document_type(id: Int): Map<String, String?>? {
        if (id > 0) {
            return database.from(DocumentTypes)
                .select(DocumentTypes.label, DocumentTypes.stat, DocumentTypes.description, DocumentTypes.documentTypeId)
                .where {
                    (DocumentTypes.documentTypeId eq id)
                }
                .map { row ->
                    mapOf(
                        "id" to row[DocumentTypes.documentTypeId].toString(),
                        "label" to row[DocumentTypes.label],
                        "stat" to row[DocumentTypes.stat],
                        "description" to row[DocumentTypes.description]
                    )
                }[0]
        }
        return null
    }

    /**
     * Извлекает все активные типы документов.
     *
     * @return Список карт, содержащих всю информацию об активных типах документов.
     */
    public fun get_all_document_types(): List<Map<String, String?>> {
        return database.from(DocumentTypes)
            .select(DocumentTypes.label, DocumentTypes.stat, DocumentTypes.description, DocumentTypes.documentTypeId)
            .map { row ->
                mapOf(
                    "id" to row[DocumentTypes.documentTypeId].toString(),
                    "label" to row[DocumentTypes.label],
                    "stat" to row[DocumentTypes.stat],
                    "description" to row[DocumentTypes.description]
                )
            }
    }
    public fun get_visible_document_types(): List<Map<String, String?>> {
        return database.from(DocumentTypes)
            .select(DocumentTypes.label, DocumentTypes.stat, DocumentTypes.description, DocumentTypes.documentTypeId)
            .where {
                (DocumentTypes.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[DocumentTypes.documentTypeId].toString(),
                    "label" to row[DocumentTypes.label],
                    "stat" to row[DocumentTypes.stat],
                    "description" to row[DocumentTypes.description]
                )
            }
    }
}

/**
 * Объект доступа к данным для управления категориями.
 */
class CategoriesDAO() : BaseDAO() {
    /**
     * Вставляет новую категорию в базу данных.
     *
     * @param name Имя категории.
     * @param description Описание категории.
     */
    public fun insert_category(name: String, description: String) {
        if (!name.equals("") && !description.equals("")) {
            database.insert(Categories) {
                set(it.name, name)
                set(it.description, description)
            }
        }
    }

    /**
     * Отмечает категорию как удаленную.
     *
     * @param id Идентификатор категории, которая будет отмечена как удаленная.
     */
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

    /**
     * Извлекает категорию по ее идентификатору.
     *
     * @param id Идентификатор категории для извлечения.
     * @return Список карт, содержащих извлеченную информацию о категории, или null, если не найдено.
     */
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

    /**
     * Извлекает все активные категории.
     *
     * @return Карту, содержащую всю информацию об активных категориях.
     */
    public fun get_all_categories(): Map<String, String?> {
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
            }[0]
    }
}

/**
 * Объект доступа к данным для управления службами.
 */

class ServicesDAO() : BaseDAO() {
    /**
     * Вставляет новую услугу в базу данных.
     *
     * @param name Имя услуги.
     * @param description Описание услуги.
     */
    public fun insert_service(name: String, description: String) {
        if (!name.equals("") && !description.equals("")) {
            database.insert(Services) {
                set(it.name, name)
                set(it.description, description)
            }
        }
    }

    /**
     * Отмечает службу как удаленную.
     *
     * @param id Идентификатор службы, которая будет отмечена как удаленная.
     */
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

    /**
     * Извлекает службу по ее идентификатору.
     *
     * @param id Идентификатор службы для извлечения.
     * @return Карту, содержащую извлеченную информацию о службе, или null, если не найдено.
     */
    public fun get_service(id: Int): Map<String, String?>? {
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
                }[0]
        }
        return null
    }

    /**
     * Извлекает все активные службы.
     *
     * @return Список карт, содержащих всю информацию об активных службах.
     */
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

/**
 * Представляет объект доступа к данным (DAO) для операций Windows.
 */
class WindowsDAO : BaseDAO() {

    /**
     * Вставляет новое окно с заданной меткой.
     *
     * @param label Метка для нового окна.
     * @throws IllegalArgumentException, если метка пустая.
     */
    public fun insert_window(label: String) {
        if (!label.equals("")) {
            database.insert(Windows) {
                set(it.label, label)
            }
        }
    }

    /**
     * Помечает окно с заданным идентификатором как заблокированное.
     *
     * @param id Идентификатор окна для блокировки.
     * @throws IllegalArgumentException, если идентификатор не положительный.
     */
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

    /**
     * Извлекает сведения об окне с указанным идентификатором.
     *
     * @param id Идентификатор окна для извлечения.
     * @return Карта, содержащая идентификатор службы, имя и описание окна,
     * или null, если окно не найдено или заблокировано.
     * @throws IllegalArgumentException, если идентификатор не положительный.
     */
    public fun get_window(id: Int): Map<String, String?>? {
        if (id > 0) {
            return database.from(Windows)
                .select(Windows.label, Windows.windowId)
                .where {
                    (Windows.windowId eq id) and (Services.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[Windows.windowId].toString(),
                        "label" to row[Windows.label],
                    )
                }[0]
        }
        return null
    }

    /**
     * Извлекает все доступные службы, которые не заблокированы.
     *
     * @return Список карт, содержащих идентификаторы служб, названия и описания.
     */
    public fun get_all_windows(): List<Map<String, String?>> {
        return database.from(Windows)
            .select(Windows.label, Windows.windowId)
            .where {
                (Windows.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[Windows.windowId].toString(),
                    "label" to row[Windows.label],
                )
            }
    }
}

class StaffDAO : BaseDAO() {
    public fun insert_staff(name: String, login: String) {
        if (!name.equals("") && !login.equals("")) {
            database.insert(Staff) {
                set(it.name, name)
                set(it.login, login)
            }
        }
    }

    public fun delete_staff(id: Int) {
        if (id > 0) {
            database.update(Staff) {
                set(it.stat, "Заблокировано")
                where {
                    it.staffId eq id
                }
            }
        }
    }
    public fun update_token_staff(login: String, token: String) {
        if (!login.equals("")) {
            database.update(Staff) {
                set(it.token, token)
                where {
                    it.login eq login
                }
            }
        }
    }

    public fun get_staff(id: Int): Map<String, String?>? {
        if (id > 0) {
            return database.from(Staff)
                .select(Staff.name, Staff.login, Staff.staffId, Staff.password, Staff.admin, Staff.token)
                .where {
                    (Staff.staffId eq id) and (Staff.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[Staff.staffId].toString(),
                        "name" to row[Staff.name],
                        "login" to row[Staff.login],
                        "password" to row[Staff.password],
                        "is_admin" to row[Staff.admin],
                        "token" to row[Staff.token]
                    )
                }[0]
        }
        return null
    }

    public fun get_staff_by_token(token: String): Map<String, String?>? {
        if (!token.equals("")) {
            return database.from(Staff)
                .select(Staff.name, Staff.staffId, Staff.admin)
                .where {
                    (Staff.token eq token) and (Staff.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[Staff.staffId].toString(),
                        "name" to row[Staff.name],
                        "is_admin" to row[Staff.admin]
                    )
                }[0]
        }
        return null
    }

    public fun get_all_staff(): List<Map<String, String?>> {
        return database.from(Staff)
            .select(Staff.name, Staff.login, Staff.staffId, Staff.password, Staff.admin, Staff.token)
            .where {
                (Staff.stat notEq "Заблокировано")
            }
            .map { row ->
                mapOf(
                    "id" to row[Staff.staffId].toString(),
                    "name" to row[Staff.name],
                    "login" to row[Staff.login],
                    "password" to row[Staff.password],
                    "is_admin" to row[Staff.admin],
                    "token" to row[Staff.token]
                )
            }
    }
}


/**
 * Объект доступа к данным для управления окнами и ассоциациями персонала.
 */
class WindowsStaffDAO() : BaseDAO() {
    /**
     * Вставляет новую связь между окном и сотрудником в базу данных.
     *
     * @param window Идентификатор окна.
     * @param staff Идентификатор сотрудника.
     */
    public fun insert_window_staff(window: Int, staff: Int) {
        if (window > 0 && staff > 0) {
            database.insert(WindowsStaffs) {
                set(it.window, window)
                set(it.staff, staff)
            }
        }
    }

    /**
     * Отмечает связь окна и персонала как удаленную.
     *
     * @param id Идентификатор связи, которая будет отмечена как удаленная.
     */
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

    /**
     * Извлекает ассоциацию окна-штафт по ее идентификатору.
     *
     * @param id Идентификатор ассоциации для извлечения.
     * @return Карту, содержащую извлеченную информацию об ассоциации, или null, если она не найдена.
     */
    public fun get_window_staff(id: Int): Map<String, String>? {
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
                }[0]

        }
        return null
    }

    /**
     * Извлекает все активные ассоциации окон и персонала.
     *
     * @return Карту, содержащую всю информацию об активных ассоциациях.
     */
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

/**
 * Объект доступа к данным для управления ассоциациями заявителя и службы.
 */
class ApplicantsDocumentsDAO() : BaseDAO() {
    /**
     * Вставляет новую связь между заявителем и документом в базу данных.
     *
     * @param claim Идентификатор заявителя.
     * @param documentType Идентификатор типа документа.
     * @param documentNumber Номер документа.
     * @param documentOwner Владелец документа.
     */
    public fun insert_applicant_service(
        applicant: Int,
        documentType: Int,
        documentNumber: String,
        documentOwner: String
    ) {
        if (applicant > 0 && documentType > 0 && !documentNumber.equals("") && !documentOwner.equals("")) {
            database.insert(ApplicantsDocuments) {
                set(it.applicant, applicant)
                set(it.documentType, documentType)
                set(it.documentNumber, documentNumber)
                set(it.documentOwner, documentOwner)
            }
        }
    }

    /**
     * Отмечает связь заявителя с услугой как удаленную.
     *
     * @param id Идентификатор связи, которая будет отмечена как удаленная.
     */
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

    /**
     * Извлекает связь заявителя с услугой по ее идентификатору.
     *
     * @param id Идентификатор связи для извлечения.
     * @return Карту, содержащую извлеченную информацию об ассоциации, или null, если не найдено.
     */
    public fun get_applicant_service(id: Int): Map<String, String?>? {
        if (id > 0) {
            return database.from(ApplicantsDocuments)
                .select(
                    ApplicantsDocuments.applicant,
                    ApplicantsDocuments.documentType,
                    ApplicantsDocuments.documentNumber,
                    ApplicantsDocuments.documentOwner,
                    ApplicantsDocuments.applicantsDocumentsId
                )
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
                }[0]
        }
        return null
    }

    /**
     * Извлекает все активные ассоциации заявителя и службы.
     *
     * @return Список карт, содержащих всю информацию об активных ассоциациях.
     */
    public fun get_all_applicants_services(): List<Map<String, String?>> {
        return database.from(ApplicantsDocuments)
            .select(
                ApplicantsDocuments.applicant,
                ApplicantsDocuments.documentType,
                ApplicantsDocuments.documentNumber,
                ApplicantsDocuments.documentOwner,
                ApplicantsDocuments.applicantsDocumentsId
            )
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

/**
 * Объект доступа к данным для управления ассоциациями заявитель-категория-окно.
 */
class ApplicantsCategoriesWindowsDAO() : BaseDAO() {
    /**
     * Вставляет новую связь между заявителем, category-service и window-staff.
     *
     * @param claimor Идентификатор заявителя.
     * @param categoryService Идентификатор category-service.
     * @param windowStaff Идентификатор window-staff.
     */
    public fun insert_applicant_category_window(applicant: Int, categoryService: Int, windowStaff: Int) {
        if (applicant > 0 && categoryService > 0 && windowStaff > 0) {
            database.insert(Main) {
                set(it.applicant, applicant)
                set(it.categoryService, categoryService)
                set(it.windowStaff, windowStaff)
            }
        }
    }

    /**
     * Отмечает связь между кандидатом и категорией и окном как удаленную.
     *
     * @param id Идентификатор связи, которая будет отмечена как удаленная.
     */
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

    /**
     * Извлекает связь между кандидатом и категорией и окном по ее идентификатору.
     *
     * @param id Идентификатор связи для извлечения.
     * @return Карту, содержащую извлеченную информацию об ассоциации, или null, если не найдено.
     */
    public fun get_applicant_category_window(id: Int): Map<String, String>? {
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
                }[0]
        }
        return null
    }

    /**
     * Извлекает все активные ассоциации заявителя-категории-окна.
     *
     * @return Список карт, содержащих всю информацию об активных ассоциациях.
     */
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
    public fun get_count_by_status(status: String): Int {
        return database.from(Main)
            .select(Main.stat)
            .where {
                (Main.stat eq status)
            }
            .map { row ->
                mapOf(
                    "id" to row[Main.applicantsWsId].toString(),
                )
            }.size
    }
}

class CategoriesServicesDAO() : BaseDAO() {
    public fun insert_categories_services(categoryID: Int, serviceID: Int): Boolean {
        try {
            if (categoryID > 0 && serviceID > 0) {
                database.insert(CategoriesServices) {
                    set(it.category, categoryID)
                    set(it.service, serviceID)
                }
            }
        }
        catch(e: Exception) {
            return false
        }
        return true
    }

    public fun delete_categories_services(serviceID: Int) {
        if (serviceID > 0) {
            database.update(CategoriesServices) {
                set(it.stat, "Заблокирован")
                set(it.blockDate, SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(Date()))
                where {
                    it.categoriesServicesId eq serviceID
                }
            }
        }
    }

    public fun get_categories_services(id: Int): Map<String, String>? {
        if (id > 0) {
            return database.from(CategoriesServices)
                .select(
                    CategoriesServices.categoriesServicesId,
                    CategoriesServices.category,
                    CategoriesServices.service,
                    CategoriesServices.stat
                )
                .where {
                    (CategoriesServices.categoriesServicesId eq id) and (Main.stat notEq "Заблокировано")
                }
                .map { row ->
                    mapOf(
                        "id" to row[CategoriesServices.categoriesServicesId].toString(),
                        "categoryId" to row[CategoriesServices.category].toString(),
                        "serviceId" to row[CategoriesServices.service].toString(),
                        "windowStaff" to row[CategoriesServices.stat].toString()
                    )
                }[0]
        }
        return null;
    }
    public fun get_all_categories_services(id: Int): List<Map<String, String>>? {
        return database.from(CategoriesServices)
            .select(CategoriesServices.categoriesServicesId, CategoriesServices.category, CategoriesServices.service)
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