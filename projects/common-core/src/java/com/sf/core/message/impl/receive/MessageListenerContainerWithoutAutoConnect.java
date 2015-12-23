/*
 * $Id: MessageListenerContainerWithoutAutoConnect.java, 2015-8-15 下午02:54:00 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.message.impl.receive;

import javax.jms.JMSException;

import org.springframework.jms.listener.AbstractJmsListeningContainer;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 * <p>
 * Title: MessageListenerContainerWithoutAutoConnect
 * </p>
 * <p>
 * Description: 不自动重连的jms receiver listener
 * </p>
 * 
 * @author sufeng
 */
public class MessageListenerContainerWithoutAutoConnect extends DefaultMessageListenerContainer {

    /**
     * 此处不做任何重连操作
     * @see org.springframework.jms.listener.DefaultMessageListenerContainer#recoverAfterListenerSetupFailure()
     */
    @Override
    protected void recoverAfterListenerSetupFailure() {
        //super.recoverAfterListenerSetupFailure();
        // 此处不做任何重连操作，do nothing
    }
    
    @Override
    protected void handleListenerSetupFailure(Throwable ex, boolean alreadyRecovered) {
        if(ex instanceof JMSException)
            invokeExceptionListener((JMSException)ex);
        if(ex instanceof AbstractJmsListeningContainer.SharedConnectionNotInitializedException){
            if(!alreadyRecovered)
                logger.debug("JMS message listener invoker needs to establish shared Connection");
        } else if(alreadyRecovered){
            logger.debug("Setup of JMS message listener invoker failed - already recovered by other invoker", ex);
        } else{
            //StringBuffer msg = new StringBuffer();
            //msg.append("Setup of JMS message listener invoker failed for destination '");
            //msg.append(getDestinationDescription()).append("' - trying to recover. Cause: ");
            //msg.append((ex instanceof JMSException) ? JmsUtils.buildExceptionMessage((JMSException)ex) : ex.getMessage());
            //if(logger.isDebugEnabled())
            //    logger.info(msg, ex);
            //else
            //    logger.info(msg);
        }
    }
    
}
