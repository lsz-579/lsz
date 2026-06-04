package com.tushu.service;

import com.tushu.dto.BookQueryDTO;
import com.tushu.entity.Book;
import com.tushu.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Page<Book> list(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Page<Book> search(BookQueryDTO query, Pageable pageable) {
        Specification<Book> spec = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(query.getTitle())) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + query.getTitle() + "%"));
            }
            if (StringUtils.hasText(query.getAuthor())) {
                predicates.add(criteriaBuilder.like(root.get("author"), "%" + query.getAuthor() + "%"));
            }
            if (StringUtils.hasText(query.getCategory())) {
                predicates.add(criteriaBuilder.equal(root.get("category"), query.getCategory()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return bookRepository.findAll(spec, pageable);
    }

    public Optional<Book> getById(Long id) {
        return bookRepository.findById(id);
    }

    public Book create(Book book) {
        if (StringUtils.hasText(book.getIsbn())) {
            bookRepository.findByIsbn(book.getIsbn()).ifPresent(b -> {
                throw new RuntimeException("ISBN已存在: " + book.getIsbn());
            });
        }
        return bookRepository.save(book);
    }

    public Book update(Long id, Book book) {
        Book existing = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("图书不存在: " + id));
        existing.setTitle(book.getTitle());
        existing.setAuthor(book.getAuthor());
        existing.setIsbn(book.getIsbn());
        existing.setCategory(book.getCategory());
        existing.setPublisher(book.getPublisher());
        existing.setPublishDate(book.getPublishDate());
        existing.setDescription(book.getDescription());
        return bookRepository.save(existing);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("图书不存在: " + id);
        }
        bookRepository.deleteById(id);
    }
}
