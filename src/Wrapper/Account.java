package Wrapper;

/**
 * Класс обертка для аккаунта.
 */
public class Account {
    private String ID;  //Идентификатор аккаунта
    private int money;  //Количество денег на акаунте

    /**
     *
     * @param ID Идетификатор аккаунта
     * @param money Количество денег по умолчанию
     */
    public Account(String ID, int money) {
        this.ID = ID;
        this.money = money;
    }

    /**
     * Геттер для ID
     * @return идентификатор аккаунта
     */
    public String getID() {
        return ID;
    }

    /**
     * Геттер для money
     * @return количество денег на счету
     */
    public int getMoney() {
        return money;
    }

    /**
     * Пополняет счет аккаунта
     * @param money количество денег для попомления
     */
    public void enrollment(int money) {
        synchronized (this) {
            this.money += money;
        }
    }

    /**
     * Снятия денег со счета аккаута
     * @param money количество денег для снятия
     * @return возврщает false, если не достаточно денег на счете, иначе true
     */
    public boolean writeOff(int money) {
        synchronized (this) {
            if(!isEnough(money))
                return false;
            this.money -= money;
            return true;
        }
    }

    /**
     * Проверяет, хватает ли денег для снятия
     * @param money количество денег, которые хотели бы снять
     * @return возвращает true, если денег достаточно, иначе false
     */
    public boolean isEnough(int money) {
        synchronized (this) {
            return money <= this.money;
        }
    }
}
