package livelogsample;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.io.IOUtils;

import com.dlmorais.livelog.LiveLogConfig;

/**
 * Listener to generate the log file.
 * 
 * @author dlmorais (daniel.lemos.morais@gmail.com)
 */
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
					File log = new File(LiveLogConfig.getLogDir() + "livelog-sample.log");
					
					final String format = "\n%s - %s - %s :: %s";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String logLine = null;
					
					List<String> pseudoClass = Arrays.asList("foo.bar.ClassA", "com.dlmorais.ClassB", "com.jepslon.ClassC");
					List<String> levels = Arrays.asList("DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL", "FATAL");
					
					List<String> contents = Arrays.asList(
							"Mussum Ipsum, cacilds vidis litro abertis.",
							"Admodum accumsan disputationi eu sit.",
							"Vide electram sadipscing et per.",
							"Leite de capivaris, leite de mula manquis.",
							"Viva Forevis aptent taciti sociosqu ad litora torquent Nao sou faixa preta cumpadi, sou preto inteiris, inteiris.");
					
					while (true) {
						Collections.shuffle(pseudoClass);
						Collections.shuffle(levels);
						Collections.shuffle(contents);
						
						logLine = String.format(format, 
								sdf.format(Calendar.getInstance().getTime()),
								pseudoClass.get(0),
								levels.get(0),
								contents.get(0));
						
						IOUtils.write(logLine.getBytes(), new FileOutputStream(log, true));
						Thread.sleep(1000);
					}
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
