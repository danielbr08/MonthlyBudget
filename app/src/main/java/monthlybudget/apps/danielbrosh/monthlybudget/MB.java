package monthlybudget.apps.danielbrosh.monthlybudget;

import java.util.Date;

public class MB {
    public MB(Date monthlyBudgetRefMonth, int budgetCategoryID, int budgetSubCategoryID, int monthlyBudgetNumber, int monthlyBudget, double monthlyBudgetBalance) {
        this.monthlyBudgetRefMonth = monthlyBudgetRefMonth;
        this.budgetCategoryID = budgetCategoryID;
        this.budgetSubCategoryID = budgetSubCategoryID;
        this.monthlyBudgetNumber = monthlyBudgetNumber;
        this.monthlyBudget = monthlyBudget;
        this.monthlyBudgetBalance = monthlyBudgetBalance;
    }

    public MB() {
    }

    public Date getMonthlyBudgetRefMonth() {
        return monthlyBudgetRefMonth;
    }

    public void setMonthlyBudgetRefMonth(Date monthlyBudgetRefMonth) {
        this.monthlyBudgetRefMonth = monthlyBudgetRefMonth;
    }

    public int getBudgetCategoryID() {
        return budgetCategoryID;
    }

    public void setBudgetCategoryID(int budgetCategoryID) {
        this.budgetCategoryID = budgetCategoryID;
    }

    public int getBudgetSubCategoryID() {
        return budgetSubCategoryID;
    }

    public void setBudgetSubCategoryID(int budgetSubCategoryID) {
        this.budgetSubCategoryID = budgetSubCategoryID;
    }

    public int getMonthlyBudgetNumber() {
        return monthlyBudgetNumber;
    }

    public void setMonthlyBudgetNumber(int monthlyBudgetNumber) {
        this.monthlyBudgetNumber = monthlyBudgetNumber;
    }

    public int getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(int monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public double getMonthlyBudgetBalance() {
        return monthlyBudgetBalance;
    }

    public void setMonthlyBudgetBalance(double monthlyBudgetBalance) {
        this.monthlyBudgetBalance = monthlyBudgetBalance;
    }

    Date monthlyBudgetRefMonth;
    int budgetCategoryID;
    int budgetSubCategoryID;
    int monthlyBudgetNumber;
    int monthlyBudget;
    double monthlyBudgetBalance;
}
