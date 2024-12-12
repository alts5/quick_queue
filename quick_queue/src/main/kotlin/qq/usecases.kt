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

    public fun add_window(window_name: String) {
        windows.insert_window(window_name);
    }
    public fun delete_window(window_id: Int) {
        windows.delete_window(window_id);
    }
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
            "rejected" to main.get_count_by_status("Снято")
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
}

class UserServices(): BaseUC() {
    public  fun get_services_with_desc(): List<Map<String, String?>?> {
        var services: ServicesDAO = ServicesDAO();
        var cats: CategoriesDAO = CategoriesDAO();

        return emptyList()
    }
    public fun get_categories(){
        var cats: CategoriesDAO = CategoriesDAO();
        for (cat in cats.get_all_categories()){
            
        }
    }
}