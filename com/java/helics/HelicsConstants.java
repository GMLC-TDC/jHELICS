
package com.java.helics;

public final class HelicsConstants{
	public static final int HELICS_INVALID_OPTION_INDEX =  -101;
/*
result returned for requesting the value of an invalid/unknown property
*/

	public static final int HELICS_INVALID_PROPERTY_VALUE =  -972;

	public static final double cHelicsBigNumber =  HELICS_BIG_NUMBER;/*
definition of time zero-the beginning of simulation
*/

	public static final double HELICS_TIME_ZERO =  0.0;
/*
definition of the minimum time resolution
*/

	public static final double HELICS_TIME_EPSILON =  1e-09;
/*
definition of an invalid time that has no meaning
*/

	public static final double HELICS_TIME_INVALID =  -1.785e+39;

	/*
	definition of time signifying the federate has terminated or run until the end of the simulation
	*/
	public static final double HELICS_TIME_MAXTIME =  HELICS_BIG_NUMBER;/*
indicator used for a true response
*/

	public static final int HELICS_TRUE =  1;
/*
indicator used for a false response
*/

	public static final int HELICS_FALSE =  0;

	private HelicsConstants(){}
}