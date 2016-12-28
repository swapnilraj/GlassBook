package io.sixth.glassbook.data.api;

import android.support.annotation.NonNull;
import java.util.Date;

/**
 * Created by thawne on 28/12/16.
 */

public class TimeManager {

  private Date start;
  private Date end;

  public TimeManager(@NonNull Date start, @NonNull Date end) {
    this.start = start;
    this.end = end;
  }

  public Date getStart() {
    return this.start;
  }

  public Date getEnd() {
    return this.end;
  }
}
