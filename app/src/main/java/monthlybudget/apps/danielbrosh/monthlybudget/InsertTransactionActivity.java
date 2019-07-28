package monthlybudget.apps.danielbrosh.monthlybudget;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static monthlybudget.apps.danielbrosh.monthlybudget.MainActivity.month;
import static monthlybudget.apps.danielbrosh.monthlybudget.MainActivity.monthlyBudgetDB;
import static monthlybudget.apps.danielbrosh.monthlybudget.R.id.paymentMethodSpinner;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.IS_AD_ENEABLED;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.LANGUAGE;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.TRAN_ID_PER_MONTH_NUMERATOR;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.dateFormat;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getSentenceCapitalLetter;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.getYearMonth;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.preferencePath;
import static monthlybudget.apps.danielbrosh.monthlybudget.global.shopsSet;

public class InsertTransactionActivity extends AppCompatActivity
{
    final SharedPreferences sharedpreference = this.getSharedPreferences(
            preferencePath, Context.MODE_PRIVATE);
    Spinner categoriesSpinner;
    Spinner paymentTypeSpinner;
    Button btnSendTransaction;
    EditText payDateEditText;
//    Button btnClose;

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

    public void setSpinnersAllignment()
    {
        ArrayAdapter<String> adapter = null;
        if(LANGUAGE.language.equals("EN"))
            adapter = new ArrayAdapter<String>(this,
                    R.layout.custom_spinner_eng, month.getCategoriesNames());
        else if(LANGUAGE.language.equals("HEB"))
            adapter = new ArrayAdapter<String>(this,
                    R.layout.custom_spinner, month.getCategoriesNames());
        categoriesSpinner.setAdapter(adapter);
        paymentTypeSpinner.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void init()
    {
        setSpinnersAllignment();

        if(shopsSet.size() == 0) {
            ArrayList<String> shops = monthlyBudgetDB.getAllShops();
            shopsSet.addAll(shops);
        }
        ArrayList<String> shopsList = new ArrayList<String>(shopsSet);
        AutoCompleteTextView shposAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.shopAutoCompleteTextView);
        ArrayAdapter<String> anotherAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, shopsList);
        shposAutoCompleteTextView.setAdapter(anotherAdapter);
        shposAutoCompleteTextView.setThreshold(2);// Set auto complete from the first character
    }

    public String getCurrentDate()
    {
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear=mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay= mcurrentDate.get(Calendar.DAY_OF_MONTH);

        String day,month;
        if(mDay < 10)
            day = "0" + mDay;
        else
            day = String.valueOf(mDay);
        if( (mMonth + 1) < 10 )
            month = "0" + (int)(mMonth + 1) ;
        else
            month = String.valueOf(mMonth + 1);
        return (day + "/" + month + "/" +  mYear);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setButtonsNames()
    {
        // Main window buttons
        ((TextView)findViewById(R.id.chooseCategoryLabel)).setText(getSentenceCapitalLetter(LANGUAGE.categoryName,'.'));
        ((TextView)findViewById(R.id.choosePaymenMethodtLabel)).setText(getSentenceCapitalLetter(LANGUAGE.paymentMethodName,'.'));
        ((TextView)findViewById(R.id.shopLabel)).setText(getSentenceCapitalLetter(LANGUAGE.shopName,'.'));
        ((TextView)findViewById(R.id.payDateLabel)).setText(getSentenceCapitalLetter(LANGUAGE.chargeDateName,'.'));
        ((TextView)findViewById(R.id.transactionPriceLabel)).setText(getSentenceCapitalLetter(LANGUAGE.transactionPrice,'.'));
        ((TextView)findViewById(R.id.sendTransactionButton)).setText(getSentenceCapitalLetter(LANGUAGE.insert,'.'));
        ((TextView)findViewById(R.id.inserTransactionLabel)).setText(getSentenceCapitalLetter(LANGUAGE.insertTransactionButtonName,'.'));

        if(LANGUAGE.language.equals("EN")) {
            LinearLayout llMain = (LinearLayout) findViewById(R.id.LLMainInsertTranssaction);
            for (int i = 1; i < llMain.getChildCount() - 1; i++) {
                LinearLayout currentLL = (LinearLayout) llMain.getChildAt(i);
                //llMain.removeViewAt(i);
                View v1 = currentLL.getChildAt(0);
                View v2 = currentLL.getChildAt(1);
                int v1ID = v1.getId(), v2ID = v2.getId();
                currentLL.removeAllViews();
                currentLL.addView(v2);
                currentLL.addView(v1);
                //llMain.addView(currentLL,i);
            }
            TextView categoryTV = ((TextView)findViewById(R.id.chooseCategoryLabel));
            TextView paymentmethodTV = ((TextView) findViewById(R.id.choosePaymenMethodtLabel));
            TextView shopTV = ((TextView) findViewById(R.id.shopLabel));
            TextView payDateTV = ((TextView) findViewById(R.id.payDateLabel));
            TextView priceTV = ((TextView) findViewById(R.id.transactionPriceLabel));
            TextView sendTransactionTV = ((TextView) findViewById(R.id.sendTransactionButton));

            Spinner categorySpinner = ((Spinner)findViewById(R.id.categorySpinner));
            Spinner paymentMethodSpinner = ((Spinner) findViewById(R.id.paymentMethodSpinner));
            EditText shopET = ((EditText) findViewById(R.id.shopAutoCompleteTextView));
            EditText payDateET = ((EditText) findViewById(R.id.payDatePlainText));
            EditText priceET = ((EditText) findViewById(R.id.transactionPricePlainText));

            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<String>(this,
                    R.layout.custom_spinner_eng, month.getCategoriesNames());
            adapter.setDropDownViewResource(R.layout.custom_spinner_eng);

            categorySpinner.setAdapter(adapter);
/*            categoryTV.setTextDirection(View.TEXT_DIRECTION_LTR);
            paymentmethodTV.setTextDirection(View.TEXT_DIRECTION_LTR);
            shopTV.setTextDirection(View.TEXT_DIRECTION_LTR);
            payDateTV.setTextDirection(View.TEXT_DIRECTION_LTR);
            priceTV.setTextDirection(View.TEXT_DIRECTION_LTR);
            sendTransactionTV.setTextDirection(View.TEXT_DIRECTION_LTR);*/

            categorySpinner.setTextDirection(View.LAYOUT_DIRECTION_LTR);
            paymentMethodSpinner.setTextDirection(View.TEXT_DIRECTION_LTR);
            shopET.setTextDirection(View.TEXT_DIRECTION_LTR);
            payDateET.setTextDirection(View.TEXT_DIRECTION_LTR);
            priceET.setTextDirection(View.TEXT_DIRECTION_LTR);

            categoryTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            paymentmethodTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            shopTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            payDateTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            priceTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            //sendTransactionTV.setTextDirection(View.TEXT_DIRECTION_LTR);
            //TextView balanceTV = ((TextView) findViewById(R.id.inserTransactionLabel));

            //categorySpinner.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            //paymentMethodSpinner.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            shopET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            payDateET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            priceET.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }

        if(month != null)
            setTitle(getYearMonth(month.getMonth(), '.'));
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_transaction);

        //sharedpreference= PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        setButtonsNames();
        setTitle( getYearMonth(month.getMonth(),'.'));
        categoriesSpinner =  (Spinner) findViewById(R.id.categorySpinner);
        paymentTypeSpinner =  (Spinner) findViewById(paymentMethodSpinner);
        btnSendTransaction = (Button) findViewById(R.id.sendTransactionButton);
        //btnClose = (Button) findViewById(R.id.closeInsertTransactionButton);
        init();

        if(month.getTransChanged()) {
            int maxIDPerMonth = monthlyBudgetDB.getMaxIDPerMonthTRN(month.getMonth());
            month.updateMonthData(maxIDPerMonth + 1);
            month.setTransChanged(false);
        }

        payDateEditText = (EditText) findViewById(R.id.payDatePlainText);
        payDateEditText.setText(getCurrentDate());

        //payDateEditText.setEnabled(false);

        payDateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //To show current date in the datepicker
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                Calendar mcurrentDate=Calendar.getInstance();
                int mYear=mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay= mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(InsertTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                     String day,month;
                    if(selectedday < 10)
                        day = "0" + selectedday;
                    else
                        day = String.valueOf(selectedday);
                     if( (selectedmonth + 1) < 10 )
                        month = "0" + (int)(selectedmonth + 1) ;
                     else
                         month = String.valueOf(selectedmonth + 1);
                     payDateEditText.setText(day + "/" + month + "/" +  selectedyear);
                     payDateEditText.setError(null);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle(LANGUAGE.selectingDate);
                mDatePicker.show();  }
        });

        btnSendTransaction.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                EditText shopET = ((EditText) findViewById(R.id.shopAutoCompleteTextView));
                EditText payDateET = ((EditText) findViewById(R.id.payDatePlainText));
                EditText transactionPriceET = ((EditText) findViewById(R.id.transactionPricePlainText));
                if( setErrorEditText(payDateET) || setErrorEditText(transactionPriceET) )
                    return;
                //Insert data
                String categoryName = categoriesSpinner.getSelectedItem().toString();

                String paymentMethod = paymentTypeSpinner.getSelectedItem().toString();
                //String category = getCategoryName(categoryHeb);
                String shop = shopET.getText().toString();
                Date payDate = global.convertStringToDate( payDateET.getText().toString(), dateFormat);
                double transactionPrice = Double.valueOf(transactionPriceET.getText().toString());
                if(isDisableADTransaction(shop,transactionPrice))
                    turnOffAD();

                else if(isEnableADTransaction(shop,transactionPrice))
                    turnOnAD();

                int idPerMonth = monthlyBudgetDB.getMaxIDPerMonthTRN(month.getMonth()) + 1;

                Transaction transaction = new Transaction( TRAN_ID_PER_MONTH_NUMERATOR++, categoryName,paymentMethod , shop, payDate, transactionPrice, new Date());
                boolean isStorno = false;
                int stornoOf = -1;

                for (Category cat : month.getCategories())
                    if (categoryName.equals(cat.getName()))
                    {
                        cat.subValBalance(transactionPrice);
                        ArrayList<Transaction> catTrans =  cat.getTransactions();
                        for (Transaction tran : catTrans)
                        {
                            isStorno = tran.isStorno(transaction);
                            if(isStorno == true)
                            {
                                stornoOf = tran.getID();
                                tran.setIsStorno(true);
                                tran.setStornoOf(transaction.getID());
                                break;
                            }
                        }
                        transaction.setIsStorno(isStorno);
                        transaction.setStornoOf(stornoOf);
                        cat.addTransaction(transaction);
                        break;
                    }
                month.setTransChanged(true);
                shopsSet.add(shop);

                //Maybe will do problems
                month.getTransactions().add(transaction);

                // send message and close window
                //showMessage("העסקה הוכנסה בהצלחה!");
                showMessageNoButton(LANGUAGE.transactionInsertedSuccessfully);

                //finish();
            }
        });
    }

    private boolean isDisableADTransaction(String val1, double val2){
        return val1.equals("TurnOffAd6542") && val2 == 7531;
    }

    private boolean isEnableADTransaction(String val1, double val2){
        return val1.equals("TurnOnAd6542") && val2 == 1357;
    }

    private void turnOnAD(){
        sharedpreference.edit().putBoolean("isAdEnable",true).apply();
        sharedpreference.edit().commit();
        IS_AD_ENEABLED = true;
        finish();
        return;
    }

    private void turnOffAD(){
        sharedpreference.edit().putBoolean("isAdEnable",false).apply();
        sharedpreference.edit().commit();
        IS_AD_ENEABLED = false;
        finish();
        return;
    }

    public boolean setErrorEditText(EditText et) {
        if( et.length() == 0 ) {
            et.setError(LANGUAGE.requiredField);
            return true;
        }
        return false;
    }

    public void showMessageNoButton(String message){//View view)
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
        finish();
    }

    public void showMessage(String message){//View view)
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage(message).setPositiveButton(LANGUAGE.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }).setTitle(LANGUAGE.messageName)
                .create();
        myAlert.show();
    }
}