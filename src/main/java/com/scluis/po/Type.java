package com.scluis.po;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sichengluis on 2021/1/20 14:07
 */
@Entity
@Table(name="t_type")
public class Type {
    @Id
    @GeneratedValue
    private Long id;
    //后端校验
    @NotBlank(message = "分类名称不能为空")
    private String typeName;
    //关联Blog和Type，一个Type对应多个Blog
    @OneToMany(mappedBy = "type")//指明关系中的维护端和被维护端，这句话代表Type和Blog的关系中，通过Blog的type属性进行维护
    private List<Blog> blogs=new ArrayList<>();

    public Type() {
    }

    public Type(String typeName) {
        this.typeName = typeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
