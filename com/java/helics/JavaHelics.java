/*
Copyright (c) 2017-2022,
Battelle Memorial Institute; Lawrence Livermore National Security, LLC; Alliance for Sustainable Energy, LLC.  See
the top-level NOTICE for additional details. All rights reserved.
SPDX-License-Identifier: BSD-3-Clause
*/

package com.java.helics;

import static com.java.helics.HelicsBigNumber.*;
import com.sun.jna.*;
import com.sun.jna.ptr.*;

public class JavaHELICS {
	
	public static final int HELICS_INVALID_OPTION_INDEX =  -101;

	/*
result returned for requesting the value of an invalid/unknown property
*/

	public static final int HELICS_INVALID_PROPERTY_VALUE =  -972;

	
	public static final double cHelicsBigNumber =  HELICS_BIG_NUMBER;
	/*
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
	public static final double HELICS_TIME_MAXTIME =  HELICS_BIG_NUMBER;
	/*
indicator used for a true response
*/

	public static final int HELICS_TRUE =  1;

	/*
indicator used for a false response
*/

	public static final int HELICS_FALSE =  0;

	public interface HelicsInterface extends Library {
		HelicsInterface INSTANCE = (HelicsInterface)Native.loadLibrary('helics', HelicsInterface.class);
		
		/** create a helics managed data buffer with initial capacity*/
		
		HelicsDataBuffer helicsCreateDataBuffer(int initialCapacity);
		/** check whether a buffer is valid*/
		
		int helicsDataBufferIsValid(HelicsDataBuffer data);
		/** wrap user data in a buffer object*/
		
		HelicsDataBuffer helicsWrapDataInBuffer(Pointer data,int dataSize,int dataCapacity);
		/** free a DataBuffer */
		
		void helicsDataBufferFree(HelicsDataBuffer data);
		/** get the data buffer size*/
		
		int helicsDataBufferSize(HelicsDataBuffer data);
		/** get the data buffer capacity*/
		
		int helicsDataBufferCapacity(HelicsDataBuffer data);
		/** get a pointer to the raw data*/
		
		Pointer helicsDataBufferData(HelicsDataBuffer data);
		/** increase the capacity a data buffer can hold without reallocating memory
@return HELICS_TRUE if the reservation was successful HELICS_FALSE otherwise*/
		
		int helicsDataBufferReserve(HelicsDataBuffer data,int newCapacity);
		/** create a new data buffer and copy an existing buffer*/
		
		HelicsDataBuffer helicsDataBufferClone(HelicsDataBuffer data);
		/** convert an integer to serialized bytes*/
		
		int helicsIntegerToBytes(int value,HelicsDataBuffer data);
		/** convert a double to serialized bytes*/
		
		int helicsDoubleToBytes(double value,HelicsDataBuffer data);
		/** convert a string to serialized bytes*/
		
		int helicsStringToBytes(String value,HelicsDataBuffer data);
		/** convert a raw string (may contain nulls) to serialized bytes*/
		
		int helicsRawStringToBytes(String str,int stringSize,HelicsDataBuffer data);
		/** convert a bool to serialized bytes*/
		
		int helicsBooleanToBytes(int value,HelicsDataBuffer data);
		/** convert a char to serialized bytes*/
		
		int helicsCharToBytes(char value,HelicsDataBuffer data);
		/** convert a named point to serialized bytes*/
		
		int helicsNamedPointToBytes(String name,double value,HelicsDataBuffer data);
		/** extract the data type from the data buffer, if the type isn't recognized UNKNOWN is returned*/
		
		int helicsDataBufferType(HelicsDataBuffer data);
		/** convert a data buffer to an int*/
		
		int helicsDataBufferToInteger(HelicsDataBuffer data);
		/** convert a data buffer to a double*/
		
		double helicsDataBufferToDouble(HelicsDataBuffer data);
		/** convert a data buffer to a boolean*/
		
		int helicsDataBufferToBoolean(HelicsDataBuffer data);
		/** convert a data buffer to a char*/
		
		char helicsDataBufferToChar(HelicsDataBuffer data);
		/** get the size of memory required to retrieve a string from a data buffer this includes space for a null terminator*/
		
		int helicsDataBufferStringSize(HelicsDataBuffer data);
		/** convert a data buffer to a time*/
		
		double helicsDataBufferToTime(HelicsDataBuffer data);
		/** convert a data buffer to complex values*/
		
		void helicsDataBufferToComplex(HelicsDataBuffer data,DoubleByReference real,DoubleByReference imag);
		/** get the number of elements that would be required if a vector were retrieved*/
		
		int helicsDataBufferVectorSize(HelicsDataBuffer data);
		/** convert the data in a data buffer to a different type representation
@param data the buffer to convert
@param newDataType the type that it is desired for the buffer to be converted to
@return true if the conversion was successful*/
		
		int helicsDataBufferConvertToType(HelicsDataBuffer data,int newDataType);
		/**
 * Get a version string for HELICS.
 */
		
		String helicsGetVersion();
		/**
 * Get the build flags used to compile HELICS.
 */
		
		String helicsGetBuildFlags();
		/**
 * Get the compiler version used to compile HELICS.
 */
		
		String helicsGetCompilerVersion();
		/**
 * Get a json formatted system information string, containing version info.
 * The string contains fields with system information like cpu, core count, operating system, and memory,
 * as well as information about the HELICS build.  Used for debugging reports and gathering other information.
 */
		
		String helicsGetSystemInfo();
		/** Load a signal handler that handles Ctrl-C and shuts down all HELICS brokers, cores,
and federates then exits the process.*/
		
		void helicsLoadSignalHandler();
		/** Load a signal handler that handles Ctrl-C and shuts down all HELICS brokers, cores,
and federates then exits the process.  This operation will execute in a newly created and detached thread returning control back to the
calling program before completing operations.*/
		
		void helicsLoadThreadedSignalHandler();
		/** Clear HELICS based signal handlers.*/
		
		void helicsClearSignalHandler();
		/** Execute a global abort by sending an error code to all cores, brokers,
and federates that were created through the current library instance.*/
		
		void helicsAbort(int errorCode,String errorString);
		/**
 * Returns true if core/broker type specified is available in current compilation.
 *
 * @param type A string representing a core type.
 *
 * @details Options include "zmq", "udp", "ipc", "interprocess", "tcp", "default", "mpi".
 */
		
		int helicsIsCoreTypeAvailable(String type);
		/**
 * Create a core object.
 *
 * @param type The type of the core to create.
 * @param name The name of the core. It can be a nullptr or empty string to have a name automatically assigned.
 * @param initString An initialization string to send to the core. The format is similar to command line arguments.
 *                   Typical options include a broker name, the broker address, the number of federates, etc.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return A HelicsCore object.
 *
 * If the core is invalid, err will contain the corresponding error message and the returned object will be NULL.

 */
		
		HelicsCore helicsCreateCore(String type,String name,String initString,HelicsError err);
		/**
 * Create a new reference to an existing core.
 *
 * @details This will create a new broker object that references the existing broker. The new broker object must be freed as well.
 *
 * @param core An existing HelicsCore.
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return A new reference to the same broker.
 */
		
		HelicsCore helicsCoreClone(HelicsCore core,HelicsError err);
		/**
 * Check if a core object is a valid object.
 *
 * @param core The HelicsCore object to test.
 */
		
		int helicsCoreIsValid(HelicsCore core);
		/**
 * Create a broker object.
 *
 * @param type The type of the broker to create.
 * @param name The name of the broker. It can be a nullptr or empty string to have a name automatically assigned.
 * @param initString An initialization string to send to the core-the format is similar to command line arguments.
 *                   Typical options include a broker address such as --broker="XSSAF" if this is a subbroker, or the number of federates,
 * or the address.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return A HelicsBroker object.
 *
 * It will be NULL if there was an error indicated in the err object.

 */
		
		HelicsBroker helicsCreateBroker(String type,String name,String initString,HelicsError err);
		/**
 * Create a new reference to an existing broker.
 *
 * @details This will create a new broker object that references the existing broker it must be freed as well.
 *
 * @param broker An existing HelicsBroker.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return A new reference to the same broker.
 */
		
		HelicsBroker helicsBrokerClone(HelicsBroker broker,HelicsError err);
		/**
 * Check if a broker object is a valid object.
 *
 * @param broker The HelicsBroker object to test.
 */
		
		int helicsBrokerIsValid(HelicsBroker broker);
		/**
 * Check if a broker is connected.
 *
 * @details A connected broker implies it is attached to cores or cores could reach out to communicate.
 *
 * @return HELICS_FALSE if not connected.
 */
		
		int helicsBrokerIsConnected(HelicsBroker broker);
		/**
 * Link a named publication and named input using a broker.
 *
 * @param broker The broker to generate the connection from.
 * @param source The name of the publication (cannot be NULL).
 * @param target The name of the target to send the publication data (cannot be NULL).
 *
 * @param[in,out] err A HelicsError object, can be NULL if the errors are to be ignored.

 */
		
		void helicsBrokerDataLink(HelicsBroker broker,String source,String target,HelicsError err);
		/**
 * Link a named filter to a source endpoint.
 *
 * @param broker The broker to generate the connection from.
 * @param filter The name of the filter (cannot be NULL).
 * @param endpoint The name of the endpoint to filter the data from (cannot be NULL).
 *
 * @param[in,out] err A HelicsError object, can be NULL if the errors are to be ignored.

 */
		
		void helicsBrokerAddSourceFilterToEndpoint(HelicsBroker broker,String filter,String endpoint,HelicsError err);
		/**
 * Link a named filter to a destination endpoint.
 *
 * @param broker The broker to generate the connection from.
 * @param filter The name of the filter (cannot be NULL).
 * @param endpoint The name of the endpoint to filter the data going to (cannot be NULL).
 *
 * @param[in,out] err A HelicsError object, can be NULL if the errors are to be ignored.

 */
		
		void helicsBrokerAddDestinationFilterToEndpoint(HelicsBroker broker,String filter,String endpoint,HelicsError err);
		/**
 * Load a file containing connection information.
 *
 * @param broker The broker to generate the connections from.
 * @param file A JSON or TOML file containing connection information.
 *
 * @param[in,out] err A HelicsError object, can be NULL if the errors are to be ignored.

 */
		
		void helicsBrokerMakeConnections(HelicsBroker broker,String file,HelicsError err);
		/**
 * Wait for the core to disconnect.
 *
 * @param core The core to wait for.
 * @param msToWait The time out in millisecond (<0 for infinite timeout).
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return HELICS_TRUE if the disconnect was successful, HELICS_FALSE if there was a timeout.
 */
		
		int helicsCoreWaitForDisconnect(HelicsCore core,int msToWait,HelicsError err);
		/**
 * Wait for the broker to disconnect.
 *
 * @param broker The broker to wait for.
 * @param msToWait The time out in millisecond (<0 for infinite timeout).
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return HELICS_TRUE if the disconnect was successful, HELICS_FALSE if there was a timeout.
 */
		
		int helicsBrokerWaitForDisconnect(HelicsBroker broker,int msToWait,HelicsError err);
		/**
 * Check if a core is connected.
 *
 * @details A connected core implies it is attached to federates or federates could be attached to it
 *
 * @return HELICS_FALSE if not connected, HELICS_TRUE if it is connected.
 */
		
		int helicsCoreIsConnected(HelicsCore core);
		/**
 * Link a named publication and named input using a core.
 *
 * @param core The core to generate the connection from.
 * @param source The name of the publication (cannot be NULL).
 * @param target The name of the target to send the publication data (cannot be NULL).
 *
 * @param[in,out] err A HelicsError object, can be NULL if the errors are to be ignored.

 */
		
		void helicsCoreDataLink(HelicsCore core,String source,String target,HelicsError err);
		/**
 * Link a named filter to a source endpoint.
 *
 * @param core The core to generate the connection from.
 * @param filter The name of the filter (cannot be NULL).
 * @param endpoint The name of the endpoint to filter the data from (cannot be NULL).
 *
 * @param[in,out] err A HelicsError object, can be NULL if the errors are to be ignored.

 */
		
		void helicsCoreAddSourceFilterToEndpoint(HelicsCore core,String filter,String endpoint,HelicsError err);
		/**
 * Link a named filter to a destination endpoint.
 *
 * @param core The core to generate the connection from.
 * @param filter The name of the filter (cannot be NULL).
 * @param endpoint The name of the endpoint to filter the data going to (cannot be NULL).
 *
 * @param[in,out] err A HelicsError object, can be NULL if the errors are to be ignored.

 */
		
		void helicsCoreAddDestinationFilterToEndpoint(HelicsCore core,String filter,String endpoint,HelicsError err);
		/**
 * Load a file containing connection information.
 *
 * @param core The core to generate the connections from.
 * @param file A JSON or TOML file containing connection information.
 *
 * @param[in,out] err A HelicsError object, can be NULL if the errors are to be ignored.

 */
		
		void helicsCoreMakeConnections(HelicsCore core,String file,HelicsError err);
		/**
 * Get an identifier for the broker.
 *
 * @param broker The broker to query.
 *
 * @return A string containing the identifier for the broker.
 */
		
		String helicsBrokerGetIdentifier(HelicsBroker broker);
		/**
 * Get an identifier for the core.
 *
 * @param core The core to query.
 *
 * @return A string with the identifier of the core.
 */
		
		String helicsCoreGetIdentifier(HelicsCore core);
		/**
 * Get the network address associated with a broker.
 *
 * @param broker The broker to query.
 *
 * @return A string with the network address of the broker.
 */
		
		String helicsBrokerGetAddress(HelicsBroker broker);
		/**
 * Get the network address associated with a core.
 *
 * @param core The core to query.
 *
 * @return A string with the network address of the broker.
 */
		
		String helicsCoreGetAddress(HelicsCore core);
		/**
 * Set the core to ready for init.
 *
 * @details This function is used for cores that have filters but no federates so there needs to be
 *          a direct signal to the core to trigger the federation initialization.
 *
 * @param core The core object to enable init values for.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsCoreSetReadyToInit(HelicsCore core,HelicsError err);
		/**
 * Connect a core to the federate based on current configuration.
 *
 * @param core The core to connect.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return HELICS_FALSE if not connected, HELICS_TRUE if it is connected.
 */
		
		int helicsCoreConnect(HelicsCore core,HelicsError err);
		/**
 * Disconnect a core from the federation.
 *
 * @param core The core to query.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsCoreDisconnect(HelicsCore core,HelicsError err);
		/**
 * Get an existing federate object from a core by name.
 *
 * @details The federate must have been created by one of the other functions and at least one of the objects referencing the created
 *          federate must still be active in the process.
 *
 * @param fedName The name of the federate to retrieve.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return NULL if no fed is available by that name otherwise a HelicsFederate with that name.
 */
		
		HelicsFederate helicsGetFederateByName(String fedName,HelicsError err);
		/**
 * Disconnect a broker.
 *
 * @param broker The broker to disconnect.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsBrokerDisconnect(HelicsBroker broker,HelicsError err);
		/**
 * Disconnect and free a federate.
 */
		
		void helicsFederateDestroy(HelicsFederate fed);
		/**
 * Disconnect and free a broker.
 */
		
		void helicsBrokerDestroy(HelicsBroker broker);
		/**
 * Disconnect and free a core.
 */
		
		void helicsCoreDestroy(HelicsCore core);
		/**
 * Release the memory associated with a core.
 */
		
		void helicsCoreFree(HelicsCore core);
		/**
 * Release the memory associated with a broker.
 */
		
		void helicsBrokerFree(HelicsBroker broker);
		/**
 * Create a value federate from a federate info object.
 *
 * @details HelicsFederate objects can be used in all functions that take a HelicsFederate or HelicsFederate object as an argument.
 *
 * @param fedName The name of the federate to create, can NULL or an empty string to use the default name from fi or an assigned name.
 * @param fi The federate info object that contains details on the federate.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return An opaque value federate object.
 */
		
		HelicsFederate helicsCreateValueFederate(String fedName,HelicsFederateInfo fi,HelicsError err);
		/**
 * Create a value federate from a JSON file, JSON string, or TOML file.
 *
 * @details HelicsFederate objects can be used in all functions that take a HelicsFederate or HelicsFederate object as an argument.
 *
 * @param configFile A JSON file or a JSON string or TOML file that contains setup and configuration information.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return An opaque value federate object.
 */
		
		HelicsFederate helicsCreateValueFederateFromConfig(String configFile,HelicsError err);
		/**
 * Create a message federate from a federate info object.
 *
 * @details helics_message_federate objects can be used in all functions that take a helics_message_federate or HelicsFederate object as an
 * argument.
 *
 * @param fedName The name of the federate to create.
 * @param fi The federate info object that contains details on the federate.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return An opaque message federate object.
 */
		
		HelicsFederate helicsCreateMessageFederate(String fedName,HelicsFederateInfo fi,HelicsError err);
		/**
 * Create a message federate from a JSON file or JSON string or TOML file.
 *
 * @details helics_message_federate objects can be used in all functions that take a helics_message_federate or HelicsFederate object as an
 * argument.
 *
 * @param configFile A Config(JSON,TOML) file or a JSON string that contains setup and configuration information.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return An opaque message federate object.
 */
		
		HelicsFederate helicsCreateMessageFederateFromConfig(String configFile,HelicsError err);
		/**
 * Create a combination federate from a federate info object.
 *
 * @details Combination federates are both value federates and message federates, objects can be used in all functions
 *                      that take a HelicsFederate, helics_message_federate or HelicsFederate object as an argument
 *
 * @param fedName A string with the name of the federate, can be NULL or an empty string to pull the default name from fi.
 * @param fi The federate info object that contains details on the federate.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return An opaque value federate object nullptr if the object creation failed.
 */
		
		HelicsFederate helicsCreateCombinationFederate(String fedName,HelicsFederateInfo fi,HelicsError err);
		/**
 * Create a combination federate from a JSON file or JSON string or TOML file.
 *
 * @details Combination federates are both value federates and message federates, objects can be used in all functions
 *          that take a HelicsFederate, helics_message_federate or HelicsFederate object as an argument
 *
 * @param configFile A JSON file or a JSON string or TOML file that contains setup and configuration information.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return An opaque combination federate object.
 */
		
		HelicsFederate helicsCreateCombinationFederateFromConfig(String configFile,HelicsError err);
		/**
 * Create a new reference to an existing federate.
 *
 * @details This will create a new HelicsFederate object that references the existing federate. The new object must be freed as well.
 *
 * @param fed An existing HelicsFederate.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return A new reference to the same federate.
 */
		
		HelicsFederate helicsFederateClone(HelicsFederate fed,HelicsError err);
		/**
 * Protect a federate from finalizing and closing if all references go out of scope
 *
 * @details this function allows a federate to be retrieved on demand, it must be explicitly close later otherwise it will be destroyed
 * when the library is closed
 *
 * @param fedName The name of an existing HelicsFederate.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error
 occurred during the execution of the function, in particular if no federate with the given name exists
 */
		
		void helicsFederateProtect(String fedName,HelicsError err);
		/**
 * remove the protection of an existing federate
 *
 * @details this function allows a federate to be retrieved on demand, it must be explicitly close
 later otherwise it will be destroyed
 * when the library is closed
 *
 * @param fed the name of an existing federate that should not be protected
 *
 * @param[in,out] err An error object that will contain an error code and string if the federate was not found.
 */
		
		void helicsFederateUnProtect(String fedName,HelicsError err);
		/**
 * checks if an existing federate is protected
 *
 *
 * @param fed the name of an existing federate to check the protection status
 *
 * @param[in,out] err An error object that will contain an error code and string if the federate was not found.
 */
		
		int helicsFederateIsProtected(String fedName,HelicsError err);
		/**
 * Create a federate info object for specifying federate information when constructing a federate.
 *
 * @return A HelicsFederateInfo object which is a reference to the created object.
 */
		
		HelicsFederateInfo helicsCreateFederateInfo();
		/**
 * Create a federate info object from an existing one and clone the information.
 *
 * @param fi A federateInfo object to duplicate.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 *  @return A HelicsFederateInfo object which is a reference to the created object.
 */
		
		HelicsFederateInfo helicsFederateInfoClone(HelicsFederateInfo fi,HelicsError err);
		/**
 * Load federate info from command line arguments contained in a string.
 *
 * @param fi A federateInfo object.
 * @param args Command line arguments specified in a string.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoLoadFromString(HelicsFederateInfo fi,String args,HelicsError err);
		/**
 * Delete the memory associated with a federate info object.
 */
		
		void helicsFederateInfoFree(HelicsFederateInfo fi);
		/**
 * Check if a federate_object is valid.
 *
 * @return HELICS_TRUE if the federate is a valid active federate, HELICS_FALSE otherwise
 */
		
		int helicsFederateIsValid(HelicsFederate fed);
		/**
 * Set the name of the core to link to for a federate.
 *
 * @param fi The federate info object to alter.
 * @param corename The identifier for a core to link to.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoSetCoreName(HelicsFederateInfo fi,String corename,HelicsError err);
		/**
 * Set the initialization string for the core usually in the form of command line arguments.
 *
 * @param fi The federate info object to alter.
 * @param coreInit A string containing command line arguments to be passed to the core.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoSetCoreInitString(HelicsFederateInfo fi,String coreInit,HelicsError err);
		/**
 * Set the initialization string that a core will pass to a generated broker usually in the form of command line arguments.
 *
 * @param fi The federate info object to alter.
 * @param brokerInit A string with command line arguments for a generated broker.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoSetBrokerInitString(HelicsFederateInfo fi,String brokerInit,HelicsError err);
		/**
 * Set the core type by integer code.
 *
 * @details Valid values available by definitions in api-data.h.
 * @param fi The federate info object to alter.
 * @param coretype An numerical code for a core type see /ref helics_CoreType.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoSetCoreType(HelicsFederateInfo fi,int coretype,HelicsError err);
		/**
 * Set the core type from a string.
 *
 * @param fi The federate info object to alter.
 * @param coretype A string naming a core type.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoSetCoreTypeFromString(HelicsFederateInfo fi,String coretype,HelicsError err);
		/**
 * Set the name or connection information for a broker.
 *
 * @details This is only used if the core is automatically created, the broker information will be transferred to the core for connection.
 * @param fi The federate info object to alter.
 * @param broker A string which defines the connection information for a broker either a name or an address.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoSetBroker(HelicsFederateInfo fi,String broker,HelicsError err);
		/**
 * Set the key for a broker connection.
 *
 * @details This is only used if the core is automatically created, the broker information will be transferred to the core for connection.
 * @param fi The federate info object to alter.
 * @param brokerkey A string containing a key for the broker to connect.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoSetBrokerKey(HelicsFederateInfo fi,String brokerkey,HelicsError err);
		/**
 * Set the port to use for the broker.
 *
 * @details This is only used if the core is automatically created, the broker information will be transferred to the core for connection.
 * This will only be useful for network broker connections.
 * @param fi The federate info object to alter.
 * @param brokerPort The integer port number to use for connection with a broker.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoSetBrokerPort(HelicsFederateInfo fi,int brokerPort,HelicsError err);
		/**
 * Set the local port to use.
 *
 * @details This is only used if the core is automatically created, the port information will be transferred to the core for connection.
 * @param fi The federate info object to alter.
 * @param localPort A string with the port information to use as the local server port can be a number or "auto" or "os_local".
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 */
		
		void helicsFederateInfoSetLocalPort(HelicsFederateInfo fi,String localPort,HelicsError err);
		/**
 * Get a property index for use in /ref helicsFederateInfoSetFlagOption, /ref helicsFederateInfoSetTimeProperty,
 * or /ref helicsFederateInfoSetIntegerProperty
 * @param val A string with the property name.
 * @return An int with the property code or (-1) if not a valid property.
 */
		
		int helicsGetPropertyIndex(String val);
		/**
 * Get a property index for use in /ref helicsFederateInfoSetFlagOption, /ref helicsFederateSetFlagOption,
 * @param val A string with the option name.
 * @return An int with the property code or (-1) if not a valid property.
 */
		
		int helicsGetFlagIndex(String val);
		/**
 * Get an option index for use in /ref helicsPublicationSetOption, /ref helicsInputSetOption, /ref helicsEndpointSetOption,
 * /ref helicsFilterSetOption, and the corresponding get functions.
 *
 * @param val A string with the option name.
 *
 * @return An int with the option index or (-1) if not a valid property.
 */
		
		int helicsGetOptionIndex(String val);
		/**
 * Get an option value for use in /ref helicsPublicationSetOption, /ref helicsInputSetOption, /ref helicsEndpointSetOption,
 * /ref helicsFilterSetOption.
 *
 * @param val A string representing the value.
 *
 * @return An int with the option value or (-1) if not a valid value.
 */
		
		int helicsGetOptionValue(String val);
		/**
 * Get the data type for use in /ref helicsFederateRegisterPublication, /ref helicsFederateRegisterInput,
 * /ref helicsFilterSetOption.
 *
 * @param val A string representing a data type.
 *
 * @return An int with the data type or HELICS_DATA_TYPE_UNKNOWN(-1) if not a valid value.
 */
		
		int helicsGetDataType(String val);
		/**
 * Set a flag in the info structure.
 *
 * @details Valid flags are available /ref helics_federate_flags.
 * @param fi The federate info object to alter.
 * @param flag A numerical index for a flag.
 * @param value The desired value of the flag HELICS_TRUE or HELICS_FALSE.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateInfoSetFlagOption(HelicsFederateInfo fi,int flag,int value,HelicsError err);
		/**
 * Set the separator character in the info structure.
 *
 * @details The separator character is the separation character for local publications/endpoints in creating their global name.
 * For example if the separator character is '/'  then a local endpoint would have a globally reachable name of fedName/localName.
 * @param fi The federate info object to alter.
 * @param separator The character to use as a separator.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateInfoSetSeparator(HelicsFederateInfo fi,char separator,HelicsError err);
		/**
 * Set the output delay for a federate.
 *
 * @param fi The federate info object to alter.
 * @param timeProperty An integer representation of the time based property to set see /ref helics_properties.
 * @param propertyValue The value of the property to set the timeProperty to.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateInfoSetTimeProperty(HelicsFederateInfo fi,int timeProperty,double propertyValue,HelicsError err);
		/**
 * Set an integer property for a federate.
 *
 * @details Set known properties.
 *
 * @param fi The federateInfo object to alter.
 * @param intProperty An int identifying the property.
 * @param propertyValue The value to set the property to.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateInfoSetIntegerProperty(HelicsFederateInfo fi,int intProperty,int propertyValue,HelicsError err);
		/**
 * Load interfaces from a file.
 *
 * @param fed The federate to which to load interfaces.
 * @param file The name of a file to load the interfaces from either JSON, or TOML.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateRegisterInterfaces(HelicsFederate fed,String file,HelicsError err);
		/**
 * Generate a global error from a federate.
 *
 * @details A global error halts the co-simulation completely.
 *
 * @param fed The federate to create an error in.
 * @param errorCode The integer code for the error.
 * @param errorString A string describing the error.
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateGlobalError(HelicsFederate fed,int errorCode,String errorString,HelicsError err);
		/**
 * Generate a local error in a federate.
 *
 * @details This will propagate through the co-simulation but not necessarily halt the co-simulation, it has a similar effect to finalize
 * but does allow some interaction with a core for a brief time.
 * @param fed The federate to create an error in.
 * @param errorCode The integer code for the error.
 * @param errorString A string describing the error.
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateLocalError(HelicsFederate fed,int errorCode,String errorString,HelicsError err);
		/**
 * Disconnect/finalize the federate. This function halts all communication in the federate and disconnects it from the core.
 */
		
		void helicsFederateFinalize(HelicsFederate fed,HelicsError err);
		/**
 * Disconnect/finalize the federate in an async call.
 */
		
		void helicsFederateFinalizeAsync(HelicsFederate fed,HelicsError err);
		/**
 * Complete the asynchronous disconnect/finalize call.
 */
		
		void helicsFederateFinalizeComplete(HelicsFederate fed,HelicsError err);
		/**
 * Disconnect/finalize the federate. This function halts all communication in the federate and disconnects it
 * from the core.  This call is identical to helicsFederateFinalize.
 */
		
		void helicsFederateDisconnect(HelicsFederate fed,HelicsError err);
		/**
 * Disconnect/finalize the federate in an async call.  This call is identical to helicsFederateFinalizeAsync.
 */
		
		void helicsFederateDisconnectAsync(HelicsFederate fed,HelicsError err);
		/**
 * Complete the asynchronous disconnect/finalize call.  This call is identical to helicsFederateFinalizeComplete
 */
		
		void helicsFederateDisconnectComplete(HelicsFederate fed,HelicsError err);
		/**
 * Release the memory associated with a federate.
 */
		
		void helicsFederateFree(HelicsFederate fed);
		/**
 * Enter the initialization state of a federate.
 *
 * @details The initialization state allows initial values to be set and received if the iteration is requested on entry to the execution
 * state. This is a blocking call and will block until the core allows it to proceed.
 *
 * @param fed The federate to operate on.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateEnterInitializingMode(HelicsFederate fed,HelicsError err);
		/**
 * Non blocking alternative to \ref helicsFederateEnterInitializingMode.
 *
 * @details The function helicsFederateEnterInitializationModeFinalize must be called to finish the operation.
 *
 * @param fed The federate to operate on.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateEnterInitializingModeAsync(HelicsFederate fed,HelicsError err);
		/**
 * Check if the current Asynchronous operation has completed.
 *
 * @param fed The federate to operate on.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return HELICS_FALSE if not completed, HELICS_TRUE if completed.
 */
		
		int helicsFederateIsAsyncOperationCompleted(HelicsFederate fed,HelicsError err);
		/**
 * Finalize the entry to initialize mode that was initiated with /ref heliceEnterInitializingModeAsync.
 *
 * @param fed The federate desiring to complete the initialization step.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateEnterInitializingModeComplete(HelicsFederate fed,HelicsError err);
		/**
 * Request that the federate enter the Execution mode.
 *
 * @details This call is blocking until granted entry by the core object. On return from this call the federate will be at time 0.
 *          For an asynchronous alternative call see /ref helicsFederateEnterExecutingModeAsync.
 *
 * @param fed A federate to change modes.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateEnterExecutingMode(HelicsFederate fed,HelicsError err);
		/**
 * Request that the federate enter the Execution mode.
 *
 * @details This call is non-blocking and will return immediately. Call /ref helicsFederateEnterExecutingModeComplete to finish the call
 * sequence.
 *
 * @param fed The federate object to complete the call.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateEnterExecutingModeAsync(HelicsFederate fed,HelicsError err);
		/**
 * Complete the call to /ref helicsFederateEnterExecutingModeAsync.
 *
 * @param fed The federate object to complete the call.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateEnterExecutingModeComplete(HelicsFederate fed,HelicsError err);
		/**
 * Request an iterative time.
 *
 * @details This call allows for finer grain control of the iterative process than /ref helicsFederateRequestTime. It takes a time and
 *          iteration request, and returns a time and iteration status.
 *
 * @param fed The federate to make the request of.
 * @param iterate The requested iteration mode.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return An iteration structure with field containing the time and iteration status.
 */
		
		HelicsIterationResult helicsFederateEnterExecutingModeIterative(HelicsFederate fed,HelicsIterationRequest iterate,HelicsError err);
		/**
 * Request an iterative entry to the execution mode.
 *
 * @details This call allows for finer grain control of the iterative process than /ref helicsFederateRequestTime. It takes a time and
 *          iteration request, and returns a time and iteration status
 *
 * @param fed The federate to make the request of.
 * @param iterate The requested iteration mode.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateEnterExecutingModeIterativeAsync(HelicsFederate fed,HelicsIterationRequest iterate,HelicsError err);
		/**
 * Complete the asynchronous iterative call into ExecutionMode.
 *
 * @param fed The federate to make the request of.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return An iteration object containing the iteration time and iteration_status.
 */
		
		HelicsIterationResult helicsFederateEnterExecutingModeIterativeComplete(HelicsFederate fed,HelicsError err);
		/**
 * Get the current state of a federate.
 *
 * @param fed The federate to query.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return State the resulting state if void return HELICS_OK.
 */
		
		HelicsFederateState helicsFederateGetState(HelicsFederate fed,HelicsError err);
		/**
 * Get the core object associated with a federate.
 *
 * @param fed A federate object.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return A core object, nullptr if invalid.
 */
		
		HelicsCore helicsFederateGetCore(HelicsFederate fed,HelicsError err);
		/**
 * Request the next time for federate execution.
 *
 * @param fed The federate to make the request of.
 * @param requestTime The next requested time.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return The time granted to the federate, will return HELICS_TIME_MAXTIME if the simulation has terminated or is invalid.
 */
		
		double helicsFederateRequestTime(HelicsFederate fed,double requestTime,HelicsError err);
		/**
 * Request the next time for federate execution.
 *
 * @param fed The federate to make the request of.
 * @param timeDelta The requested amount of time to advance.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return The time granted to the federate, will return HELICS_TIME_MAXTIME if the simulation has terminated or is invalid
 */
		
		double helicsFederateRequestTimeAdvance(HelicsFederate fed,double timeDelta,HelicsError err);
		/**
 * Request the next time step for federate execution.
 *
 * @details Feds should have setup the period or minDelta for this to work well but it will request the next time step which is the current
 * time plus the minimum time step.
 *
 * @param fed The federate to make the request of.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return The time granted to the federate, will return HELICS_TIME_MAXTIME if the simulation has terminated or is invalid
 */
		
		double helicsFederateRequestNextStep(HelicsFederate fed,HelicsError err);
		/**
 * Request an iterative time.
 *
 * @details This call allows for finer grain control of the iterative process than /ref helicsFederateRequestTime. It takes a time and and
 * iteration request, and returns a time and iteration status.
 *
 * @param fed The federate to make the request of.
 * @param requestTime The next desired time.
 * @param iterate The requested iteration mode.
 *
 * @param[out] outIteration  The iteration specification of the result.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return The granted time, will return HELICS_TIME_MAXTIME if the simulation has terminated along with the appropriate iteration result.
 */
		
		double helicsFederateRequestTimeIterative(HelicsFederate fed,double requestTime,HelicsIterationRequest iterate,HelicsIterationResult outIteration,HelicsError err);
		/**
 * Request the next time for federate execution in an asynchronous call.
 *
 * @details Call /ref helicsFederateRequestTimeComplete to finish the call.
 *
 * @param fed The federate to make the request of.
 * @param requestTime The next requested time.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateRequestTimeAsync(HelicsFederate fed,double requestTime,HelicsError err);
		/**
 * Complete an asynchronous requestTime call.
 *
 * @param fed The federate to make the request of.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return The time granted to the federate, will return HELICS_TIME_MAXTIME if the simulation has terminated.
 */
		
		double helicsFederateRequestTimeComplete(HelicsFederate fed,HelicsError err);
		/**
 * Request an iterative time through an asynchronous call.
 *
 * @details This call allows for finer grain control of the iterative process than /ref helicsFederateRequestTime. It takes a time and
 * iteration request, and returns a time and iteration status. Call /ref helicsFederateRequestTimeIterativeComplete to finish the process.
 *
 * @param fed The federate to make the request of.
 * @param requestTime The next desired time.
 * @param iterate The requested iteration mode.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateRequestTimeIterativeAsync(HelicsFederate fed,double requestTime,HelicsIterationRequest iterate,HelicsError err);
		/**
 * Complete an iterative time request asynchronous call.
 *
 * @param fed The federate to make the request of.
 *
 * @param[out] outIterate The iteration specification of the result.
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return The granted time, will return HELICS_TIME_MAXTIME if the simulation has terminated.
 */
		
		double helicsFederateRequestTimeIterativeComplete(HelicsFederate fed,HelicsIterationResult outIterate,HelicsError err);
		/**
 * Tell helics to process internal communications for a period of time.
 *
 * @param fed The federate to tell to process.
 *
 * @param period The length of time to process communications and then return control.
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 */
		
		void helicsFederateProcessCommunications(HelicsFederate fed,double period,HelicsError err);
		/**
 * Get the name of the federate.
 *
 * @param fed The federate object to query.
 *
 * @return A pointer to a string with the name.
 */
		
		String helicsFederateGetName(HelicsFederate fed);
		/**
 * Set a time based property for a federate.
 *
 * @param fed The federate object to set the property for.
 * @param timeProperty A integer code for a time property.
 * @param time The requested value of the property.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateSetTimeProperty(HelicsFederate fed,int timeProperty,double time,HelicsError err);
		/**
 * Set a flag for the federate.
 *
 * @param fed The federate to alter a flag for.
 * @param flag The flag to change.
 * @param flagValue The new value of the flag. 0 for false, !=0 for true.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateSetFlagOption(HelicsFederate fed,int flag,int flagValue,HelicsError err);
		/**
 * Set the separator character in a federate.
 *
 * @details The separator character is the separation character for local publications/endpoints in creating their global name.
 *          For example if the separator character is '/' then a local endpoint would have a globally reachable name of fedName/localName.
 *
 * @param fed The federate info object to alter.
 * @param separator The character to use as a separator.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateSetSeparator(HelicsFederate fed,char separator,HelicsError err);
		/**
 * Set an integer based property of a federate.
 *
 * @param fed The federate to change the property for.
 * @param intProperty The property to set.
 * @param propertyVal The value of the property.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateSetIntegerProperty(HelicsFederate fed,int intProperty,int propertyVal,HelicsError err);
		/**
 * Get the current value of a time based property in a federate.
 *
 * @param fed The federate query.
 * @param timeProperty The property to query.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		double helicsFederateGetTimeProperty(HelicsFederate fed,int timeProperty,HelicsError err);
		/**
 * Get a flag value for a federate.
 *
 * @param fed The federate to get the flag for.
 * @param flag The flag to query.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 *
 * @return The value of the flag.
 */
		
		int helicsFederateGetFlagOption(HelicsFederate fed,int flag,HelicsError err);
		/**
 * Get the current value of an integer property (such as a logging level).
 *
 * @param fed The federate to get the flag for.
 * @param intProperty A code for the property to set /ref helics_handle_options.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 *
 * @return The value of the property.
 */
		
		int helicsFederateGetIntegerProperty(HelicsFederate fed,int intProperty,HelicsError err);
		/**
 * Get the current time of the federate.
 *
 * @param fed The federate object to query.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 *
 * @return The current time of the federate.
 */
		
		double helicsFederateGetCurrentTime(HelicsFederate fed,HelicsError err);
		/**
 * Set a federation global value through a federate.
 *
 * @details This overwrites any previous value for this name.
 * @param fed The federate to set the global through.
 * @param valueName The name of the global to set.
 * @param value The value of the global.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsFederateSetGlobal(HelicsFederate fed,String valueName,String value,HelicsError err);
		/**
 * Set a federate tag value.
 *
 * @details This overwrites any previous value for this tag.
 * @param fed The federate to set the tag for.
 * @param tagName The name of the tag to set.
 * @param value The value of the tag.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsFederateSetTag(HelicsFederate fed,String tagName,String value,HelicsError err);
		/**
 * Get a federate tag value.
 *
 * @param fed The federate to get the tag for.
 * @param tagName The name of the tag to query.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		String helicsFederateGetTag(HelicsFederate fed,String tagName,HelicsError err);
		/**
 * Add a time dependency for a federate. The federate will depend on the given named federate for time synchronization.
 *
 * @param fed The federate to add the dependency for.
 * @param fedName The name of the federate to depend on.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsFederateAddDependency(HelicsFederate fed,String fedName,HelicsError err);
		/**
 * Set the logging file for a federate (actually on the core associated with a federate).
 *
 * @param fed The federate to set the log file for.
 * @param logFile The name of the log file.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsFederateSetLogFile(HelicsFederate fed,String logFile,HelicsError err);
		/**
 * Log an error message through a federate.
 *
 * @param fed The federate to log the error message through.
 * @param logmessage The message to put in the log.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsFederateLogErrorMessage(HelicsFederate fed,String logmessage,HelicsError err);
		/**
 * Log a warning message through a federate.
 *
 * @param fed The federate to log the warning message through.
 * @param logmessage The message to put in the log.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsFederateLogWarningMessage(HelicsFederate fed,String logmessage,HelicsError err);
		/**
 * Log an info message through a federate.
 *
 * @param fed The federate to log the info message through.
 * @param logmessage The message to put in the log.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsFederateLogInfoMessage(HelicsFederate fed,String logmessage,HelicsError err);
		/**
 * Log a debug message through a federate.
 *
 * @param fed The federate to log the debug message through.
 * @param logmessage The message to put in the log.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsFederateLogDebugMessage(HelicsFederate fed,String logmessage,HelicsError err);
		/**
 * Log a message through a federate.
 *
 * @param fed The federate to log the message through.
 * @param loglevel The level of the message to log see /ref helics_log_levels.
 * @param logmessage The message to put in the log.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsFederateLogLevelMessage(HelicsFederate fed,int loglevel,String logmessage,HelicsError err);
		/**
 * Send a command to another helics object through a federate.
 *
 * @param fed The federate to send the command through.
 * @param target The name of the object to send the command to.
 * @param command The command to send.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsFederateSendCommand(HelicsFederate fed,String target,String command,HelicsError err);
		/**
 * Get a command sent to the federate.
 *
 * @param fed The federate to get the command for.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return A string with the command for the federate, if the string is empty no command is available.
 */
		
		String helicsFederateGetCommand(HelicsFederate fed,HelicsError err);
		/**
 * Get the source of the most recently retrieved command sent to the federate.
 *
 * @param fed The federate to get the command for.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return A string with the command for the federate, if the string is empty no command is available.
 */
		
		String helicsFederateGetCommandSource(HelicsFederate fed,HelicsError err);
		/**
 * Get a command sent to the federate. Blocks until a command is received.
 *
 * @param fed The federate to get the command for.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return A string with the command for the federate, if the string is empty no command is available.
 */
		
		String helicsFederateWaitCommand(HelicsFederate fed,HelicsError err);
		/**
 * Set a global value in a core.
 *
 * @details This overwrites any previous value for this name.
 *
 * @param core The core to set the global through.
 * @param valueName The name of the global to set.
 * @param value The value of the global.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsCoreSetGlobal(HelicsCore core,String valueName,String value,HelicsError err);
		/**
 * Set a federation global value.
 *
 * @details This overwrites any previous value for this name.
 *
 * @param broker The broker to set the global through.
 * @param valueName The name of the global to set.
 * @param value The value of the global.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsBrokerSetGlobal(HelicsBroker broker,String valueName,String value,HelicsError err);
		/**
 * Send a command to another helics object though a core using asynchronous(fast) operations.
 *
 * @param core The core to send the command through.
 * @param target The name of the object to send the command to.
 * @param command The command to send.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsCoreSendCommand(HelicsCore core,String target,String command,HelicsError err);
		/**
 * Send a command to another helics object though a core using ordered operations.
 *
 * @param core The core to send the command through.
 * @param target The name of the object to send the command to.
 * @param command The command to send.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsCoreSendOrderedCommand(HelicsCore core,String target,String command,HelicsError err);
		/**
 * Send a command to another helics object through a broker using asynchronous(fast) messages.
 *
 * @param broker The broker to send the command through.
 * @param target The name of the object to send the command to.
 * @param command The command to send.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsBrokerSendCommand(HelicsBroker broker,String target,String command,HelicsError err);
		/**
 * Send a command to another helics object through a broker using ordered sequencing.
 *
 * @param broker The broker to send the command through.
 * @param target The name of the object to send the command to.
 * @param command The command to send.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsBrokerSendOrderedCommand(HelicsBroker broker,String target,String command,HelicsError err);
		/**
 * Set the log file on a core.
 *
 * @param core The core to set the log file for.
 * @param logFileName The name of the file to log to.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsCoreSetLogFile(HelicsCore core,String logFileName,HelicsError err);
		/**
 * Set the log file on a broker.
 *
 * @param broker The broker to set the log file for.
 * @param logFileName The name of the file to log to.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsBrokerSetLogFile(HelicsBroker broker,String logFileName,HelicsError err);
		/**
 * Set a broker time barrier.
 *
 * @param broker The broker to set the time barrier for.
 * @param barrierTime The time to set the barrier at.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsBrokerSetTimeBarrier(HelicsBroker broker,double barrierTime,HelicsError err);
		/**
 * Clear any time barrier on a broker.
 *
 * @param broker The broker to clear the barriers on.
 */
		
		void helicsBrokerClearTimeBarrier(HelicsBroker broker);
		/**
 * Generate a global error through a broker. This will terminate the federation.
 *
 * @param broker The broker to generate the global error on.
 * @param errorCode The error code to associate with the global error.
 * @param errorString An error message to associate with the global error.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsBrokerGlobalError(HelicsBroker broker,int errorCode,String errorString,HelicsError err);
		/**
 * Generate a global error through a broker. This will terminate the federation.
 *
 * @param core The core to generate the global error.
 * @param errorCode The error code to associate with the global error.
 * @param errorString An error message to associate with the global error.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsCoreGlobalError(HelicsCore core,int errorCode,String errorString,HelicsError err);
		/**
 * Create a query object.
 *
 * @details A query object consists of a target and query string.
 *
 * @param target The name of the target to query.
 * @param query The query to make of the target.
 */
		
		HelicsQuery helicsCreateQuery(String target,String query);
		/**
 * Execute a query.
 *
 * @details The call will block until the query finishes which may require communication or other delays.
 *
 * @param query The query object to use in the query.
 * @param fed A federate to send the query through.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.

 *
 * @return A pointer to a string.  The string will remain valid until the query is freed or executed again.
 * The return will be nullptr if fed or query is an invalid object, the return string will be "#invalid" if the query itself was
 * invalid.
 */
		
		String helicsQueryExecute(HelicsQuery query,HelicsFederate fed,HelicsError err);
		/**
 * Execute a query directly on a core.
 *
 * @details The call will block until the query finishes which may require communication or other delays.
 *
 * @param query The query object to use in the query.
 * @param core The core to send the query to.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return A pointer to a string.  The string will remain valid until the query is freed or executed again.
 * The return will be nullptr if core or query is an invalid object, the return string will be "#invalid" if the query itself was
 * invalid.
 */
		
		String helicsQueryCoreExecute(HelicsQuery query,HelicsCore core,HelicsError err);
		/**
 * Execute a query directly on a broker.
 *
 * @details The call will block until the query finishes which may require communication or other delays.
 *
 * @param query The query object to use in the query.
 * @param broker The broker to send the query to.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return A pointer to a string.  The string will remain valid until the query is freed or executed again.
 * The return will be nullptr if broker or query is an invalid object, the return string will be "#invalid" if the query itself was
 * invalid
 */
		
		String helicsQueryBrokerExecute(HelicsQuery query,HelicsBroker broker,HelicsError err);
		/**
 * Execute a query in a non-blocking call.
 *
 * @param query The query object to use in the query.
 * @param fed A federate to send the query through.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsQueryExecuteAsync(HelicsQuery query,HelicsFederate fed,HelicsError err);
		/**
 * Complete the return from a query called with /ref helicsExecuteQueryAsync.
 *
 * @details The function will block until the query completes /ref isQueryComplete can be called to determine if a query has completed or
 * not.
 *
 * @param query The query object to complete execution of.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 *
 * @return A pointer to a string. The string will remain valid until the query is freed or executed again.
 * The return will be nullptr if query is an invalid object
 */
		
		String helicsQueryExecuteComplete(HelicsQuery query,HelicsError err);
		/**
 * Check if an asynchronously executed query has completed.
 *
 * @details This function should usually be called after a QueryExecuteAsync function has been called.
 *
 * @param query The query object to check if completed.
 *
 * @return Will return HELICS_TRUE if an asynchronous query has completed or a regular query call was made with a result,
 * and false if an asynchronous query has not completed or is invalid
 */
		
		int helicsQueryIsCompleted(HelicsQuery query);
		/**
 * Update the target of a query.
 *
 * @param query The query object to change the target of.
 * @param target the name of the target to query
 *
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsQuerySetTarget(HelicsQuery query,String target,HelicsError err);
		/**
 * Update the queryString of a query.
 *
 * @param query The query object to change the target of.
 * @param queryString the new queryString
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsQuerySetQueryString(HelicsQuery query,String queryString,HelicsError err);
		/**
 * Update the ordering mode of the query, fast runs on priority channels, ordered goes on normal channels but goes in sequence
 *
 * @param query The query object to change the order for.
 * @param mode 0 for fast, 1 for ordered
 *
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsQuerySetOrdering(HelicsQuery query,int mode,HelicsError err);
		/**
 * Free the memory associated with a query object.
 */
		
		void helicsQueryFree(HelicsQuery query);
		/**
 * Function to do some housekeeping work.
 *
 * @details This runs some cleanup routines and tries to close out any residual thread that haven't been shutdown yet.
 */
		
		void helicsCleanupLibrary();
		/**
 * Create a subscription.
 *
 * @details The subscription becomes part of the federate and is destroyed when the federate is freed so there are no separate free
 * functions for subscriptions and publications.
 *
 * @param fed The federate object in which to create a subscription, must have been created with /ref helicsCreateValueFederate or
 * /ref helicsCreateCombinationFederate.
 * @param key The identifier matching a publication to get a subscription for.
 * @param units A string listing the units of the subscription (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the subscription.
 */
		
		HelicsInput helicsFederateRegisterSubscription(HelicsFederate fed,String key,String units,HelicsError err);
		/**
 * Register a publication with a known type.
 *
 * @details The publication becomes part of the federate and is destroyed when the federate is freed so there are no separate free
 * functions for subscriptions and publications.
 *
 * @param fed The federate object in which to create a publication.
 * @param key The identifier for the publication the global publication key will be prepended with the federate name.
 * @param type A code identifying the type of the input see /ref HelicsDataTypes for available options.
 * @param units A string listing the units of the subscription (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the publication.
 */
		
		HelicsPublication helicsFederateRegisterPublication(HelicsFederate fed,String key,int type,String units,HelicsError err);
		/**
 * Register a publication with a defined type.
 *
 * @details The publication becomes part of the federate and is destroyed when the federate is freed so there are no separate free
 * functions for subscriptions and publications.
 *
 * @param fed The federate object in which to create a publication.
 * @param key The identifier for the publication.
 * @param type A string labeling the type of the publication.
 * @param units A string listing the units of the subscription (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the publication.
 */
		
		HelicsPublication helicsFederateRegisterTypePublication(HelicsFederate fed,String key,String type,String units,HelicsError err);
		/**
 * Register a global named publication with an arbitrary type.
 *
 * @details The publication becomes part of the federate and is destroyed when the federate is freed so there are no separate free
 * functions for subscriptions and publications.
 *
 * @param fed The federate object in which to create a publication.
 * @param key The identifier for the publication.
 * @param type A code identifying the type of the input see /ref HelicsDataTypes for available options.
 * @param units A string listing the units of the subscription (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the publication.
 */
		
		HelicsPublication helicsFederateRegisterGlobalPublication(HelicsFederate fed,String key,int type,String units,HelicsError err);
		/**
 * Register a global publication with a defined type.
 *
 * @details The publication becomes part of the federate and is destroyed when the federate is freed so there are no separate free
 * functions for subscriptions and publications.
 *
 * @param fed The federate object in which to create a publication.
 * @param key The identifier for the publication.
 * @param type A string describing the expected type of the publication.
 * @param units A string listing the units of the subscription (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the publication.
 */
		
		HelicsPublication helicsFederateRegisterGlobalTypePublication(HelicsFederate fed,String key,String type,String units,HelicsError err);
		/**
 * Register a named input.
 *
 * @details The input becomes part of the federate and is destroyed when the federate is freed so there are no separate free
 * functions for subscriptions, inputs, and publications.
 *
 * @param fed The federate object in which to create an input.
 * @param key The identifier for the publication the global input key will be prepended with the federate name.
 * @param type A code identifying the type of the input see /ref HelicsDataTypes for available options.
 * @param units A string listing the units of the input (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the input.
 */
		
		HelicsInput helicsFederateRegisterInput(HelicsFederate fed,String key,int type,String units,HelicsError err);
		/**
 * Register an input with a defined type.
 *
 * @details The input becomes part of the federate and is destroyed when the federate is freed so there are no separate free
 * functions for subscriptions, inputs, and publications.
 *
 * @param fed The federate object in which to create an input.
 * @param key The identifier for the input.
 * @param type A string describing the expected type of the input.
 * @param units A string listing the units of the input maybe NULL.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the publication.
 */
		
		HelicsInput helicsFederateRegisterTypeInput(HelicsFederate fed,String key,String type,String units,HelicsError err);
		/**
 * Register a global named input.
 *
 * @details The publication becomes part of the federate and is destroyed when the federate is freed so there are no separate free
 * functions for subscriptions and publications.
 *
 * @param fed The federate object in which to create a publication.
 * @param key The identifier for the publication.
 * @param type A code identifying the type of the input see /ref HelicsDataTypes for available options.
 * @param units A string listing the units of the subscription maybe NULL.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the publication.
 */
		
		HelicsPublication helicsFederateRegisterGlobalInput(HelicsFederate fed,String key,int type,String units,HelicsError err);
		/**
 * Register a global publication with an arbitrary type.
 *
 * @details The publication becomes part of the federate and is destroyed when the federate is freed so there are no separate free
 * functions for subscriptions and publications.
 *
 * @param fed The federate object in which to create a publication.
 * @param key The identifier for the publication.
 * @param type A string defining the type of the input.
 * @param units A string listing the units of the subscription maybe NULL.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the publication.
 */
		
		HelicsPublication helicsFederateRegisterGlobalTypeInput(HelicsFederate fed,String key,String type,String units,HelicsError err);
		/**
 * Get a publication object from a key.
 *
 * @param fed The value federate object to use to get the publication.
 * @param key The name of the publication.
 *
 * @param[in,out] err The error object to complete if there is an error.

 *
 * @return A HelicsPublication object, the object will not be valid and err will contain an error code if no publication with the
 * specified key exists.
 */
		
		HelicsPublication helicsFederateGetPublication(HelicsFederate fed,String key,HelicsError err);
		/**
 * Get a publication by its index, typically already created via registerInterfaces file or something of that nature.
 *
 * @param fed The federate object in which to create a publication.
 * @param index The index of the publication to get.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsPublication.
 */
		
		HelicsPublication helicsFederateGetPublicationByIndex(HelicsFederate fed,int index,HelicsError err);
		/**
 * Get an input object from a key.
 *
 * @param fed The value federate object to use to get the publication.
 * @param key The name of the input.
 *
 * @param[in,out] err The error object to complete if there is an error.

 *
 * @return A HelicsInput object, the object will not be valid and err will contain an error code if no input with the specified
 * key exists.
 */
		
		HelicsInput helicsFederateGetInput(HelicsFederate fed,String key,HelicsError err);
		/**
 * Get an input by its index, typically already created via registerInterfaces file or something of that nature.
 *
 * @param fed The federate object in which to create a publication.
 * @param index The index of the publication to get.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsInput, which will be NULL if an invalid index.
 */
		
		HelicsInput helicsFederateGetInputByIndex(HelicsFederate fed,int index,HelicsError err);
		/**
 * Get an input object from a subscription target.
 *
 * @param fed The value federate object to use to get the publication.
 * @param key The name of the publication that a subscription is targeting.
 *
 * @param[in,out] err The error object to complete if there is an error.

 *
 * @return A HelicsInput object, the object will not be valid and err will contain an error code if no input with the specified
 * key exists.
 */
		
		HelicsInput helicsFederateGetSubscription(HelicsFederate fed,String key,HelicsError err);
		/**
 * Clear all the update flags from a federates inputs.
 *
 * @param fed The value federate object for which to clear update flags.
 */
		
		void helicsFederateClearUpdates(HelicsFederate fed);
		/**
 * Register the publications via JSON publication string.
 *
 * @param fed The value federate object to use to register the publications.
 * @param json The JSON publication string.
 *
 * @param[in,out] err The error object to complete if there is an error.

 *
 * @details This would be the same JSON that would be used to publish data.
 */
		
		void helicsFederateRegisterFromPublicationJSON(HelicsFederate fed,String json,HelicsError err);
		/**
 * Publish data contained in a JSON file or string.
 *
 * @param fed The value federate object through which to publish the data.
 * @param json The publication file name or literal JSON data string.
 *
 * @param[in,out] err The error object to complete if there is an error.

 */
		
		void helicsFederatePublishJSON(HelicsFederate fed,String json,HelicsError err);
		/**
 * Check if a publication is valid.
 *
 * @param pub The publication to check.
 *
 * @return HELICS_TRUE if the publication is a valid publication.
 */
		
		int helicsPublicationIsValid(HelicsPublication pub);
		/**
 * Publish a string.
 *
 * @param pub The publication to publish for.
 * @param val The null terminated string to publish.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsPublicationPublishString(HelicsPublication pub,String val,HelicsError err);
		/**
 * Publish an integer value.
 *
 * @param pub The publication to publish for.
 * @param val The numerical value to publish.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsPublicationPublishInteger(HelicsPublication pub,int val,HelicsError err);
		/**
 * Publish a Boolean Value.
 *
 * @param pub The publication to publish for.
 * @param val The boolean value to publish.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsPublicationPublishBoolean(HelicsPublication pub,int val,HelicsError err);
		/**
 * Publish a double floating point value.
 *
 * @param pub The publication to publish for.
 * @param val The numerical value to publish.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsPublicationPublishDouble(HelicsPublication pub,double val,HelicsError err);
		/**
 * Publish a time value.
 *
 * @param pub The publication to publish for.
 * @param val The numerical value to publish.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsPublicationPublishTime(HelicsPublication pub,double val,HelicsError err);
		/**
 * Publish a single character.
 *
 * @param pub The publication to publish for.
 * @param val The numerical value to publish.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsPublicationPublishChar(HelicsPublication pub,char val,HelicsError err);
		/**
 * Publish a complex value (or pair of values).
 *
 * @param pub The publication to publish for.
 * @param real The real part of a complex number to publish.
 * @param imag The imaginary part of a complex number to publish.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsPublicationPublishComplex(HelicsPublication pub,double real,double imag,HelicsError err);
		/**
 * Publish a named point.
 *
 * @param pub The publication to publish for.
 * @param field A null terminated string for the field name of the namedPoint to publish.
 * @param val A double for the value to publish.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsPublicationPublishNamedPoint(HelicsPublication pub,String field,double val,HelicsError err);
		/**
 * Add a named input to the list of targets a publication publishes to.
 *
 * @param pub The publication to add the target for.
 * @param target The name of an input that the data should be sent to.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsPublicationAddTarget(HelicsPublication pub,String target,HelicsError err);
		/**
 * Check if an input is valid.
 *
 * @param ipt The input to check.
 *
 * @return HELICS_TRUE if the Input object represents a valid input.
 */
		
		int helicsInputIsValid(HelicsIterationRequest ipt);
		/**
 * Add a publication to the list of data that an input subscribes to.
 *
 * @param ipt The named input to modify.
 * @param target The name of a publication that an input should subscribe to.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsInputAddTarget(HelicsIterationRequest ipt,String target,HelicsError err);
		/**
 * Get the size of the raw value for subscription.
 *
 * @return The size of the raw data/string in bytes.
 */
		
		int helicsInputGetByteCount(HelicsIterationRequest ipt);
		/**
 * Get the raw data for the latest value of a subscription.
 *
 * @param ipt The input to get the data for.
 *
 * @param[out] data The memory location of the data
 * @param maxDataLength The maximum size of information that data can hold.
 * @param[out] actualSize The actual length of data copied to data.
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsInputGetBytes(HelicsIterationRequest ipt,Pointer data,int maxDataLength,IntByReference actualSize,HelicsError err);
		/**
 * Get the size of a value for subscription assuming return as a string.
 *
 * @return The size of the string.
 */
		
		int helicsInputGetStringSize(HelicsIterationRequest ipt);
		/**
 * Get an integer value from a subscription.
 *
 * @param ipt The input to get the data for.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 *
 * @return An int64_t value with the current value of the input.
 */
		
		int helicsInputGetInteger(HelicsIterationRequest ipt,HelicsError err);
		/**
 * Get a boolean value from a subscription.
 *
 * @param ipt The input to get the data for.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 *
 * @return A boolean value of current input value.
 */
		
		int helicsInputGetBoolean(HelicsIterationRequest ipt,HelicsError err);
		/**
 * Get a double value from a subscription.
 *
 * @param ipt The input to get the data for.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 *
 * @return The double value of the input.
 */
		
		double helicsInputGetDouble(HelicsIterationRequest ipt,HelicsError err);
		/**
 * Get a time value from a subscription.
 *
 * @param ipt The input to get the data for.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 *
 * @return The resulting time value.
 */
		
		double helicsInputGetTime(HelicsIterationRequest ipt,HelicsError err);
		/**
 * Get a single character value from an input.
 *
 * @param ipt The input to get the data for.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 *
 * @return The resulting character value.
 *         NAK (negative acknowledgment) symbol returned on error
 */
		
		char helicsInputGetChar(HelicsIterationRequest ipt,HelicsError err);
		/**
 * Get a pair of double forming a complex number from a subscriptions.
 *
 * @param ipt The input to get the data for.
 *
 * @param[out] real Memory location to place the real part of a value.
 * @param[out] imag Memory location to place the imaginary part of a value.
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 * On error the values will not be altered.
 */
		
		void helicsInputGetComplex(HelicsIterationRequest ipt,DoubleByReference real,DoubleByReference imag,HelicsError err);
		/**
 * Get the size of a value for subscription assuming return as an array of doubles.
 *
 * @return The number of doubles in a returned vector.
 */
		
		int helicsInputGetVectorSize(HelicsIterationRequest ipt);
		/**
 * Set the default as a string.
 *
 * @param ipt The input to set the default for.
 * @param defaultString A pointer to the default string.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsInputSetDefaultString(HelicsIterationRequest ipt,String defaultString,HelicsError err);
		/**
 * Set the default as an integer.
 *
 * @param ipt The input to set the default for.
 * @param val The default integer.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsInputSetDefaultInteger(HelicsIterationRequest ipt,int val,HelicsError err);
		/**
 * Set the default as a boolean.
 *
 * @param ipt The input to set the default for.
 * @param val The default boolean value.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsInputSetDefaultBoolean(HelicsIterationRequest ipt,int val,HelicsError err);
		/**
 * Set the default as a time.
 *
 * @param ipt The input to set the default for.
 * @param val The default time value.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsInputSetDefaultTime(HelicsIterationRequest ipt,double val,HelicsError err);
		/**
 * Set the default as a char.
 *
 * @param ipt The input to set the default for.
 * @param val The default char value.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsInputSetDefaultChar(HelicsIterationRequest ipt,char val,HelicsError err);
		/**
 * Set the default as a double.
 *
 * @param ipt The input to set the default for.
 * @param val The default double value.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsInputSetDefaultDouble(HelicsIterationRequest ipt,double val,HelicsError err);
		/**
 * Set the default as a complex number.
 *
 * @param ipt The input to set the default for.
 * @param real The default real value.
 * @param imag The default imaginary value.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsInputSetDefaultComplex(HelicsIterationRequest ipt,double real,double imag,HelicsError err);
		/**
 * Set the default as a NamedPoint.
 *
 * @param ipt The input to set the default for.
 * @param defaultName A pointer to a null terminated string representing the field name to use in the named point.
 * @param val A double value for the value of the named point.
 *
 * @param[in,out] err An error object that will contain an error code and string if any error occurred during the execution of the function.
 */
		
		void helicsInputSetDefaultNamedPoint(HelicsIterationRequest ipt,String defaultName,double val,HelicsError err);
		/**
 * Get the type of an input.
 *
 * @param ipt The input to query.
 *
 * @return A void enumeration, HELICS_OK if everything worked.
 */
		
		String helicsInputGetType(HelicsIterationRequest ipt);
		/**
 * Get the type the publisher to an input is sending.
 *
 * @param ipt The input to query.
 *
 * @return A const char * with the type name.
 */
		
		String helicsInputGetPublicationType(HelicsIterationRequest ipt);
		/**
 * Get the type the publisher to an input is sending.
 *
 * @param ipt The input to query.
 *
 * @return An int containing the enumeration value of the publication type.
 */
		
		int helicsInputGetPublicationDataType(HelicsIterationRequest ipt);
		/**
 * Get the type of a publication.
 *
 * @param pub The publication to query.
 *
 * @return A void enumeration, HELICS_OK if everything worked.
 */
		
		String helicsPublicationGetType(HelicsPublication pub);
		/**
 * Get the key of an input.
 *
 * @param ipt The input to query.
 *
 * @return A const char with the input name.
 */
		
		String helicsInputGetName(HelicsIterationRequest ipt);
		/**
 * Get the target of a subscription.
 *
 * @return A const char with the subscription target.
 */
		
		String helicsSubscriptionGetTarget(HelicsIterationRequest ipt);
		/**
 * Get the name of a publication.
 *
 * @details This will be the global key used to identify the publication to the federation.
 *
 * @param pub The publication to query.
 *
 * @return A const char with the publication name.
 */
		
		String helicsPublicationGetName(HelicsPublication pub);
		/**
 * Get the units of an input.
 *
 * @param ipt The input to query.
 *
 * @return A void enumeration, HELICS_OK if everything worked.
 */
		
		String helicsInputGetUnits(HelicsIterationRequest ipt);
		/**
 * Get the units of the publication that an input is linked to.
 *
 * @param ipt The input to query.
 *
 * @return A void enumeration, HELICS_OK if everything worked.
 */
		
		String helicsInputGetInjectionUnits(HelicsIterationRequest ipt);
		/**
 * Get the units of an input.
 *
 * @details The same as helicsInputGetUnits.
 *
 * @param ipt The input to query.
 *
 * @return A void enumeration, HELICS_OK if everything worked.
 */
		
		String helicsInputGetExtractionUnits(HelicsIterationRequest ipt);
		/**
 * Get the units of a publication.
 *
 * @param pub The publication to query.
 *
 * @return A void enumeration, HELICS_OK if everything worked.
 */
		
		String helicsPublicationGetUnits(HelicsPublication pub);
		/**
 * Get the data in the info field of an input.
 *
 * @param inp The input to query.
 *
 * @return A string with the info field string.
 */
		
		String helicsInputGetInfo(HelicsIterationRequest inp);
		/**
 * Set the data in the info field for an input.
 *
 * @param inp The input to query.
 * @param info The string to set.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsInputSetInfo(HelicsIterationRequest inp,String info,HelicsError err);
		/**
 * Get the data in a specified tag of an input.
 *
 * @param inp The input object to query.
 * @param tagname The name of the tag to get the value for.
 * @return A string with the tag data.
 */
		
		String helicsInputGetTag(HelicsIterationRequest inp,String tagname);
		/**
 * Set the data in a specific tag for an input.
 *
 * @param inp The input object to query.
 * @param tagname The string to set.
 * @param tagvalue The string value to associate with a tag.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsInputSetTag(HelicsIterationRequest inp,String tagname,String tagvalue,HelicsError err);
		/**
 * Get the data in the info field of an publication.
 *
 * @param pub The publication to query.
 *
 * @return A string with the info field string.
 */
		
		String helicsPublicationGetInfo(HelicsPublication pub);
		/**
 * Set the data in the info field for a publication.
 *
 * @param pub The publication to set the info field for.
 * @param info The string to set.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsPublicationSetInfo(HelicsPublication pub,String info,HelicsError err);
		/**
 * Get the data in a specified tag of a publication.
 *
 * @param pub The publication object to query.
 * @param tagname The name of the tag to query.
 * @return A string with the tag data.
 */
		
		String helicsPublicationGetTag(HelicsPublication pub,String tagname);
		/**
 * Set the data in a specific tag for a publication.
 *
 * @param pub The publication object to set a tag for.
 * @param tagname The name of the tag to set.
 * @param tagvalue The string value to associate with a tag.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsPublicationSetTag(HelicsPublication pub,String tagname,String tagvalue,HelicsError err);
		/**
 * Get the current value of an input handle option
 *
 * @param inp The input to query.
 * @param option Integer representation of the option in question see /ref helics_handle_options.
 *
 * @return An integer value with the current value of the given option.
 */
		
		int helicsInputGetOption(HelicsIterationRequest inp,int option);
		/**
 * Set an option on an input
 *
 * @param inp The input to query.
 * @param option The option to set for the input /ref helics_handle_options.
 * @param value The value to set the option to.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsInputSetOption(HelicsIterationRequest inp,int option,int value,HelicsError err);
		/**
 * Get the value of an option for a publication
 *
 * @param pub The publication to query.
 * @param option The value to query see /ref helics_handle_options.
 *
 * @return A string with the info field string.
 */
		
		int helicsPublicationGetOption(HelicsPublication pub,int option);
		/**
 * Set the value of an option for a publication
 *
 * @param pub The publication to query.
 * @param option Integer code for the option to set /ref helics_handle_options.
 * @param val The value to set the option to.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsPublicationSetOption(HelicsPublication pub,int option,int val,HelicsError err);
		/**
 * Set the minimum change detection tolerance.
 *
 * @param pub The publication to modify.
 * @param tolerance The tolerance level for publication, values changing less than this value will not be published.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsPublicationSetMinimumChange(HelicsPublication pub,double tolerance,HelicsError err);
		/**
 * Set the minimum change detection tolerance.
 *
 * @param inp The input to modify.
 * @param tolerance The tolerance level for registering an update, values changing less than this value will not show as being updated.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsInputSetMinimumChange(HelicsIterationRequest inp,double tolerance,HelicsError err);
		/**
 * Check if a particular subscription was updated.
 *
 * @return HELICS_TRUE if it has been updated since the last value retrieval.
 */
		
		int helicsInputIsUpdated(HelicsIterationRequest ipt);
		/**
 * Get the last time a subscription was updated.
 */
		
		double helicsInputLastUpdateTime(HelicsIterationRequest ipt);
		/**
 * Clear the updated flag from an input.
 */
		
		void helicsInputClearUpdate(HelicsIterationRequest ipt);
		/**
 * Get the number of publications in a federate.
 *
 * @return (-1) if fed was not a valid federate otherwise returns the number of publications.
 */
		
		int helicsFederateGetPublicationCount(HelicsFederate fed);
		/**
 * Get the number of subscriptions in a federate.
 *
 * @return (-1) if fed was not a valid federate otherwise returns the number of subscriptions.
 */
		
		int helicsFederateGetInputCount(HelicsFederate fed);
		/**
 * Create an endpoint.
 *
 * @details The endpoint becomes part of the federate and is destroyed when the federate is freed
 *          so there are no separate free functions for endpoints.
 *
 * @param fed The federate object in which to create an endpoint must have been created
 *           with helicsCreateMessageFederate or helicsCreateCombinationFederate.
 * @param name The identifier for the endpoint. This will be prepended with the federate name for the global identifier.
 * @param type A string describing the expected type of the publication (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the endpoint, or nullptr on failure.
 */
		
		HelicsEndpoint helicsFederateRegisterEndpoint(HelicsFederate fed,String name,String type,HelicsError err);
		/**
 * Create an endpoint.
 *
 * @details The endpoint becomes part of the federate and is destroyed when the federate is freed
 *          so there are no separate free functions for endpoints.
 *
 * @param fed The federate object in which to create an endpoint must have been created
              with helicsCreateMessageFederate or helicsCreateCombinationFederate.
 * @param name The identifier for the endpoint, the given name is the global identifier.
 * @param type A string describing the expected type of the publication (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 * @return An object containing the endpoint, or nullptr on failure.
 */
		
		HelicsEndpoint helicsFederateRegisterGlobalEndpoint(HelicsFederate fed,String name,String type,HelicsError err);
		/**
 * Create a targeted endpoint.  Targeted endpoints have specific destinations predefined and do not allow sending messages to other
 * endpoints
 *
 * @details The endpoint becomes part of the federate and is destroyed when the federate is freed
 *          so there are no separate free functions for endpoints.
 *
 * @param fed The federate object in which to create an endpoint must have been created
 *           with helicsCreateMessageFederate or helicsCreateCombinationFederate.
 * @param name The identifier for the endpoint. This will be prepended with the federate name for the global identifier.
 * @param type A string describing the expected type of the publication (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return An object containing the endpoint, or nullptr on failure.
 */
		
		HelicsEndpoint helicsFederateRegisterTargetedEndpoint(HelicsFederate fed,String name,String type,HelicsError err);
		/**
 * Create a global targeted endpoint, Targeted endpoints have specific destinations predefined and do not allow sending messages to other
 endpoints
 *
 * @details The endpoint becomes part of the federate and is destroyed when the federate is freed
 *          so there are no separate free functions for endpoints.
 *
 * @param fed The federate object in which to create an endpoint must have been created
              with helicsCreateMessageFederate or helicsCreateCombinationFederate.
 * @param name The identifier for the endpoint, the given name is the global identifier.
 * @param type A string describing the expected type of the publication (may be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 * @return An object containing the endpoint, or nullptr on failure.
 */
		
		HelicsEndpoint helicsFederateRegisterGlobalTargetedEndpoint(HelicsFederate fed,String name,String type,HelicsError err);
		/**
 * Get an endpoint object from a name.
 *
 * @param fed The message federate object to use to get the endpoint.
 * @param name The name of the endpoint.
 *
 * @param[in,out] err The error object to complete if there is an error.

 *
 * @return A HelicsEndpoint object.
 *
 * The object will not be valid and err will contain an error code if no endpoint with the specified name exists.
 */
		
		HelicsEndpoint helicsFederateGetEndpoint(HelicsFederate fed,String name,HelicsError err);
		/**
 * Get an endpoint by its index, typically already created via registerInterfaces file or something of that nature.
 *
 * @param fed The federate object in which to create a publication.
 * @param index The index of the publication to get.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsEndpoint.
 *
 * The HelicsEndpoint returned will be NULL if given an invalid index.
 */
		
		HelicsEndpoint helicsFederateGetEndpointByIndex(HelicsFederate fed,int index,HelicsError err);
		/**
 * Check if an endpoint is valid.
 *
 * @param endpoint The endpoint object to check.
 *
 * @return HELICS_TRUE if the Endpoint object represents a valid endpoint.
 */
		
		int helicsEndpointIsValid(HelicsEndpoint endpoint);
		/**
 * Set the default destination for an endpoint if no other endpoint is given.
 *
 * @param endpoint The endpoint to set the destination for.
 * @param dst A string naming the desired default endpoint.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsEndpointSetDefaultDestination(HelicsEndpoint endpoint,String dst,HelicsError err);
		/**
 * Get the default destination for an endpoint.
 *
 * @param endpoint The endpoint to set the destination for.
 *
 * @return A string with the default destination.
 */
		
		String helicsEndpointGetDefaultDestination(HelicsEndpoint endpoint);
		/**
 * Send a message object from a specific endpoint.
 *
 * @param endpoint The endpoint to send the data from.
 * @param message The actual message to send which will be copied.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsEndpointSendMessage(HelicsEndpoint endpoint,HelicsMessage message,HelicsError err);
		/**
 * Send a message object from a specific endpoint, the message will not be copied and the message object will no longer be valid
 * after the call.
 *
 * @param endpoint The endpoint to send the data from.
 * @param message The actual message to send which will be copied.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsEndpointSendMessageZeroCopy(HelicsEndpoint endpoint,HelicsMessage message,HelicsError err);
		/**
 * Subscribe an endpoint to a publication.
 *
 * @param endpoint The endpoint to use.
 * @param key The name of the publication.
 *
 * @param[in,out] err A pointer to an error object for catching errors.
 */
		
		void helicsEndpointSubscribe(HelicsEndpoint endpoint,String key,HelicsError err);
		/**
 * Check if the federate has any outstanding messages.
 *
 * @param fed The federate to check.
 *
 * @return HELICS_TRUE if the federate has a message waiting, HELICS_FALSE otherwise.
 */
		
		int helicsFederateHasMessage(HelicsFederate fed);
		/**
 * Check if a given endpoint has any unread messages.
 *
 * @param endpoint The endpoint to check.
 *
 * @return HELICS_TRUE if the endpoint has a message, HELICS_FALSE otherwise.
 */
		
		int helicsEndpointHasMessage(HelicsEndpoint endpoint);
		/**
 * Returns the number of pending receives for the specified destination endpoint.
 *
 * @param fed The federate to get the number of waiting messages from.
 */
		
		int helicsFederatePendingMessageCount(HelicsFederate fed);
		/**
 * Returns the number of pending receives for all endpoints of a particular federate.
 *
 * @param endpoint The endpoint to query.
 */
		
		int helicsEndpointPendingMessageCount(HelicsEndpoint endpoint);
		/**
 * Receive a packet from a particular endpoint.
 *
 * @param[in] endpoint The identifier for the endpoint.
 *
 * @return A message object.
 */
		
		HelicsMessage helicsEndpointGetMessage(HelicsEndpoint endpoint);
		/**
 * Create a new empty message object.
 *
 * @details The message is empty and isValid will return false since there is no data associated with the message yet.
 *
 * @param endpoint The endpoint object to associate the message with.
 *
 * @param[in,out] err An error object to fill out in case of an error.

 *
 * @return A new HelicsMessage.
 */
		
		HelicsMessage helicsEndpointCreateMessage(HelicsEndpoint endpoint,HelicsError err);
		/**
 * Clear all stored messages stored from an endpoint.
 *
 * @details This clears messages retrieved through helicsEndpointGetMessage or helicsEndpointCreateMessage
 *
 * @param endpoint The endpoint to clear the message for.
 */
		
		void helicsEndpointClearMessages(HelicsEndpoint endpoint);
		/**
 * Receive a communication message for any endpoint in the federate.
 *
 * @details The return order will be in order of endpoint creation.
 *          So all messages that are available for the first endpoint, then all for the second, and so on.
 *          Within a single endpoint, the messages are ordered by time, then source_id, then order of arrival.
 *
 * @return A HelicsMessage which references the data in the message.
 */
		
		HelicsMessage helicsFederateGetMessage(HelicsFederate fed);
		/**
 * Create a new empty message object.
 *
 * @details The message is empty and isValid will return false since there is no data associated with the message yet.
 *
 * @param fed the federate object to associate the message with
 *
 * @param[in,out] err An error object to fill out in case of an error.

 *
 * @return A HelicsMessage containing the message data.
 */
		
		HelicsMessage helicsFederateCreateMessage(HelicsFederate fed,HelicsError err);
		/**
 * Clear all stored messages from a federate.
 *
 * @details This clears messages retrieved through helicsEndpointGetMessage or helicsFederateGetMessage
 *
 * @param fed The federate to clear the message for.
 */
		
		void helicsFederateClearMessages(HelicsFederate fed);
		/**
 * Get the type specified for an endpoint.
 *
 * @param endpoint The endpoint object in question.
 *
 * @return The defined type of the endpoint.
 */
		
		String helicsEndpointGetType(HelicsEndpoint endpoint);
		/**
 * Get the name of an endpoint.
 *
 * @param endpoint The endpoint object in question.
 *
 * @return The name of the endpoint.
 */
		
		String helicsEndpointGetName(HelicsEndpoint endpoint);
		/**
 * Get the number of endpoints in a federate.
 *
 * @param fed The message federate to query.
 *
 * @return (-1) if fed was not a valid federate, otherwise returns the number of endpoints.
 */
		
		int helicsFederateGetEndpointCount(HelicsFederate fed);
		/**
 * Get the local information field of an endpoint.
 *
 * @param end The endpoint to query.
 *
 * @return A string with the info field string.
 */
		
		String helicsEndpointGetInfo(HelicsEndpoint end);
		/**
 * Set the data in the interface information field for an endpoint.
 *
 * @param endpoint The endpoint to set the information for
 * @param info The string to store in the field
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsEndpointSetInfo(HelicsEndpoint endpoint,String info,HelicsError err);
		/**
 * Get the data in a specified tag of an endpoint
 *
 * @param endpoint The endpoint to query.
 * @param tagname The name of the tag to query.
 * @return A string with the tag data.
 */
		
		String helicsEndpointGetTag(HelicsEndpoint endpoint,String tagname);
		/**
 * Set the data in a specific tag for an endpoint.
 *
 * @param endpoint The endpoint to query.
 * @param tagname The string to set.
 * @param tagvalue The string value to associate with a tag.
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsEndpointSetTag(HelicsEndpoint endpoint,String tagname,String tagvalue,HelicsError err);
		/**
 * Set a handle option on an endpoint.
 *
 * @param endpoint The endpoint to modify.
 * @param option Integer code for the option to set /ref helics_handle_options.
 * @param value The value to set the option to.
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsEndpointSetOption(HelicsEndpoint endpoint,int option,int value,HelicsError err);
		/**
 * Set a handle option on an endpoint.
 *
 * @param endpoint The endpoint to modify.
 * @param option Integer code for the option to set /ref helics_handle_options.
 * @return the value of the option, for boolean options will be 0 or 1
 */
		
		int helicsEndpointGetOption(HelicsEndpoint endpoint,int option);
		/**
 * add a source target to an endpoint,  Specifying an endpoint to receive undirected messages from
 *
 * @param endpoint The endpoint to modify.
 * @param targetEndpoint the endpoint to get messages from
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsEndpointAddSourceTarget(HelicsEndpoint endpoint,String targetEndpoint,HelicsError err);
		/**
 * add a destination target to an endpoint,  Specifying an endpoint to send undirected messages to
 *
 * @param endpoint The endpoint to modify.
 * @param targetEndpoint the name of the endpoint to send messages to
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsEndpointAddDestinationTarget(HelicsEndpoint endpoint,String targetEndpoint,HelicsError err);
		/**
 * remove an endpoint from being targeted
 *
 * @param endpoint The endpoint to modify.
 * @param targetEndpoint the name of the endpoint to send messages to
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsEndpointRemoveTarget(HelicsEndpoint endpoint,String targetEndpoint,HelicsError err);
		/**
 * add a source Filter to an endpoint
 *
 * @param endpoint The endpoint to modify.
 * @param filterName the name of the filter to add
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsEndpointAddSourceFilter(HelicsEndpoint endpoint,String filterName,HelicsError err);
		/**
 * add a destination filter to an endpoint
 *
 * @param endpoint The endpoint to modify.
 * @param filterName The name of the filter to add.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsEndpointAddDestinationFilter(HelicsEndpoint endpoint,String filterName,HelicsError err);
		/**
 * Get the source endpoint of a message.
 *
 * @param message The message object in question.
 *
 * @return A string with the source endpoint.
 */
		
		String helicsMessageGetSource(HelicsMessage message);
		/**
 * Get the destination endpoint of a message.
 *
 * @param message The message object in question.
 *
 * @return A string with the destination endpoint.
 */
		
		String helicsMessageGetDestination(HelicsMessage message);
		/**
 * Get the original source endpoint of a message, the source may have been modified by filters or other actions.
 *
 * @param message The message object in question.
 *
 * @return A string with the source of a message.
 */
		
		String helicsMessageGetOriginalSource(HelicsMessage message);
		/**
 * Get the original destination endpoint of a message, the destination may have been modified by filters or other actions.
 *
 * @param message The message object in question.
 *
 * @return A string with the original destination of a message.
 */
		
		String helicsMessageGetOriginalDestination(HelicsMessage message);
		/**
 * Get the helics time associated with a message.
 *
 * @param message The message object in question.
 *
 * @return The time associated with a message.
 */
		
		double helicsMessageGetTime(HelicsMessage message);
		/**
 * Get the payload of a message as a string.
 *
 * @param message The message object in question.
 *
 * @return A string representing the payload of a message.
 */
		
		String helicsMessageGetString(HelicsMessage message);
		/**
 * Get the messageID of a message.
 *
 * @param message The message object in question.
 *
 * @return The messageID.
 */
		
		int helicsMessageGetMessageID(HelicsMessage message);
		/**
 * Check if a flag is set on a message.
 *
 * @param message The message object in question.
 * @param flag The flag to check should be between [0,15].
 *
 * @return The flags associated with a message.
 */
		
		int helicsMessageGetFlagOption(HelicsMessage message,int flag);
		/**
 * Get the size of the data payload in bytes.
 *
 * @param message The message object in question.
 *
 * @return The size of the data payload.
 */
		
		int helicsMessageGetByteCount(HelicsMessage message);
		/**
 * A check if the message contains a valid payload.
 *
 * @param message The message object in question.
 *
 * @return HELICS_TRUE if the message contains a payload.
 */
		
		int helicsMessageIsValid(HelicsMessage message);
		/**
 * Set the source of a message.
 *
 * @param message The message object in question.
 * @param src A string containing the source.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageSetSource(HelicsMessage message,String src,HelicsError err);
		/**
 * Set the destination of a message.
 *
 * @param message The message object in question.
 * @param dst A string containing the new destination.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageSetDestination(HelicsMessage message,String dst,HelicsError err);
		/**
 * Set the original source of a message.
 *
 * @param message The message object in question.
 * @param src A string containing the new original source.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageSetOriginalSource(HelicsMessage message,String src,HelicsError err);
		/**
 * Set the original destination of a message.
 *
 * @param message The message object in question.
 * @param dst A string containing the new original source.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageSetOriginalDestination(HelicsMessage message,String dst,HelicsError err);
		/**
 * Set the delivery time for a message.
 *
 * @param message The message object in question.
 * @param time The time the message should be delivered.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageSetTime(HelicsMessage message,double time,HelicsError err);
		/**
 * Resize the data buffer for a message.
 *
 * @details The message data buffer will be resized. There are no guarantees on what is in the buffer in newly allocated space.
 *          If the allocated space is not sufficient new allocations will occur.
 *
 * @param message The message object in question.
 * @param newSize The new size in bytes of the buffer.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageResize(HelicsMessage message,int newSize,HelicsError err);
		/**
 * Reserve space in a buffer but don't actually resize.
 *
 * @details The message data buffer will be reserved but not resized.
 *
 * @param message The message object in question.
 * @param reserveSize The number of bytes to reserve in the message object.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageReserve(HelicsMessage message,int reserveSize,HelicsError err);
		/**
 * Set the message ID for the message.
 *
 * @details Normally this is not needed and the core of HELICS will adjust as needed.
 *
 * @param message The message object in question.
 * @param messageID A new message ID.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageSetMessageID(HelicsMessage message,int messageID,HelicsError err);
		/**
 * Clear the flags of a message.
 *
 * @param message The message object in question
 */
		
		void helicsMessageClearFlags(HelicsMessage message);
		/**
 * Set a flag on a message.
 *
 * @param message The message object in question.
 * @param flag An index of a flag to set on the message.
 * @param flagValue The desired value of the flag.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageSetFlagOption(HelicsMessage message,int flag,int flagValue,HelicsError err);
		/**
 * Set the data payload of a message as a string.
 *
 * @param message The message object in question.
 * @param data A null terminated string containing the message data.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageSetString(HelicsMessage message,String data,HelicsError err);
		/**
 * Copy a message object.
 *
 * @param src_message The message object to copy from.
 * @param dst_message The message object to copy to.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageCopy(HelicsMessage src_message,HelicsMessage dst_message,HelicsError err);
		/**
 * Clone a message object.
 *
 * @param message The message object to copy from.
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		HelicsMessage helicsMessageClone(HelicsMessage message,HelicsError err);
		/**
 * Free a message object from memory
 * @param message The message object to copy from.
 * @details memory for message is managed so not using this function does not create memory leaks, this is an indication
 * to the system that the memory for this message is done being used and can be reused for a new message.
 * helicsFederateClearMessages() can also be used to clear up all stored messages at once
 */
		
		void helicsMessageFree(HelicsMessage message);
		/**
 * Reset a message to empty state
 * @param message The message object to copy from.
 * @details The message after this function will be empty, with no source or destination
 *
 * @param[in,out] err An error object to fill out in case of an error.
 */
		
		void helicsMessageClear(HelicsMessage message,HelicsError err);
		/**
 * Create a source Filter on the specified federate.
 *
 * @details Filters can be created through a federate or a core, linking through a federate allows
 *          a few extra features of name matching to function on the federate interface but otherwise equivalent behavior
 *
 * @param fed The federate to register through.
 * @param type The type of filter to create /ref HelicsFilterTypes.
 * @param name The name of the filter (can be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsFilter object.
 */
		
		HelicsFilter helicsFederateRegisterFilter(HelicsFederate fed,int type,String name,HelicsError err);
		/**
 * Create a global source filter through a federate.
 *
 * @details Filters can be created through a federate or a core, linking through a federate allows
 *          a few extra features of name matching to function on the federate interface but otherwise equivalent behavior.
 *
 * @param fed The federate to register through.
 * @param type The type of filter to create /ref HelicsFilterTypes.
 * @param name The name of the filter (can be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsFilter object.
 */
		
		HelicsFilter helicsFederateRegisterGlobalFilter(HelicsFederate fed,int type,String name,HelicsError err);
		/**
 * Create a cloning Filter on the specified federate.
 *
 * @details Cloning filters copy a message and send it to multiple locations, source and destination can be added
 *          through other functions.
 *
 * @param fed The federate to register through.
 * @param name The name of the filter (can be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsFilter object.
 */
		
		HelicsFilter helicsFederateRegisterCloningFilter(HelicsFederate fed,String name,HelicsError err);
		/**
 * Create a global cloning Filter on the specified federate.
 *
 * @details Cloning filters copy a message and send it to multiple locations, source and destination can be added
 *          through other functions.
 *
 * @param fed The federate to register through.
 * @param name The name of the filter (can be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsFilter object.
 */
		
		HelicsFilter helicsFederateRegisterGlobalCloningFilter(HelicsFederate fed,String name,HelicsError err);
		/**
 * Create a source Filter on the specified core.
 *
 * @details Filters can be created through a federate or a core, linking through a federate allows
 *          a few extra features of name matching to function on the federate interface but otherwise equivalent behavior.
 *
 * @param core The core to register through.
 * @param type The type of filter to create /ref HelicsFilterTypes.
 * @param name The name of the filter (can be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsFilter object.
 */
		
		HelicsFilter helicsCoreRegisterFilter(HelicsCore core,int type,String name,HelicsError err);
		/**
 * Create a cloning Filter on the specified core.
 *
 * @details Cloning filters copy a message and send it to multiple locations, source and destination can be added
 *          through other functions.
 *
 * @param core The core to register through.
 * @param name The name of the filter (can be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsFilter object.
 */
		
		HelicsFilter helicsCoreRegisterCloningFilter(HelicsCore core,String name,HelicsError err);
		/**
 * Get the number of filters registered through a federate.
 *
 * @param fed The federate object to use to get the filter.
 *
 * @return A count of the number of filters registered through a federate.
 */
		
		int helicsFederateGetFilterCount(HelicsFederate fed);
		/**
 * Get a filter by its name, typically already created via registerInterfaces file or something of that nature.
 *
 * @param fed The federate object to use to get the filter.
 * @param name The name of the filter.
 *
 * @param[in,out] err The error object to complete if there is an error.

 *
 * @return A HelicsFilter object, the object will not be valid and err will contain an error code if no filter with the specified name
 * exists.
 */
		
		HelicsFilter helicsFederateGetFilter(HelicsFederate fed,String name,HelicsError err);
		/**
 * Get a filter by its index, typically already created via registerInterfaces file or something of that nature.
 *
 * @param fed The federate object in which to create a publication.
 * @param index The index of the publication to get.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsFilter, which will be NULL if an invalid index is given.
 */
		
		HelicsFilter helicsFederateGetFilterByIndex(HelicsFederate fed,int index,HelicsError err);
		/**
 * Check if a filter is valid.
 *
 * @param filt The filter object to check.
 *
 * @return HELICS_TRUE if the Filter object represents a valid filter.
 */
		
		int helicsFilterIsValid(HelicsFilter filt);
		/**
 * Get the name of the filter and store in the given string.
 *
 * @param filt The given filter.
 *
 * @return A string with the name of the filter.
 */
		
		String helicsFilterGetName(HelicsFilter filt);
		/**
 * Set a property on a filter.
 *
 * @param filt The filter to modify.
 * @param prop A string containing the property to set.
 * @param val A numerical value for the property.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsFilterSet(HelicsFilter filt,String prop,double val,HelicsError err);
		/**
 * Set a string property on a filter.
 *
 * @param filt The filter to modify.
 * @param prop A string containing the property to set.
 * @param val A string containing the new value.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsFilterSetString(HelicsFilter filt,String prop,String val,HelicsError err);
		/**
 * Add a destination target to a filter.
 *
 * @details All messages going to a destination are copied to the delivery address(es).
 * @param filt The given filter to add a destination target to.
 * @param dst The name of the endpoint to add as a destination target.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsFilterAddDestinationTarget(HelicsFilter filt,String dst,HelicsError err);
		/**
 * Add a source target to a filter.
 *
 * @details All messages coming from a source are copied to the delivery address(es).
 *
 * @param filt The given filter.
 * @param source The name of the endpoint to add as a source target.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsFilterAddSourceTarget(HelicsFilter filt,String source,HelicsError err);
		/**
 * Add a delivery endpoint to a cloning filter.
 *
 * @details All cloned messages are sent to the delivery address(es).
 *
 * @param filt The given filter.
 * @param deliveryEndpoint The name of the endpoint to deliver messages to.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsFilterAddDeliveryEndpoint(HelicsFilter filt,String deliveryEndpoint,HelicsError err);
		/**
 * Remove a destination target from a filter.
 *
 * @param filt The given filter.
 * @param target The named endpoint to remove as a target.
 *
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsFilterRemoveTarget(HelicsFilter filt,String target,HelicsError err);
		/**
 * Remove a delivery destination from a cloning filter.
 *
 * @param filt The given filter (must be a cloning filter).
 * @param deliveryEndpoint A string with the delivery endpoint to remove.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsFilterRemoveDeliveryEndpoint(HelicsFilter filt,String deliveryEndpoint,HelicsError err);
		/**
 * Get the data in the info field of a filter.
 *
 * @param filt The given filter.
 *
 * @return A string with the info field string.
 */
		
		String helicsFilterGetInfo(HelicsFilter filt);
		/**
 * Set the data in the info field for a filter.
 *
 * @param filt The given filter.
 * @param info The string to set.
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsFilterSetInfo(HelicsFilter filt,String info,HelicsError err);
		/**
 * Get the data in a specified tag of a filter.
 *
 * @param filt The filter to query.
 * @param tagname The name of the tag to query.
 * @return A string with the tag data.
 */
		
		String helicsFilterGetTag(HelicsFilter filt,String tagname);
		/**
 * Set the data in a specific tag for a filter.
 *
 * @param filt The filter object to set the tag for.
 * @param tagname The string to set.
 * @param tagvalue the string value to associate with a tag.
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsFilterSetTag(HelicsFilter filt,String tagname,String tagvalue,HelicsError err);
		/**
 * Set an option value for a filter.
 *
 * @param filt The given filter.
 * @param option The option to set /ref helics_handle_options.
 * @param value The value of the option commonly 0 for false 1 for true.
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsFilterSetOption(HelicsFilter filt,int option,int value,HelicsError err);
		/**
 * Get a handle option for the filter.
 *
 * @param filt The given filter to query.
 * @param option The option to query /ref helics_handle_options.
 */
		
		int helicsFilterGetOption(HelicsFilter filt,int option);
		/**
 * Create a Translator on the specified federate.
 *
 * @details Translators can be created through a federate or a core. Linking through a federate allows
 *          a few extra features of name matching to function on the federate interface but otherwise have equivalent behavior.
 *
 * @param fed The federate to register through.
 * @param type The type of translator to create /ref HelicsTranslatorTypes.
 * @param name The name of the translator (can be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsTranslator object.
 */
		
		HelicsTranslator helicsFederateRegisterTranslator(HelicsFederate fed,int type,String name,HelicsError err);
		/**
 * Create a global translator through a federate.
 *
 * @details Translators can be created through a federate or a core. Linking through a federate allows
 *          a few extra features of name matching to function on the federate interface but otherwise have equivalent behavior.
 *
 * @param fed The federate to register through.
 * @param type The type of translator to create /ref HelicsTranslatorTypes.
 * @param name The name of the translator (can be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsTranslator object.
 */
		
		HelicsTranslator helicsFederateRegisterGlobalTranslator(HelicsFederate fed,int type,String name,HelicsError err);
		/**
 * Create a Translator on the specified core.
 *
 * @details Translators can be created through a federate or a core. Linking through a federate allows
 *          a few extra features of name matching to function on the federate interface but otherwise have equivalent behavior.
 *
 * @param core The core to register through.
 * @param type The type of translator to create /ref HelicsTranslatorTypes.
 * @param name The name of the translator (can be NULL).
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsTranslator object.
 */
		
		HelicsTranslator helicsCoreRegisterTranslator(HelicsCore core,int type,String name,HelicsError err);
		/**
 * Get the number of translators registered through a federate.
 *
 * @param fed The federate object to use to get the translator.
 *
 * @return A count of the number of translators registered through a federate.
 */
		
		int helicsFederateGetTranslatorCount(HelicsFederate fed);
		/**
 * Get a translator by its name, typically already created via registerInterfaces file or something of that nature.
 *
 * @param fed The federate object to use to get the translator.
 * @param name The name of the translator.
 *
 * @param[in,out] err The error object to complete if there is an error.

 *
 * @return A HelicsTranslator object. If no translator with the specified name exists, the object will not be valid and
 * err will contain an error code.
 */
		
		HelicsTranslator helicsFederateGetTranslator(HelicsFederate fed,String name,HelicsError err);
		/**
 * Get a translator by its index, typically already created via registerInterfaces file or something of that nature.
 *
 * @param fed The federate object in which to create a publication.
 * @param index The index of the translator to get.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 *
 * @return A HelicsTranslator, which will be NULL if an invalid index is given.
 */
		
		HelicsTranslator helicsFederateGetTranslatorByIndex(HelicsFederate fed,int index,HelicsError err);
		/**
 * Check if a translator is valid.
 *
 * @param trans The translator object to check.
 *
 * @return HELICS_TRUE if the Translator object represents a valid translator.
 */
		
		int helicsTranslatorIsValid(HelicsTranslator trans);
		/**
 * Get the name of the translator and store in the given string.
 *
 * @param trans The given translator.
 *
 * @return A string with the name of the translator.
 */
		
		String helicsTranslatorGetName(HelicsTranslator trans);
		/**
 * Set a property on a translator.
 *
 * @param trans The translator to modify.
 * @param prop A string containing the property to set.
 * @param val A numerical value for the property.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsTranslatorSet(HelicsTranslator trans,String prop,double val,HelicsError err);
		/**
 * Set a string property on a translator.
 *
 * @param trans The translator to modify.
 * @param prop A string containing the property to set.
 * @param val A string containing the new value.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsTranslatorSetString(HelicsTranslator trans,String prop,String val,HelicsError err);
		/**
 * Add an input to send a translator output.
 *
 * @details All messages sent to a translator endpoint get translated and published to the translators target inputs.
 * This method adds an input to a translators which will receive translated messages.
 * @param trans The given translator to add a destination target to.
 * @param input The name of the input which will be receiving translated messages
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsTranslatorAddInputTarget(HelicsTranslator trans,String input,HelicsError err);
		/**
 * Add a source publication target to a translator.
 *
 * @details When a publication publishes data the translator will receive it and convert it to a message sent to a translators destination
 endpoints.
 * This method adds a publication which publishes data the translator receives and sends to its destination endpoints.
 *
 * @param trans The given translator.
 * @param pub The name of the publication to subscribe.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsTranslatorAddPublicationTarget(HelicsTranslator trans,String pub,HelicsError err);
		/**
 * Add a source endpoint target to a translator.
 *
 * @details The translator will "translate" all message sent to it.  This method adds an endpoint which can send the translator data.
 *
 * @param trans The given translator.
 * @param ept The name of the endpoint which will send the endpoint data
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsTranslatorAddSourceEndpoint(HelicsTranslator trans,String ept,HelicsError err);
		/**
 * Add a destination target endpoint to a translator.
 *
 * @details The translator will "translate" all message sent to it.  This method adds an endpoint which will receive data published to the
 translator.
 *
 * @param trans The given translator.
 * @param ept The name of the endpoint the translator sends data to.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsTranslatorAddDestinationEndpoint(HelicsTranslator trans,String ept,HelicsError err);
		/**
 * Remove a target from a translator.
 *
 * @param trans The given translator.
 * @param target The name of the interface to remove as a target.
 *
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsTranslatorRemoveTarget(HelicsTranslator trans,String target,HelicsError err);
		/**
 * Get the data in the info field of a translator.
 *
 * @param trans The given translator.
 *
 * @return A string with the info field string.
 */
		
		String helicsTranslatorGetInfo(HelicsTranslator trans);
		/**
 * Set the data in the info field for a translator.
 *
 * @param trans The given translator.
 * @param info The string to set.
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsTranslatorSetInfo(HelicsTranslator trans,String info,HelicsError err);
		/**
 * Get the data in a specified tag of a translator.
 *
 * @param trans The translator to query.
 * @param tagname The name of the tag to query.
 * @return A string with the tag data.
 */
		
		String helicsTranslatorGetTag(HelicsTranslator trans,String tagname);
		/**
 * Set the data in a specific tag for a translator.
 *
 * @param trans The translator object to set the tag for.
 * @param tagname The string to set.
 * @param tagvalue The string value to associate with a tag.
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsTranslatorSetTag(HelicsTranslator trans,String tagname,String tagvalue,HelicsError err);
		/**
 * Set an option value for a translator.
 *
 * @param trans The given translator.
 * @param option The option to set /ref helics_handle_options.
 * @param value The value of the option, commonly 0 for false or 1 for true.
 *
 * @param[in,out] err An error object to fill out in case of an error.

 */
		
		void helicsTranslatorSetOption(HelicsTranslator trans,int option,int value,HelicsError err);
		/**
 * Get a handle option for the translator.
 *
 * @param trans The given translator to query.
 * @param option The option to query /ref helics_handle_options.
 */
		
		int helicsTranslatorGetOption(HelicsTranslator trans,int option);
		/**
 * Set the data for a query callback.
 *
 * @details There are many queries that HELICS understands directly, but it is occasionally useful to have a federate be able to respond
 * to specific queries with answers specific to a federate.
 *
 * @param buffer The buffer received in a helicsQueryCallback.
 * @param queryResult Pointer to the data with the query result to fill the buffer with.
 * @param strSize The size of the string.
 *
 * @param[in,out] err A pointer to an error object for catching errors.

 */
		
		void helicsQueryBufferFill(HelicsQueryBuffer buffer,String queryResult,int strSize,HelicsError err);
	}
}