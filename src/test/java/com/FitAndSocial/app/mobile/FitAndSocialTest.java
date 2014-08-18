package com.FitAndSocial.app.mobile;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.FitAndSocial.app.mobile.FitAndSocial;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class FitAndSocialTest{

    @Test
    public void testAddClickButton_withPositiveValues(){
        final FitAndSocial myActivity = Robolectric.buildActivity(FitAndSocial.class).create().get();


//        Button button = (Button) myActivity.findViewById(R.id.add);
//
//        EditText numberOne = (EditText) myActivity.findViewById(R.id.number_one);
//        EditText numberTow = (EditText) myActivity.findViewById(R.id.number_two);
//
//        TextView result = (TextView) myActivity.findViewById(R.id.result);
//
//        numberOne.setText("5");
//        numberTow.setText("10");
//
//        button.performClick();
//
//
////        myActivity.getFirstNumber().setText("5");
////        myActivity.getSecondNumber().setText("3");
////        myActivity.getAddButton().performClick();
//        Assert.assertEquals("Total = 15", result.getText().toString());
    }

}
