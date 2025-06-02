package gui;

import java.util.HashMap;
import java.util.Map;

/**
 * Менеджер для управления локализацией приложения.
 * Поддерживает русский, английский и испанский (Гондурас) языки.
 */
public class LanguageManager {
    private static LanguageManager instance;
    private String currentLanguage = "ru"; // По умолчанию русский
    
    // Словари для всех языков
    private Map<String, Map<String, String>> translations;
    
    private LanguageManager() {
        initializeTranslations();
    }
    
    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    
    private void initializeTranslations() {
        translations = new HashMap<>();
        
        // Русский язык
        Map<String, String> ru = new HashMap<>();
        ru.put("app_title", "Управление коллекцией транспорта");
        ru.put("login", "Войти");
        ru.put("register", "Зарегистрироваться");
        ru.put("username", "Логин");
        ru.put("password", "Пароль");
        ru.put("confirm_password", "Подтвердите пароль");
        ru.put("continue", "Продолжить");
        ru.put("welcome", "Здравствуйте");
        ru.put("nice_to_meet", "Приятно познакомиться");
        ru.put("login_back", "С возвращением");
        ru.put("no_account", "Нет аккаунта?");
        ru.put("add", "добавить");
        ru.put("delete", "удалить");
        ru.put("sort", "сортировать");
        ru.put("save", "сохранить");
        ru.put("account", "аккаунт");
        ru.put("info", "инфо");
        ru.put("help", "помощь");
        ru.put("map", "карта");
        ru.put("id", "ID");
        ru.put("name", "название");
        ru.put("coordinates_x", "координата X");
        ru.put("coordinates_y", "координата Y");
        ru.put("creation", "создание");
        ru.put("engine_power", "мощность двигателя");
        ru.put("type", "тип");
        ru.put("fuel_type", "тип топлива");
        ru.put("owner", "владелец");
        ru.put("error", "Ошибка");
        ru.put("warning", "Предупреждение");
        ru.put("information", "Информация");
        ru.put("ok", "ОК");
        ru.put("yes", "Да");
        ru.put("no", "Нет");
        ru.put("logout_confirm", "Подтверждение выхода");
        ru.put("logout_question", "Вы действительно хотите выйти?");
        ru.put("insert_row_tooltip", "Нажмите, чтобы вставить строку здесь");
        ru.put("collection_info_title", "Информация о коллекции");
        ru.put("collection_type", "Тип коллекции: ");
        ru.put("init_date", "Дата инициализации: ");
        ru.put("elements_count", "Количество элементов: ");
        ru.put("help_title", "Справка");
        ru.put("help_content", "Доступные действия:\n\n• Добавить - создать новое транспортное средство\n• Удалить - удалить выбранное транспортное средство (клавиша Delete)\n• Сортировать - упорядочить по ID\n• Сохранить - сохранить изменения в базу данных\n• Вставка строки - наведите курсор между строками и кликните\n• Редактирование - дважды кликните по ячейке\n• Выпадающие списки - для типов транспорта и топлива\n• Только владелец может редактировать свои записи");
        
        // Error messages and validation texts
        ru.put("error_insert_row", "Ошибка при вставке строки: ");
        ru.put("error_add_vehicle", "Ошибка при добавлении транспортного средства: ");
        ru.put("warning_select_vehicle", "Выберите транспортное средство для удаления.");
        ru.put("error_vehicle_not_found", "Транспортное средство не найдено.");
        ru.put("warning_only_own_vehicles", "Вы можете удалять только свои транспортные средства.");
        ru.put("error_delete_vehicle", "Ошибка при удалении транспортного средства: ");
        ru.put("info_changes_saved", "Изменения успешно сохранены в базу данных.");
        ru.put("error_save_db", "Ошибка при сохранении в базу данных: ");
        ru.put("warning_only_edit_own", "Вы можете редактировать только свои транспортные средства.");
        ru.put("error_name_empty", "Название не может быть пустым.");
        ru.put("error_coord_x_negative", "Координата X не может быть отрицательной.");
        ru.put("error_coord_x_max", "Координата X не может быть больше 225.");
        ru.put("error_coord_x_number", "Координата X должна быть числом.");
        ru.put("error_coord_y_negative", "Координата Y не может быть отрицательной.");
        ru.put("error_coord_y_max", "Координата Y не может быть больше 493.");
        ru.put("error_coord_y_number", "Координата Y должна быть числом.");
        ru.put("error_engine_power_positive", "Мощность двигателя должна быть больше 0.");
        ru.put("error_engine_power_number", "Мощность двигателя должна быть числом.");
        ru.put("error_vehicle_type_invalid", "Неверный тип транспортного средства.");
        ru.put("error_fuel_type_invalid", "Неверный тип топлива.");
        ru.put("error_update_data", "Ошибка при обновлении данных: ");
        
        // Sort and filter
        ru.put("sort_filter_title", "Сортировка и фильтрация");
        ru.put("sort_by", "Сортировать по:");
        ru.put("sort_order", "Порядок:");
        ru.put("ascending", "По возрастанию");
        ru.put("descending", "По убыванию");
        ru.put("filter_by", "Фильтровать по:");
        ru.put("filter_value", "Значение фильтра:");
        ru.put("apply", "Применить");
        ru.put("clear_filter", "Очистить фильтр");
        ru.put("cancel", "Отмена");
        ru.put("all_values", "Все значения");
        ru.put("map_window_title", "Карта координат");
        ru.put("legend", "Легенда");
        ru.put("error_open_map", "Ошибка при открытии карты: ");
        
        // English
        Map<String, String> en = new HashMap<>();
        en.put("app_title", "Vehicle Collection Management");
        en.put("login", "Login");
        en.put("register", "Register");
        en.put("username", "Username");
        en.put("password", "Password");
        en.put("confirm_password", "Confirm Password");
        en.put("continue", "Continue");
        en.put("welcome", "Hello");
        en.put("nice_to_meet", "Nice to meet you");
        en.put("login_back", "Welcome back");
        en.put("no_account", "No account?");
        en.put("add", "add");
        en.put("delete", "delete");
        en.put("sort", "sort");
        en.put("save", "save");
        en.put("account", "account");
        en.put("info", "info");
        en.put("help", "help");
        en.put("map", "map");
        en.put("id", "ID");
        en.put("name", "name");
        en.put("coordinates_x", "coordinates X");
        en.put("coordinates_y", "coordinates Y");
        en.put("creation", "creation");
        en.put("engine_power", "engine power");
        en.put("type", "type");
        en.put("fuel_type", "fuel type");
        en.put("owner", "owner");
        en.put("error", "Error");
        en.put("warning", "Warning");
        en.put("information", "Information");
        en.put("ok", "OK");
        en.put("yes", "Yes");
        en.put("no", "No");
        en.put("logout_confirm", "Logout Confirmation");
        en.put("logout_question", "Do you really want to log out?");
        en.put("insert_row_tooltip", "Click to insert row here");
        en.put("collection_info_title", "Collection Information");
        en.put("collection_type", "Collection type: ");
        en.put("init_date", "Initialization date: ");
        en.put("elements_count", "Number of elements: ");
        en.put("help_title", "Help");
        en.put("help_content", "Available actions:\n\n• Add - create new vehicle\n• Delete - remove selected vehicle (Delete key)\n• Sort - order by ID\n• Save - save changes to database\n• Insert row - hover between rows and click\n• Edit - double click on cell\n• Dropdowns - for vehicle and fuel types\n• Only owner can edit their records");
        
        // Error messages and validation texts
        en.put("error_insert_row", "Error inserting row: ");
        en.put("error_add_vehicle", "Error adding vehicle: ");
        en.put("warning_select_vehicle", "Please select a vehicle to delete.");
        en.put("error_vehicle_not_found", "Vehicle not found.");
        en.put("warning_only_own_vehicles", "You can only delete your own vehicles.");
        en.put("error_delete_vehicle", "Error deleting vehicle: ");
        en.put("info_changes_saved", "Changes successfully saved to database.");
        en.put("error_save_db", "Error saving to database: ");
        en.put("warning_only_edit_own", "You can only edit your own vehicles.");
        en.put("error_name_empty", "Name cannot be empty.");
        en.put("error_coord_x_negative", "X coordinate cannot be negative.");
        en.put("error_coord_x_max", "X coordinate cannot be greater than 225.");
        en.put("error_coord_x_number", "X coordinate must be a number.");
        en.put("error_coord_y_negative", "Y coordinate cannot be negative.");
        en.put("error_coord_y_max", "Y coordinate cannot be greater than 493.");
        en.put("error_coord_y_number", "Y coordinate must be a number.");
        en.put("error_engine_power_positive", "Engine power must be greater than 0.");
        en.put("error_engine_power_number", "Engine power must be a number.");
        en.put("error_vehicle_type_invalid", "Invalid vehicle type.");
        en.put("error_fuel_type_invalid", "Invalid fuel type.");
        en.put("error_update_data", "Error updating data: ");
        
        // Sort and filter
        en.put("sort_filter_title", "Sort and Filter");
        en.put("sort_by", "Sort by:");
        en.put("sort_order", "Order:");
        en.put("ascending", "Ascending");
        en.put("descending", "Descending");
        en.put("filter_by", "Filter by:");
        en.put("filter_value", "Filter value:");
        en.put("apply", "Apply");
        en.put("clear_filter", "Clear Filter");
        en.put("cancel", "Cancel");
        en.put("all_values", "All values");
        en.put("map_window_title", "Coordinate Map");
        en.put("legend", "Legend");
        en.put("error_open_map", "Error opening map: ");
        
        // Spanish (Honduras)
        Map<String, String> es = new HashMap<>();
        es.put("app_title", "Gestión de Colección de Vehículos");
        es.put("login", "Iniciar Sesión");
        es.put("register", "Registrarse");
        es.put("username", "Usuario");
        es.put("password", "Contraseña");
        es.put("confirm_password", "Confirmar Contraseña");
        es.put("continue", "Continuar");
        es.put("welcome", "Hola");
        es.put("nice_to_meet", "Mucho gusto");
        es.put("login_back", "Bienvenido de vuelta");
        es.put("no_account", "¿No tiene cuenta?");
        es.put("add", "agregar");
        es.put("delete", "eliminar");
        es.put("sort", "ordenar");
        es.put("save", "guardar");
        es.put("account", "cuenta");
        es.put("info", "info");
        es.put("help", "ayuda");
        es.put("map", "mapa");
        es.put("id", "ID");
        es.put("name", "nombre");
        es.put("coordinates_x", "coordenada X");
        es.put("coordinates_y", "coordenada Y");
        es.put("creation", "creación");
        es.put("engine_power", "potencia del motor");
        es.put("type", "tipo");
        es.put("fuel_type", "tipo de combustible");
        es.put("owner", "propietario");
        es.put("error", "Error");
        es.put("warning", "Advertencia");
        es.put("information", "Información");
        es.put("ok", "OK");
        es.put("yes", "Sí");
        es.put("no", "No");
        es.put("logout_confirm", "Confirmar Cierre de Sesión");
        es.put("logout_question", "¿Realmente desea cerrar sesión?");
        es.put("insert_row_tooltip", "Haga clic para insertar fila aquí");
        es.put("collection_info_title", "Información de la Colección");
        es.put("collection_type", "Tipo de colección: ");
        es.put("init_date", "Fecha de inicialización: ");
        es.put("elements_count", "Número de elementos: ");
        es.put("help_title", "Ayuda");
        es.put("help_content", "Acciones disponibles:\n\n• Agregar - crear nuevo vehículo\n• Eliminar - quitar vehículo seleccionado (tecla Delete)\n• Ordenar - ordenar por ID\n• Guardar - guardar cambios en base de datos\n• Insertar fila - pasar cursor entre filas y hacer clic\n• Editar - doble clic en celda\n• Menús desplegables - para tipos de vehículo y combustible\n• Solo el propietario puede editar sus registros");
        
        // Error messages and validation texts
        es.put("error_insert_row", "Error al insertar fila: ");
        es.put("error_add_vehicle", "Error al agregar vehículo: ");
        es.put("warning_select_vehicle", "Por favor seleccione un vehículo para eliminar.");
        es.put("error_vehicle_not_found", "Vehículo no encontrado.");
        es.put("warning_only_own_vehicles", "Solo puede eliminar sus propios vehículos.");
        es.put("error_delete_vehicle", "Error al eliminar vehículo: ");
        es.put("info_changes_saved", "Cambios guardados exitosamente en la base de datos.");
        es.put("error_save_db", "Error al guardar en la base de datos: ");
        es.put("warning_only_edit_own", "Solo puede editar sus propios vehículos.");
        es.put("error_name_empty", "El nombre no puede estar vacío.");
        es.put("error_coord_x_negative", "La coordenada X no puede ser negativa.");
        es.put("error_coord_x_max", "La coordenada X no puede ser mayor a 225.");
        es.put("error_coord_x_number", "La coordenada X debe ser un número.");
        es.put("error_coord_y_negative", "La coordenada Y no puede ser negativa.");
        es.put("error_coord_y_max", "La coordenada Y no puede ser mayor a 493.");
        es.put("error_coord_y_number", "La coordenada Y debe ser un número.");
        es.put("error_engine_power_positive", "La potencia del motor debe ser mayor a 0.");
        es.put("error_engine_power_number", "La potencia del motor debe ser un número.");
        es.put("error_vehicle_type_invalid", "Tipo de vehículo inválido.");
        es.put("error_fuel_type_invalid", "Tipo de combustible inválido.");
        es.put("error_update_data", "Error al actualizar datos: ");
        
        // Sort and filter
        es.put("sort_filter_title", "Ordenar y Filtrar");
        es.put("sort_by", "Ordenar por:");
        es.put("sort_order", "Orden:");
        es.put("ascending", "Ascendente");
        es.put("descending", "Descendente");
        es.put("filter_by", "Filtrar por:");
        es.put("filter_value", "Valor del filtro:");
        es.put("apply", "Aplicar");
        es.put("clear_filter", "Limpiar Filtro");
        es.put("cancel", "Cancelar");
        es.put("all_values", "Todos los valores");
        es.put("map_window_title", "Mapa de Coordenadas");
        es.put("legend", "Leyenda");
        es.put("error_open_map", "Error al abrir el mapa: ");
        
        translations.put("ru", ru);
        translations.put("en", en);
        translations.put("es", es);
    }
    
    public String getText(String key) {
        Map<String, String> currentTranslations = translations.get(currentLanguage);
        return currentTranslations.getOrDefault(key, key);
    }
    
    public void setLanguage(String language) {
        if (translations.containsKey(language)) {
            this.currentLanguage = language;
        }
    }
    
    public String getCurrentLanguage() {
        return currentLanguage;
    }
    
    public String[] getAvailableLanguages() {
        return new String[]{"ru", "en", "es"};
    }
    
    public String getLanguageDisplayName(String languageCode) {
        switch (languageCode) {
            case "ru": return "Русский";
            case "en": return "English";
            case "es": return "Español";
            default: return languageCode;
        }
    }
} 