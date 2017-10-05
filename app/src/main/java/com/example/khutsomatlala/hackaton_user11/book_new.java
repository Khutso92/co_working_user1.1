package com.example.khutsomatlala.hackaton_user11;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Calendar;

/**
 * Created by Admin on 9/15/2017.
 */

public class book_new  extends AppCompatActivity {


    private int mYear;
    private int mMonth;
    private int mDay;

    private static int HOUR_PRICE = 20;

    private TextView mDateDisplay, mPrice;
    private Button mPickDate;
    EditText mNumberHours;

    int hours;
    static final int DATE_DIALOG_ID = 0;

    String pic,date1,date;

    ImageView BookPic;

    Boolean Checked = true;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent i = getIntent();
        pic = i.getStringExtra("pic");

        mDateDisplay = (TextView) findViewById(R.id.showMyDate);
        mPickDate = (Button) findViewById(R.id.myDatePickerButton);
        mNumberHours = (EditText) findViewById(R.id.EdtNumberHours);
        mPrice = (TextView) findViewById(R.id.txtPrice);

        BookPic = (ImageView) findViewById(R.id.BookPic);

        mPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        Glide.with(this)
                .load(pic)
                //  .override(300, 200)
                .centerCrop()
                .into(BookPic);

        // display the current date
        updateDisplay();

    }

    private void updateDisplay() {
        this.mDateDisplay.setText(
                new StringBuilder()

                        // Month is 0 based so add 1
                        .append(mDay).append("-")
                        .append(mMonth + 1).append("-")
                        .append(mYear).append(" "));


    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateDisplay();


                }
            };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth, mDay);
        }
        return null;
    }


    public void calPrice(View view) {




        if (mNumberHours.length() > 0) {

            hours = Integer.parseInt(mNumberHours.getText().toString().trim());


            if (hours <= 24) {
                mPrice.setText("R" + hours * HOUR_PRICE);

                Checked = false;

            } else

            {
                Toast.makeText(this, "Hours must be less than 24 hours", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, " Field required", Toast.LENGTH_SHORT).show();
        }

    }


    public void email( View view ){

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); //email apps will handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, " Space booking ");
        intent.putExtra(Intent.EXTRA_TEXT, "Booking information" );
        startActivity(intent);

    }
}

