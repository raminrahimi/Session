package fileOperation;

import Entity.Inventory;
import Entity.Payment;
import Entity.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.logging.Logger;

public class Generate {
    static Logger log = Logger.getLogger(Transaction.class.getName());
    public static final String PaymentfilePath = "SalaryPaymentMultiThread/src/files/PaymentFile.txt";
    public static final String InventoryfilePath = "SalaryPaymentMultiThread/src/files/InventoryFile.txt";
    public static final String TransactionfilePath = "SalaryPaymentMultiThread/src/files/TransactionFile.txt";

    public void createFiles() {
        createAlltFile();

    }

    public void createAlltFile() {
        {
            Path path = Paths.get(PaymentfilePath);
            Path path1 = Paths.get(InventoryfilePath);
            Path path2 = Paths.get(TransactionfilePath);
            try {
                Files.deleteIfExists(Paths.get(PaymentfilePath));
                Files.deleteIfExists(Paths.get(InventoryfilePath));
                Files.deleteIfExists(Paths.get(TransactionfilePath));
                Path createdFilePath = Files.createFile(path);
                Path createdFilePath2 = Files.createFile(path1);
                Path createdFilePath3 = Files.createFile(path2);
                log.info("PaymentFile Created" + createdFilePath);
                log.info("InventoryFile Created" + createdFilePath2);
                log.info("TransactionFile Created" + createdFilePath3);
            } catch (IOException e) {
                e.printStackTrace();
                log.info("PaymentFile creation failed!");
                log.info("InventoryFile creation failed!");
                log.info("Transaction creation failed!");
            }
        }
    }

    public void generateRandomPaymentData(int numberOfCreditors, int min, int max) throws IOException {
        FileChannel.open(Paths.get(Generate.PaymentfilePath), StandardOpenOption.WRITE).truncate(0).close();
        List<Payment> paymentList = new ArrayList<>();
        BigDecimal debtorAmount = new BigDecimal(0);
        Write write = new Write();
        Set<Integer> amountSet = random(min, max, numberOfCreditors);
        Set<Integer> lastDigitOfDepositNumber = random(min, max, numberOfCreditors);
        Integer[] amounts = new Integer[amountSet.size()];
        for (Integer integer : amountSet) {
            debtorAmount = debtorAmount.add(new BigDecimal(integer.toString()));
        }
        write.writePaymentFile(new Payment(true, "1.10.100.1", debtorAmount));
        amountSet.toArray(amounts);
        for (Integer integer : lastDigitOfDepositNumber) {
            paymentList.add(new Payment(false, "1.20.100." + integer));
        }
        int count = amountSet.size() - 1;
        for (Payment payment : paymentList) {
            payment.setAmount(new BigDecimal(amounts[count]));
            count--;
            write.writePaymentFile(payment);
        }
    }

    public Set<Integer> random(int min, int max, int number) {
        Set<Integer> set = new LinkedHashSet<Integer>();
        while (set.size() < number) {
            set.add((int) (Math.random() * (max - min + 1) + min));
        }

        return set;
    }

    public void generateRandomInventories(List<Payment> paymentList, BigDecimal debtorInventory) throws IOException {
        List<Inventory> inventories = new ArrayList<>();
        Write write = new Write();
        Set<Integer> randomInventorySet = random(0, 5000, paymentList.size());
        Integer[] randomInventoriesArray = new Integer[randomInventorySet.size()];
        randomInventorySet.toArray(randomInventoriesArray);
        int count = paymentList.size() - 1;
        for (Payment payment : paymentList) {
            if (payment.isDebtor()) {
                inventories.add(new Inventory(payment.getDepositNumber(), debtorInventory));
            } else {
                inventories.add(new Inventory(payment.getDepositNumber(), new BigDecimal(randomInventoriesArray[count])));
                count--;
            }
        }
        write.updateInventoryFile(inventories);
    }

    public void clearTransactionFile() throws IOException {
        FileChannel.open(Paths.get(Generate.TransactionfilePath), StandardOpenOption.WRITE).truncate(0).close();
    }

    public boolean checkIfFilesAreEmpty() throws IOException {
        Read read = new Read();
        return read.readInventoryFile().isEmpty() && read.readPaymentFile().isEmpty();
    }

    public boolean checkIfFilesExist(){
        return Files.exists(Paths.get(PaymentfilePath)) && Files.exists(Paths.get(InventoryfilePath)) && Files.exists(Paths.get(TransactionfilePath));
    }
}
