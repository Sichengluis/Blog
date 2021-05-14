package com.scluis.service;

import com.scluis.po.Type;
import com.scluis.repository.typeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sichengluis on 2021/1/23 10:49
 */
@Service
public class typeServiceImpl implements typeService{

    @Autowired
    private typeRepository typeRepository;

    @Override
    public Type getType(Long id) {
        return typeRepository.findOne(id);
    }

    @Override
    public Type getTypeByTypeName(String typeName) {
        return typeRepository.findByTypeName(typeName);
    }

    @Override
    public Page<Type> getTypeList(Pageable pageable) {
        return typeRepository.findAll(pageable);//调用springbootJpa封装的分页查询方法
    }

    @Override
    public List<Type> getTypeList() {
        return typeRepository.findAll();
    }

    /**
     * 功能描述: 返回博客数量最多的size个分类
     * @Param: [size 返回分类的数量]
     * @Return: java.util.List<com.scluis.po.Type>
     * @Author: Sichengluis
     * @Date: 2021/2/2 22:32
     */
    @Override
    public List<Type> getTypeList(Integer size) {
        Sort sort=new Sort(Sort.Direction.DESC,"blogs.size");
        //根据type中blogs的size查询，取第一页，每页元素数量是size
        Pageable pageable=new PageRequest(0,size,sort);
        return typeRepository.findTopType(pageable);
    }

    @Override
    public List<Type> getTypes(Integer size) {
        List<Type> res=new ArrayList<>();
        List<Type> list=typeRepository.findAll();
        for(int i=0;i<list.size();i++){
            if(i==size){
                break;
            }
            res.add(list.get(i));
        }
        return res;
    }

    /**
     * 功能描述: 添加或者修改博客分类
     * @Param: [type]
     * @Return: com.scluis.po.Type
     * @Author: Sichengluis
     * @Date: 2021/2/2 22:33
     */
    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.delete(id);
    }


}
