package com.tushu.repository;

import com.tushu.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    long countByStatus(Integer status);

    @Query("SELECT b.category, COUNT(b) FROM Book b GROUP BY b.category")
    List<Object[]> countByCategory();

    @Query("SELECT br.borrowerName, COUNT(br) FROM BorrowRecord br GROUP BY br.borrowerName ORDER BY COUNT(br) DESC")
    List<Object[]> topBorrowers();

    @Query(value = "SELECT b.id, b.title, b.author, COUNT(br.id) AS cnt " +
           "FROM book b LEFT JOIN borrow_record br ON b.id = br.book_id " +
           "GROUP BY b.id, b.title, b.author ORDER BY cnt DESC LIMIT ?1", nativeQuery = true)
    List<Object[]> topBooks(int limit);
}
