/*
enumeration of options that apply to handles

Attributes:
	HELICS_HANDLE_OPTION_CONNECTION_REQUIRED: value:397	specify that a connection is required for an interface and will generate an error if not available
	HELICS_HANDLE_OPTION_CONNECTION_OPTIONAL: value:402	specify that a connection is NOT required for an interface and will only be made if available no warning will be issues if not available
	HELICS_HANDLE_OPTION_SINGLE_CONNECTION_ONLY: value:407	specify that only a single connection is allowed for an interface
	HELICS_HANDLE_OPTION_MULTIPLE_CONNECTIONS_ALLOWED: value:409	specify that multiple connections are allowed for an interface
	HELICS_HANDLE_OPTION_BUFFER_DATA: value:411	specify that the last data should be buffered and sent on subscriptions after init
	HELICS_HANDLE_OPTION_STRICT_TYPE_CHECKING: value:414	specify that the types should be checked strictly for pub/sub and filters
	HELICS_HANDLE_OPTION_IGNORE_UNIT_MISMATCH: value:447	specify that the mismatching units should be ignored
	HELICS_HANDLE_OPTION_ONLY_TRANSMIT_ON_CHANGE: value:452	specify that an interface will only transmit on change(only applicable to publications)
	HELICS_HANDLE_OPTION_ONLY_UPDATE_ON_CHANGE: value:454	specify that an interface will only update if the value has actually changed
	HELICS_HANDLE_OPTION_IGNORE_INTERRUPTS: value:475	specify that an interface does not participate in determining time interrupts
	HELICS_HANDLE_OPTION_MULTI_INPUT_HANDLING_METHOD: value:507	specify the multi-input processing method for inputs
	HELICS_HANDLE_OPTION_INPUT_PRIORITY_LOCATION: value:510	specify the source index with the highest priority
	HELICS_HANDLE_OPTION_CLEAR_PRIORITY_LIST: value:512	specify that the priority list should be cleared or question if it is cleared
	HELICS_HANDLE_OPTION_CONNECTIONS: value:522	specify the required number of connections or get the actual number of connections
*/
package com.java.helics;

public enum HelicsHandleOptions{
	HELICS_HANDLE_OPTION_CONNECTION_REQUIRED(397),
	HELICS_HANDLE_OPTION_CONNECTION_OPTIONAL(402),
	HELICS_HANDLE_OPTION_SINGLE_CONNECTION_ONLY(407),
	HELICS_HANDLE_OPTION_MULTIPLE_CONNECTIONS_ALLOWED(409),
	HELICS_HANDLE_OPTION_BUFFER_DATA(411),
	HELICS_HANDLE_OPTION_STRICT_TYPE_CHECKING(414),
	HELICS_HANDLE_OPTION_IGNORE_UNIT_MISMATCH(447),
	HELICS_HANDLE_OPTION_ONLY_TRANSMIT_ON_CHANGE(452),
	HELICS_HANDLE_OPTION_ONLY_UPDATE_ON_CHANGE(454),
	HELICS_HANDLE_OPTION_IGNORE_INTERRUPTS(475),
	HELICS_HANDLE_OPTION_MULTI_INPUT_HANDLING_METHOD(507),
	HELICS_HANDLE_OPTION_INPUT_PRIORITY_LOCATION(510),
	HELICS_HANDLE_OPTION_CLEAR_PRIORITY_LIST(512),
	HELICS_HANDLE_OPTION_CONNECTIONS(522);
	private final int value;
	private HelicsHandleOptions(int value){
		this.value = value;
	}
	public int value(){
		return this.value;
	}
}