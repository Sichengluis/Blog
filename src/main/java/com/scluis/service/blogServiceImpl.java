package com.scluis.service;

import com.scluis.po.Blog;
import com.scluis.po.Type;
import com.scluis.repository.blogRepository;
import com.scluis.utils.markdown2Html;
import com.scluis.utils.notFoundException;
import com.scluis.vo.blogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Created by Sichengluis on 2021/1/24 16:09
 */
@Service
public class blogServiceImpl implements blogService {
    @Autowired
    private blogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findOne(id);
    }

    @Override
    public Blog getHtmlContentBlog(Long id) {
        Blog blog=blogRepository.findOne(id);
        if(blog==null){
            throw new notFoundException("博客走丢了~");
        }
        else{
            //不改变查询到的blog的内容，而是新建一个临时blog专门用来显示博客信息
            Blog tmpBlog=new Blog();
            BeanUtils.copyProperties(blog,tmpBlog);
            String content=tmpBlog.getContent();
            tmpBlog.setContent( markdown2Html.markdownToHtmlExtensions(content));
            blogRepository.updateViewTimes(id);
            return tmpBlog;
        }
    }

    /**
     * 功能描述: 根据博客的某一属性排序，返回前size个博客
     * @Param: [size 返回的博客数量, sortBy 排序依据的属性]
     * @Return: java.util.List<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/2 22:25
     */
    @Override
    public List<Blog> getBlogList(Integer size,String sortBy) {
        Sort sort=new Sort(Sort.Direction.DESC,sortBy);
        Pageable pageable=new PageRequest(0,size,sort);
        //返回浏览量最多的前几条博客
        return blogRepository.findTopBlogsByProp(pageable);
    }


    public List<Blog> getBlogList(Integer size){
        Sort sort=new Sort(Sort.Direction.DESC,"updateTime");
        Pageable pageable=new PageRequest(0,size,sort);
        return blogRepository.findRecommendedBlogs(pageable);
    }

    /**
     * 功能描述: 根据关键字匹配博客内容或者标题进行全局搜索
     * @Param: [globalQueryKey 搜索关键字, pageable]
     * @Return: org.springframework.data.domain.Page<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/6 17:16
     */


    @Override
    public Page<Blog> getBlogList( String globalQueryKey,Pageable pageable) {
        return blogRepository.globalSearchBlogs(globalQueryKey,pageable);
    }


    /**
     * 功能描述: 根据查询条件分页获取博客列表
     * @Param: [pageable, blogQuery]
     * @Return: org.springframework.data.domain.Page<com.scluis.po.Blog>
     * @Author: Sichengluis
     * @Date: 2021/2/2 22:23
     */
    @Override
    public Page<Blog> getBlogList(Pageable pageable,blogQuery blogQuery) {

        if(blogQuery==null){//没有查询条件，查询全部
            return blogRepository.findAll(pageable);
        }
        else{//根据条件查询博客列表
            return blogRepository.findAll(new Specification<Blog>() {
                @Override
                public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    //此方法帮我们处理动态查询条件
                    //root用来指明pojo的属性，criteriaBuilder构建查询，criteriaQuery执行查询
                    List<Predicate> predicateList=new ArrayList<>();
                    if(!"".equals(blogQuery.getTitle())&&blogQuery.getTitle()!=null){
                        predicateList.add(criteriaBuilder.like(root.<String>get("title"),"%"+blogQuery.getTitle()+"%"));
                    }
                    if(blogQuery.getTypeId()!=null){
                        predicateList.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),blogQuery.getTypeId()));
                    }
                    if(blogQuery.isRecommend()){
                        predicateList.add(criteriaBuilder.equal(root.<Boolean>get("recommend"),blogQuery.isRecommend()));
                    }
                    criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                    return null;
                }
            },pageable);
        }

    }

    @Override
    public Page<Blog> getBlogList(Long tagId, Pageable pageable) {
        if(tagId!=null){
            return blogRepository.findAll(new Specification<Blog>() {
                @Override
                public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    //blog和tag两个表关联查询
                    Join join=root.join("tags");
                    return criteriaBuilder.equal(join.get("id"),tagId);
                }
            },pageable);
        }
        else return blogRepository.findAll(pageable);

    }

    @Override
    public Map<String, List<Blog>> getBlogList() {
        List<String> years=blogRepository.findAllYears();
        Map<String, List<Blog>>res=new TreeMap<>((o1,o2)-> Integer.parseInt(o2)-Integer.parseInt(o1));
        for (String year:years) {
            List<Blog> blogsByYear = blogRepository.findBlogsByYear(year);
            Collections.sort(blogsByYear, (Blog o1, Blog o2)-> {
                //降序
                return o2.getCreateTime().compareTo(o1.getCreateTime());
            });
            res.put(year,blogsByYear);
        }
        return res;
    }

    @Override
    public Long getBlogNum() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //新增
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViewTimes(0);
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blogFind = blogRepository.findOne(id);
        if(blogFind==null){
            throw new notFoundException("不存在相关博客~");
        }
        //用户修改博客时，页面中没有创建时间等信息，所以需要把用户提交的博客对象赋值给已经存在的博客对象
        copyUpdateProps(blog,blogFind);
        blogFind.setUpdateTime(new Date());
        return  blogRepository.save(blogFind);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.delete(id);
    }

    /**
     * 功能描述: 将用户提交的博客内容赋值给已经存在的博客
     * @Param: [source 源博客 用户提交的博客, target 目标博客 已经存在的博客]
     * @Return: void
     * @Author: Sichengluis
     * @Date: 2021/2/2 22:27
     */
    void copyUpdateProps(Blog source,Blog target){
        target.setFlag(source.getFlag());
        target.setTitle(source.getTitle());
        target.setContent(source.getContent());
        target.setTags(source.getTags());
        target.setType(source.getType());
        target.setFirstPicture(source.getFirstPicture());
        target.setRecommend(source.isRecommend());
        target.setOpenAppreciation(source.isOpenAppreciation());
        target.setDisplayShareStatment(source.isDisplayShareStatment());
        target.setOpenComment(source.isOpenComment());
        target.setPublished(source.isPublished());
    }
}
