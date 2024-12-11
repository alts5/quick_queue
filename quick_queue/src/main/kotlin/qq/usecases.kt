package qq

import io.ktor.util.*
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