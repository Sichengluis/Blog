package com.scluis.service;

import com.scluis.po.Tag;
import com.scluis.repository.tagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Created by Sichengluis on 2021/1/24 14:17
 */
@Service
public class tagServiceImpl implements tagService {
    @Autowired
    private tagRepository tagRepository;

    @Override
    public Tag getTag(Long id) {
        return tagRepository.findOne(id);
    }

    @Override
    public Tag getTagByTagName(String tagName) {
        return tagRepository.findTagByTagName(tagName);
    }

    @Override
    public Page<Tag> getTagList(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> getTagList() {

        return tagRepository.findAll();
    }

    @Override
    public List<Tag> getTagList(List<Long> tagIdList) {

        return tagRepository.findAll(tagIdList);
    }

    /**
     * 功能描述: 返回有最多博客数量的标签
     * @Param: [size 返回标签的个数]
     * @Return: java.util.List<com.scluis.po.Tag>
     * @Author: Sichengluis
     * @Date: 2021/2/2 22:30
     */
    @Override
    public List<Tag> getTagList(Integer size) {
        Sort sort=new Sort(Sort.Direction.DESC,"blogs.size");//根据tag对象中blogs数组的大小排序
        //分页查询获取第一页（根据blogs.size排序获取前size条数据），type同理
        Pageable pageable=new PageRequest(0,size,sort);
        return tagRepository.findTopTag(pageable);
    }

    /**
     * 功能描述: 添加或者修改博客标签
     * @Param: [tag]
     * @Return: com.scluis.po.Tag
     * @Author: Sichengluis
     * @Date: 2021/2/2 22:35
     */
    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
         tagRepository.delete(id);
    }

}
