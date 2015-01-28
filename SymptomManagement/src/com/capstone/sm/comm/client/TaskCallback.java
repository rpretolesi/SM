/* 
**
** Copyright 2014, Jules White
**
** 
*/
package com.capstone.sm.comm.client;

public interface TaskCallback<T> {

    public void success(T result);

    public void error(Exception e);

}
