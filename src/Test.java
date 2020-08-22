import org.apache.log4j.PropertyConfigurator;

public class Test {
    public static void main(String[] args) {
        int qua_acc = 10;
        int qua_thr = 50;

        String log4jConfPath = "src/Log/log4j.properties";
        PropertyConfigurator.configure(log4jConfPath);

        MoneyTransfer moneyTransfer = new MoneyTransfer(qua_acc, qua_thr);
        moneyTransfer.startTransfer();
    }
}
