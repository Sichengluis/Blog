package com.scluis.service;

import com.scluis.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Sichengluis on 2021/1/23 10:47
 */
public interface typeService {
    Type saveType(Type type);
    Type getType(Long id);
    /**
     * 功能描述: 分页获取所有类型
     * @Param: [pageable pageable中size指定一页的大小，page指明查询第几页，sort指明查出来的列表排序方式]
     * @Return: org.springframework.data.domain.Page<com.scluis.po.Type>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:24
     */
    Page<Type> getTypeList(Pageable pageable);
    /**
     * 功能描述: 获取所有类型（不分页）
     * @Return: java.util.List<com.scluis.po.Type>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:24
     */
    List<Type> getTypeList();
    //获取top分类（文章数最多的分类）
    /**
     * 功能描述: 返回具有最多博客的类型数组，用于页面展示Top分类
     * @Param: [size 返回分类数组的大小]
     * @Return: java.util.List<com.scluis.po.Type>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:21
     */
    List<Type> getTypeList(Integer size);

    /**
     * 分类页面最开始显示前size个分类标签
     * @param size
     * @return
     */
    List<Type> getTypes(Integer size);
    void deleteType(Long id);
    Type getTypeByTypeName(String typeName);
}
