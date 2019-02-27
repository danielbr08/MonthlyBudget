/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package monthlybudget.apps.danielbrosh.monthlybudget;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static monthlybudget.apps.danielbrosh.monthlybudget.R.id.monthSpinner;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.DB_FILE_NAME;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.DB_FOLDER_PATH;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.DB_SUFFIX;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.DCIM;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.DEFAULT_LANGUAGE;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.FILE_NAME;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.IS_AD_ENEABLED;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.IS_MONTH_CHANGABLE;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.LANGUAGE;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.LOG_REPORT;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.PROJECT_PATH;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.TRAN_ID_PER_MONTH_NUMERATOR;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.convertStringToDate;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.copyFile;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.dateFormat;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getDateStartMonth;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getSartOfMonth;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getTodayDate;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getYearMonth;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.reverseDateString;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.setCatPaymentMethodArray;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.setMyBudget;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.shopsSet;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.writeToFile;

//import android.text.format.DateFormat;

public class MainActivity extends AppCompatActivity
{
    InterstitialAd mInterstitialAd;
    Intent budgetScreen, transactionsScreen, insertTransactionScreen, createBudgetScreen;
    Spinner refMonthSpinner,languageSpinner;
    Button insertTransactionButton;
    Button budgetButton;
    Button transactionsButton;
    Button createBudgetButton;
    Button closeMainButton;
    boolean Touched=false; // Indicate for language spinner
    boolean onResumeOfOnCreateFlag = true;

    public static myDBAdapter monthlyBudgetDB;

    public static Month month;

    public void initAdFields()
    {
        MobileAds.initialize(this,"ca-app-pub-9791546601159997~5576316736");
        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9791546601159997/3594093179");
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");// For test
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }});
    }
    public void showAD(boolean isChanceDepend)
    {
        int randNumber = (int) (Math.random() * 2) + 1;
        if (isChanceDepend && randNumber != 2 )
            return;
        try
        {
            if(mInterstitialAd.isLoaded())
                mInterstitialAd.show();
        }
        catch(Exception e)
        {
            LOG_REPORT.add("Exception in showAD method : " + "\n" + e.getMessage().toString() );
        }

/*        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9791546601159997~2411718492");


        AdRequest.Builder builder = new AdRequest.Builder();
        mInterstitialAd.loadAd(builder.build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                try
                {
                    mInterstitialAd.show();
                    //Log.e()
                }
                catch(Exception e)
                {
                    e.getMessage().toString();
                }
            }
        });*/
    }

    public void initLanguageSpinner() {
        //global.setCatArrayHebNames();
        ArrayList<String> allMonths = new ArrayList();
        allMonths.add("עברית");
        allMonths.add("EN");

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_spinner, allMonths);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
    }

    public void changeTouchValue(boolean touched)
    {
        Touched=touched;
    }

    public void initRefMonthSpinner()
    {
        //global.setCatArrayHebNames();
        ArrayList<String> allMonths = monthlyBudgetDB.getAllMonthesYearMonth("DESC");//getAllMonthes();

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_spinner,allMonths);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        refMonthSpinner.setAdapter(adapter);

/*        String refMonth = refMonthSpinner.getSelectedItem().toString();
        refMonth = ("01." + refMonth);
        refMonth = refMonth.replace('.','/');
        month = new Month(convertStringToDate(refMonth,dateFormat));
        refMonth = refMonth.replace('/','.').substring(0,refMonth.length() -3);*/
    }

    public void onCloseApp()
    {
        if(month != null &&  month.getTransChanged())
        {
            month.updateMonthData(0);
        }
        if(month == null)
            LOG_REPORT.add("Month object is null.\n");
        if(!monthlyBudgetDB.checkBudgetExists())
            LOG_REPORT.add("Budget is not exists.\n");
        if(!monthlyBudgetDB.checkCurrentRefMonthExists())
            LOG_REPORT.add("Current monthly budget is not exists.\n");
        writeToFile(LOG_REPORT,"MB_LOG_REPORT",DCIM);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume()
    {
        super.onResume();

        boolean isAdChanceDepend = !onResumeOfOnCreateFlag;
        if (onResumeOfOnCreateFlag)
            onResumeOfOnCreateFlag = false;

        // After inserted transactions
        if(month != null && month.getTransChanged() == true)
        {
            int maxIDPerMonth = monthlyBudgetDB.getMaxIDPerMonthTRN(month.getMonth());
            month.updateMonthData(maxIDPerMonth + 1);
            month.setTransChanged(false);
        }

        // After creating a new Budget
        if(monthlyBudgetDB.checkBudgetExists() && month == null)
        {
            budgetButton.setEnabled(true);
            transactionsButton.setEnabled(true);
            insertTransactionButton.setEnabled(true);

            //monthlyBudgetDB.deleteDataRefMonth(getDateStartMonth());
            month = new Month(getDateStartMonth());
            IS_MONTH_CHANGABLE = true;
            TRAN_ID_PER_MONTH_NUMERATOR = 1;
            setTitle(getYearMonth(month.getMonth(), '.'));
            initRefMonthSpinner();

            if(month.getTransactions().size() == 0)
            {
                //Write frequence transactions
                setFrqTrans(null,0);
                //month.writeCategories( SEPARATOR, SEPARATOR);
            }
            month.setTransChanged(false);
        }
        if(IS_AD_ENEABLED)
            showAD(isAdChanceDepend);
    }

    public static void setFrqTrans(ArrayList<Budget> freqBudgets, int idPerMonth)
    {
        int maxBudgetNumberBGT = monthlyBudgetDB.getMaxBudgetNumberBGT();
        ArrayList<Budget> allBudget = freqBudgets;
        if(allBudget == null)
            allBudget= monthlyBudgetDB.getBudgetDataFromDB(maxBudgetNumberBGT);
        for (Budget budget:allBudget)
        {
            if(!budget.isConstPayment())
                continue;

            String categoryName = budget.getCategory();
            double transactionPrice = Double.valueOf(budget.getValue());
            String shop = budget.getShop();
            int chargeDay = budget.getChargeDay();
            Date payDate = new Date();
            String paymentMethod = LANGUAGE.creditCardName;

            Calendar c = Calendar.getInstance();
// set the calendar to start of today
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            int lastDayInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            if(chargeDay > lastDayInMonth)
                chargeDay = lastDayInMonth;
            c.set(Calendar.DAY_OF_MONTH, chargeDay);
            payDate = c.getTime();

            //Insert data
            Transaction transaction = new Transaction(TRAN_ID_PER_MONTH_NUMERATOR++, categoryName,paymentMethod , shop, payDate, transactionPrice, new Date());
            transaction.setIsStorno(false);
            transaction.setStornoOf(-1);
            month.setTransChanged(true);

            for (Category cat : month.getCategories())
            {
                if (categoryName.equals(cat.getName()))
                {
                    cat.subValRemaining(transactionPrice);
                    cat.addTransaction(transaction);
                }
            }
            shopsSet.add(shop);
        }
        month.setAllTransactions();
        if( month.getTransChanged())
            month.updateMonthData(idPerMonth + 1);
        month.setTransChanged(false);
    }
/*
    @Override
    public void onStop()
    {
        super.onStop();
        if(month != null && month.getTransChanged() == true)
        {
            int maxIDPerMonth = monthlyBudgetDB.getMaxIDPerMonthTRN(month.getMonth());
            month.updateMonthData(maxIDPerMonth + 1);
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        onCloseApp();
        copyFile(DB_FOLDER_PATH + "/" + DB_FILE_NAME + "." + DB_SUFFIX, PROJECT_PATH);
        //Uplod file to GD
        writeToFile(LOG_REPORT,"MB_LOG_REPORT",DCIM);
        finish();
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        //write catArray to the file
    }

/*    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }*/

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setTitle(String refMonth)
    {
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
        tv.setText(LANGUAGE.appName + "\n"  + refMonth);
        tv.setTextSize(18);

        ab.setCustomView(tv);
        ab.setDisplayShowCustomEnabled(true); //show custom title
        ab.setDisplayShowTitleEnabled(false); //hide the default title
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setButtonsNames()
    {
        // Main window buttons
        //((TextView)findViewById(R.id.monthLabel)).setText(language.monthName);
        ((Button)findViewById(R.id.budgetButton)).setText(LANGUAGE.budgetButtonName);
        ((Button)findViewById(R.id.transactionsButton)).setText(LANGUAGE.transactionsButtonName);
        ((Button)findViewById(R.id.insertTransactionButton)).setText(LANGUAGE.insertTransactionButtonName);
        ((Button)findViewById(R.id.createBudgetButton)).setText(LANGUAGE.createBudgetButtonName);
        ((Button)findViewById(R.id.closeMainButton)).setText(LANGUAGE.close);
        if(month != null)
            setTitle(getYearMonth(month.getMonth(), '.'));
        else
            setTitle("");
/*
        // Budget window buttons
        ((TextView)findViewById(R.id.budgetLabel)).setText(language.budgetTitleName);
        ((TextView)findViewById(R.id.categoryLabel)).setText(language.categoryName);
        ((TextView)findViewById(R.id.budgetLabel)).setText(language.budgetName);
        ((TextView)findViewById(R.id.balanceLabel)).setText(language.balanceName);

        // Transactions window buttons
        ((TextView)findViewById(R.id.textViewTotalTransactions)).setText(language.totalName);

        // Transactions window buttons
        ((TextView)findViewById(R.id.createBudgetLabel)).setText(language.createBudgetName);*/
    }

    public void initSpinnerByDefault()
    {
        if (DEFAULT_LANGUAGE.equals("עברית"))
            languageSpinner.setSelection(0, true);
        else if (DEFAULT_LANGUAGE.equals("EN"))
            languageSpinner.setSelection(1, true);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SharedPreferences  sharedpreference = this.getSharedPreferences(
                "monthlybudget.apps.danielbrosh.monthlybudget", Context.MODE_PRIVATE);
        //sharedpreference= PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        DEFAULT_LANGUAGE = sharedpreference.getString("default_language","EN");
        IS_AD_ENEABLED = sharedpreference.getBoolean("isAdEnable",true);
        //sharedpreference.edit().remove("isAdEnable").commit();
        initAdFields();
        refMonthSpinner = (Spinner) findViewById(monthSpinner);
        languageSpinner = (Spinner) findViewById(R.id.languageSpinner);
        initLanguageSpinner();

        LANGUAGE = new Language(DEFAULT_LANGUAGE);
        initSpinnerByDefault();
        setButtonsNames();
        setCatPaymentMethodArray();
        setTitle("");

        budgetScreen = null;
        transactionsScreen = null;
        insertTransactionScreen = null;
        createBudgetScreen = null;
        refMonthSpinner = (Spinner) findViewById(monthSpinner);
        //DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
        DCIM = getExternalFilesDir(Environment.DIRECTORY_DCIM).toString();
        PROJECT_PATH = DCIM + "/" + FILE_NAME;

        //copyFile(PROJECT_PATH + "/" + DB_FILE_NAME + "." + DB_SUFFIX, DB_FOLDER_PATH);
        monthlyBudgetDB = new myDBAdapter(this);

        insertTransactionButton = (Button) findViewById(R.id.insertTransactionButton);
        budgetButton = (Button) findViewById(R.id.budgetButton);
        transactionsButton = (Button) findViewById(R.id.transactionsButton);
        createBudgetButton = (Button) findViewById(R.id.createBudgetButton);
        closeMainButton = (Button) findViewById(R.id.closeMainButton);

        if(false)
            setMyBudget();

        if(!monthlyBudgetDB.checkBudgetExists() )
        {
            budgetButton.setEnabled(false);
            transactionsButton.setEnabled(false);
            insertTransactionButton.setEnabled(false);
        }
        else
        {
            boolean isSetFrqTransNeeded = false;
            if(!monthlyBudgetDB.checkCurrentRefMonthExists())
                isSetFrqTransNeeded = true;
            month = new Month(getDateStartMonth());
            IS_MONTH_CHANGABLE = true;
            setTitle(getYearMonth(month.getMonth(), '.'));
            initRefMonthSpinner();
            if(isSetFrqTransNeeded)
                //Write frequence transactions
                setFrqTrans(null,0);
        }
        //Listening to button event
        budgetButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                if (budgetScreen == null)
                    budgetScreen = new Intent(MainActivity.this, BudgetActivity.class);

                //Sending data to another Activity by key[name] and value[something]
                //nextScreen.putExtra("name", "something");
                //nextScreen.putExtra("email", "something2");
                startActivity(budgetScreen);
            }
        });

        //Listening to button event
        insertTransactionButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                if (insertTransactionScreen == null)
                    insertTransactionScreen = new Intent(MainActivity.this, InsertTransactionActivity.class);
                startActivity(insertTransactionScreen);

            }
        });

        //Listening to button event
        transactionsButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                if (transactionsScreen == null)
                    transactionsScreen = new Intent(MainActivity.this, TransactionsActivity.class);
                    startActivity(transactionsScreen);
            }
        });

        //Listening to button event
        createBudgetButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                //Starting a new Intent
                if (createBudgetScreen == null)
                    createBudgetScreen = new Intent(MainActivity.this, Create_Budget_Activity.class);
                    //initRefMonthSpinner();
                startActivity(createBudgetScreen);
            }
        });

        //Listening to button event
        closeMainButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onClick(View arg0) {
                onCloseApp();
                // finish();
                // System.exit(0);
                // finish();
            }
        });

        languageSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                changeTouchValue(true);
                return false;
            }
        });

        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(!Touched)
                    return;
                String lang = languageSpinner.getSelectedItem().toString();

                if (lang.equals("עברית")) {

                    LANGUAGE = new Language("HEB");
                    sharedpreference.edit().putString("default_language","HEB".toString()).apply();
                    sharedpreference.edit().commit();

                }
                else if (lang.equals("EN")) {
                    LANGUAGE = new Language("EN");
                    sharedpreference.edit().putString("default_language","EN".toString()).apply();
                    sharedpreference.edit().commit();
                }
                setButtonsNames();
                setCatPaymentMethodArray();
                changeTouchValue(false);
/*                if(!monthlyBudgetDB.checkBudgetExists())
                    setTitle("");*/

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        refMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String refMonth = refMonthSpinner.getSelectedItem().toString();
                refMonth = (refMonth + ".01");
                refMonth = reverseDateString(refMonth,"\\.");
                refMonth = refMonth.replace('.', '/');
                // Close the current month by writing to files+
                if(month.getTransChanged())
                {
                    int maxIDPerMonth = monthlyBudgetDB.getMaxIDPerMonthTRN(month.getMonth());
                    month.updateMonthData(maxIDPerMonth + 1);
                    month.setTransChanged(false);
                }

                month = new Month(convertStringToDate(refMonth, dateFormat));
                setTitle(getYearMonth(month.getMonth(), '.'));
                if(month.getMonth().before(getSartOfMonth(getTodayDate())))
                    IS_MONTH_CHANGABLE = false;
                else
                    IS_MONTH_CHANGABLE = true;
                insertTransactionButton.setEnabled(IS_MONTH_CHANGABLE);
                createBudgetButton.setEnabled(IS_MONTH_CHANGABLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        //showAD(false);
    }

}

