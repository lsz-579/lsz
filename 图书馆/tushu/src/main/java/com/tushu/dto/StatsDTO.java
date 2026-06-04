package com.tushu.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StatsDTO {
    private long totalBooks;
    private long totalBorrows;
    private long activeBorrows;
    private List<Map<String, Object>> categoryDistribution;
    private List<Map<String, Object>> topBorrowedBooks;
}
