# Загрузка посылок

`Загрузка посылок` — это консольное Java-приложение для управления процессом загрузки посылок в грузовики с использованием различных алгоритмов. Приложение загружает посылки из файла, проверяет их валидность, выбирает алгоритм загрузки и сохраняет результаты в формате JSON.

## 📥 Входные данные

1. **Файл посылок** (`parcels.txt`) — текстовый файл, содержащий информацию о посылках.

## 🚀 Установка

1. Клонируйте репозиторий на ваш компьютер.
2. Откройте проект в **IntelliJ IDEA**.
3. Убедитесь, что у вас установлена **JDK** версии 16 или выше.

### Работа приложения:

1. Запустите проект через класс `TruckLoadingApplication`.
2. При запуске программы посылки загружаются из файла `parcels.txt`.
3. Программа проверяет посылки на валидность с помощью валидатора.
4. Далее при помощи командной строки spring shell вы можете выбрать:

   - `load-evenly` — Равномерная погрузка.
   - `load-by-name-even` — Погрузка по именам (равномерная).
   - `load-optimal` — Максимально качественная погрузка.
   - `load-by-name-optimal` — Погрузка по именам (качественная).
   - `show-fullTrucks` — Показать содержимое грузовиков из JSON.
   - `manage-parcels-menu` — Редактировать посылки.

5. После выполнения алгоритма программа выводит результат в консоль и сохраняет данные в файл.

## 🔗 Пример вывода

При выборе алгоритма "Максимально качественная погрузка" программа выводит результат на консоль.
Пример вывода после выполнения:

```
++++++++
+@@@@  +
+@@@@  +
+$$$$$ +
+&&&-- +
+&&&***+
+&&&***+
++++++++
```

## 📋 Менеджер посылок

Приложение также включает функционал для управления посылками через `ParcelService`. Вы можете:

- Создать новую посылку.
- Обновить существующую посылку.
- Получить посылку по её имени.
- Получить все посылки.
- Удалить посылку.

### Доступные команды менеджера посылок:
1. При помощи командной строки spring shell вы можете выбрать:
- `create-parcel` — создать посылку.
- `update-parcel` — обновить посылку.
- `get-parcel` — получить посылку по имени.
- `get-all-parcels` — получить все посылки.
- `delete-parcel` — удалить посылку.

## 🛠️ Зависимости

Проект использует следующие библиотеки:

- **Spring Framework** — для реализации функциональности приложения.
- **SLF4J** и **Logback** — для логирования.
- **JUnit** и **Mockito** — для тестирования.
- **Jackson** — для работы с JSON.
- **Lombok** — для упрощения кода.

