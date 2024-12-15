import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс TableService управляет информацией о столах в антикафе
 */
public class TableService {
    /**
     * Карта, представляющая все столы в ресторане, с ключами в виде их уникальных идентификаторов.
     */
    public static Map<Integer, Table> tables = Map.of(
            1, new Table(1),
            2, new Table(2),
            3, new Table(3),
            4, new Table(4),
            5, new Table(5),
            6, new Table(6),
            7, new Table(777),
            8, new Table(8),
            9, new Table(9),
            10, new Table(10));


    /**
     * Возвращает список свободных столов в ресторане.
     *
     * @return список объектов Table, представляющих свободные столы.
     */
    public static List<Table> getFreeTables() {


        return tables.values().stream()
                .filter(Table::isFree)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список зарезервированных столов в ресторане.
     *
     * @return список объектов Table, представляющих зарезервированные столы.
     */
    public static List<Table> getReservedTables() {
        return tables.values().stream()
                .filter(t -> !t.isFree())
                .collect(Collectors.toList());
    }
}


