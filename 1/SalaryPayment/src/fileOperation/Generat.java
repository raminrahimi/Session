package fileOperation;

import Entity.Inventory;
import Entity.Payment;
import Entity.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class Generat {
    public static final String PaymentfilePath = "src/files/PaymentFile.txt";
    public static final String InventoryfilePath = "src/files/InventoryFile.txt";
    public static final String TransactionfilePath = "src/files/TransactionFile.txt";
    static Logger log = Logger.getLogger(Transaction.class.getName());

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


    public void generateRandomPaymentData(int numberOfCreditors, int min, int max)  {
        List<Payment> debtor = new ArrayList<>();
        List<Payment> creditor = new ArrayList<>();
        List<Payment> paymentList = new ArrayList<>();
        BigDecimal debtorAmount = new BigDecimal(0);
        Write objwrite = new Write();
        Set<Integer> amountSet = random(min, max, numberOfCreditors);
        Set<Integer> AddlastDepositNumber = random(min, max, numberOfCreditors);
        Integer[] amounts = new Integer[amountSet.size()];
        for (Integer integer : amountSet) {
            debtorAmount = debtorAmount.add(new BigDecimal(integer.toString()));
        }
        debtor.add(new Payment(true, "1.10.100.1", debtorAmount));
        objwrite.WritePaymentFile(debtor);
        amountSet.toArray(amounts);
        for (Integer integer : AddlastDepositNumber) {
            paymentList.add(new Payment(false, "1.20.100." + integer));
        }
        int count = 0;
        for (Payment payment : paymentList) {
            payment.setAmount(new BigDecimal(amounts[count]));
            creditor.add(payment);
            count++;
        }
        objwrite.WritePaymentFile(creditor);
    }

    public Set<Integer> random(int Min, int Max, int number) {
        Set<Integer> set = new LinkedHashSet<Integer>();
        while (set.size() < number) {
            set.add((int) (Math.random() * (Max - Min + 1) + Min));
        }
        return set;
    }

    public void generateRandomInventories(List<Payment> paymentList, BigDecimal debtorInventory) {
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
        for (Inventory inventory : inventories) {
            write.WriteInventoryFile(inventory);
        }
    }


}
