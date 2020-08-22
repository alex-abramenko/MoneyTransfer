package Data;

import Utility.Constants;
import Wrapper.Account;

import java.util.ArrayList;

/**
 * Класс для хранения данных
 */
public class Database {
    public static ArrayList<Account> account_list = new ArrayList<>();  //Лист с аккаунтами
    private static int transfers_made = 0;                              //Выполненных переводов

    /**
     * Увеличивает количество выполненных переводов transfers_made на 1
     * @return возвращает false, если уже выплненно максимум переводов MAX_TRANSFER, и true в противном случаи
     */
    synchronized public static boolean incrementTransfersMade() {
        if (transfers_made >= Constants.MAX_TRANSFER)
            return false;
        transfers_made++;
        return true;
    }

    /**
     * Возвращает количество выполненных переводов
     * @return количество выполненных переводов
     */
    public static int getTransfers_made() {
        return transfers_made;
    }
}
