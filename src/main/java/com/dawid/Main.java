package com.dawid;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.mail.Session;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


@SpringBootApplication
public class Main {

	static Path log;
	static Date[] weekends;
	static Session session;

	public static void main(String[] args) {

		new SpringApplicationBuilder(Main.class).headless(false).run(args);

		log = new File("C:\\Users\\" + System.getProperty("user.name").toLowerCase() + "\\HolidayApp\\Log").toPath();
		weekends = getDays();
		if(!Files.exists(log)){
			try {
				Files.createDirectories(log.getParent());
				Files.createFile(log);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		new Login();
	}
	public static Date[] getDays() {
		Calendar calendar = new GregorianCalendar(2017, 0, 1);
		Date[] weekends = new Date[105];

		int j = 0;
		for (int i = 0; i < 366 && calendar.get(Calendar.YEAR) == 2017; i++) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				Date d = calendar.getTime();
				//System.out.println(d);
				weekends[j] = d;
				j++;
			}
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return weekends;
	}
}
