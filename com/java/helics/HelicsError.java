/*
helics error object
*/
package com.java.helics;

import com.sun.jna.Structure;

public static class HelicsError extends Structure{
	/*an error code associated with the error*/
	public int error_code;
	/*a message associated with the error*/
	public string message;
}