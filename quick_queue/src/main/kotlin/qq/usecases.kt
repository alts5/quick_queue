package qq

open class BaseUC()

class AdminServices(): BaseUC() {
    var windows: WindowsDAO = WindowsDAO();

    public fun add(window_name: String) {
        windows.insert_window(window_name);
    }
    public fun delete(window_id: Int) {
        windows.delete_window(window_id);
    }
}