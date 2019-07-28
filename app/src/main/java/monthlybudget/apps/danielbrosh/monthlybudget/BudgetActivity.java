package monthlybudget.apps.danielbrosh.monthlybudget;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import static monthlybudget.apps.danielbrosh.monthlybudget.MainActivity.month;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.LANGUAGE;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getYearMonth;

public class BudgetActivity extends AppCompatActivity {
    LinearLayout ll;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        setButtonsNames();
        setTitle(getYearMonth(month.getMonth(), '.'));

        ll = (LinearLayout) findViewById(R.id.LLBudget);
        setCategoriesUI();
        //setCloseButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  onResumeFragments();
        // setCategoriesInGui();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void addCategoryRow(String categoryName, String Budget, String balance, boolean isExceptionFromBudget){//Bundle savedInstanceState)
        TextView categoryNameTV = new TextView(BudgetActivity.this);
        TextView budgetTV= new TextView(BudgetActivity.this);
        TextView balanceTV = new TextView(BudgetActivity.this);
        LinearLayout newll = new LinearLayout(BudgetActivity.this);

        categoryNameTV.setText(categoryName);
        budgetTV.setText(balance);
        balanceTV.setText(Budget);

        setDirections(categoryNameTV,budgetTV,balanceTV);
        setAlignments(categoryNameTV,budgetTV,balanceTV);

        if(categoryName.equals(LANGUAGE.totalName)) {
            setBold(categoryNameTV);
            setBold(budgetTV);
            setBold(balanceTV);
        }
        if(isExceptionFromBudget == true) {
            setTextColor(categoryNameTV,Color.RED);
            setTextColor(budgetTV,Color.RED);
            setTextColor(balanceTV,Color.RED);
        }

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        setLayoutParams(categoryNameTV,screenWidth/3,ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(budgetTV,screenWidth/3,ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(balanceTV,screenWidth/3,ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(newll,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        newll.setOrientation(LinearLayout.HORIZONTAL);

        addViewsLL(newll,categoryNameTV,budgetTV,balanceTV);
        ll.addView(newll);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setCategoriesUI()
    {
        int budgetTotal = 0;
        double balanceTotal = 0;
        boolean isExceptionFromBudget = false;

        for (Category category : month.getCategories())
        {
            String categoryName = category.getName();
            double balance = category.getBalanceValue();
            balance = Math.round(balance * 100.d)/ 100.0d ;
            int budget = category.getBudgetValue();
            if(balance < 0)
                isExceptionFromBudget = true;
            addCategoryRow(categoryName,String.valueOf(budget),String.valueOf(balance),isExceptionFromBudget);

            budgetTotal += budget;
            balanceTotal += balance;
            isExceptionFromBudget = false;
        }
        balanceTotal =  Math.round(balanceTotal * 100.d) / 100.0d ;
        if(balanceTotal < 0)
            isExceptionFromBudget = true;
        addCategoryRow(LANGUAGE.totalName,String.valueOf(budgetTotal),String.valueOf(balanceTotal),isExceptionFromBudget);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setTitle(String refMonth) {
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        TextView tv = new TextView(getApplicationContext());

        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(lp);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(Color.WHITE);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setText(LANGUAGE.appName + "\n" + refMonth);
        tv.setTextSize(18);

        ab.setCustomView(tv);
        ab.setDisplayShowCustomEnabled(true); //show custom title
        ab.setDisplayShowTitleEnabled(false); //hide the default title
    }

    private void setLayoutParams(View v, int width, int heigth){
        v.setLayoutParams(new LinearLayout.LayoutParams(width, heigth));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setAlignments(TextView categoryNameTV, TextView budgetTV, TextView balanceTV) {

        if(LANGUAGE.language.equals("HEB"))
            balanceTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        else if(LANGUAGE.language.equals("EN"))
            balanceTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

        budgetTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        categoryNameTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setDirections(TextView categoryNameTV, TextView budgetTV, TextView balanceTV) {
            budgetTV.setTextDirection(View.TEXT_DIRECTION_LTR);
            balanceTV.setTextDirection(View.TEXT_DIRECTION_LTR);
            if(LANGUAGE.language.equals("HEB"))
                categoryNameTV.setTextDirection(View.TEXT_DIRECTION_RTL);
            else if(LANGUAGE.language.equals("EN"))
                categoryNameTV.setTextDirection(View.TEXT_DIRECTION_LTR);
    }

    private void setTextColor(TextView tv, int color){
        tv.setTextColor(color);
    }

    private void setBold(TextView tv){
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(13);
        setTextColor(tv,Color.BLACK);
    }

    private void addViewsLL(LinearLayout newll,TextView categoryNameTV,TextView budgetTV, TextView balanceTV){
        if(LANGUAGE.language.equals("HEB")) {
            newll.addView(categoryNameTV);
            newll.addView(budgetTV);
            newll.addView(balanceTV);

        }
        else if(LANGUAGE.language.equals("EN")) {
            newll.addView(balanceTV);
            newll.addView(budgetTV);
            newll.addView(categoryNameTV);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setButtonsNames()
    {
        LinearLayout llBudgetTitle = (LinearLayout)findViewById(R.id.LLBudgetTitle);
        TextView budgetTitleTV = ((TextView)findViewById(R.id.BudgetTitleLabel));
        TextView categoryTV = ((TextView) findViewById(R.id.categoryLabel));
        TextView budgetTV = ((TextView) findViewById(R.id.budgetLabel));
        TextView balanceTV = ((TextView) findViewById(R.id.balanceLabel));
        // Budget window buttons
        budgetTitleTV.setText(LANGUAGE.budgetName);
        categoryTV.setText(LANGUAGE.budgetCategoryName);
        budgetTV.setText(LANGUAGE.budgetName);
        balanceTV.setText(LANGUAGE.balanceName);

        llBudgetTitle.removeAllViews();
        addViewsLL(llBudgetTitle,categoryTV,budgetTV,balanceTV);

        if(LANGUAGE.language.equals("HEB"))
            addViewsLL(llBudgetTitle,categoryTV,budgetTV,balanceTV);
        else if(LANGUAGE.language.equals("EN"))
            addViewsLL(llBudgetTitle,balanceTV,budgetTV,categoryTV);

//        if(month != null)
//            setTitle(getYearMonth(month.getMonth(), '.'));
    }

    public void setCloseButton()
    {
        final Button myButton = new Button(this);
        myButton.setText(LANGUAGE.close);
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayout newll = new LinearLayout(BudgetActivity.this);
        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        newll.setOrientation(LinearLayout.HORIZONTAL);
        newll.addView(myButton,lp);
        ll.addView(newll);
    }
}