package qq
import org.ktorm.database.Database
import org.ktorm.dsl.*
import java.util.UUID
import java.math.BigInteger
import java.security.MessageDigest


open class BaseService {
    val database = Database.connect(
        url = "jdbc:mysql://"
                + System.getenv("DB_HOST") + ":"
                + System.getenv("DB_PORT") + "/"
                + System.getenv("DB_NAME"),
        driver = "com.mysql.jdbc.Driver",
        user = System.getenv("DB_USER"),
        password = System.getenv("DB_PASSWORD")
    )
    public fun md5(text: String): String {
        val crypt = MessageDigest.getInstance("MD5");
        crypt.update(text.toByteArray());
        return BigInteger(1, crypt.digest()).toString(16)
    }
}

class DocumentTypesService(): BaseService() {
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

    public fun get_document_type(id: Int): Query? {
        if (id > 0) {
            return database.from(DocumentTypes)
                .select(DocumentTypes.label, DocumentTypes.description)
                .where {
                    (DocumentTypes.documentTypeId eq id) and (DocumentTypes.stat notEq "Заблокировано")
                }
        }
        return null
    }

    public fun get_all_document_types(): Query {
        return database.from(DocumentTypes)
            .select(DocumentTypes.label, DocumentTypes.description)
            .where {
                (DocumentTypes.stat notEq "Заблокировано")
            }
    }
}

class CategoriesService(): BaseService() {
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
    public fun get_category(id: Int): Query? {
        if (id > 0) {
            return database.from(Categories)
                .select(Categories.name, Categories.description)
                .where {
                    (Categories.categoryId eq id) and (Categories.stat notEq "Заблокировано")
                }
        }
        return null
    }

    public fun get_all_categories(): Query {
        return database.from(Categories)
            .select(Categories.name, Categories.description)
            .where {
                (Categories.stat notEq "Заблокировано")
            }
    }
}

class ServicesService(): BaseService() {
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
    public fun get_service(id: Int): Query? {
        if (id > 0) {
            return database.from(Services)
                .select(Services.name, Services.description)
                .where {
                    (Services.serviceId eq id) and (Services.stat notEq "Заблокировано")
                }
        }
        return null
    }

    public fun get_all_services(): Query {
        return database.from(Services)
            .select(Services.name, Services.description)
            .where {
                (Services.stat notEq "Заблокировано")
            }
    }
}

class WindowsService(): BaseService() {
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
    public fun get_window(id: Int): Query? {
        if (id > 0) {
            return database.from(Windows)
                .select(Windows.label)
                .where {
                    (Windows.windowId eq id) and (Windows.stat notEq "Заблокировано")
                }
        }
        return null
    }

    public fun get_all_windows(): Query {
        return database.from(Windows)
            .select(Windows.label)
            .where {
                (Windows.stat notEq "Заблокировано")
            }
}
}

class WindowService(): BaseService() {
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
    public fun get_window(id: Int): Query? {
        if (id > 0) {
            return database.from(Windows)
                .select(Windows.label)
                .where {
                    (Windows.windowId eq id) and (Windows.stat notEq "Заблокировано")
                }
        }
        return null
    }

    public fun get_all_windows(): Query {
        return database.from(Windows)
            .select(Windows.label)
            .where {
                (Windows.stat notEq "Заблокировано")
            }
    }
}

class WindowsServices(): BaseService() {
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
    public fun get_staff(id: Int): Query? {
        if (id > 0) {
            return database.from(Windows)
                .select(Windows.label)
                .where {
                    (Windows.windowId eq id) and (Windows.stat notEq "Заблокировано")
                }
        }
        return null
    }

    public fun get_all_staffs(): Query {
        return database.from(Windows)
            .select(Windows.label)
            .where {
                (Windows.stat notEq "Заблокировано")
            }
    }
}

class ApplicantsService(): BaseService() {
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
    public fun get_applicant(id: Int): Query? {
        if (id > 0) {
            return database.from(Applicants)
                .select(Applicants.hash)
                .where {
                    (Applicants.applicantId eq id) and (Applicants.stat notEq "Заблокирован")
                }
        }
        return null
    }

    public fun get_all_applicants(): Query {
        return database.from(Applicants)
            .select(Applicants.hash)
            .where {
                (Applicants.stat notEq "Заблокирован")
            }
    }
}

class StaffService(): BaseService() {
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
    public fun get_staff(id: Int): Query? {
        if (id > 0) {
            return database.from(Staff)
                .select(Staff.name, Staff.login, Staff.password)
                .where {
                    (Staff.staffId eq id) and (Staff.stat notEq "Заблокирован")
                }
        }
        return null
    }

    public fun get_all_staffs(): Query {
        return database.from(Staff)
            .select(Staff.name, Staff.login, Staff.password)
            .where {
                (Staff.stat notEq "Заблокирован")
            }
    }
}




