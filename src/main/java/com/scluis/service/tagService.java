package com.scluis.service;

import com.scluis.po.Tag;
import com.scluis.po.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Created by Sichengluis on 2021/1/24 14:14
 */
public interface tagService {
    Tag saveTag(Tag tag);
    Tag getTag(Long id);
    /**
     * 功能描述: //分页获取全部标签
     * @Param: [pageable]
     * @Return: org.springframework.data.domain.Page<com.scluis.po.Tag>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:19
     */
    Page<Tag> getTagList(Pageable pageable);
    /**
     * 功能描述: 获取全部标签（不分页）
     * @Return: java.util.List<com.scluis.po.Tag>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:20
     */
    List<Tag> getTagList();

    /**
     * 获取全部标签中的前size个，用于显示或隐藏标签
     * @param size
     * @return
     */
    List<Tag> getTags(Integer size);
    /**
     * 功能描述:根据标签id获取对应标签
     * @Param: [tagIdList 标签id组成的List]
     * @Return: java.util.List<com.scluis.po.Tag>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:20
     */
    List<Tag> getTagList(List<Long> tagIdList);
    /**
     * 功能描述: 返回具有最多博客的标签数组，用于页面展示Top标签
     * @Param: [size 返回标签数组的大小]
     * @Return: java.util.List<com.scluis.po.Tag>
     * @Author: Sichengluis
     * @Date: 2021/2/2 18:21
     */
    List<Tag> getTagList(Integer size);
    void deleteTag(Long id);
    Tag getTagByTagName(String tagName);
}
