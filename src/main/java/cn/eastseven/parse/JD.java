package cn.eastseven.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

public class JD {

	public static final String link = "http://www.jd.com/allSort.aspx";
	public static final int timeoutMillis = 1000;
	
	public static void main(String[] args) throws Exception {
		
		new JD().start();
	}

	public void start() throws Exception {
		URL url = new URL(link);
		Document doc = Jsoup.parse(url, timeoutMillis);
		Element allsort = doc.getElementById("allsort");
		Elements divs = allsort.select("div.m");
		
		Link root = new Link();
		root.setName("root");
		root.setId(0);
		int index = 1;
		int id = 1;
		for(Object obj : divs.toArray()) {
			if (obj instanceof Element) {
				Element div = (Element) obj;
				Link level1 = new Link();
				level1.setId(id);
				id++;
				level1.setLevel(1);
				level1.setSequence(index);
				
				Elements mt = div.select("div.mt");
				System.out.println(mt);
				String name = mt.select("h2 > a").text();
				level1.setName(name);
				
				int index2 = 1;
				Elements dls = div.select("div.mc > dl");
				if(dls.isEmpty()) continue;
				for(Iterator<Element> iter = dls.iterator(); iter.hasNext();) {
					Element dl = iter.next();
					Link level2 = new Link();
					level2.setId(id);
					id++;
					level2.setLevel(2);
					String name2 = dl.select("dt").text();
					level2.setName(name2);
					level2.setParent(level1);
					level2.setSequence(index2);

					int index3 = 1;
					Elements links = dl.select("dd > em > a");
					if(links.isEmpty()) continue;
					for(Iterator<Element> _iter = links.iterator(); _iter.hasNext();) {
						Element a = _iter.next();
						Link level3 = new Link();
						level3.setId(id);
						id++;
						level3.setSequence(index3);
						level3.setLevel(3);
						level3.setName(a.text());
						level3.setUrl(a.attr("href"));
						level3.setParent(level2);
						index3++;
					}
					index2++;
				}
				level1.setParent(root);
			}
			index++;
		}
		System.out.println(" ========================= ");
		String data = "";
		for(Link link : root.getChildren()) {
			if(link.getChildren().isEmpty()) continue;
			System.out.println(link.toSql());
			data += link.toSql() + "\n";
			for(Link child : link.getChildren()) {
				if(child.getChildren().isEmpty()) continue;
				System.out.println(child.toSql());
				data += child.toSql() + "\n";
				for(Link last : child.getChildren()) {
					System.out.println(last.toSql());
					data += last.toSql() + "\n";
				}
			}
		}
		
		writeToFile(data, "src/main/resources/jd-menu-data.sql");
		
		String json = JSON.toJSONString(root);
		System.out.println(json);
		writeToFile(json, "src/main/resources/jd-menu-data.json");
	}

	private void writeToFile(String data, String filename) throws IOException, UnsupportedEncodingException, FileNotFoundException {
		
		File dataFile = new File(filename);
		if(dataFile.exists()) {
			dataFile.deleteOnExit();
		}
		dataFile.createNewFile();
		
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(dataFile), "utf-8");
		out.write(data);
		out.flush();
		out.close();
	}
	
	class Link {
		private int id;
		private int pid;
		private int sequence = 0;
		private int level = 0;
		private String name = "";
		private String url = "";
		private Link parent = null;
		private List<Link> children = new ArrayList<JD.Link>();
		
		public void setId(int id) {
			this.id = id;
		}
		
		public int getId() {
			return id;
		}
		
		public void setPid(int pid) {
			this.pid = pid;
		}
		
		public int getPid() {
			return pid;
		}
		
		public void setSequence(int sequence) {
			this.sequence = sequence;
		}
		
		public int getSequence() {
			return sequence;
		}
		
		public void setLevel(int level) {
			this.level = level;
		}
		
		public int getLevel() {
			return level;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getUrl() {
			return url;
		}
		
		public void setParent(Link parent) {
			if(parent == null) {
				throw new IllegalArgumentException("parent is null");
			}
			this.parent = parent;
			this.parent.addChild(this);
			this.pid = parent.getId();
		}
		
		public Link getParent() {
			return parent;
		}
		
		public void addChild(Link link) {
			this.children.add(link);
		}
		
		public List<Link> getChildren() {
			return children;
		}

//		@Override
//		public String toString() {
//			return "Link [id=" + id + ", pid="+pid+" , sequence=" + sequence + ", level=" + level + ", name=" + name + ", url=" + url + ", parent=" + parent + ", children=" + children.size() + "]";
//		}

		public String toSql() {
			String sql = "insert into jd_menu values(";
			sql += this.id + "," + this.pid + "," + this.sequence + "," + this.level + ",'" + this.name + "'";
			if(StringUtil.isBlank(this.url)) {
				sql += ",null";
			} else {
				sql += ",'"+this.url+"'";
			}
			sql += ");";
			
			return sql;
		}
	}
}
