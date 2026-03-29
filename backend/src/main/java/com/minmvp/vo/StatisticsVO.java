package com.minmvp.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVO {

    private List<DailyCount> daily;

    private List<RoleCount> roleDistribution;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyCount {
        private String date;
        private Integer count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoleCount {
        private String name;
        private Integer value;
    }
}
