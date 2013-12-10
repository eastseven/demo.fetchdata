package cn.eastseven;

import redis.clients.jedis.Jedis;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		Jedis jedis = new Jedis("10.10.10.22");
		jedis.set("foo", "bar");
		String value = jedis.get("foo");
		System.out.println(value);
	}
}
