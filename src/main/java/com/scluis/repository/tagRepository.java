package com.scluis.repository;

import com.scluis.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Sichengluis on 2021/1/24 14:13
 */
public interface tagRepository extends JpaRepository<Tag,Long> {
    Tag findTagByTagName(String tagName);

    @Query("select t from  Tag t")
    List<Tag> findTopTag(Pageable pageable);
}
