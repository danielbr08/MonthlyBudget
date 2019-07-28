package monthlybudget.apps.danielbrosh.monthlybudget;

import java.util.ArrayList;
import java.util.List;

import static monthlybudget.apps.danielbrosh.monthlybudget.global.LANGUAGE;

public class Category {
    private String name;
    private String subCategoryName;
    private double balance;
    private int budget;
    private ArrayList<Transaction> transactions;

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Category(String name, String subCategoryName) {
        this.name = name;
        this.subCategoryName = subCategoryName;
        this.balance = 0;
        this.budget = 0;
    }

    public Category(String name) {
        this.name = name;
        this.subCategoryName = LANGUAGE.subCategoryName;
        this.balance = 0;
        this.budget = 0;
    }

    public Category(String name, int budget, double balance, List<Transaction> transactions) {
        this.name = name;
        this.subCategoryName = LANGUAGE.subCategoryName;//need to complete this part
        this.balance = balance;
        this.budget = budget;
        this.transactions = new ArrayList<Transaction>();
        if( transactions != null && transactions.size() > 0)
            for (Transaction tran : transactions)
                this.transactions.add(tran);
    }

    public Category(String name,String subCategoryName, int budget, double balance, List<Transaction> transactions) {
        this.name = name;
        this.subCategoryName = subCategoryName;
        this.balance = balance;
        this.budget = budget;
        this.transactions = new ArrayList<Transaction>();
        if( transactions != null && transactions.size() > 0)
            for (Transaction tran : transactions)
                this.transactions.add(tran);
    }

    public void setBudgetValue(int budget) {
        this.budget = budget;
    }

    public void setBalanceValue(double balance) {
        this.balance = balance;
    }

    public void subValBalance(double valToSub)
    {
        balance -= valToSub;
    }

    public String getName() {
        return name;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public double getBalanceValue() {
        return balance;
    }

    public int getBudgetValue() {
        return budget;
    }

    public ArrayList<Transaction>  getTransactions() {
        return this.transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

}
