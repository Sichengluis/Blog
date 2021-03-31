package com.scluis;

import com.scluis.repository.blogRepository;
import com.scluis.service.blogService;
import com.scluis.utils.html2PlainText;
import com.scluis.utils.markdown2Html;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = blogApplication.class)
public class BlogApplicationTests {

	@Autowired
	blogRepository blogRepository;
	@Autowired
    blogService blogService;
	@Test
	public  void contextLoads() {
//		String md = blogService.getBlog(1l).getContent();
		String md=blogRepository.findOne(1l).getContent();
		System.out.println("转换之前的md文本为："+md);
		String html= markdown2Html.convert(md);
		System.out.println("转成的html为："+html);
		String plainText= html2PlainText.convert(html);
		System.out.println("转成的纯文本为："+plainText);
	}


}
