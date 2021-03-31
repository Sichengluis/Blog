package com.scluis.repository;

import com.scluis.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Sichengluis on 2021/1/24 16:09
 */
//继承JpaSpecificationExecutor<Blog>就可以使用jpa为我们封装的组合查询
public interface blogRepository extends JpaRepository<Blog,Long>,JpaSpecificationExecutor<Blog> {
    /**
     * 功能描述: 分页获取推荐的博客
     * @Param: [pageable]
     * @Return: java.util.List<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:59
     */
    @Query("select b from Blog b where b.recommend=true")
    List<Blog> findRecommendedBlogs(Pageable pageable);
    /**
     * 功能描述: 分页返回根据某一属性排序的数据
     * @Param: [pageable]
     * @Return: java.util.List<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/2 19:00
     */
    @Query("select b from Blog b ")
    List<Blog> findTopBlogsByProp(Pageable pageable);

    /**
     * 功能描述: 搜索博客标题或者内容与关键字类似的博客
     * @Param: [globalQueryKey 搜索的关键字, pageable]
     * @Return: org.springframework.data.domain.Page<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/2 19:06
     */
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")//?1代表第一个参数，即globalQueryKey
    Page<Blog> globalSearchBlogs(String globalQueryKey,Pageable pageable);

    /**
     * 功能描述: 博客浏览次数增加
     * @Param: [id 博客id]
     * @Return: int
     * @Author: Sichengluis
     * @Date: 2021/2/5 19:31
     */
    @Transactional
    @Modifying
    @Query("update Blog b set b.viewTimes=b.viewTimes+1 where b.id=?1")
    int updateViewTimes(Long id);

    /**
     * 功能描述: 返回博客涉及到的所有年份
     * @Param: []
     * @Return: java.util.List<java.lang.String>
     * @Author: Sichengluis
     * @Date: 2021/2/10 13:34
     */
    @Query("select function('date_format',b.createTime,'%Y') as year from Blog b group by function('date_format',b.createTime,'%Y') order by year" )
    List<String> findAllYears();

    /**
     * 功能描述: 得到某一个年份的所有博客
     * @Param: [year]
     * @Return: java.util.List<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/10 13:35
     */
    @Query("select b from Blog b where function('date_format',b.createTime,'%Y')=?1")
    List<Blog> findBlogsByYear(String year);
}
