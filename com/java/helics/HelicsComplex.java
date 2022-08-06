/*
structure defining a basic complex type
*/
package com.java.helics;

import com.sun.jna.Structure;

public static class HelicsComplex extends Structure{
	/*None*/
	public double real;
	/*None*/
	public double imag;
}