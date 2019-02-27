package monthlybudget.apps.danielbrosh.monthlybudget;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import static monthlybudget.apps.danielbrosh.monthlybudget.MainActivity.month;
import static monthlybudget.apps.danielbrosh.monthlybudget.MainActivity.monthlyBudgetDB;
import static monthlybudget.apps.danielbrosh.monthlybudget.MainActivity.setFrqTrans;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.LANGUAGE;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.SEPARATOR;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getDateStartMonth;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getWordCapitalLetter;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.reverseLinearLayout;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.setHeaderProperties;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.setLanguageConf;

public class Create_Budget_Activity extends AppCompatActivity {

    AlertDialog.Builder myAlert;
    private ArrayList<Budget> allBudgets;
    private ArrayList<String> allCategories;
    private boolean isInputValid;
    private Display display;
    private int screenWidth;
    private int buttonSize = 120;
    private Drawable dfaultBackground;

    private LinearLayout LLMain;
    //Button to add a row
    private Button addCategoryButton;
    //Button to write all the inserted categories to budget file
    private Button OKButton;
    private LinearLayout newll;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setTitle() {
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
        tv.setText(LANGUAGE.appName);
        tv.setTextSize(18);

        ab.setCustomView(tv);
        ab.setDisplayShowCustomEnabled(true); //show custom title
        ab.setDisplayShowTitleEnabled(false); //hide the default title
    }

    public void setSpinnerAllignment(Spinner spinner)
    {
        ArrayAdapter<String> adapter;
        if(LANGUAGE.language.equals("EN"))
        {
            adapter = new ArrayAdapter<String>(this,
                    R.layout.custom_spinner_eng, month.getCategoriesNames());
            spinner.setAdapter(adapter);
        }
        else if(LANGUAGE.language.equals("HEB"))
        {
            adapter = new ArrayAdapter<String>(this,
                    R.layout.custom_spinner, month.getCategoriesNames());
            spinner.setAdapter(adapter);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addRow(LinearLayout ll, boolean isWithCloseBtn) {
        LLMain = (LinearLayout) findViewById(R.id.LLMainCreateBudget);
        if (LLMain.getChildCount() > 2) {
            LLMain.removeViewAt(LLMain.getChildCount() - 1);// Remove Close button
            LLMain.removeViewAt(LLMain.getChildCount() - 1);// Remove add button
        }
        LLMain.addView(ll);
        setAddAndDeleteButton();// Adding add button
        LinearLayout addButtonRowLL = (LinearLayout) LLMain.getChildAt(LLMain.getChildCount() - 1);
        if (isWithCloseBtn)
            setCloseButton();// Adding close button
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setAddAndDeleteButton() {
        final LinearLayout newll = new LinearLayout(Create_Budget_Activity.this);

        final ImageButton addRowButton = new ImageButton(this);
        final ImageButton deleteRowsButton = new ImageButton(this);
        final TextView emptyTV = new TextView(this);

        //Set default background color
        addRowButton.setBackgroundDrawable(dfaultBackground);
        deleteRowsButton.setBackgroundDrawable(dfaultBackground);

        //addRowButton.setTooltipText("הוסף שורה");
        //deleteRowsButton.setTooltipText("נקה");

        addRowButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                boolean isEmptyRowExists = false;
                for (int i = 2; i < LLMain.getChildCount() - 2; i++) {
                    LinearLayout rowLL = (LinearLayout) LLMain.getChildAt(i);
                    if(LANGUAGE.language.equals("EN"))
                        reverseLinearLayout(rowLL);
                    EditText categoryNameET = (EditText) rowLL.getChildAt(1);
                    EditText categoryValueET = (EditText) rowLL.getChildAt(2);
                    if(LANGUAGE.language.equals("EN"))
                        reverseLinearLayout(rowLL);
                    if (categoryNameET.getText().toString().equals("") ||
                            categoryValueET.getText().toString().equals("")) {
                        isEmptyRowExists = true;
                        break;
                    }
                }
                if (!isEmptyRowExists)
                    add_New_row(null, 0,false, null, 0);
            }
        });

        deleteRowsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LLMain.removeViews(2, LLMain.getChildCount() - 2);
/*                for (int i = 1; i < LLMain.getChildCount() - 2; i++)
                {
                    deleteSpecificRow(i);
                }*/
                add_New_row(null, 0,false, null, 0);
            }
        });
        deleteRowsButton.setImageDrawable(getResources().getDrawable(R.drawable.clean_screen));
        deleteRowsButton.setScaleType(ImageView.ScaleType.FIT_XY);

        addRowButton.setImageDrawable(getResources().getDrawable(R.drawable.add_button_md));
        //addRowButton.setBackground(null);
        //addRowButton.getLayoutParams().width = 40;
        //addRowButton.getLayoutParams().height = 40;
        addRowButton.setScaleType(ImageView.ScaleType.FIT_XY);


        addRowButton.setLayoutParams(new LinearLayout.LayoutParams(buttonSize, buttonSize));
        deleteRowsButton.setLayoutParams(new LinearLayout.LayoutParams(buttonSize, buttonSize));
        addRowButton.setAdjustViewBounds(true);
        deleteRowsButton.setAdjustViewBounds(true);

        int a = screenWidth - 2*buttonSize;
        emptyTV.setLayoutParams(new LinearLayout.LayoutParams(screenWidth - 2*buttonSize, buttonSize));
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(40, 40);

        //addRowButton.setPadding();
        newll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        newll.setOrientation(LinearLayout.HORIZONTAL);
        newll.addView(addRowButton);//,lp);
        newll.addView(deleteRowsButton);
        newll.addView(emptyTV);
        if(LANGUAGE.language.equals("EN"))
            setLanguageConf(newll);
        LLMain.addView(newll);
    }

    public void setCloseButton() {
        final Button closeButton = new Button(this);
        int size = (150 * buttonSize)/100;
        closeButton.setHeight(size);
        closeButton.setText(LANGUAGE.createButton);
        closeButton.setTextColor(Color.BLACK);
        closeButton.setTypeface(null, Typeface.BOLD);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View view) {
                setBudgets();
                int budgetNumber = monthlyBudgetDB.getMaxBudgetNumberBGT();
                ArrayList<Budget> newBudgets = getAddedCategories(budgetNumber);
                boolean isOriginContentBudgetChanged = isOriginBudgetChanged(budgetNumber);
                boolean isBudgetChange = isBudgetChange(budgetNumber);
                boolean isAddedBudgetsExists = newBudgets.size() > 0;
                if (!isInputValid || !isBudgetChange)
                    return;
                else if (allBudgets.size() == 0)// Nothing needed to do
                {
                    showMessageNoButton(LANGUAGE.pleaseInsertBudget);
                    return;
                }
/*                else if(!isBudgetChange)// Nothing needed to do
                {
                    //showMessageNoButton("אנא הזן תקציב!");
                    return;
                }*/
                else if(month == null)
                    questionTrueAnswer("CRT");// First time create budget
                else if(isOriginContentBudgetChanged)// ReWriting of monthly budget needed
                {
                    if (monthlyBudgetDB.checkCurrentRefMonthExists() )
                        showQuestion(LANGUAGE.createBudgetQuestion);
                    return;
                }
                else if(isAddedBudgetsExists)// Insert the added budgets needed only
                    questionTrueAnswer("ADD");// Values of old budget updated only
            }
        });

        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout newll = new LinearLayout(Create_Budget_Activity.this);

        newll.setOrientation(LinearLayout.HORIZONTAL);
        newll.addView(closeButton, lp);
        LLMain.addView(newll);
    }

    public void setBudgets() {
        allCategories.clear();
        allBudgets.clear();
        int priorityCat = 1;
        for (int i = 2; i < LLMain.getChildCount() - 2; i++) {
            EditText categoryET,valueET,shopET;
            CheckBox constPaymentCB;
            Spinner chargeDaySP;
            if(!LANGUAGE.language.equals("EN"))
            {
                categoryET = ((EditText) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(1));
                valueET = ((EditText) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(2));
                constPaymentCB = ((CheckBox) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(3));
                shopET = ((EditText) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(4));
                chargeDaySP = ((Spinner) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(5));
            }
            else {
                categoryET = ((EditText) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(4));
                valueET = ((EditText) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(3));
                constPaymentCB = ((CheckBox) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(2));
                shopET = ((EditText) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(1));
                chargeDaySP = ((Spinner) ((LinearLayout) LLMain.getChildAt(i)).getChildAt(0));
            }
            String category = categoryET.getText().toString().trim();
            String valueStr = valueET.getText().toString().trim();
            boolean constPayment = constPaymentCB.isChecked();
            String shop = shopET.getText().toString().trim();
            //String chargeDayStr = chargeDayET.getText().toString().trim();
            String chargeDayStr = chargeDaySP.getSelectedItem().toString().trim();
            int chargeDay = 0;

            if(!constPayment)
            {
                shopET.setText("");
                shop = null;
                chargeDayStr = "0";
//                chargeDayET.setText(chargeDayStr);
            }
            chargeDay = Integer.valueOf(chargeDayStr);

            if (valueStr.equals(""))
                valueStr = "0";
            int value = Integer.valueOf(valueStr);


            allCategories.add(category);
            //String categorySon = ((EditText)((LinearLayout)LLMain.getChildAt(i)).getChildAt(1)).getText().toString();
            verifyBudgetInput(categoryET, valueET, constPaymentCB, shopET, chargeDaySP);// chargeDayET);
            if (isInputValid)
                allBudgets.add(new Budget(priorityCat++,category,value,constPayment,shop,chargeDay));
            else
                return;
        }
    }

    public void verifyBudgetInput(EditText categoryET, EditText valueET, CheckBox constPaymentCB, EditText shopET,Spinner chargeDaySP ){//EditText chargeDayET) {
        isInputValid = true;
        String category = categoryET.getText().toString().trim();
        String valueStr = valueET.getText().toString().trim();
        boolean constPayment = constPaymentCB.isChecked();
        String shop = shopET.getText().toString().trim();
        //String chargeDayStr = chargeDayET.getText().toString().trim();
        String chargeDayStr = chargeDaySP.getSelectedItem().toString().trim();

        if (chargeDayStr.equals(""))
            chargeDayStr = "0";
        int chargeDay = Integer.valueOf(chargeDayStr);

        if (valueStr.equals(""))
            valueStr = "0";
        int value = Integer.valueOf(valueStr);

        //Check duplicate of category
        if (Collections.frequency(allCategories, category) > 1) {
            setErrorEditText(categoryET, LANGUAGE.duplicateCategory);
            isInputValid = false;
        }

        //Check illegal characters
        if (category.contains(SEPARATOR)) {
            setErrorEditText(categoryET, LANGUAGE.illegalCharacter);
            isInputValid = false;
        }
        //Check illegal category
        if (category.length() == 0) {
            setErrorEditText(categoryET, LANGUAGE.pleaseInsertCategory);
            isInputValid = false;
        }
        //Check illegal value
        if (value == 0) {
            setErrorEditText(valueET, LANGUAGE.pleaseInsertValue);
            isInputValid = false;
        }

/*        if(constPayment && shop.length() == 0 && chargeDayStr.length()  == 0)
        {
            setErrorEditText(shopET, "נא להזין חנות!");
            setErrorEditText(chargeDayET, "נא להזין יום לחיוב!");
            isInputValid = false;
        }*/

        if(constPayment && shop.length() == 0)
        {
            setErrorEditText(shopET, LANGUAGE.pleaseInsertShop);
            isInputValid = false;
        }

/*        if(constPayment && chargeDay == 0)
        {
            setErrorEditText(chargeDayET, "נא להזין יום לחיוב!");
            isInputValid = false;
        }*/

/*        if(constPayment && chargeDay > 31)
        {
            setErrorEditText(chargeDayET, "נא להזין יום חוקי לחיוב!");
            isInputValid = false;
        }*/

        //Check illegal characters
        if (shop.contains(SEPARATOR)) {
            setErrorEditText(shopET, LANGUAGE.illegalCharacter);
            isInputValid = false;
        }

        //allCategories.add(budget.getCategory());
        // Need to reduce duplicates categories by define set of categories
        if (!isInputValid)
            return;
    }

    public void setErrorEditText(EditText et, String errorMesage)
    {
        et.setError(errorMesage);
    }

    public void showQuestion(String message)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        questionTrueAnswer("DEL");
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        questionFalseAnswer();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton(LANGUAGE.yes, dialogClickListener)
                .setNegativeButton(LANGUAGE.no, dialogClickListener).show();
    }

    public void showMessageNoButton(String message)//View view)
    {
        final AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage(message);//.create()
        myAlert.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                myAlert.create().dismiss();
                //finish();
            }}, 1000); // 1000 milliseconds delay
    }

    public void showMessageToast(String message, boolean isFinishNeeded)//View view)
    {
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
        if(isFinishNeeded)
            finish();
/*        TextView msg = new TextView(this);
        // You Can Customise your Title here
        msg.setText(message);
        //title.setBackgroundColor(Color.DKGRAY);
        msg.setPadding(10, 10, 10, 10);
        msg.setGravity(Gravity.CENTER);
        msg.setTextColor(Color.BLACK);
        msg.setTextSize(20);
        myAlert = new AlertDialog.Builder(this);
        myAlert.setView(msg);//.create()
        myAlert.show();
        Handler handler = new Handler();
        if(isFinishNeeded)
            handler.postDelayed(new Runnable() {
                public void run() {
                    myAlert.create().dismiss();
                    finish();
                }
            }, 1000); // 1000 milliseconds delay
        else
        handler.postDelayed(new Runnable() {
            public void run() {
                myAlert.create().dismiss();
                //finish();
            }
        }, 1000); // 1000 milliseconds delay*/

    }

    public void writeBudget(long budgetNumber)
    {
        long status = 0;
        int categoryID = 0;
        int subCategoryID = 0;
        for (Budget bgt:allBudgets)
        {
            categoryID = monthlyBudgetDB.getCategoryId(bgt.getCategory());
            if(categoryID == -1) {
                status = monthlyBudgetDB.insertCategoryData(bgt.getCategory());
                categoryID = monthlyBudgetDB.getCategoryId(bgt.getCategory());
            }
            subCategoryID = monthlyBudgetDB.getSubCategoryId(categoryID,bgt.getCategorySon());
            if(subCategoryID == -1) {
                status = monthlyBudgetDB.insertSubCategoryData(categoryID, bgt.getCategorySon());
                subCategoryID = monthlyBudgetDB.getSubCategoryId(categoryID, bgt.getCategorySon());
            }
            int catPriority = bgt.getCatPriority();
            status = monthlyBudgetDB.insertBudgetTableData(budgetNumber, categoryID, subCategoryID,catPriority, bgt.getValue(), bgt.isConstPayment(), bgt.getShop(), bgt.getChargeDay());
        }
    }

    public boolean isOriginBudgetChanged(int budgetNumber)
    {
        ArrayList<Budget> oldBudget = monthlyBudgetDB.getBudgetDataFromDB(budgetNumber);
        int counter = 0;
        for (Budget oldBgt:oldBudget)
            for (Budget bgt:allBudgets)
                if(oldBgt.equals(bgt))
                {
                    counter++;
                    break;
                }
        return counter != oldBudget.size();
    }

    public boolean isBudgetChange(int budgetNumber)
    {
        ArrayList<Budget> oldBudget = monthlyBudgetDB.getBudgetDataFromDB(budgetNumber);
        boolean isBudgetsEquals = false;
        for (Budget bgt:allBudgets)
        {
            for (Budget oldBgt:oldBudget)
            {
                if(bgt.equals(oldBgt))
                {
                    isBudgetsEquals = true;
                    break;
                }
            }
            if(isBudgetsEquals == false)
                return true;
            isBudgetsEquals = false;
        }
        return allBudgets.size() != oldBudget.size();
    }

    public ArrayList<Budget> getAddedCategories(int budgetNumber)
    {
        ArrayList<Budget> oldBudget = monthlyBudgetDB.getBudgetDataFromDB(budgetNumber);
        ArrayList<Budget> addedBudgets = new ArrayList<>();
        boolean isBudgetExists = false;
        for (Budget bgt:allBudgets)
        {
            for (Budget oldBgt:oldBudget)
            {
                if(bgt.equals(oldBgt))
                {
                    isBudgetExists = true;
                    continue;
                }
            }
            if(isBudgetExists == false)
                addedBudgets.add(bgt);
            isBudgetExists = false;
        }
        return addedBudgets;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addCategoriesToMonthlyBudget(ArrayList<Budget> catAdd, int budgetNumber)
    {
        Date startCurrentMonth = getDateStartMonth();
        monthlyBudgetDB.insertAddedCategoriesToMBFromBudget(startCurrentMonth,catAdd);
        monthlyBudgetDB.updateBudgetNumberMB(startCurrentMonth,budgetNumber);
        int maxIDPerMonth = monthlyBudgetDB.getMaxIDPerMonthTRN(month.getMonth());
        month.initCategories();
        setFrqTrans(catAdd,maxIDPerMonth);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void questionTrueAnswer(String operation)
    {
        int budgetNumber = monthlyBudgetDB.getMaxBudgetNumberBGT() + 1;
        writeBudget(budgetNumber);
        ArrayList<Budget> addedBudget = new ArrayList<>();
        if(!operation.equals("CRT"))
            addedBudget = getAddedCategories(budgetNumber-1);
        if(operation.equals("ADD"))
            addCategoriesToMonthlyBudget(addedBudget,budgetNumber);
        else if(operation.equals("DEL"))
            monthlyBudgetDB.deleteDataRefMonth(getDateStartMonth());
        else if(operation.equals("CRT"))
            ; // Delete or add not needed
        //deleteCurrentMonth();
        month = null;
        showMessageToast(LANGUAGE.budgetCreatedSuccessfully,true);
    }

    private void questionFalseAnswer()
    {
    }

/*    public void writeBudget(ArrayList<String> lines, String fileName, String dirPath) {
        // Writing the categories and budget values to file
        writeToFile(lines, fileName, dirPath);
    }*/

    public void setButtonsNames()
    {
        // CreateBudget window buttons
        ((TextView)findViewById(R.id.createBudgetLabel)).setText(LANGUAGE.createBudgetName);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_budget);
        setTitle();
        dfaultBackground = new View(this).getBackground();
        setButtonsNames();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//Rotate the screen to to be on portrait moade only
        display = getWindowManager().getDefaultDisplay();
        screenWidth = display.getWidth();

        addCategoryButton = new Button(Create_Budget_Activity.this);
        OKButton = new Button(Create_Budget_Activity.this);
        newll = new LinearLayout(Create_Budget_Activity.this);

        allBudgets = new ArrayList<>();
        allCategories = new ArrayList<>();
        //setAddButton();
        setTitleRow();
        setBudgetGui();

        if(LANGUAGE.language.equals("EN"))
        {
/*            for (int i=1;i<LLMain.getChildCount() - 1;i++)
            {
                //LinearLayout currentLL = (LinearLayout) LLMain.getChildAt(i);
                //for (int j = 0; j < currentLL.getChildCount(); j++)

                setLanguageConf((LinearLayout) LLMain.getChildAt(i));
            }*/
        int lastRowIndex = LLMain.getChildCount() - 2;
        setLanguageConf((LinearLayout) LLMain.getChildAt(1));
        //setLanguageConf((LinearLayout) LLMain.getChildAt(lastRowIndex));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setTitleRow()
    {
        final LinearLayout titleLL = new LinearLayout(Create_Budget_Activity.this);
        TextView emptyTV, categoryNameTV, categoryValueTV, constPaymentTV, shopTV, payDateTV;

        emptyTV = new TextView(Create_Budget_Activity.this);
        categoryNameTV = new TextView(Create_Budget_Activity.this);
        categoryValueTV = new TextView(Create_Budget_Activity.this);
        constPaymentTV = new TextView(Create_Budget_Activity.this);
        shopTV = new TextView(Create_Budget_Activity.this);
        payDateTV = new TextView(Create_Budget_Activity.this);

        emptyTV.setText("");
        categoryNameTV.setText(getWordCapitalLetter(LANGUAGE.categoryName));
        categoryValueTV.setText(getWordCapitalLetter(LANGUAGE.budgetName));
        constPaymentTV.setText(getWordCapitalLetter(LANGUAGE.constantDate));
        shopTV.setText(getWordCapitalLetter(LANGUAGE.shopName));
        payDateTV.setText(getWordCapitalLetter(LANGUAGE.chargeDay));

        //int screenHeight = display.getHeight();
        //categoryNameLabel.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4 , ViewGroup.LayoutParams.WRAP_CONTENT));
        //categoryFamilyEditText.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 3 ,ViewGroup.LayoutParams.WRAP_CONTENT));
        //categoryValueLabel.setLayoutParams(new LinearLayout.LayoutParams(screenWidth / 4 ,ViewGroup.LayoutParams.WRAP_CONTENT));

        emptyTV.setLayoutParams(new LinearLayout.LayoutParams(buttonSize, ViewGroup.LayoutParams.WRAP_CONTENT));
        categoryNameTV.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 27/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        categoryValueTV.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 17/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        constPaymentTV.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 12/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        shopTV.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 22/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        payDateTV.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 22/100, ViewGroup.LayoutParams.WRAP_CONTENT));


        //categoryValueEditText.setTextSize(18);
        //LinearLayout.LayoutParams lp =  new LinearLayout.LayoutParams(screenWidth / 4,categoryNameEditText.getHeight());

        emptyTV.setTextSize(15);
        categoryNameTV.setTextSize(15);
        categoryValueTV.setTextSize(15);
        constPaymentTV.setTextSize(15);
        shopTV.setTextSize(15);
        payDateTV.setTextSize(15);

        setHeaderProperties(emptyTV);
        setHeaderProperties(categoryNameTV);
        setHeaderProperties(categoryValueTV);
        setHeaderProperties(constPaymentTV);
        setHeaderProperties(shopTV);
        setHeaderProperties(payDateTV);

        titleLL.addView(emptyTV);
        titleLL.addView(categoryNameTV);
        titleLL.addView(categoryValueTV);
        titleLL.addView(constPaymentTV);
        titleLL.addView(shopTV);
        titleLL.addView(payDateTV);

        newll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        newll.setOrientation(LinearLayout.HORIZONTAL);

        //LinearLayout llMain = (LinearLayout)findViewById(R.id.LLMainCreateBudget);
        LLMain = (LinearLayout) findViewById(R.id.LLMainCreateBudget);
        LLMain.addView(titleLL);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setBudgetGui()
    {
        allBudgets = monthlyBudgetDB.getBudgetDataFromDB(monthlyBudgetDB.getMaxBudgetNumberBGT());
        if(allBudgets == null || allBudgets.size() == 0)
        {
            add_New_row(null, 0, false, null, 0);
            allBudgets = new ArrayList<>();
        }
        else
            for(Budget budget:allBudgets)
            {
                add_New_row(budget.getCategory(), budget.getValue(), budget.isConstPayment(), budget.getShop(), budget.getChargeDay());
                setCloseButton();// Adding close button
            }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void add_New_row(String categoryName, int categoryValue, boolean isConstPayment, String shop, int chargeDay ) {
        boolean isEmptyRow = (categoryName == null && categoryValue == 0 && isConstPayment == false && shop == null && chargeDay == 0);
        final LinearLayout newll = new LinearLayout(Create_Budget_Activity.this);
        final EditText categoryNameET, categoryValueET;
        final EditText shopET;
        //final EditText chargeDayET;
        final Spinner optionalDaysSpinner;
        CheckBox constPaymentCB;

        categoryNameET = new EditText(Create_Budget_Activity.this);
        categoryValueET = new EditText(Create_Budget_Activity.this);
        constPaymentCB = new CheckBox(Create_Budget_Activity.this);
        shopET = new EditText(Create_Budget_Activity.this);
        //chargeDayET = new EditText(Create_Budget_Activity.this);
        optionalDaysSpinner = new Spinner(Create_Budget_Activity.this);
        setSpinnerOptionalDays(optionalDaysSpinner);

        categoryNameET.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 27/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        categoryValueET.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 14/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        constPaymentCB.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 14/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        shopET.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 23/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        //chargeDayET.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 11/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        optionalDaysSpinner.setLayoutParams(new LinearLayout.LayoutParams((screenWidth - buttonSize ) * 22/100, ViewGroup.LayoutParams.WRAP_CONTENT));
        constPaymentCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    shopET.setVisibility(View.VISIBLE);
                    //chargeDayET.setVisibility(View.VISIBLE);
                    optionalDaysSpinner.setVisibility(View.VISIBLE);
                }
                else
                {
                    shopET.setVisibility(View.INVISIBLE);
                    //chargeDayET.setVisibility(View.INVISIBLE);
                    optionalDaysSpinner.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (!isEmptyRow)
            categoryNameET.setText(categoryName);
        //EditText categoryFamilyEditText = new EditText(Create_Budget_Activity.this);
        if (!isEmptyRow)
            categoryValueET.setText(String.valueOf(categoryValue));

        constPaymentCB.setChecked(isConstPayment);

        if (!isEmptyRow)
        {
            shopET.setText(shop);
            //chargeDayET.setText(String.valueOf(chargeDay));
            optionalDaysSpinner.setSelection(chargeDay - 1);
        }

        categoryNameET.requestFocus();
        categoryNameET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        categoryValueET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        constPaymentCB.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        shopET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        //chargeDayET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        categoryNameET.setTextSize(12);
        categoryValueET.setTextSize(12);
        constPaymentCB.setTextSize(12);
        shopET.setTextSize(12);
        //chargeDayET.setTextSize(12);

        newll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        newll.setOrientation(LinearLayout.HORIZONTAL);

        optionalDaysSpinner.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(categoryNameET.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(categoryValueET.getWindowToken(), 0);
                //imm.hideSoftInputFromWindow(constPaymentCB.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(shopET.getWindowToken(), 0);
                return false;
            }
        }) ;

        final ImageButton deleteRowButton = new ImageButton(this);
        //deleteRowButton.setTooltipText("מחק שורה");

        deleteRowButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                LLMain.removeView(newll);
                //for (int i = 1; i < LLMain.getChildCount() - 2; i++)
                //deleteSpecificRow(i);
                if (LLMain.getChildCount() == 4)
                    add_New_row(null, 0,false, null, 0);
            }
        });
        deleteRowButton.setImageDrawable(getResources().getDrawable(R.drawable.delete_icon));
        deleteRowButton.setBackgroundDrawable(dfaultBackground);
        //deleteRowButton.setBackgroundColor(Color.parseColor("#FAFAFA"));View v;v.getBackground().deleteRowButton.setScaleType(ImageView.ScaleType.FIT_XY);
        deleteRowButton.setLayoutParams(new LinearLayout.LayoutParams(buttonSize, buttonSize));
        deleteRowButton.setAdjustViewBounds(true);

//        if(language.language.equals("HEB")) {

            newll.addView(deleteRowButton);

            if (!constPaymentCB.isChecked()) {
                shopET.setVisibility(View.INVISIBLE);
                //chargeDayET.setVisibility(View.INVISIBLE);
                optionalDaysSpinner.setVisibility(View.INVISIBLE);
            }

            newll.addView(categoryNameET);
            newll.addView(categoryValueET);
            newll.addView(constPaymentCB);
            newll.addView(shopET);
            //newll.addView(chargeDayET);
            newll.addView(optionalDaysSpinner);
        if(LANGUAGE.language.equals("EN"))
            setLanguageConf(newll);
//        }
/*        else if(language.language.equals("EN"))
        {
            newll.addView(optionalDaysSpinner);
            //newll.addView(chargeDayET);
            if (!constPaymentCB.isChecked()) {
                shopET.setVisibility(View.INVISIBLE);
                //chargeDayET.setVisibility(View.INVISIBLE);
                optionalDaysSpinner.setVisibility(View.INVISIBLE);
            }
            newll.addView(shopET);
            newll.addView(constPaymentCB);
            newll.addView(categoryValueET);
            newll.addView(categoryNameET);


            newll.addView(deleteRowButton);

        }*/
        boolean isWithCloseBtn = isEmptyRow;
        addRow(newll, isWithCloseBtn);
    }

        public void setSpinnerOptionalDays(Spinner OptionalDaysSP)
    {
        ArrayList<String> daysInMonth = new ArrayList<>();
        int i = 1;
        while(i <= 31 )
            daysInMonth.add(String.valueOf(i++));
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_spinner,daysInMonth );
        if(LANGUAGE.language.equals("HEB"))
        adapter = new ArrayAdapter<String>(this,
                R.layout.custom_spinner,daysInMonth );
        else if(LANGUAGE.language.equals("EN"))
            adapter = new ArrayAdapter<String>(this,
                    R.layout.custom_spinner_eng,daysInMonth );
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        OptionalDaysSP.setAdapter(adapter);
        OptionalDaysSP.setSelection(1,true);
    }


    public void deleteSpecificRow(int index) {
        LLMain.removeViewAt(index);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setDeleteButton() {
        final ImageButton deleteRowButton = new ImageButton(this);

        deleteRowButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        });
        deleteRowButton.setImageDrawable(getResources().getDrawable(R.drawable.delete_icon));
        //addRowButton.setBackground(null);
        //addRowButton.getLayoutParams().width = 40;
        //addRowButton.getLayoutParams().height = 40;
        deleteRowButton.setScaleType(ImageView.ScaleType.FIT_XY);


        deleteRowButton.setLayoutParams(new LinearLayout.LayoutParams(buttonSize, buttonSize));
        deleteRowButton.setAdjustViewBounds(true);
        //LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(40, 40);
        LinearLayout newll = new LinearLayout(Create_Budget_Activity.this);

        //addRowButton.setPadding();

        newll.setOrientation(LinearLayout.HORIZONTAL);
        newll.addView(deleteRowButton);//,lp);
        LLMain.addView(newll);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (myAlert != null)
            myAlert.create().dismiss();
    }

}
