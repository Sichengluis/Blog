package com.scluis.repository;

import com.scluis.po.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Sichengluis on 2021/1/23 10:50
 */
public interface typeRepository extends JpaRepository<Type,Long> {
    Type findByTypeName(String typeName);
    //自定义查询（自定义jpql语句）
    @Query("select t from Type t")
    List<Type> findTopType(Pageable pageable);
}
