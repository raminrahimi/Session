package fileOperation;

import Entity.Inventory;
import Entity.Payment;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Read {
    public boolean isDebtor(String s) {
        if (s.equals("debtor")) return true;
        return false;
    }

    public List<Payment> readPaymentFile() throws IOException {
        List<Payment> paymentList = new ArrayList<>();
        Path path = Paths.get(Generate.PaymentfilePath);
        List contentOfPaymentFile = Files.readAllLines(path);
        for (Object content : contentOfPaymentFile) {
            String[] str = content.toString().split("\t");
            paymentList.add(new Payment(isDebtor(str[0]), str[1], new BigDecimal(Integer.parseInt(str[2]))));
        }
        return paymentList;
    }

    public List<Inventory> readInventoryFile() throws IOException {
        List<Inventory> inventoryList = new ArrayList<>();
        Path path = Paths.get(Generate.InventoryfilePath);
        List contentOfPaymentFile = Files.readAllLines(path);
        for (Object content : contentOfPaymentFile) {
            String[] str = content.toString().split("\t");
            inventoryList.add(new Inventory(str[0], new BigDecimal(Integer.parseInt(str[1]))));
        }
        return inventoryList;
    }
}
