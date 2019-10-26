package monthlybudget.apps.danielbrosh.monthlybudget;

public class SubCat {

    private String id;
    private long subCategoryId;
    private String subCategoryName;

    public SubCat( String id, long subCategoryId, String subCategoryName) {
        this.id = id;
        this.subCategoryId = subCategoryId;
        this.subCategoryName = subCategoryName;
    }

    public long getSubCategoryId() {
        return subCategoryId;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public String getId() {
        return id;
    }
}
