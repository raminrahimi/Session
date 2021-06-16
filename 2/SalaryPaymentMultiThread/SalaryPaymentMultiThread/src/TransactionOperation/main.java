package TransactionOperation;

import Entity.Transaction;
import fileOperation.Generate;
import fileOperation.Read;
import Entity.Payment;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class main {
    static Logger log = Logger.getLogger(Transaction.class.getName());
    static int NUMBER_OF_THREADS = 10;

    public static void main(String[] args) {
        List<Payment> paymentList;
        TransactionOperation transactionOperation = new TransactionOperation();
        Generate generate = new Generate();
        Read read = new Read();
        try {

            if (!generate.checkIfFilesExist()) {
                log.info("no files were found!");
                generate.createFiles();
                generate.generateRandomPaymentData(50, 1000, 6000);
                paymentList = read.readPaymentFile();
                Random random = new Random();
                BigDecimal debtorInventory = new BigDecimal(random.nextInt(60000000 - 40000000) + 40000000);
                generate.generateRandomInventories(paymentList, debtorInventory);
                log.info("new files created and new data generated!");
            } else if (generate.checkIfFilesAreEmpty()) {
                log.info("files are empty!");
                generate.generateRandomPaymentData(50, 1000, 6000);
                paymentList = read.readPaymentFile();
                Random random = new Random();
                BigDecimal debtorInventory = new BigDecimal(random.nextInt(60000000 - 40000000) + 40000000);
                generate.generateRandomInventories(paymentList, debtorInventory);
                generate.clearTransactionFile();
                paymentList = read.readPaymentFile();
                log.info("new data were generated in empty files!");
            } else {
                generate.clearTransactionFile();
                log.info("files were found!");
                paymentList = read.readPaymentFile();
            }
            transactionOperation.checkIfTransacionsPossible();
            ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
            Payment debtorPayment = paymentList.get(0);
            paymentList.remove(debtorPayment);
            int firstIndex = 0;
            int step = (paymentList.size() / 5);
            int lastIndex = (paymentList.size() / 5);
            int maxIteration = paymentList.size() / step;
            int count = 0;
            while (count != maxIteration) {
                executorService.execute(new TransactionOperation(paymentList.subList(firstIndex, lastIndex), debtorPayment));
                firstIndex = firstIndex + step;
                lastIndex = lastIndex + step;
                count++;
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
