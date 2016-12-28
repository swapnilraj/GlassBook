package io.sixth.glassbook.utils;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.Calendar;

/**
 * Created by thawne on 28/12/16.
 */

public class MyDatePickerDialog extends DatePickerDialog {

  /**
   * @param callBack How the parent is notified that the date is set.
   * @param year The initial year of the dialog.
   * @param monthOfYear The initial month of the dialog.
   * @param dayOfMonth The initial day of the dialog.
   */
  public static MyDatePickerDialog newInstance(OnDateSetListener callBack, int year,
      int monthOfYear,
      int dayOfMonth) {
    MyDatePickerDialog ret = new MyDatePickerDialog();
    ret.initialize(callBack, year, monthOfYear, dayOfMonth);
    return ret;
  }

  @Override
  public boolean isOutOfRange(int year, int month, int day) {
    final Calendar now = Calendar.getInstance();

    final Calendar selected = Calendar.getInstance();
    selected.set(year, month, day);

    return selected.compareTo(now) == -1;
  }
}