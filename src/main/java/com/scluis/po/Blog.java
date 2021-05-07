package com.scluis.po;

import com.scluis.utils.html2PlainText;
import com.scluis.utils.markdown2Html;
import org.hibernate.annotations.Fetch;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sichengluis on 2021/1/20 13:54
 */
@Entity
@Table(name="t_blog")
public class Blog {
    //设置id是主键且自动生成
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "博客标题不能为空")
    private String title;//博客标题
    @NotBlank(message = "博客内容不能为空")
    private String content;//博客内容
    private String firstPicture;//文章首图
    private String flag;//转载原创等
    private Integer viewTimes;//浏览次数
    private boolean openAppreciation;//开启赞赏
    private boolean displayShareStatment;//显示转载声明
    private boolean openComment;//开启评论
    private boolean published;//是否已经发布
    private boolean recommend;//是否被推荐
    @Temporal(TemporalType.TIMESTAMP)//指定数据库中时间的数据类型是时间戳，这样日期和时间都有，DATA只有日期，TIME只有时间
    private Date createTime;//创建时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;//修改时间
    //关联Blog和Type两个实体类，多个Blog对应一个Type
    @NotNull(message = "博客分类不能为空")
    @ManyToOne//实体的关联中，多方应该作为关系的维护方
    private Type type;
    //级联新增，当Blog中新增一个Tag，那么Tag表中也会增加一个Tag
    @ManyToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
    private List<Tag> tags=new ArrayList<>();
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "blog")
    private List<Comment> comments=new ArrayList<>();
    @Transient//不和数据库表映射
    private String tagIds;
    @Transient
    private String description;
    public void initTagIds(){
        this.tagIds=tagList2tagIds(this.getTags());
    }
    public void initDescription(){
        String htmlContent= markdown2Html.convert(this.getContent());
        this.description= html2PlainText.convert(htmlContent);
    }
    //1,2,3
    private String tagList2tagIds(List<Tag> tags) {
        if (!tags.isEmpty()) {//博客有标签
            StringBuffer ids = new StringBuffer();
            boolean flag = false;
            for (Tag tag : tags) {
                if (flag) {
                    ids.append(",");
                } else {
                    flag = true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        } else {
            return null;
        }
    }
    public Blog(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFirstPicture() {
        return firstPicture;
    }

    public void setFirstPicture(String firstPicture) {
        this.firstPicture = firstPicture;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(Integer viewTimes) {
        this.viewTimes = viewTimes;
    }

    public boolean isOpenAppreciation() {
        return openAppreciation;
    }

    public void setOpenAppreciation(boolean openAppreciation) {
        this.openAppreciation = openAppreciation;
    }

    public boolean isDisplayShareStatment() {
        return displayShareStatment;
    }

    public void setDisplayShareStatment(boolean displayShareStatment) {
        this.displayShareStatment = displayShareStatment;
    }

    public boolean isOpenComment() {
        return openComment;
    }

    public void setOpenComment(boolean openComment) {
        this.openComment = openComment;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRecommend() {
        return recommend;
    }

    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }



    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", firstPicture='" + firstPicture + '\'' +
                ", flag='" + flag + '\'' +
                ", viewTimes=" + viewTimes +
                ", openAppreciation=" + openAppreciation +
                ", displayShareStatment=" + displayShareStatment +
                ", openComment=" + openComment +
                ", published=" + published +
                ", recommend=" + recommend +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", type=" + type +
                ", tags=" + tags +
                ", user=" + user +
                ", comments=" + comments +
                ", tagIds='" + tagIds + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
