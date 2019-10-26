package monthlybudget.apps.danielbrosh.monthlybudget;

public class Cat {
    public Cat(long catId, String catName) {
        this.catId = catId;
        this.catName = catName;
    }

    public long getCatId() {
        return catId;
    }

    public String getCatName() {
        return catName;
    }

    private long catId;
    private String catName;
}
