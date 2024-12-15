import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс Main представляет собой программу для управления антикафе, использующую классы VisitService и TableService
 * Позволяет выполнять различные действия, такие как занятие и освобождение столиков, просмотр информации о столиках, визитах и прибыли, просмотр статистических данных
 */
public class Main {
    /**
     * Представляет собой меню для пользователя антикафе
     */
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static String menu = """
            
            1. Занять столик
            2. Освободить столик
            3. Просмотреть столики, которые уже заняты
            4. Просмотреть свободные столики
            5. Посмотреть, сколько минут сидят за каждым столиком
            6. Посмотреть, сколько гостям нужно заплатить (конкретным)
            7. Посмотреть, сколько придется заплатить всем гостям за столиками, если они прямо сейчас покинут заведение 
            8. Сколько всего заработано
            9. Сколько в среднем занят столик по времени
            10. Узнать, какой столик чаще всего выбирается
            11. Узнать, какой столик больше всего принес в кассу
            12. Получить список всех визитов
            13. Получить список всех завершенных визитов
            """;
    private static Scanner in = new Scanner(System.in);

    /**
     * Основной блок программы, управляющий выполнением действий пользователя в антикафе
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        logger.info("Старт программы");

        while (true) {
            System.out.println("\nПриветствуем в нашем кафе - Luxe!");
            System.out.println(menu);
            System.out.println("Выберите действие: ");
            String optionLine = in.nextLine();
            String tableIdLine;
            int option = 0;
            int tableId;
            option = toDigit(optionLine);


            switch (option) {
                case 1 -> {
                    System.out.println("Список свободных столиков: ");
                    for (Table table : VisitService.getFreeTables()) {
                        System.out.println(table);
                    }
                    System.out.println("Выберите столик: ");
                    tableIdLine = in.nextLine();
                    tableId = toDigit(tableIdLine);
                    try {
                        Visit visit = VisitService.createVisit(new Client(), tableId);
                        System.out.printf("Столик успешно занят.%n ID визита: %d%nСтарт визита: %s", visit.getId(), visit.getFormattedTime());
                    } catch (RuntimeException ex) {
                        logger.error(ex.getMessage());
                        System.out.println(ex.getMessage());
                    }
                }
                case 2 -> {
                    System.out.println("Занятые столики");
                    System.out.println(VisitService.getReservedTables());
                    System.out.println("Выберите столик: ");
                    tableIdLine = in.nextLine();
                    tableId = toDigit(tableIdLine);

                    try {
                        VisitService.finishVisit(tableId);
                    } catch (RuntimeException ex) {
                        System.out.println(ex.getMessage());
                    }


                }
                case 3 -> {
                    System.out.println("Список занятых столиков: ");
                    for (Table table : VisitService.getReservedTables()) {
                        System.out.println(table);
                    }

                }
                case 4 -> {
                    System.out.println("Список свободных столиков");
                    for (Table table : VisitService.getFreeTables()) {
                        System.out.println(table);
                    }
                }
                case 5 -> {
                    System.out.println("Просмотр информации о том, сколько всего минут сидят за каждым столом");
                    System.out.println(VisitService.getTotalCurrentDuration());

                }
                case 6 -> {
                    System.out.println("Выберите столик: ");
                    tableIdLine = in.nextLine();
                    tableId = toDigit(tableIdLine);
                    try {
                        System.out.println(VisitService.getCurrentCost(tableId));
                    } catch (RuntimeException ex) {
                        System.out.println(ex.getMessage());
                    }

                }
                case 7 -> {
                    System.out.println("Просмотр информации о том, сколько придётся заплатить всем гостям за столиками, если они прямо сейчас покинут антикафе");
                    System.out.println(VisitService.getTotalCurrentDuration());
                    System.out.println(VisitService.getTotalCurrentCost());
                }
                case 8 -> {
                    System.out.println("Общая прибыль");
                    System.out.println(VisitService.getTotalCostOfAllTime());
                }
                case 9 -> {
                    System.out.println("Cредняя занятость столика по времени");
                    Map<Table, DoubleSummaryStatistics> map = VisitService.getAverageDurationOfAllTables();

                    for (Table table : map.keySet()
                    ) {
                        System.out.println(table + ": " + map.get(table).getAverage());
                    }
                }
                case 10 -> {
                    System.out.println("Столик, который чаще всего выбирается");
                    try {
                        System.out.println(VisitService.getTheMostPopularTable());
                    } catch (RuntimeException ex) {
                        System.out.println(ex.getMessage());
                    }

                }
                case 11 -> {
                    System.out.println("Столик,принёсший большего всего прибыли");
                    try {
                        System.out.println(VisitService.getTheMostEarnedTable());
                    } catch (RuntimeException ex) {
                        System.out.println(ex.getMessage());
                    }

                }
                case 12 -> {
                    System.out.println("Cписок всех визитов:");
                    for (Visit visit : VisitService.getVisits()) {
                        System.out.printf("    Столик: %s%n    Длительность: %d минут%n   Завершен: %s%n", visit.getTable(), visit.getDuration(), visit.isFinished() ? "Да" : "Нет");
                        if (visit.isFinished())
                            System.out.printf("    Стоимость: %f рублей%n", visit.getCost());
                        System.out.println();
                    }
                }
                case 13 -> {
                    System.out.println("Список всех завершённых визитов");
                    for (Visit visit : VisitService.getFinishedVisits()) {
                        System.out.printf("    Столик: %s%n    Длительность: %d минут", visit.getTable(), visit.getDuration());
                        if (visit.isFinished())
                            System.out.printf("    Стоимость: %f рублей", visit.getCost());
                    }
                }
                default -> System.out.println("Некорректный символ");

            }

        }

    }

    public static int toDigit(String line) {
        return StringUtils.isNumeric(line) ? Integer.parseInt(line) : 0;
    }
}