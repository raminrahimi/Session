package TransactionOperation;

import fileOperation.Read;
import fileOperation.Write;
import Entity.Inventory;
import Entity.Payment;
import Entity.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;


public class TransactionOperation implements Runnable {
    static Logger log = Logger.getLogger(Transaction.class.getName());
    private static int COUNT = 0;
    private List<Payment> paymentList;
    private Payment debtorPayment;

    public TransactionOperation() {
    }

    public TransactionOperation(List<Payment> paymentList, Payment debtorPayment) {
        this.paymentList = paymentList;
        this.debtorPayment = debtorPayment;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public Payment getDebtorPayment() {
        return debtorPayment;
    }

    public void setDebtorPayment(Payment debtorPayment) {
        this.debtorPayment = debtorPayment;
    }

    @Override
    public void run() {
        try {
            updateInventoryAndSaveTransaction();
        } catch (IOException ioException) {
            log.info(ioException.getMessage());
        }
    }

    private void updateInventoryAndSaveTransaction() throws IOException {
        synchronized (TransactionOperation.class) {
            Read read = new Read();
            List<Inventory> inventoryList = read.readInventoryFile();
            Inventory debtorInventory = new Inventory();
            for (Inventory inventory : inventoryList) {
                if (debtorPayment.getDepositNumber().equals(inventory.getDepositNumber())) {
                    debtorInventory = inventory;
                }
            }
            for (Payment payment : getPaymentList()) {
                for (Inventory inventory : inventoryList) {
                    if (payment.getDepositNumber().equals(inventory.getDepositNumber())) {
                        inventory.setAmount(payment.getAmount().add(inventory.getAmount()));
                        saveTransaction(new Transaction(debtorInventory.getDepositNumber(), inventory.getDepositNumber(), payment.getAmount()));
                    }
                }
            }
            if (COUNT == 0) {
                for (Inventory inventory : inventoryList) {
                    if (inventory.getDepositNumber().equals(debtorPayment.getDepositNumber())) {
                        inventory.setAmount(inventory.getAmount().subtract(debtorPayment.getAmount()));
                    }
                }
                COUNT++;
            }
            saveNewInventories(inventoryList);
        }
    }

    private void saveNewInventories(List<Inventory> inventoryList) throws IOException {
        Write write = new Write();
        write.updateInventoryFile(inventoryList);
    }

    private void saveTransaction(Transaction transaction) throws IOException {
        Write write = new Write();
        write.writeTranactionFile(transaction);
    }

    public void checkIfTransacionsPossible() throws Exception {
        BigDecimal debtorInventory = null;
        String debtorDepositNumber = "";
        BigDecimal debtorPayment = null;
        Read read = new Read();
        List<Payment> paymentList = read.readPaymentFile();
        List<Inventory> inventoryList = read.readInventoryFile();
        for (Payment payment : paymentList) {
            if (payment.isDebtor()) {
                debtorPayment = payment.getAmount();
                debtorDepositNumber = payment.getDepositNumber();
            }
        }
        for (Inventory inventory : inventoryList) {
            if (inventory.getDepositNumber().equals(debtorDepositNumber)) {
                debtorInventory = inventory.getAmount();
            }
        }
        if (debtorInventory.compareTo(debtorPayment) > 0) {
            log.info("Transactions are allowed!");
        } else {
            throw new Exception("Transactions failed! Not Enough Inventory!");
        }
    }
}
