package monthlybudget.apps.danielbrosh.monthlybudget;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
//import android.support.annotation.RequiresApi;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import static monthlybudget.apps.danielbrosh.monthlybudget.MainActivity.month;

/**
 * Created by daniel.brosh on 7/23/2017.
 */

public class global {
    public static TextView budget;
    public static ArrayList<String> catPaymentMethodArray = new ArrayList<String>();
    public static final String dateFormat = "dd/MM/yyyy";
    public static final String dateFormat2 =  "EEE MMM dd HH:mm:ss zzz yyyy";
    //public static String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString();
    public static int TRAN_ID_PER_MONTH_NUMERATOR = 1;
    public static String DCIM = "";
    public static String PROJECT_PATH = "";
    public static final String DB_FOLDER_PATH = "/data/data/monthlybudget.apps.danielbrosh.monthlybudget/databases";
    public static final String preferencePath = "monthlybudget.apps.danielbrosh.monthlybudget";
    public static final String DB_FILE_NAME = "MonthlyBudget";
    public static final String FILE_NAME = "Monthly Budget";
    public static final String FILE_NAME_BUDGET = "Budget";
    public static final String FILE_NAME_ORG = "Monthly Budget ORG";
    public static final String TXT_SUFFIX = "txt";
    public static final String DB_SUFFIX = "db";
    public static final String SEPARATOR = "-->";
    //public static long TRANSACTIONS_ID_NUMERATOR = 0;
    public static final char DOWN_ARROW = 'ꜜ';
    public static final char UP_ARROW = 'ꜛ';
    public static boolean IS_MONTH_CHANGABLE = false;
    public static Set<String> shopsSet = new TreeSet<String>();

    public static boolean isFirstTime = true;

    public static Thread closeActivity;

    public static Language LANGUAGE;
    public static String DEFAULT_LANGUAGE;
    public static boolean IS_AD_ENEABLED = true;
    public static ArrayList<String> LOG_REPORT = new ArrayList<String>();

    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void setHeaderProperties(TextView tv) {
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(15);
        tv.setClickable(true);
        Linkify.addLinks(tv,Linkify.ALL);
    }

    public static String getSentenceCapitalLetter(String sentence,char separator) {
        if(sentence.indexOf(separator) == -1)
            return getWordCapitalLetter(sentence);
        return new String(getWordCapitalLetter(sentence.substring(0,sentence.indexOf(separator)+1)) + getSentenceCapitalLetter(sentence.substring(sentence.indexOf(separator)+1),separator));
    }

    public static String getWordCapitalLetter(String word) {
        char firstLetter = word.charAt(0);
        if(firstLetter >= 97 && firstLetter <= 122)
            firstLetter -= 32;
        return new String(firstLetter + word.substring(1));
    }

    public static void reverseLinearLayout(LinearLayout linearLayout) {
        for(int i = linearLayout.getChildCount()-1 ; i >= 0 ; i--)
        {
            View item = linearLayout.getChildAt(i);
            linearLayout.removeViewAt(i);
            linearLayout.addView(item);
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void setLanguageConf(LinearLayout l) {
        for (int i = 0;i < l.getChildCount();i++) {
            View v = l.getChildAt(i);
            v.setTextDirection(View.TEXT_DIRECTION_LTR);
            v.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }
        reverseLinearLayout(l);
    }

    public static String wrapStrForDb(String str) {
        return "'" + str + "'";
    }

    public static String getYearMonth(Date date,char separator) {
        int month = date.getMonth() + 1;
        String monthStr = String.valueOf(month);
        if(month < 10)
            monthStr = "0" + month;

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        String yearStr = String.valueOf(year);

        return yearStr + separator + monthStr;
    }

    public static Date getTodayDate() {
        Calendar c = Calendar.getInstance();

// set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getDateStartMonth() {
        Calendar c = Calendar.getInstance();

        // set the calendar to start of today
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static void writeToFile(ArrayList<String> data, String fileName, String dirPath) {
        if(data == null || data.size() == 0)
            return;
        final File path = new File(dirPath);

        // Make sure the path directory exists.
        if(!path.exists()) {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        final File file = new File(path, fileName + "." + TXT_SUFFIX);
        FileOutputStream fOut = null;
        OutputStreamWriter myOutWriter = null;
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file,false);
            myOutWriter = new OutputStreamWriter(fOut);

            for (String line:data)
                myOutWriter.write(line + "\n");
        }

        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        finally {
            if(myOutWriter != null && fOut != null) {
                try {
                    myOutWriter.close();
                    fOut.flush();
                    fOut.close();
                }
                catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }
        }
    }

    public static Date getSartOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH,1);
        return c.getTime();
    }

    public static ArrayList<String> getAllMonths() {
        String today = convertDateToString(getTodayDate(),dateFormat);
        today  = today.replace('/','.');
        today = today.substring(today.indexOf(".")+ 1);
        ArrayList<String> allMonths = new ArrayList<String>();
        //allMonths.add(today);
        File root = new File(global.PROJECT_PATH);

        for (File file : root.listFiles())
            if (file.isDirectory())
                allMonths.add(file.getName().replace('-','.'));
        //Collections.sort(allMonths,Collections.reverseOrder(COMPARE_String));
        return allMonths;

    }

    public static void setCatPaymentMethodArray() {
        catPaymentMethodArray.clear();
        catPaymentMethodArray.add(LANGUAGE.creditCardName);
        catPaymentMethodArray.add(LANGUAGE.cashName);
        catPaymentMethodArray.add(LANGUAGE.checkName);
        catPaymentMethodArray.add(LANGUAGE.bankTransferName);
    }

    public  static void strikeThroughText(TextView price) {
        price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void deleteFiles(String path) {
        File dir = new File(path);
        if (dir.isDirectory())
            for (File child : dir.listFiles())
                deleteFiles(child.getPath());
        dir.delete();
    }

    public static void setDelay(final long seconds) {
        closeActivity = new Thread(new Runnable() {
            @Override
            public void run () {
                try {
                    Thread.sleep(seconds * 1000);
                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
            }
        });
        closeActivity.run();
    }

    //  Input: String with date include day
    public static String reverseDateString(String date, String separator) {
        String[] l = date.split(separator);
        if(separator == "\\.")
            separator = ".";
        return l[2] + separator + l[1] + separator + l[0];
    }

    public static Date convertStringToDate(String stringDate, String format){ // "dd/MM/yyyy"
    //String lastTimeDateString = "06/27/2017";
    java.text.DateFormat df = new SimpleDateFormat(format, Locale.US);
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

    public static String convertDateToString(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static Comparator<Transaction> COMPARE_BY_ID = new Comparator<Transaction>() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public int compare(Transaction one, Transaction other) {
            return Long.compare(one.getID(),other.getID());
        }
    };

    public static Comparator<Transaction> COMPARE_BY_PAYMENT_METHOD = new Comparator<Transaction>() {
        public int compare(Transaction one, Transaction other) {
            return one.getPaymentMethod().compareToIgnoreCase(other.getPaymentMethod());
        }
    };

    public static Comparator<Transaction> COMPARE_BY_Category = new Comparator<Transaction>() {
        public int compare(Transaction one, Transaction other) {
            return one.getCategory().compareToIgnoreCase(other.getCategory());
        }
    };

    public static Comparator<Transaction> COMPARE_BY_SHOP = new Comparator<Transaction>() {
        public int compare(Transaction one, Transaction other) {
            return one.getShop().compareToIgnoreCase(other.getShop());
        }
    };

    public static Comparator<Transaction> COMPARE_BY_TRANSACTION_DATE = new Comparator<Transaction>() {
        public int compare(Transaction one, Transaction other) {
            return one.getPayDate().compareTo(other.getPayDate());
        }
    };

    public static Comparator<Transaction> COMPARE_BY_PRICE = new Comparator<Transaction>() {
        public int compare(Transaction one, Transaction other) {
            return Double.compare(one.getPrice(), other.getPrice());
        }
    };

    public static Comparator<Transaction> COMPARE_BY_REGISTRATION_DATE = new Comparator<Transaction>() {
        public int compare(Transaction one, Transaction other) {
            return one.getRegistrationDate().compareTo(other.getPayDate());
        }
    };

    public static Comparator<String> COMPARE_String = new Comparator<String>() {
        public int compare(String one, String other) {
            return one.compareToIgnoreCase(other);
        }
    };

/*    public static Comparator<Budget> COMPARE_BY_CATEGORY_PRIORITY = new Comparator<Budget>() {
        public int compare(Budget one, Budget other) {
            return Integer.compare(one.getCatPriority(), other.getCatPriority());
        }
    };*/

    public static void sort(String sortBy,char ascOrDesc) {
        if(sortBy.equals(LANGUAGE.IDName)) {
            if(ascOrDesc == UP_ARROW)
                Collections.sort(month.getTransactions(),global.COMPARE_BY_ID);
            else if(ascOrDesc == DOWN_ARROW)
                Collections.sort(month.getTransactions(), Collections.reverseOrder(global.COMPARE_BY_ID));
        }
        else if(sortBy.equals(getWordCapitalLetter(LANGUAGE.categoryName))) {
            if(ascOrDesc == UP_ARROW)
                Collections.sort(month.getTransactions(),global.COMPARE_BY_Category);
            else if(ascOrDesc == DOWN_ARROW)
                Collections.sort(month.getTransactions(), Collections.reverseOrder(global.COMPARE_BY_Category));
        }
        else if(sortBy.equals(getSentenceCapitalLetter(LANGUAGE.paymentMethodName,'.'))) {
            if(ascOrDesc == UP_ARROW)
                Collections.sort(month.getTransactions(), global.COMPARE_BY_PAYMENT_METHOD);
            else if(ascOrDesc == DOWN_ARROW)
                Collections.sort(month.getTransactions(), Collections.reverseOrder(global.COMPARE_BY_PAYMENT_METHOD));
        }
        else if(sortBy.equals(getWordCapitalLetter(LANGUAGE.shopName))) {
            if(ascOrDesc == UP_ARROW)
                Collections.sort(month.getTransactions(), global.COMPARE_BY_SHOP);
            else if(ascOrDesc == DOWN_ARROW)
                Collections.sort(month.getTransactions(), Collections.reverseOrder(global.COMPARE_BY_SHOP));
        }
        else if(sortBy.equals(getSentenceCapitalLetter(LANGUAGE.chargeDateName,'.'))) {
            if(ascOrDesc == UP_ARROW)
                Collections.sort(month.getTransactions(), global.COMPARE_BY_TRANSACTION_DATE);
            else if(ascOrDesc == DOWN_ARROW)
                Collections.sort(month.getTransactions(), Collections.reverseOrder(global.COMPARE_BY_TRANSACTION_DATE));
        }
        else if(sortBy.equals(getWordCapitalLetter(LANGUAGE.sumName))) {
            if(ascOrDesc == UP_ARROW)
                Collections.sort(month.getTransactions(), global.COMPARE_BY_PRICE);
            else if(ascOrDesc == DOWN_ARROW)
                Collections.sort(month.getTransactions(), Collections.reverseOrder(global.COMPARE_BY_PRICE));
        }
        else if(sortBy.equals(getSentenceCapitalLetter(LANGUAGE.regisrationDateName,'.'))) {
            if(ascOrDesc == UP_ARROW)
                Collections.sort(month.getTransactions(), global.COMPARE_BY_REGISTRATION_DATE);
            else if(ascOrDesc == DOWN_ARROW)
                Collections.sort(month.getTransactions(), Collections.reverseOrder(global.COMPARE_BY_REGISTRATION_DATE));
        }
/*
        else if(sortBy.equals(language.regisrationDateName))
        {
            if(ascOrDesc == UP_ARROW)
                Collections.sort(month.getTransactions(), global.COMPARE_BY_CATEGORY_PRIORITY);
            else if(ascOrDesc == DOWN_ARROW)
                Collections.sort(month.getTransactions(), Collections.reverseOrder(global.COMPARE_BY_CATEGORY_PRIORITY));
        }*/
    }


    //@RequiresApi(api = Build.VERSION_CODES.O)
    public static void copyFile(String sourceFilePath, String destinationtDirPath) {
        File src = new File(sourceFilePath);
        File destDir = new File(destinationtDirPath);
        File destFile = new File(destinationtDirPath + "/" + DB_FILE_NAME + "." + DB_SUFFIX);
        try {
            // make sure the target file exists
            if (src.exists()) {
                if(!destDir.exists())
                    destDir.mkdir();

                InputStream in = new FileInputStream(src);
                OutputStream out = new FileOutputStream(destFile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
            else
            {
                // Get error file not found
            }
        }
        catch(Exception e) {
            String s = e.getMessage().toString();
        }
    }

}
