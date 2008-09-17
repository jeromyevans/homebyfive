package com.blueskyminds.housepad.core.analysis.suburb;

import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.MonthOfYear;
import com.blueskyminds.housepad.core.region.reference.RegionRef;

/**
 * Date Started: 6/12/2007
 * <p/>
 * History:
 */
public class SuburbAnalysisModel {

    private RegionRef region;
    private Interval interval;
    private MonthOfYear monthOfYear;

    public SuburbAnalysisModel(RegionRef region) {
        this.region = region;
    }

    public Interval getInterval() {
        return interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public MonthOfYear getMonthOfYear() {
        return monthOfYear;
    }

    public void setMonthOfYear(MonthOfYear monthOfYear) {
        this.monthOfYear = monthOfYear;
    }
    
    public RegionRef getRegion() {
        return region;
    }
}
