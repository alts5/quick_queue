package qq

import java.math.BigInteger
import java.security.MessageDigest

open class BaseUC() {
    public fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    public fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}

class AdminServices(): BaseUC() {
    var windows: WindowsDAO = WindowsDAO();
    var staff: StaffDAO = StaffDAO();
    var main: ApplicantsCategoriesWindowsDAO = ApplicantsCategoriesWindowsDAO();
    var doctypes: DocumentTypesDAO = DocumentTypesDAO();
    var categories: CategoriesDAO = CategoriesDAO();
    var services: ServicesDAO = ServicesDAO();

    public fun check_user_creds(login: String, password: String): String? {
        for (staff_obj in staff.get_all_staff()) {
            if (staff_obj["login"] == login && staff_obj["password"] == this.md5(password)) {
                var token = this.md5(this.getRandomString(10))
                staff.update_token_staff(login, token)
                return token;
            }
        }
        return null;
    }
    public fun get_staff_info_by_token(token: String): Map<String, String?>? {
        return staff.get_staff_by_token(token);
    }
    public fun get_dashboard_stat(): MutableMap<String, Int> {
        var temp = mutableMapOf("waiting" to main.get_count_by_status("Ожидает"),
            "invited" to main.get_count_by_status("Приглашён"),
            "serviced" to main.get_count_by_status("Обслужен"),
            "rejected" to main.get_count_by_status("Снят")
            )
        return temp
    }

    /* --- Типы документов --- */
    public fun add_new_doctype(label: String?, description: String?): Boolean {
        return doctypes.insert_document_type(label, description)
    }

    public fun get_doctypes_list(): List<Map<String, String?>> {
        return doctypes.get_all_document_types();
    }
    public fun delete_doctype(id: String): Boolean {
        return doctypes.delete_document_type(id.toInt());
    }
    public fun change_doctype_stat(id: String): Boolean {
        if (doctypes.get_document_type(id.toInt())?.get("stat").equals("Доступно")) {
            return doctypes.set_stat_field(id.toInt(), "Заблокировано");
        }
        else {
            return doctypes.set_stat_field(id.toInt(), "Доступно");
        }
    }


    /* --- Категории --- */
    public fun add_new_category(label: String?, description: String?): Boolean {
        return categories.insert_category(label, description)
    }

    public fun get_categories_list(): List<Map<String, String?>> {
        return categories.get_all_categories();
    }
    public fun delete_category(id: String): Boolean {
        return categories.delete_category(id.toInt());
    }
    public fun change_category_stat(id: String): Boolean {
        if (categories.get_category(id.toInt())?.get("stat").equals("Доступно")) {
            return categories.set_stat_field(id.toInt(), "Заблокировано");
        }
        else {
            return categories.set_stat_field(id.toInt(), "Доступно");
        }
    }

    /* --- Сервисы --- */
    public fun add_new_service(label: String?, description: String?): Boolean {
        return services.insert_service(label, description)
    }

    public fun get_services_list(): List<Map<String, String?>> {
        return services.get_all_services();
    }
    public fun delete_service(id: String): Boolean {
        return services.delete_service(id.toInt());
    }
    public fun change_service_stat(id: String): Boolean {
        if (services.get_service(id.toInt())?.get("stat").equals("Доступно")) {
            return services.set_stat_field(id.toInt(), "Заблокировано");
        }
        else {
            return services.set_stat_field(id.toInt(), "Доступно");
        }
    }

    /* --- Окна --- */
    public fun add_new_window(label: String?): Boolean {
        return windows.insert_window(label)
    }
    public fun get_windows_list(): List<Map<String, String?>> {
        return windows.get_all_windows();
    }
    public fun delete_window(id: String): Boolean {
        return windows.delete_window(id.toInt());
    }
    public fun change_window_stat(id: String): Boolean {
        if (windows.get_window(id.toInt())?.get("stat").equals("Доступно")) {
            return windows.set_stat_field(id.toInt(), "Заблокировано");
        }
        else {
            return windows.set_stat_field(id.toInt(), "Доступно");
        }
    }

    /* --- Операторы --- */
    public fun add_new_staff(name: String?, login: String?): Boolean {
        return staff.insert_staff(name, login)
    }
    public fun get_staff_list(): List<Map<String, String?>> {
        return staff.get_all_staff();
    }
    public fun delete_staff(id: String): Boolean {
        return staff.delete_staff(id.toInt());
    }
    public fun change_staff_stat(id: String): Boolean {
        if (staff.get_staff(id.toInt())?.get("stat").equals("Активен")) {
            return staff.set_stat_field(id.toInt(), "Заблокирован");
        }
        else {
            return staff.set_stat_field(id.toInt(), "Активен");
        }
    }

    public fun set_app_status(id: String?, stat: String?, win: String?) {
        main.update_stat(id, stat, win);
    }
}

class UserServices(): BaseUC() {
    var services: ServicesDAO = ServicesDAO();
    var cats: CategoriesDAO = CategoriesDAO();
    var applicant: ApplicantsDAO = ApplicantsDAO();
    var main: ApplicantsCategoriesWindowsDAO = ApplicantsCategoriesWindowsDAO();
    var settings: SettingsDAO = SettingsDAO();


    public fun get_services_with_desc(): List<Map<String, String?>?> {

        return emptyList()
    }

    public fun get_categories() {
        var cats: CategoriesDAO = CategoriesDAO();
        for (cat in cats.get_all_categories()) {

        }
    }

    public fun simpleApplicant(): String {
        val key = this.getRandomString(32)
        if (!applicant.insert_applicant(key)) {
            return ""
        }
        val app = applicant.get_applicant_by_hash(key)
        if (app != null) {
            val id = app["id"].toString().toInt()
            main.insert_applicant_category_window(id, null, null)
        }
        return key
    }

    public fun getQueue(): List<Map<String, String?>> {
        val queue = main.get_all_applicants_categories_windows()
        return queue
    }


    public fun get121Ticket(hash: String): Int? {
        println(hash)
        val id = applicant.get_applicant_by_hash(hash)?.get("id")?.toString()
        if (id == "0") {
            return null
        }
        var queue = this.getQueue()
        queue = queue.filter { entry -> entry["stat"] == "Ожидает" }
        var order = 0
        for (i in queue) {
            if (i["id"] == id) {
                return order
            }
            order += 1
        }
        return 0
    }

    public fun getMode(key: String): Map<String, String?> {
        return settings.get_setting(key)

    }
    public fun get_visible_services_list(): List<Map<String, String?>> {
        return services.get_all_visible_services();
    }


}

class SystemServices(): BaseUC() {
    var settings: SettingsDAO = SettingsDAO();
    var main: ApplicantsCategoriesWindowsDAO = ApplicantsCategoriesWindowsDAO();

    public fun getSysMode(): String? {
        return settings.get_setting("systemMode")["value"];
    }
    public fun getSys(): Map<String, String?> {
        var mappa = mapOf(
            "systemMode" to settings.get_setting("systemMode")["value"],
            "startTime" to settings.get_setting("startTime")["value"],
            "endTime" to settings.get_setting("endTime")["value"],
            "footerName" to settings.get_setting("footerName")["value"],
            "logoPath" to settings.get_setting("logoPath")["value"]
            )
        return mappa
    }

    public fun update_settings(systemMode: String, startTime: String, endTime: String,footerName: String, logoPath: String) {
        settings.update_field("systemMode", systemMode);
        settings.update_field("startTime", startTime);
        settings.update_field("endTime", endTime);
        settings.update_field("footerName", footerName);
        settings.update_field("logoPath", logoPath);
    }

    public fun getQueue(): List<Map<String, String?>> {
        val queue = main.get_all_applicants_categories_windows_join();
        return queue
    }

    public fun getQueueSingle(): List<Map<String, String?>> {
        val queue = main.get_all_applicants_categories_windows_join_single();
        return queue
    }



}