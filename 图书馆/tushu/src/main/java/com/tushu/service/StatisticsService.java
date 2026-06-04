package com.tushu.service;

import com.tushu.dto.StatsDTO;
import com.tushu.repository.BookRepository;
import com.tushu.repository.BorrowRecordRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatisticsService {

    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;

    public StatisticsService(BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public StatsDTO getStats() {
        StatsDTO stats = new StatsDTO();
        stats.setTotalBooks(bookRepository.count());
        stats.setTotalBorrows(borrowRecordRepository.count());
        stats.setActiveBorrows(borrowRecordRepository.countByStatus(0));

        List<Map<String, Object>> categoryDist = new ArrayList<>();
        for (Object[] row : borrowRecordRepository.countByCategory()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", row[0] != null ? row[0] : "未分类");
            item.put("value", row[1]);
            categoryDist.add(item);
        }
        stats.setCategoryDistribution(categoryDist);

        List<Map<String, Object>> topBooks = new ArrayList<>();
        List<Object[]> rows = borrowRecordRepository.topBooks(10);
        for (int i = 0; i < Math.min(rows.size(), 10); i++) {
            Object[] row = rows.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("rank", i + 1);
            item.put("title", row[1]);
            item.put("author", row[2]);
            item.put("borrowCount", row[3]);
            topBooks.add(item);
        }
        stats.setTopBorrowedBooks(topBooks);

        return stats;
    }
}
