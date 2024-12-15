public class Table {
    /**
     * Уникальный индефикатор стола
     */
    private final int id;
    /**
     * Метод показывает, какой стол свободный
     */
    private boolean isFree;

    /**
     * Возвращает уникальный индефикатор стола
     *
     * @return id (уникальный индефикатор стола)
     */

    public int getId() {
        return id;
    }

    /**
     * Метод проверяет, свободил ли стол
     *
     * @return true - стол свободный, false - стол занят
     */

    public boolean isFree() {
        return isFree;
    }

    /**
     * Устанавливает состояение свободы для стола
     *
     * @param free Если true - стол считается свободным, false - стол считается занятым
     */
    public void setFree(boolean free) {
        isFree = free;
    }


    /**
     * Конструктор для создания объекта стола с указанным идентификатором.
     * Стол считается свободным при создании по умолчанию
     *
     * @param id
     */
    public Table(int id) {
        this.id = id;
        this.isFree = true;
    }

    /**
     * Представляет строковое предстовление объекта стола
     *
     * @return строковое представление стола, например, Столик №5
     */
    public String toString() {
        return String.format("Столик № %d", id);
    }
}
