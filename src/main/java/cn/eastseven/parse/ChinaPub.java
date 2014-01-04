package cn.eastseven.parse;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.eastseven.model.Book;

import com.google.common.collect.Lists;

public class ChinaPub {

	private static final String LINK_HEAD = "http://product.china-pub.com/";
	private static final int timeoutMillis = 10000;
	private static URL url = null;
	
	public void start() {
		int num = 4000000;//2964781;
		int valid = 1, invalid = 1;
		for (int index = 0; index < num; index++) {
			String link = LINK_HEAD + (++index);
			try {
				Jsoup.parse(new URL(link), timeoutMillis);
				//System.out.println(link + ": childNodeSize=" + doc.childNodeSize());
				valid++;
			} catch (Exception e) {
				//e.printStackTrace();
				invalid++;
				continue;
			}
		}
		System.out.println("有效地址="+valid+"；无效地址="+invalid);
	}

	public List<String> getLinks() {
		List<String> links = Lists.newArrayList();
		
		try {
			Document doc = Jsoup.parse(new URL("http://www.china-pub.com/Browse/"), timeoutMillis);
			Elements a = doc.select("div#wrap > ul > li.clr > a");
			System.out.println(a);
			int size = a.size();
			int num = 0;
			for (int index = 0; index < size; index++) {
				Element e = a.get(index);
				String href = e.attr("href");
				String text = e.text();
				Pattern p = Pattern.compile("\\d+");
				Matcher m = p.matcher(text);
				while(m.find()) {
					int start = m.start();
					int end = m.end();
					//System.out.println(text+": "+text.substring(start, end));
					num += Integer.valueOf(text.substring(start, end));
				}
				
				Document list = Jsoup.parse(new URL(href), timeoutMillis);
				Elements result = list.select("li.result_name > a").not(".fourhours");
				System.out.println(result);
			}
			System.out.println("合计："+num);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return links;
	}
	
	public Book convertToBook(String link) {
		Book book = new Book();
		
		try {
			
			url = new URL(link);
			Document doc = Jsoup.parse(url, 1000);
			
			Elements proBook = doc.select("div#right > .pro_book");
			String title = proBook.select(".pro_book > h1").text();
			String imgSrc = proBook.select(".pro_book > .pro_book_img > dl > dt > a > img").attr("src");
			book.setName(title);
			book.setCoverUrl(imgSrc);
			System.out.println(title + "\n" + imgSrc);
			
			String price = proBook.select(".pro_buy_intr > ul > li > span").first().text();
			book.setPrice(price.replaceAll("￥", ""));
			System.out.println(price);
			
			Elements bookInfo = doc.select("div#right > .right_pro_intr");
			Elements lis = bookInfo.select("div#con_a_1 > .pro_r_deta > ul > li");
			int size = lis.size();
			
			for(int index = 0; index < size; index++) {
				Element li = lis.get(index);
				String html = li.html().replaceAll(RegexUtil.A, RegexUtil.BLANK).replaceAll(RegexUtil.STRONG, RegexUtil.BLANK);
				html = html.replaceAll("&nbsp;", "");
				html = html.replaceAll(RegexUtil.BR, RegexUtil.BLANK).replaceAll(RegexUtil.DIV, RegexUtil.BLANK).replaceAll(RegexUtil.SPAN, RegexUtil.BLANK);
				html = html.replaceAll(RegexUtil.STYLE, RegexUtil.BLANK);
				html = html.replaceAll(RegexUtil.ENTER_TAB_ALL, "").replaceAll("&gt;", ">");
				html = html.trim();
				System.out.println(html);
				
				String prefix = "";
				if(html.startsWith("作者：")) {
					prefix = "作者：";
					book.setAuthor(html.replaceAll(prefix, "").trim());
				}
				if(html.startsWith("出版社：")) {
					prefix = "出版社：";
					book.setPress(html.replaceAll(prefix, "").trim());
				}
				if(html.startsWith("ISBN：")) {
					prefix = "ISBN：";
					book.setIsbn(html.replaceAll(prefix, "").trim());
				}
				if(html.startsWith("上架时间：")) {
					prefix = "上架时间：";
					book.setPubTime(html.replaceAll(prefix, "").trim());
				}
				if(html.startsWith("开本：")) {
					prefix = "开本：";
					book.setFormat(html.replaceAll(prefix, "").trim());
				}
				if(html.startsWith("页码：")) {
					prefix = "页码：";
					book.setPageNumber(html.replaceAll(prefix, "").trim());
				}
				if(html.startsWith("版次：")) {
					prefix = "版次：";
					book.setRevision(html.replaceAll(prefix, "").trim());
				}
			}
			System.out.println(book);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return book;
	}
	
	public static void main(String[] args) {
		new ChinaPub().start();
	}

}
