package com.scluis.repository;

import com.scluis.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Sichengluis on 2021/2/3 16:40
 */
public interface commentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByBlogIdAndParentCommentIsNull(Long id,Sort sort);
}
