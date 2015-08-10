package com.ihn.server.util.logging;

import java.net.URL;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import org.springframework.util.ResourceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IHNLogFactory {

	private Logger logger = LoggerFactory.getLogger("server");
	
    private LoggerContext lc;

    /**
     * 
     * @param file
     *            log configuration file path, for example <br>
     *            class path:"classpath:logback.xml" <br>
     *            or local file path: "src/resources/logback.xml"
     */
    public IHNLogFactory(String file) {
        try {
            URL url = ResourceUtils.getURL(file);
            reset(url);
        } catch (Exception je) {
            je.printStackTrace();
        }
    }

    /**
     * @param url
     *            logback configuration file path
     */
    public IHNLogFactory(URL url) {
        reset(url);
    }

    /**
     * get logger
     * 
     * @param logModule
     *            log name
     * @return
     */
    public Logger getLogger(String logModule) {
        return lc.getLogger(logModule);
    }

    /**
     * reset log
     * 
     * @param url
     *            
     */
    public void reset(URL url) {
        logger.info("LogFactory reset :" + url);
        try {
            String name = url.toString();
            int index = name.lastIndexOf(".");
            URL testurl = new URL(new StringBuilder(name.substring(0, index)).append("-test").append(
                name.substring(index)).toString());
            testurl.openConnection().connect();
            url = testurl;
        } catch (Exception e) {
        }
        logger.info("LogFactory configure :" + url);
        lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            lc.shutdownAndReset();
            configurator.setContext(lc);
            configurator.doConfigure(url);
            LogManager.reset(this);
            if (logger.isDebugEnabled()) {
                StatusPrinter.print(lc);
            }
            logger.info("LogFactory reset successful ...");
        } catch (JoranException je) {
            StatusPrinter.print(lc.getStatusManager());
        }
    }

    /**
     * stop log factory
     */
    public void destroy() {
        lc.stop();
    }
    
	
}
