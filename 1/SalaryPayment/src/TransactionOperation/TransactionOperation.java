package TransactionOperation;

import fileOperation.Read;
import fileOperation.Write;
import Entity.Inventory;
import Entity.Payment;
import Entity.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TransactionOperation {

    static Logger log = Logger.getLogger(Transaction.class.getName());

    public List<Transaction> transaction()  {

        Write write = new Write();
        Payment debtorPayment = findDebtor();
        List<Payment> creditors = findcreditors();
        List<Transaction> transactionList = new ArrayList<>();
        BigDecimal debtorInventoryAmount = findDebtorInventory();
        BigDecimal sumOfCreditorsPaymentAmount = findCreditorsAmount();
        if (debtorInventoryAmount.compareTo(sumOfCreditorsPaymentAmount) == 1) {
            for (Payment creditorPayment : creditors
            ) {
                Transaction transaction = new Transaction(debtorPayment.getDepositNumber(), creditorPayment.getDepositNumber(), creditorPayment.getAmount());
                transactionList.add(transaction);
                write.WriteTranactionFile(transaction);
            }
            log.info("all Payment successfull!!");
        } else {
            log.info(" Not enough Inventory!");
        }
        return transactionList;
    }


    private BigDecimal findCreditorsAmount() {
        Payment debtorPayment = findDebtor();
        Read read = new Read();
        BigDecimal creditorsInventoryAmount = new BigDecimal(0);
        List<Inventory> inventoryList = read.readInventoryFile();
        for (Inventory inventory : inventoryList) {
            if (!inventory.getDepositNumber().equals(debtorPayment.getDepositNumber())) {
                creditorsInventoryAmount = creditorsInventoryAmount.add(inventory.getAmount());
            }
        }
        return creditorsInventoryAmount;
    }


    public BigDecimal findDebtorInventory() {
        Read read = new Read();
        Payment debtorPayment = findDebtor();
        BigDecimal debtorInventoryAmount = null;
        List<Inventory> inventoryList = read.readInventoryFile();
        for (Inventory inventory : inventoryList) {
            if (inventory.getDepositNumber().equals(debtorPayment.getDepositNumber())) {
                debtorInventoryAmount = new BigDecimal(String.valueOf(inventory.getAmount()));
            }
        }
        return debtorInventoryAmount;
    }


    public Payment findDebtor() {
        Payment debtor = null;
        Read read = new Read();
        List<Payment> paymentList = read.readPaymentFile();
        for (Payment payment : paymentList
        ) {
            if (payment.isDebtor()) {
                debtor = payment;
            }
        }
        return debtor;
    }

    public List<Payment> findcreditors() {
        List<Payment> creditorsPaymentList = new ArrayList<>();
        Read read = new Read();
        List<Payment> paymentList = read.readPaymentFile();
        for (Payment payment : paymentList) {
            if (!payment.isDebtor()) {
                creditorsPaymentList.add(payment);
            }
        }
        return creditorsPaymentList;
    }


    public List<Inventory> updateInventories(List<Transaction> transactionList) {
        BigDecimal sumOfCreditorsPaymentAmount = SumOfTransactions();
        Read read = new Read();
        List<Inventory> inventoryList = read.readInventoryFile();
        for (Transaction transaction : transactionList) {
            for (Inventory inventory : inventoryList) {
                if (transaction.getCreditorDepositNumber().equals(inventory.getDepositNumber())) {
                    inventory.setAmount(inventory.getAmount().add(transaction.getAmount()));
                }

            }

        }

        for (Inventory inventory : inventoryList) {
            if (inventory.getDepositNumber().equals("1.10.100.1")) {
                sumOfCreditorsPaymentAmount = inventory.getAmount().subtract(sumOfCreditorsPaymentAmount);
                inventory.setAmount(sumOfCreditorsPaymentAmount);
            }
        }
        return inventoryList;
    }


    public BigDecimal SumOfTransactions() {
        BigDecimal sumOfTransactions = new BigDecimal(0);
        Read read = new Read();
        List<Payment> paymentList = read.readPaymentFile();
        for (Payment payment : paymentList) {
            if (!payment.getDepositNumber().equals("1.10.100.1")) {
                sumOfTransactions = sumOfTransactions.add(payment.getAmount());
            }
        }
        return sumOfTransactions;
    }
}
