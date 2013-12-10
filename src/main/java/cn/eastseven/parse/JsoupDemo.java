package cn.eastseven.parse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupDemo {

	public static void main(String[] args) {
		List<Person> list = new ArrayList<Person>();
		initData(list);
		for (Person person : list) {
			if("".equals(person.getLastName())) continue;
			System.out.println(person);
		}
	}

	private static void initData(List<Person> list) {
		try {
			Document doc = Jsoup.parse(new File("src/main/resources/sanguolist.html"), "utf-8");
			Elements tables = doc.select("div[id=mw-content-text] > table");
			int size = tables.size();
			for (int index = 0; index < size; index++) {
				Element table = tables.get(index);
				Elements trs = table.select("tr");
				int trSize = trs.size();
				for (int trIndex = 0; trIndex < trSize; trIndex++) {
					Element tr = trs.get(trIndex);
					Person person = new Person();
					Elements tds = tr.select("td");
					int i = 0;
					for(Iterator<Element> iter = tds.iterator(); iter.hasNext();) {
						Element td = iter.next();
						String html = td.html();
						html = html.replaceAll("<[aA]\\s+[^>]+>|</[aA]>", "");
						html = html.replaceAll("<center>|</center>", "");
						html = html.replaceAll("<[bB]>|</[bB]>", "");
						html = html.replaceAll("<span\\s+[^>]+>|</span>", "");
						html = html.trim();
						if("姓名".equals(html)) break;
						switch (i) {
						case 0:
							person.setFirstName(html);
							break;
						case 1:
							person.setLastName(html);
							break;
						case 2:
							person.setHometown(html);
							break;
						case 3:
							person.setDesc(html);
							break;
						case 4:
							person.setIsReal(html.length() > 0);
							break;
						default:
							break;
						}
						i++;
					}
					if("".equals(person.getFirstName())) continue;
					if(!person.getIsReal()) continue;
					list.add(person);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class Person {

	private String firstName = "";
	private String lastName  = "";
	private String hometown  = "";
	private String desc      = "";
	private boolean isReal   = false;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public boolean getIsReal() {
		return isReal;
	}

	public void setIsReal(boolean isReal) {
		this.isReal = isReal;
	}

	@Override
	public String toString() {
		return "Person [姓名=" + firstName + ", 字=" + lastName
				+ ", 籍贯=" + hometown + ", 列传=" + desc + ", 正史人物="
				+ isReal + "]";
	}

}