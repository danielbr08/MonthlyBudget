package monthlybudget.apps.danielbrosh.monthlybudget;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import static monthlybudget.apps.danielbrosh.monthlybudget.MainActivity.month;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.LANGUAGE;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getYearMonth;


public class BudgetActivity extends AppCompatActivity {
    //ArrayList<Category> catArray = new ArrayList<Category>();
    //ArrayList<String> catNamesArray = new ArrayList<String>();
    LinearLayout ll;

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void addCategoryRow(String categoryName, String Budget, String Remainder, boolean isExceptionFromBudget)//Bundle savedInstanceState)
    {
        TextView categoryNameTextView = new TextView(BudgetActivity.this);
        TextView budgetTextView = new TextView(BudgetActivity.this);
        TextView remainderTextView = new TextView(BudgetActivity.this);
        PieChart pieChart = new PieChart(BudgetActivity.this);

        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        yvalues.add(new Entry(15f, 0));
        //yvalues.add(new Entry(85f, 1));

        ArrayList<String> l = new ArrayList<>();
        l.add("");
        //l.add("2");

        PieDataSet dataSet = new PieDataSet(yvalues, "");
        PieData data = new PieData(l,dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);

        LinearLayout newll = new LinearLayout(BudgetActivity.this);

        //dataSet.setColors(Collections.singletonList(Color.GREEN));
        //dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieChart.setDescription("");
        //pieChart.setDrawHoleEnabled(false);
        //pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(30f);
        //pieChart.setHoleRadius(30f);
        //data.setValueTextSize(11f);
        //data.setValueTextColor(Color.DKGRAY);

        pieChart.animateXY(1400, 1400);

        categoryNameTextView.setText(categoryName);
        remainderTextView.setText(Remainder);
        budgetTextView.setText(Budget);

        budgetTextView.setTextDirection(View.TEXT_DIRECTION_LTR);
        remainderTextView.setTextDirection(View.TEXT_DIRECTION_LTR);
        if(LANGUAGE.language.equals("HEB")) {
            categoryNameTextView.setTextDirection(View.TEXT_DIRECTION_RTL);
            remainderTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        else if(LANGUAGE.language.equals("EN"))
        {
            categoryNameTextView.setTextDirection(View.TEXT_DIRECTION_LTR);
            remainderTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }

        budgetTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //budgetTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        //remainderTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        categoryNameTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        if(categoryName == LANGUAGE.totalName)
        {
            categoryNameTextView.setTypeface(null, Typeface.BOLD);
            categoryNameTextView.setTextSize(13);
            categoryNameTextView.setTextColor(Color.BLACK);
            budgetTextView.setTypeface(null, Typeface.BOLD);
            budgetTextView.setTextSize(13);
            budgetTextView.setTextColor(Color.BLACK);
            remainderTextView.setTypeface(null, Typeface.BOLD);
            remainderTextView.setTextSize(13);
            remainderTextView.setTextColor(Color.BLACK);
        }

        if(isExceptionFromBudget == true)
        {
            categoryNameTextView.setTextColor(Color.RED);
            budgetTextView.setTextColor(Color.RED);
            remainderTextView.setTextColor(Color.RED);
        }

        Display display = getWindowManager().getDefaultDisplay();
        int screenWidth = display.getWidth();
        categoryNameTextView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
        budgetTextView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
        remainderTextView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
        pieChart.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4, ViewGroup.LayoutParams.WRAP_CONTENT));
        //categoryValueEditText.setTextSize(18);

        newll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        newll.setOrientation(LinearLayout.HORIZONTAL);

        if(LANGUAGE.language.equals("HEB"))
        {
            newll.addView(categoryNameTextView);
            newll.addView(budgetTextView);
            newll.addView(remainderTextView);
            newll.addView(pieChart);

        }
        else if(LANGUAGE.language.equals("EN"))
        {
            newll.addView(pieChart);
            newll.addView(remainderTextView);
            newll.addView(budgetTextView);
            newll.addView(categoryNameTextView);
        }

        ll.addView(newll);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //  onResumeFragments();
        // setCategoriesInGui();
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

        if(LANGUAGE.language.equals("HEB"))
        {
            llBudgetTitle.removeAllViews();
            llBudgetTitle.addView(categoryTV);
            llBudgetTitle.addView(budgetTV);
            llBudgetTitle.addView(balanceTV);
        }
        else if(LANGUAGE.language.equals("EN"))
        {
            llBudgetTitle.removeAllViews();
            llBudgetTitle.addView(balanceTV);
            llBudgetTitle.addView(budgetTV);
            llBudgetTitle.addView(categoryTV);
        }

        if(month != null)
            setTitle(getYearMonth(month.getMonth(), '.'));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        setButtonsNames();
        setTitle(getYearMonth(month.getMonth(), '.'));

        ll = (LinearLayout) findViewById(R.id.LLBudget);
        setCategoriesInGui();
        //setCloseButton();
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

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout newll = new LinearLayout(BudgetActivity.this);

        LinearLayout.LayoutParams paramLL = (new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,1f));

        newll.setOrientation(LinearLayout.HORIZONTAL);
        newll.addView(myButton,lp);
        ll.addView(newll);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setCategoriesInGui()
    {
        int budgetTotal = 0;
        double remainderTotal = 0;
        boolean isExceptionFromBudget = false;

        for (Category category : month.getCategories())
        {
            String categoryName = category.getName();
            double remaining = category.getRamainingValue();
            remaining = Math.round(remaining * 100.d)/ 100.0d ;
            int budget = category.getBudgetValue();
            if(remaining < 0)
                isExceptionFromBudget = true;
            addCategoryRow(categoryName,String.valueOf(budget),String.valueOf(remaining),isExceptionFromBudget);

            budgetTotal += budget;
            remainderTotal += remaining;
            isExceptionFromBudget = false;
        }
        remainderTotal =  Math.round(remainderTotal * 100.d) / 100.0d ;
        if(remainderTotal < 0)
            isExceptionFromBudget = true;
        addCategoryRow(LANGUAGE.totalName,String.valueOf(budgetTotal),String.valueOf(remainderTotal),isExceptionFromBudget);

        //setValueGui("BudgetTotal", budgetTotal);
        //setValueGui("RemainderTotal", remainderTotal);
    }

/*    public ArrayList<String> makeStrListCategoriesOriginFile(String separator)
    {
        ArrayList<String> lines = new ArrayList<>();
        String line = "";
        for (Category cat: month.getCategories())
        {
            line = cat.getName() + separator + cat.getBudgetValue() + separator + cat.getRamainingValue();
            lines.add(line);
        }
        return lines;
    }*/

/*
    public void setCategoriesFromGui()
    {
        setCatNamesArray();
        month.getCategories().clear();
        for (String catName:global.catNamesArray)
        {
            int budgetValue = Integer.valueOf(getValueGui("Budget" + catName));
            double remainderValue = Double.valueOf(getValueGui("Remainder" + catName));
            month.getCategories().add(new Category(catName, budgetValue, remainderValue,null));
        }
    }
    // Initialize the list of the categories
    public void initCatArray()
    {
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath() + "/Monthly Budget";
        boolean isFileExists = false;
        final File filePath = new File(dirPath + "/Monthly Budget.txt");
        if(filePath.exists())
            isFileExists = true;
        if(isFileExists)
            catArray = getCategoriesFromFile(dirPath,"-->", "-->");
        else
        {
            setCategoriesFromGui();
            global.writeCategories(dirPath, "-->", null);
        }
    }

    public void subValue(String categoryName, double value)
    {
        int index = global.getIndexByName(categoryName);
        Category category = catArray.get(index);
        category.subValRemaining(value);
        catArray.set(index,category);
    }*/

/*    public void setIDRemainderArray()
    {
        IDRemainderArray.clear();
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderNavit));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderFood));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderAfternoon));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderLoanBank));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderLoanParentN));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderLoanParentD));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderMortgage));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderGarden));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderInsCars));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderFuel));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderElectric));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderTax));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderWater));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderGas));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderDonations));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderInsLife));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderCells));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderInternet));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderPharm));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderClothes));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderPlay));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderFun));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderInsHouse));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderGames));
        IDRemainderArray.add((TextView)findViewById(R.id.RemainderSavingPlan));
    }

    public void setIDBudgetArray()
    {
        IDBudgetArray.clear();
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetNavit));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetFood));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetAfternoon));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetLoanBank));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetLoanParentN));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetLoanParentD));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetMortgage));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetGarden));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetInsCars));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetFuel));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetElectric));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetTax));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetWater));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetGas));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetDonations));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetInsLife));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetCells));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetInternet));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetPharm));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetClothes));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetPlay));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetFun));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetInsHouse));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetGames));
        IDBudgetArray.add((TextView)findViewById(R.id.BudgetSavingPlan));
    }

    public Date convertStringToDate(String stringDate)
    {
        //String lastTimeDateString = "06/27/2017";
        java.text.DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        Date date = null;
        try {
            date = df.parse(stringDate);
            String newStringDate = df.format(date);
            System.out.println(newStringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // need end of month
        return date;
    }

    public void checkDate()
    {
        Calendar c = Calendar.getInstance();

// set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

// and get that as a Date
        Date today = c.getTime();

        String lastTimeDateString = getDateFromFile();
        Date lastTimeDate = convertStringToDate(lastTimeDateString);
        Date forMonth = lastTimeDate;

        // set the ref month to start of next month
        Calendar cal = Calendar.getInstance();
        cal.setTime(forMonth);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = 1;

        c.set(Calendar.YEAR,year);
        forMonth = c.getTime();
        if (c.before(forMonth))
        {
            // create a new file for this month
            // and close the file of previouse month
        }
    }

*/
}