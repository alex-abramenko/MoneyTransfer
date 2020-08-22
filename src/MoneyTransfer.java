import Data.Database;
import Model.TransferThread;
import Utility.Constants;
import Wrapper.Account;
import org.apache.log4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Основной класс для инициализации данных и запуска потоков
 * Весь процессов переводов записывается в логи по пути $PROJECT_PATH/logs/log_file.log
 */
public class MoneyTransfer {
    public static final Logger log = Logger.getLogger(MoneyTransfer.class);

    private int quantity_accounts;      //количество создаваемых аккаунтов
    private int quantity_threads;       //количество создаваемых и запускаемых потоков
    private TransferThread[] threads;   //массив классов потоков

    /**
     *
     * @param quantity_accounts количество аккаунтов для создания
     * @param quantity_threads количество потоков для создания и запуска
     */
    public MoneyTransfer(int quantity_accounts, int quantity_threads) {
        this.quantity_accounts = quantity_accounts;
        this.quantity_threads = quantity_threads;

        initDatabase();
        initThreads();

        log.info(String.format("Параметры выполнения: количество аккаунтов %d; количество потоков %d;" +
                        " денег по умолчанию %d; максимум переводов %d",
                quantity_accounts, quantity_threads, Constants.DEFAULT_QUANTITY_MONEY, Constants.MAX_TRANSFER));

        /*
        * Находит сумму денег всех аккаунтов до начала запуска потоков и записывает в лог
         */
        int sum_money = 0;
        for (Account o : Database.account_list)
            sum_money += o.getMoney();
        log.info(String.format("Сумма денег всех аккаунтов до транзакций: %d", sum_money));
    }

    /**
     * Создает экзепляры классы Account и добавляет в лист из Database
     */
    private void initDatabase() {
        for (int i = 0; i < quantity_accounts; i++) {
            String ID  = String.valueOf(ThreadLocalRandom.current().nextInt(1, quantity_accounts*1000));
            Database.account_list.add(new Account(ID, Constants.DEFAULT_QUANTITY_MONEY));
        }
    }

    /**
     * Создает экземпляры класса TransferThread и добавляет в массив
     */
    private void initThreads() {
        threads = new TransferThread[quantity_threads];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new TransferThread();
        }
    }

    /**
     * Запускает работу всех потоков.
     * Потоки обосзначаются демонами.
     * Главный поток работает до тех пор, пока не будет выполнено максимальное количество переводов.
     */
    public void startTransfer() {
        for (TransferThread thread : threads) {
            thread.setDaemon(true);
            thread.start();
        }

        while(Database.getTransfers_made() < Constants.MAX_TRANSFER) {
            Thread.yield();
        }

        /*
         * Находит сумму денег всех аккаунтов после работы потоков и записывает в лог
         */
        int sum_money = 0;
        for (Account o : Database.account_list)
            sum_money += o.getMoney();
        log.info(String.format("Сумма денег всех аккаунтов после транзакций: %d\n", sum_money));
    }
}
