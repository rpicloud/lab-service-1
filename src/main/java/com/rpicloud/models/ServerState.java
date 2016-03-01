package com.rpicloud.models;

import java.io.IOException;

/**
 * Created by mixmox on 01/03/16.
 */
public class ServerState {

    private String exception = null;
    private int timeout = 0;

    public void invoke() throws Exception {
        if(timeout != 0){
            try {
                Thread.sleep(timeout * 1000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        if (exception != null){
            if(exception.toLowerCase().equals("ioexception")){

                throw new IOException();
            }
            else if(exception.toLowerCase().equals("nullpointerexception")){
                throw new NullPointerException();
            }
            else {
                throw new Exception();
            }

        }
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
