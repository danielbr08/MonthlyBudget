package monthlybudget.apps.danielbrosh.monthlybudget;

import java.util.Date;

import static monthlybudget.apps.danielbrosh.monthlybudget.global.LANGUAGE;

/**
 * Created by daniel.brosh on 12/26/2017.
 */

public class Budget {

    public Budget() {
    }

    public String getId() {
        return id;
    }

    public long getBudgetNumber() {
        return budgetNumber;
    }

    public void setBudgetNumber(int budgetNumber) {
        this.budgetNumber = budgetNumber;
    }

    private String id;
    private long budgetNumber;
    private String category;
    private String subCategory;
    private int value;
    private boolean isConstPayment;
    private String shop;
    private int chargeDay;
    private int catPriority;

    public Budget(int catPriority,String category, int value, boolean isConstPayment, String shop, int chargeDay) {
        this.category = category;
        this.value = value;
        this.isConstPayment = isConstPayment;
        this.shop = shop;
        this.chargeDay = chargeDay;
        this.catPriority = catPriority;
        this.subCategory = "";
    }

    public Budget(int catPriority,String category, String subCategory, int value, boolean isConstPayment, String shop, int chargeDay) {
        this.category = category;
        this.subCategory = subCategory;
        this.value = value;
        this.isConstPayment = isConstPayment;
        this.shop = shop;
        this.chargeDay = chargeDay;
        this.catPriority = catPriority;

        if(subCategory == null || subCategory.equals(""))
            this.subCategory = LANGUAGE.subCategoryName;
    }

    public Budget(String id,long budgetNumber, int catPriority,String category, String subCategory, int value, boolean isConstPayment, String shop, int chargeDay) {
        this.id = id;
        this.category = category;
        this.subCategory = subCategory;
        this.value = value;
        this.isConstPayment = isConstPayment;
        this.shop = shop;
        this.chargeDay = chargeDay;
        this.catPriority = catPriority;

        if(subCategory == null || subCategory.equals(""))
            this.subCategory = LANGUAGE.subCategoryName;
    }

    public String getCategory() {
        return category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public int getValue() {
        return value;
    }

    public boolean equals(Object budget) {
        return budget instanceof Budget
                && category.equals(((Budget)budget).category)
                && ( (subCategory == null && ((Budget)budget).subCategory == null) ||
                     ( subCategory != null && subCategory.equals(((Budget)budget).subCategory) ) )
                && value ==((Budget)budget).value
                && isConstPayment ==((Budget)budget).isConstPayment
                && ((shop == ((Budget)budget).shop)//null
                     || shop != null && shop.equals(((Budget)budget).shop))
                && chargeDay ==((Budget)budget).chargeDay;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isConstPayment() {
        return isConstPayment;
    }

    public void setConstPayment(boolean constPayment) {
        isConstPayment = constPayment;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public int getChargeDay() {
        return chargeDay;
    }

    public void setChargeDay(Date paymentDate) {
        this.chargeDay = chargeDay;
    }

    public int getCatPriority() {
        return catPriority;
    }

    public void setCatPriority(int catPriority) {
        this.catPriority = catPriority;
    }
}
