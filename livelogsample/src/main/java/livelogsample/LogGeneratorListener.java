package livelogsample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.IOUtils;

@WebListener
public class LogGeneratorListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					File log = new File("C:/Users/danie/Programs/apache-tomcat-8.0.35/logs/Teste.log");
					
					while (true) {
						IOUtils.write(("\n"+Calendar.getInstance().getTime().toLocaleString()).getBytes(), new FileOutputStream(log, true));
						Thread.sleep(2000);
					}
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

}
