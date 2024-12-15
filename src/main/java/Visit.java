import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс Visit представляет собой информацию о посещении клиентом антикафе
 */
public class Visit {
    private static int lastId = 1;
    private int id;
    private Client client;
    private Table table;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;
    private double cost;
    private boolean finished;

    /**
     * Возвращат последний присвоенный индефикатор посещения
     *
     * @return последний присвоенный индефикатор
     */
    public static int getLastId() {
        return lastId;
    }


    /**
     * Возвращает текущую продолжительность посещения в минутах
     * Продолжительность измеряется от текущего времени до времени начала посещения
     *
     * @return текующая продолжительность посещения в минутах
     */
    public long getCurrentDuration() {
        return calculateDuration();

    }

    /**
     * Устанавливает последний присвоенный идентификатор посещения.
     *
     * @param lastId Новое значение последнего присвоенного идентификатора.
     */
    public static void setLastId(int lastId) {
        Visit.lastId = lastId;
    }

    /**
     * Возвращает отформатированное время начала посещения.
     * Время форматируется в виде строки в формате "мм:чч".
     *
     * @return отформатированное время начала посещения
     */
    public String getFormattedTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("mm:HH");
        return dateTimeFormatter.format(startTime);
    }

    /**
     * Возвращает уникальный индефикатор посещения
     *
     * @return уникальный индефикатор посещения
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает уникальный индефикатор посещения
     *
     * @param id уникальный индефикатор посещения
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Конструктор для создания объекта посещения с указанным клиентом, столом и временем начала.
     * Устанавливает уникальный идентификатор посещения, клиента, стола и временем начала посещения.
     * Устанавливает флаг завершенности посещения  false.
     *
     * @param client    клиент, который совершает посещение
     * @param table     стол для посещения
     * @param startTime время начала посещения
     */
    public Visit(Client client, Table table, LocalDateTime startTime) {
        this.id = lastId++;
        this.client = client;
        this.table = table;
        this.startTime = startTime;
        this.finished = false;
    }

    /**
     * Возвращает клиента, совершившего данное посещение.
     *
     * @return объект клиента, связанный с текущим посещением.
     */

    public Client getClient() {
        return client;
    }

    /**
     * Устанавливает клиента, совершившего посещение.
     *
     * @param client новый клиент, связанный с посещением.
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Возвращает объект стола, связанный с текущим посещением.
     *
     * @return объект стола, связанный с текущем посещением
     */
    public Table getTable() {
        return table;
    }

    /**
     * Устанавливает новый стол для текущего посещения.
     *
     * @param table новый объект стола для замены текущего.
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * Возвращает время начала текущего посещения
     *
     * @return объект LocalDateTime, представляющий время начала посещения.
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Устанавливает новое время начала для текущего посещения.
     *
     * @param startTime новый объект LocalDateTime, представляющий новое время начала посещения.
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Возвращает время завершения текущего посещения.
     *
     * @return объект LocalDateTime, представляющий время завершения посещения.
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * Устанавливает время завершения текущего посещения.
     *
     * @param endTime новое время завершения посещения в формате LocalDateTime.
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Возвращает продолжительность текущего посещения в секундах.
     *
     * @return продолжительность посещения в секундах.
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Устанавливает продолжительность текущего посещения в секундах.
     *
     * @param duration новая продолжительность посещения в секундах.
     */
    public void setDuration(long duration) {
        this.duration = duration;
    }

    /**
     * Возвращает стоимость текущего посещения
     *
     * @return стоимость посещения.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Устанавливает стоимость текущего посещения.
     *
     * @param cost новая стоимость посещения
     */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Проверяет, завершено ли текущее посещение.
     *
     * @return текущее значение
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Устанавливает флаг завершенности текущего посещения.
     *
     * @param finished новое значение флага завершенности (true, если посещение завершено, false - посещение продолжается).
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * Рассчитывает продолжительность текущего посещения в секундах от времени начала до текущего момента.
     *
     * @return продолжительность посещения в секундах.
     */
    public long calculateDuration() {
        return Duration.between(startTime, LocalDateTime.now()).toMinutes();
    }

    /**
     * Рассчитывает стоимость текущего посещения на основе заданной цены за минуту.
     *
     * @param pricePerMinute цена за минуту посещения.
     * @return стоимость посещения
     */
    public double calculateCost(double pricePerMinute) {
        return calculateDuration() * pricePerMinute;
    }

}
