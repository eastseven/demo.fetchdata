package cn.eastseven.model;

/**
 * 作者<br/>
 * 出版社<br/>
 * ISBN<br/>
 * 出版日期<br/>
 * 开本<br/>
 * 页码<br/>
 * 版次<br/>
 * 
 * @author eastseven
 * 
 */
public class Book {

	// key
	private String isbn;
	private String name;
	private String author;
	private String press;
	private String pubTime;
	private String format;// 开本
	private String pageNumber;// 页码
	private String revision;// 版次
	
	private String price;
	private String coverUrl;

	private String url;
	
	public Book() {}
	
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPress() {
		return press;
	}

	public void setPress(String press) {
		this.press = press;
	}

	public String getPubTime() {
		return pubTime;
	}

	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", name=" + name + ", author=" + author
				+ ", press=" + press + ", pubTime=" + pubTime + ", format="
				+ format + ", pageNumber=" + pageNumber + ", revision="
				+ revision + ", price=" + price + ", coverUrl=" + coverUrl
				+ ", url=" + url
				+ "]";
	}

}
