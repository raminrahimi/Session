package fileOperation;

import Entity.Inventory;
import Entity.Payment;
import Entity.Transaction;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class Write {
    public void writePaymentFile(Payment payment) throws IOException {
        String s = payment.toString() + "\n";
        Files.write(
                Paths.get(Generate.PaymentfilePath),
                s.getBytes(),
                StandardOpenOption.APPEND);
    }

    public void writeTranactionFile(Transaction transaction) throws IOException {
        String s = transaction.toString() + "\n";
        Files.write(
                Paths.get(Generate.TransactionfilePath),
                s.getBytes(),
                StandardOpenOption.APPEND);
    }

    public void updateInventoryFile(List<Inventory> inventoryList) throws IOException {
        FileChannel.open(Paths.get(Generate.InventoryfilePath), StandardOpenOption.WRITE).truncate(0).close();
        String s = "";
        for (Inventory inventory : inventoryList) {
            s += inventory.toString() + "\n";
        }
        Files.write(
                Paths.get(Generate.InventoryfilePath),
                s.getBytes(),
                StandardOpenOption.APPEND);
    }
}


