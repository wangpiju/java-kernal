package com.hs3.utils;

import com.hs3.entity.report.TeamReport;

import java.math.BigDecimal;
import java.util.Comparator;

public class SortTeamReport
        implements Comparator<TeamReport> {
    public int compare(TeamReport arg0, TeamReport arg1) {
        TeamReport teamReport0 = arg0;
        TeamReport teamReport1 = arg1;

        int flag = teamReport0.getCount().compareTo(teamReport1.getCount());
        if (flag == 0) {
            return teamReport0.getAccount().compareTo(teamReport1.getAccount());
        }
        if (flag == 1) {
            return -1;
        }
        return 1;
    }
}
