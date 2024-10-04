## Found 22 TODO items in 12 files
### .gitignore
`(13, 3) spring-shell.log` TODO лог вижу в гитигноре, но сам лог почему-то попал в гит, надо удалить 

### pom.xml
`(26, 69) <artifactId>spring-boot-starter-web</artifactId>`  1) а у тебя разве что-то используется из web'a? 2) На уязвимости в будущем стоит обращать внимание и не использовать версии с ними 

### src\main\java\com\liga\appparcelsloading\config\Config.java
`(23, 26) public class Config {`  не очень привычно смотреть на конфиг приложения, когда ВСЕ кастомные файлы у тебя создаются вручную, в целом норм, но читабельнее было б все-таки смешанный стиль - аннотации для кастомных сервисов, java-конфиг для конфигурации библиотек/стартеров и тд (т.е. не твоего кода)

### src\main\java\com\liga\appparcelsloading\controller\ManagerAppController.java
`(38, 40) public class ManagerAppController {`  очень много логики для контроллера. 1) надо декомпозировать, а точнее даже просто схлопнуть половину shell-методов используя аргументы. 2) тут дофигища логики, которую контроллер вообще не должен выполнять. Его дело только как принять команду вызвать бизнес-сервис и как правильно сериализовать результат выполнения

`(49, 84) @ShellMethod(value = "Запуск процесса загрузки посылок.", key = "showMenu") ` выглядит как лишнимй метод - для этого есть help у shell'a, который генерит эту всe

`(63, 35) public void loadEvenly() {`  ВСЕ shell-методы должны возвращать результат выполнения. Ты самостоятельно (кроме логов) не должен ничего писать в консоль.

`(64, 141) algorithmLoadingParcels(new EvenTruckLoadingAlgorithm(parcelLoaderService, truckFactoryService, parcelMapper, truckJsonWriter));`  создания экземпляров классов-сервисов НЕ ДОЛЖНО БЫТЬ, этим должен заниматься только спринг. Для чего ты каждый раз на каждую команду создаешь?

### src\main\java\com\liga\appparcelsloading\repository\DefaultParcelRepository.java
`(17, 51) private final Map<String, Parcel> parcels;`  HashMap'a, которая используется для заполнения этого поля для кэша непотокобезопасна, могут возникнуть странности при работе в многопоточке. Стоит заменить хотя бы на ConcurrentHashMa

`(20, 57) this.parcels = parcelMapper.getAllParcels();`  лучше такие вещи как заполнение какого-нибудь кэша делать не в конструкторе класса, а через @PostConstruct

`(49, 47) public Parcel getByName(String name) {`  стоит избегать называния методов с префиксами get/set - это можно сказать зарезервированные глаголы для геттеров и сеттеров.

`(51, 34) if (parcel == null) { ` ты здесь проверяешь на null, но не считаешь это ошибкой что посылка по названию не нашлась, звучит странновато наверн, я в бросал исключение. А еще для этих ситуаций хорошо подходит Optional

`(67, 54) return new ArrayList<>(parcels.values());`  не уверен что есть большой смысл создавать новый список из существующей коллекции. Можно либо скастить, либо просто возвращать коллекцию

### src\main\java\com\liga\appparcelsloading\service\ParcelService.java

`(18, 33) public class ParcelService { ` это не сервис, а контроллер, который должен вызывать уже сервис, в котором должна быть написана бизнес-логика

`(45, 37) public void updateParcel() {`  что-то не сильно отличается от create, не находишь?

`(76, 42) String name = scanner.next();`  от ВСЕХ чтений консоли нужно избавиться - у тебя для этого есть shell - подавай все это через аргументы команды

### src\main\java\com\liga\appparcelsloading\util\JsonFileReader.java

`(27, 83) public <T> List<T> read(String fileName, TypeReference<List<T>> typeRef) {`  немного странно что этот метод публичный и используется только этим же классом

### src\main\java\com\liga\appparcelsloading\validator\TruckCountValidate.java

`(27, 103) parcelLoaderService.placeParcels(truck, symbolParcels, truck.length, truck[0].length);`  а это что за подстава? почему в валидации изменяется состояние Truck'a?? От этого нужно избавиться

`(35, 41) log.error(errorMessage); ` не уверен что имеет смысл логировать исключение, которое ты только собираешься бросить

### src\main\webapp\index.jsp

`(1, 13) <html>` TODO кажется вся папка webapp лишняя

### src\test\java\com\liga\appparcelsloading\DataTest.java

`(14, 90) private static final int[][] nine = new int[][]{{9, 9, 9}, {9, 9, 9}, {9, 9, 9}};`  1) не нужно в тестах убирать в константы то что используется единожды. 2) называется как не константа, хотя по сути это она и есть

### src\test\java\com\liga\appparcelsloading\service\FullTruckLoadServiceTest.java

`(35, 59) validateTruckCount = new TruckCountValidate();`  у тебя ж это все бины, ты зачем это все вручную создаешь в тестах?

### src\test\java\com\liga\appparcelsloading\util\JsonFileReaderTest.java

`(19, 43) void testRead() throws Exception { ` этот тест немного жесть. почему бы просто не положить в тестовые ресурсы готовый json и не прочитать его? и не придется программно создавать и удалять файл