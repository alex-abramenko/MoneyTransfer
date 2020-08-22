package Model;

import Data.Database;
import Utility.Constants;
import org.apache.log4j.Logger;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Класс поток для осуществления переводов.
 * Делается до тех пор, пока количество переводов не превышает максимума.
 * Выбирает случайный аккаунт, как для перевода, так и для снятия.
 * После проверяет хватает ли денег на аккаунте для снятия и не превышин ли лимит максимума переводов.
 * Если true, то выполняет перевод и записывает в логи, иначе записывает в лог ошибку.
 */
public class TransferThread extends Thread {
    public static final Logger log = Logger.getLogger(TransferThread.class);

    @Override
    public void run() {
        while (Database.getTransfers_made() < Constants.MAX_TRANSFER) {
            try {
                sleep(ThreadLocalRandom.current().nextInt(Constants.MIN_SLEEP, Constants.MAX_SLEEP + 1));

                int quantity_money = ThreadLocalRandom.current().nextInt(1, Constants.DEFAULT_QUANTITY_MONEY + 1);
                int pos_to_writeOff = ThreadLocalRandom.current().nextInt(0, Database.account_list.size());
                int pos_to_enrollment = 0;

                do {
                    pos_to_enrollment = ThreadLocalRandom.current().nextInt(0, Database.account_list.size());
                } while (pos_to_enrollment == pos_to_writeOff);

                if(Database.account_list.get(pos_to_writeOff).isEnough(quantity_money)
                        && Database.incrementTransfersMade()) {
                    Database.account_list.get(pos_to_writeOff).writeOff(quantity_money);
                    Database.account_list.get(pos_to_enrollment).enrollment(quantity_money);
                    log.info(String.format("Перевод #%d: ACC@%s >>> %d руб. >>> ACC%s",
                            Database.getTransfers_made(),
                            Database.account_list.get(pos_to_writeOff).getID(),
                            quantity_money,
                            Database.account_list.get(pos_to_enrollment).getID()));
                } else {
                    log.error(String.format("Перевод не прошел: ACC@%s >>> %d руб. >>> ACC%s",
                            Database.account_list.get(pos_to_writeOff).getID(),
                            quantity_money,
                            Database.account_list.get(pos_to_enrollment).getID()));
                }

            } catch (InterruptedException | IllegalArgumentException e) {
                log.error(e.getMessage());
                Database.incrementTransfersMade();
            }
        }
    }
}
