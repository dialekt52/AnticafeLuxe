import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс VisitService управляет информацией о посещениях ресторана, включая ценообразование и список посещений
 */
public class VisitService {
    /**
     * Цена за минуту пребывания клиента в антикафе
     */
    private static double pricePerMinute = 5;
    /**
     * Список всех посещений в антикафе
     */
    private static List<Visit> visits = new ArrayList<>();

    /**
     * Получет текущую цену за минуту посещения
     *
     * @return цена за минуту
     */
    public static double getPricePerMinute() {
        return pricePerMinute;
    }

    /**
     * Устанавливает цену за минуту прибывания клиента
     *
     * @param pricePerMinute новая цена за минуту
     */
    public static void setPricePerMinute(double pricePerMinute) {
        VisitService.pricePerMinute = pricePerMinute;
    }

    /**
     * Получает список всех посещений в антикафе
     *
     * @return список объектов Visit, представляющих каждое записанное посещение
     */
    public static List<Visit> getVisits() {
        return visits;
    }

    /**
     * Устанавливает список посещений в соответствии с предоставленным списком
     *
     * @param visits новый список посещений
     */
    public static void setVisits(List<Visit> visits) {
        VisitService.visits = visits;
    }

    /**
     * Создает новое посещение клиентом ресторана и связывает его с указанным столом
     *
     * @param client  клиент, совершающий посещение
     * @param tableId идентификатор стола, на который бронируется посещение
     * @return объект Visit, представляющий созданное посещение
     */
    public static Visit createVisit(Client client, int tableId) {
        if (tableId < 1 || tableId > 10)
            throw new IllegalArgumentException("Всего 10 столиков, выберите в из них");
        Table table = TableService.tables.get(tableId);
        if (!table.isFree())
            throw new RuntimeException("Этот столик уже занят");
        Visit visit = new Visit(client, table, LocalDateTime.now());
        table.setFree(false);
        visits.add(visit);
        return visit;
    }

    /**
     * Завершает указанное посещение по идентификатору стола, рассчитывает его продолжительность и стоимость
     * Меняет статус завершенности посещения на true и освобождает стол
     *
     * @param tableId bдентификатор стола, для которого завершается посещение
     * @return объект Visit, представляющий завершенное посещение
     */
    public static Visit finishVisit(int tableId) {
        if (tableId < 1 || tableId > 10)
            throw new IllegalArgumentException("Всего 10 столиков, выберите в из них");
        Table table = TableService.tables.get(tableId);
        if (table.isFree()) {
            throw new RuntimeException("Выбранный столик свободен");
        }
        Visit visit = visits.stream()
                .filter(v -> v.getTable().getId() == tableId && !v.isFinished())
                .findFirst().orElseThrow();
        visit.setDuration(visit.getCurrentDuration());
        visit.setCost(visit.calculateCost(pricePerMinute));
        visit.setFinished(true);
        table.setFree(true);
        return visit;
    }

    /**
     * Возвращает список свободных столов в антикафе
     *
     * @return список объектов Table, представляющих свободные столы
     */
    public static List<Table> getFreeTables() {
        return TableService.getFreeTables();
    }

    /**
     * Возвращает список зарезервированных столов в антикафе
     *
     * @return список объектов Table, представляющих зарезервированные столы
     */
    public static List<Table> getReservedTables() {
        return TableService.getReservedTables();
    }

    /**
     * Получает список завершенных посещений в антикафе
     *
     * @return список объектов Visit, представляющих завершенные посещения в антикафе
     */
    public static List<Visit> getFinishedVisits() {
        return visits.stream()
                .filter(Visit::isFinished).collect(Collectors.toList());
    }

    /**
     * Получает текущую продолжительность посещения для указанного стола
     *
     * @param tableId идентификатор стола, для которого нужно получить текущую продолжительность посещения
     * @return текущая продолжительность посещения в секундах
     */
    public static long getCurrentDuration(int tableId) {
        Visit visit = visits.stream()
                .filter(v -> v.getTable().getId() == tableId && !v.isFinished())
                .findFirst().orElseThrow();
        return visit.calculateDuration();
    }

    /**
     * Получает общую текущую продолжительность посещения для всех зарезервированных столов
     *
     * @return карта, где ключи - объекты столов, а значения - текущая продолжительность посещения в секундах
     */
    public static Map<Table, Long> getTotalCurrentDuration() {
        Map<Table, Long> durations = new HashMap<>();
        for (Table table : TableService.getReservedTables()) {
            durations.put(table, getCurrentDuration(table.getId()));
        }
        return durations;
    }

    /**
     * Получает текущую стоимость посещения для указанного стола
     *
     * @param tableId идентификатор стола, для которого нужно получить текущую стоимость посещения
     * @return текущая стоимость посещения
     */
    public static double getCurrentCost(int tableId) {
        if (tableId < 1 || tableId > 10)
            throw new IllegalArgumentException("Всего 10 столиков, выберите из них");
        Visit visit = visits.stream()
                .filter(v -> v.getTable().getId() == tableId && !v.isFinished())
                .findFirst().orElseThrow(() -> new RuntimeException("Столик свободен"));
        return visit.calculateCost(pricePerMinute);
    }

    /**
     * Получает общую текущую стоимость посещения для всех зарезервированных столов
     *
     * @return карта, где ключи - объекты столов, а значения - текущая стоимость посещения.
     */
    public static Map<Table, Double> getTotalCurrentCost() {
        Map<Table, Double> costs = new HashMap<>();
        for (Table table : TableService.getReservedTables()) {
            costs.put(table, getCurrentCost(table.getId()));
        }
        return costs;
    }

    /**
     * Получает общую стоимость посещения для всех завершенных посещений
     *
     * @return общая стоимость посещения всех завершенных посещений.
     */
    public static double getTotalCostOfAllTime() {
        return visits.stream()
                .filter(Visit::isFinished).mapToDouble(Visit::getCost).sum();
    }

    /**
     * Получает самый популярный стол на основе количества завершенных посещений
     *
     * @return Map.Entry, представляющий самый популярный стол и количество его посещений
     */
    public static Map.Entry<Table, Long> getTheMostPopularTable() {
        Map<Table, Long> map = visits.stream()
                .filter(Visit::isFinished).collect(Collectors.groupingBy(Visit::getTable, Collectors.counting()));
        return map.entrySet().stream()
                .max(Map.Entry.comparingByValue()).orElseThrow(() -> new RuntimeException("Визитов нет"));
    }

    /**
     * Получает среднюю продолжительность посещения для всех столов
     *
     * @return карта, где <i>ключи</i> - объекты столов, а значения - средняя продолжительность посещения в секундах
     */
    public static Map<Table, DoubleSummaryStatistics> getAverageDurationOfAllTables() {
        return visits.stream()
                .filter(Visit::isFinished)
                .collect(Collectors.groupingBy(Visit::getTable, Collectors.summarizingDouble(Visit::getDuration)));
    }

    /**
     * Получает стол, наиболее прибыльный на основе суммарной выручки от завершенных посещений
     *
     * @return Map.Entry, представляющий наиболее прибыльный стол и статистику по выручке
     */
    public static Map.Entry<Table, DoubleSummaryStatistics> getTheMostEarnedTable() {
        Map<Table, DoubleSummaryStatistics> map = visits.stream()
                .filter(Visit::isFinished)
                .collect(Collectors.groupingBy(Visit::getTable, Collectors.summarizingDouble(Visit::getCost)));
        return map.entrySet().stream().max(Comparator.comparing(entry -> entry.getValue().getSum())).orElseThrow(() -> new RuntimeException("Нет завершенных визитов"));
    }
}
