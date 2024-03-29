/*
enumeration of the different iteration results

Attributes:
	HELICS_ITERATION_REQUEST_NO_ITERATION: value:0	no iteration is requested
	HELICS_ITERATION_REQUEST_FORCE_ITERATION: value:1	force iteration return when able
	HELICS_ITERATION_REQUEST_ITERATE_IF_NEEDED: value:2	only return an iteration if necessary
*/
package com.java.helics;

public enum HelicsIterationRequest{
	HELICS_ITERATION_REQUEST_NO_ITERATION(0),
	HELICS_ITERATION_REQUEST_FORCE_ITERATION(1),
	HELICS_ITERATION_REQUEST_ITERATE_IF_NEEDED(2);
	private final int value;
	private HelicsIterationRequest(int value){
		this.value = value;
	}
	public int value(){
		return this.value;
	}
}