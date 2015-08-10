package com.ihn.server.util.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author
 * @since 
 *
 */
public class StreamGobbler extends Thread
{
    InputStream is;
    String type;  
    
    StreamGobbler(InputStream is, String type){
        this.is = is;
        this.type = type;
    }
    
    public void run(){
        try{
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line=null;
            while ( (line = br.readLine()) != null){
                System.out.println(type + ">" + line);
                System.out.flush();
            }
        } catch (IOException ioe){
                ioe.printStackTrace();  
        }
    }
    
} 

