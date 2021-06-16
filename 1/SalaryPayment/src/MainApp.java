import TransactionOperation.TransactionOperation;
import fileOperation.Generat;
import fileOperation.Read;
import fileOperation.Write;
import Entity.Inventory;
import Entity.Payment;
import Entity.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

public class MainApp {
    public static void main(String[] args) throws IOException {

        Generat generat = new Generat();
        Read read = new Read();
        Write write = new Write();
        generat.createFiles();
        generat.generateRandomPaymentData(50, 0, 5000);
        List<Payment> paymentList = read.readPaymentFile();
        Random random = new Random();
        BigDecimal debtorInventory = new BigDecimal(random.nextInt(60000000 - 40000000) + 40000000);
        generat.generateRandomInventories(paymentList, debtorInventory);
        TransactionOperation transactionOperation = new TransactionOperation();
        List<Transaction> transactionList = transactionOperation.transaction();
        List<Inventory> inventories = transactionOperation.updateInventories(transactionList);
        write.updateInventoryFile(inventories);

    }
}
