package com.ihn.server.internal.launch;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServerLaunch  implements ApplicationContextAware{

    @Override
    public void setApplicationContext(ApplicationContext applicationcontext)
            throws BeansException {
        BizContext.setCtx(applicationcontext);
    }

}
