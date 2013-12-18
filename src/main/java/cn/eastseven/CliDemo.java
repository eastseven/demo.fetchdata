package cn.eastseven;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CliDemo {

	public static void main(String[] args) {
		Options options = new Options();
		options.addOption("start", false, "启动");
		options.addOption("stop", false, "停止");
		options.addOption("restart", false, "重启");
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(cmd == null) {
			System.out.println("cmd is null");
			return;
		}
		
		if(cmd.hasOption("start")) {
			System.out.println("system is start");
		}
		
		if(cmd.hasOption("stop")) {
			System.out.println("system is stop");
		}
		
		if(cmd.hasOption("restart")) {
			System.out.println("system is restart");
		}
	}

}
