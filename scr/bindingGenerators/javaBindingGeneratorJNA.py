import glob
import json
import logging
import os
import shutil
from typing import List

import clang.cindex as cidx

from . import clangparser


javaBindingGeneratorLogger = logging.getLogger(__name__)
javaBindingGeneratorLogger.setLevel(logging.DEBUG)
logFormatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
logFileHandler = logging.FileHandler('javaBindingGeneratorLogger.log',mode='w',encoding='utf-8')
logStreamHandler = logging.StreamHandler()
logFileHandler.setLevel(logging.DEBUG)
logFileHandler.setFormatter(logFormatter)
logStreamHandler.setLevel(logging.INFO)
logStreamHandler.setFormatter(logFormatter)
javaBindingGeneratorLogger.addHandler(logFileHandler)
javaBindingGeneratorLogger.addHandler(logStreamHandler)

class JavaBindingGenerator(object):
    '''
    This class generates all the Java HELICS language binding source code.
    
    @param headerFiles: List[str] - the list of the HELICS C API headers
    @method generateSource(void) - this function generates all the Java
        HELICS language binding source code files.
    '''


    def __init__(self, rootDir: str, headerFiles: List[str]):
        '''
        Constructor
        '''
        self.__helicsParser = clangparser.HelicsHeaderParser(headerFiles)
        self.__rootDir = os.path.abspath(rootDir)
        
    def generateSource(self):
        """
            Function that creates the HELICS Java Bindings
        """
        def createBoilerPlate(headerFilename, constantlist, importStandAloneConstFiles) -> str:
            javaBoilerPlateStr = ""
            copyRightStr = "/*\n"
            copyRightStr += "Copyright (c) 2017-2022,\n"
            copyRightStr += "Battelle Memorial Institute; Lawrence Livermore National Security, LLC; Alliance for Sustainable Energy, LLC.  See\n"
            copyRightStr += "the top-level NOTICE for additional details. All rights reserved.\n"
            copyRightStr += "SPDX-License-Identifier: BSD-3-Clause\n"
            copyRightStr += "*/\n\n"
            javaBoilerPlateStr = copyRightStr
            javaBoilerPlateStr += "package com.java.helics;\n\n"  
            for constfile in importStandAloneConstFiles:
                javaBoilerPlateStr += f"import static com.java.helics.{constfile}.*\n"
            javaBoilerPlateStr += "import com.sun.jna.*;\n"
            javaBoilerPlateStr += "import com.sun.jna.ptr.*;\n\n"
            javaBoilerPlateStr += "public class JavaHELICS {\n\t"
            for constant in constantlist:
                javaBoilerPlateStr += f"{constant}\n\t"
            javaBoilerPlateStr += "public interface HelicsInterface extends Library {\n\t\t"
            javaBoilerPlateStr += "HelicsInterface INSTANCE = (HelicsInterface)Native.loadLibrary('helics', HelicsInterface.class);\n\t\t"
            return javaBoilerPlateStr  
             
        def createEnum(enumDict: dict()) -> None:
            """
                Create Java Binding Enumerations for each C Enumeration
            """
            javaBindingGeneratorLogger.debug(f"creating Java enum definition for:\n{json.dumps(enumDict,indent=4,sort_keys=True)}")
            enumSpelling = enumDict.get('spelling','')
            enumComment = enumDict.get('brief_comment','')
            with open(os.path.join(self.__rootDir, f"javaBindings\\+helics\\{enumSpelling}.java"),"w") as enumMFile:
                
                enumMFile.write("/*\n"+f"{enumComment}\n\nAttributes:")
                docStrBody = ""
                enumStrBody = ""
                size = len(enumDict.get('enumerations',{}).keys())
                count = 0
                for enumKey in enumDict.get('enumerations',{}).keys():
                    keywordSpelling = enumDict.get('enumerations',{}).get(enumKey,{}).get('spelling','')
                    keywordValue = enumDict.get('enumerations',{}).get(enumKey,{}).get('value')
                    keywordComment = enumDict.get('enumerations',{}).get(enumKey,{}).get('brief_comment','')
                    if keywordComment == None:
                        keywordComment = ''
                    docStrBody += f"\n\t{keywordSpelling}: value:{keywordValue}\t{keywordComment}"
                    if count == size - 1:
                        enumStrBody += f"\n\t{keywordSpelling}({keywordValue});"
                    else:
                        enumStrBody += f"\n\t{keywordSpelling}({keywordValue}),"
                    count = count + 1 
                enumMFile.write(docStrBody)
                enumMFile.write("\n*/")
                enumMFile.write(f"\npackage com.java.helics;\n\npublic enum {enumSpelling}"+"{")
                enumMFile.write(enumStrBody)
                enumMFile.write(f"\n\tprivate final int value;")
                enumMFile.write(f"\n\tprivate {enumSpelling}(int value)"+"{"+"\n\t\tthis.value = value;\n\t}")
                enumMFile.write(f"\n\tpublic int value()"+"{"+"\n\t\treturn this.value;\n\t}")
                enumMFile.write("\n}")
        def createStruct(structDict: dict()) -> None:
            """
                Create Java Binding Objects for each C Structures
            """
            javaBindingGeneratorLogger.debug(f"creating Java struct definition for:\n{json.dumps(structDict,indent=4,sort_keys=True)}")
            strucSpelling = structDict.get('spelling','')
            strucComment = structDict.get('brief_comment','')
            with open(os.path.join(self.__rootDir, f"javaBindings\\+helics\\{strucSpelling}.java"),"w") as structFile:
                structFile.write("/*\n"+f"{strucComment}\n*/")
                structFile.write("\npackage com.java.helics")
                structFile.write("\n\nimport com.sun.jna.Structure")
                structFile.write(f"\n\npublic static class {strucSpelling} extends Structure"+"{")
                structStrBody = ""
                for structKey in structDict.get('members',{}).keys():
                    keywordSpelling = structDict.get('members',{}).get(structKey,{}).get('spelling','')
                    keywordtype = structDict.get('members',{}).get(structKey,{}).get('type')
                    keywordcomm = structDict.get('members',{}).get(structKey,{}).get('brief_comment')
                    if keywordtype == "Pointer" and strucSpelling == "HelicsError":
                        structStrBody += f"\n\t/*{keywordcomm}*/\n\tpublic string {keywordSpelling};"
                    else: 
                        structStrBody += f"\n\t/*{keywordcomm}*/\n\tpublic {keywordtype} {keywordSpelling};"
            
               
                structFile.write(structStrBody)
                structFile.write(f"\n"+"}")

        def createMacro(macroDict: dict()) -> str:
            """
                Create Java Binding Constants for each C Macro
            """
            javaBindingGeneratorLogger.debug(f"creating Java Macro style definition for:\n{json.dumps(macroDict,indent=4,sort_keys=True)}")
            macroSpelling = macroDict.get("spelling","")
            macroComment = macroDict.get("brief_comment","")
            macroValue = macroDict.get("value")
            newMacroSpellingTmp = macroSpelling.title().split('_')
            newMacroSpelling = "".join(newMacroSpellingTmp)
            ignoreMacro = "HELICS_DATA_TYPE_CHAR"
            if isinstance(macroValue, float) and macroSpelling != ignoreMacro:
                with open(os.path.join(self.__rootDir, f"javaBindings\\+helics\\{newMacroSpelling}.java","w"), "w") as macroFile:
                    if macroComment != None:
                        macroFile.write("/*\n")
                        macroFile.write(f"{macroComment}\n")
                        macroFile.write("*/\n")
                    macroFile.write(f"\npackage com.java.helics;\n\npublic final class {newMacroSpelling}"+"{")
                    macroFile.write(f"\n\tpublic static final double {macroSpelling} = {macroValue};")
                    macroFile.write(f"\n\tprivate {newMacroSpelling}()"+"{}")
                    macroFile.write("\n}")
                    return newMacroSpelling
            elif isinstance(macroValue, int) and macroSpelling != ignoreMacro:
                with open(os.path.join(self.__rootDir, f"javaBindings\\+helics\\{newMacroSpelling}.java","w"), "w") as macroFile:
                    if macroComment != None:
                        macroFile.write("/*\n")
                        macroFile.write(f"{macroComment}\n")
                        macroFile.write("*/\n")
                    macroFile.write(f"\npackage com.java.helics;\n\npublic final class {newMacroSpelling}"+"{")
                    macroFile.write(f"\n\tpublic static final int {macroSpelling} = {macroValue};")
                    macroFile.write(f"\n\tprivate {newMacroSpelling}()"+"{}")
                    macroFile.write("\n}")
                    return newMacroSpelling
            else:
                return ""
        def createVar(varDict: dict())-> str:
            javaBindingGeneratorLogger.debug(f"creating Java var definition for:\n{json.dumps(varDict,indent=4,sort_keys=True)}")
            varSpelling = varDict.get("spelling","")
            varComment = varDict.get("brief_comment","")
            varValue = varDict.get("value")
            varType = varDict.get("type")
            varstr = ""
            if isinstance(varValue, str):
                if varComment != None:
                    varstr += "\n\t/*\n\t"
                    varstr += f"{varComment}\n\t"
                    varstr += "*/"
                if varType == "Double" or varType == "HelicsTime":
                    varstr += f"\n\tpublic static final double {varSpelling} =  {varValue};"
            elif isinstance(varValue, float):
                if varComment != None:
                    varstr += "/*\n"
                    varstr += f"{varComment}\n"
                    varstr += "*/\n"
                varstr +=(f"\n\tpublic static final double {varSpelling} =  {varValue};\n")
            elif isinstance(varValue, int):
                if varComment != None:
                    varstr += "/*\n"
                    varstr += f"{varComment}\n"
                    varstr += "*/\n"
                varstr +=(f"\n\tpublic static final int {varSpelling} =  {varValue};\n")
            return varstr
        def createTypeSafePointer(typeSafepointerDict: dict()):
            javaBindingGeneratorLogger.debug(f"creating Java typesafe pointer definition for:\n{json.dumps(typeSafepointerDict,indent=4,sort_keys=True)}")
            tsPointerSpelling = typeSafepointerDict.get("spelling","")
            tsPointerComment = typeSafepointerDict.get("brief_comment","")
            tsPointerType = typeSafepointerDict.get("value")
            if tsPointerType == "void *":
                with open(os.path.join(self.__rootDir, f"javaBindings\\+helics\\{tsPointerSpelling}.java","w"), "w") as tysPointerFile:
                    if tsPointerComment != None:
                        tysPointerFile.write("/*\n")
                        tysPointerFile.write(f"{tsPointerComment}\n")
                        tysPointerFile.write("*/\n")
                    tysPointerFile.write(f"\npackage com.java.helics;\n\nimport com.sun.jna.PointerType.java\n\npublic class {tsPointerSpelling}"+" extends {}")
            else:
                return
        def createFunction(functionDict: dict(), cursorIdx: int):
            javaBindingGeneratorLogger.debug(f"creating MATLAB function definition for:\n{json.dumps(functionDict,indent=4,sort_keys=True)}")
            functionsToIgnoreJNA = [
                "helicsCreateBrokerFromArgs",
                "helicsCreateCoreFromArgs",
                "helicsFederateInfoLoadFromArgs",
                "helicsEndpointSendBytes",
                "helicsEndpointSendBytesAt",
                "helicsEndpointSendBytesTo",
                "helicsEndpointSendBytesToAt",
                "helicsInputGetNamedPoint",
                "helicsInputGetString",
                "helicsInputGetVector",
                "helicsInputGetComplexVector",
                "helicsInputSetDefaultBytes",
                "helicsInputSetDefaultVector",
                "helicsInputSetDefaultComplexVector",
                "helicsMessageAppendData",
                "helicsMessageGetBytes",
                "helicsMessageSetData",
                "helicsMessageGetBytesPointer",
                "helicsPublicationPublishBytes",
                "helicsPublicationPublishVector",
                "helicsPublicationPublishComplexVector",
                "helicsLoadSignalHandlerCallback",
                "helicsLoadSignalHandlerCallbackNoExit",
                "helicsBrokerSetLoggingCallback",
                "helicsCoreSetLoggingCallback",
                "helicsFederateSetLoggingCallback",
                "helicsFilterSetCustomCallback",
                "helicsFederateSetQueryCallback",
                "helicsFederateSetTimeUpdateCallback",
                "helicsFederateSetStateChangeCallback",
                "helicsFederateSetTimeRequestEntryCallback",
                "helicsFederateSetTimeRequestReturnCallback",
                "helicsTranslatorSetCustomCallback",
                "helicsTimeToBytes",
                "helicsComplexToBytes",
                "helicsComplexObjectToBytes",
                "helicsVectorToBytes",
                "helicsComplexVectorToBytes",
                "helicsDataBufferToString",
                "helicsDataBufferToRawString",
                "helicsDataBufferToVector",
                "helicsDataBufferToComplexVector",
                "helicsDataBufferToNamedPoint",
                "helicsCloseLibrary",
                "helicsErrorInitialize",
                "helicsErrorClear"
            ]
            functionName = functionDict.get("spelling")
            if functionName not in functionsToIgnoreJNA:
                functionName = functionDict.get("spelling")
                functionComment = functionDict.get("raw_comment")
                functionJavaCallStr = "\n\t\t"
                functionJavaCallStr += functionComment+"\n\t\t"
                functionReturnType = getFunctionReturnType(functionDict)
                functionJavaCallStr += "\n\t\t" + functionReturnType 
                functionJavaCallStr += " "+ functionName + "("
                size = len(functionDict.get('arguments',{}).keys())
                count = 0
                for a in functionDict.get('arguments',{}).keys():
                    if count == size - 1:
                        functionJavaCallStr += getFunctionArgs(functionDict.get('arguments',{}).get(a,{})) + ");"
                    else:
                        functionJavaCallStr += getFunctionArgs(functionDict.get('arguments',{}).get(a,{})) + ","
                    count = count + 1
                return functionJavaCallStr
            
            else:
                return ""

        def getFunctionReturnType(functionDict: dict()) -> str:
            returnFunctionTypeTextMap = {
                "Char_S": "char",
                "Char_S_*": "String",
                "Double": "double",
                "Int": "int",
                "Void": "void",
                "Void_*": "Pointer",
                "HelicsBool": "int",
                "HelicsBroker": "HelicsBroker",
                "HelicsCore": "HelicsCore",
                "HelicsDataBuffer": "HelicsDataBuffer",
                "HelicsEndpoint": "HelicsEndpoint",
                "HelicsFederate": "HelicsFederate",
                "HelicsFederateInfo": "HelicsFederateInfo",
                "HelicsFederateState": "HelicsFederateState",
                "HelicsFilter": "HelicsFilter",
                "HelicsInput": "HelicsInput",
                "HelicsIterationResult": "HelicsIterationResult",
                "HelicsMessage": "HelicsMessage",
                "HelicsPublication": "HelicsPublication",
                "HelicsQuery": "HelicsQuery",
                "HelicsTime": "double",
                "HelicsTranslator": "HelicsTranslator",
                "int32_t": "int",
                "int64_t": "long"
            }
            returnstr = ""     
            returnType = functionDict.get('double_pointer_type')
            if returnType == None:
                returnType = functionDict.get('pointer_type')
                if returnType== None:
                    returnType = functionDict.get('result_type')
            returnstr = returnFunctionTypeTextMap.get(returnType)
            if returnstr == None:
                raise RuntimeError(f"Unhandled c return type conversion for {returnType}. Please update the returnTextMap. functionDict: {json.dumps(functionDict, indent=4, sort_keys=True)}")     
            return returnstr

        def getFunctionArgs(argDict: dict()):
            argFunctionCallTextMap = {
                "Char_S": "char",
                "Char_S_*": "String",
                "Double": "double",
                "Double_*": "DoubleByReference",
                "Int": "int",
                "Int_*": "IntByReference",
                "Void_*": "Pointer",
                "HelicsBool": "int",
                "HelicsBroker":"HelicsBroker",
                "HelicsCore": "HelicsCore",
                "HelicsDataBuffer": "HelicsDataBuffer",
                "HelicsDataTypes": "int",
                "HelicsEndpoint": "HelicsEndpoint",
                "HelicsError_*": "HelicsError",
                "HelicsFederate": "HelicsFederate",
                "HelicsFederateInfo": "HelicsFederateInfo",
                "HelicsFilter": "HelicsFilter",
                "HelicsFilterTypes": "int",
                "HelicsInput": "HelicsIterationRequest",
                "HelicsIterationRequest": "HelicsIterationRequest",
                "HelicsIterationResult_*": "HelicsIterationResult", 
                "HelicsMessage": "HelicsMessage",
                "HelicsPublication": "HelicsPublication",
                "HelicsQuery": "HelicsQuery",
                "HelicsQueryBuffer": "HelicsQueryBuffer",
                "HelicsTime": "double",
                "HelicsTranslator": "HelicsTranslator",
                "HelicsTranslatorTypes": "int",
                "int32_t": "int",
                "int64_t": "long"
            }
            returnstr = ""
            argType = argDict.get("double_pointer_type")
            if argType == None:
                argType = argDict.get("pointer_type")
                if argType == None:
                    argType = argDict.get("type")
            returnstr = argFunctionCallTextMap.get(argType)
            if argType == None:
                raise RuntimeError(f"Unhandled c argument type conversion for {argType}. Please update the argFunctionCallTextMap")
           
            returnstr += " "+ argDict.get("spelling")
            return returnstr 

        helicsJAVAStr = ""
        helicsJAVAWrapperFunctions = []
        helicsJAVAConstants= []
        helicsJAVAStandAloneConstantFiles = []
        for cu in self.__helicsParser.parsedInfo.keys():
            if self.__helicsParser.parsedInfo[cu]["kind"] == cidx.CursorKind.ENUM_DECL.name:
                createEnum(self.__helicsParser.parsedInfo[cu])
            if self.__helicsParser.parsedInfo[cu]["kind"] == cidx.CursorKind.MACRO_DEFINITION.name:
                standAloneConstFile = createMacro(self.__helicsParser.parsedInfo[cu])
                if standAloneConstFile != "":
                    helicsJAVAStandAloneConstantFiles.append(standAloneConstFile)
            if self.__helicsParser.parsedInfo[cu]["kind"] == cidx.CursorKind.VAR_DECL.name:
                const = createVar(self.__helicsParser.parsedInfo[cu])
                helicsJAVAConstants.append(const)
            if self.__helicsParser.parsedInfo[cu]["kind"] == cidx.CursorKind.STRUCT_DECL.name:
                createStruct(self.__helicsParser.parsedInfo[cu])
            if self.__helicsParser.parsedInfo[cu]["kind"] == cidx.CursorKind.TYPEDEF_DECL.name:
                createTypeSafePointer(self.__helicsParser.parsedInfo[cu])
            if self.__helicsParser.parsedInfo[cu]["kind"] == cidx.CursorKind.FUNCTION_DECL.name:
                functionJAVAWrapperFunctionStr = createFunction(self.__helicsParser.parsedInfo[cu],int(cu))
                helicsJAVAWrapperFunctions.append(functionJAVAWrapperFunctionStr)
        helicsJAVAStr += createBoilerPlate("helics", helicsJAVAConstants, helicsJAVAStandAloneConstantFiles)
        for wrapperStr in helicsJAVAWrapperFunctions:
            helicsJAVAStr += wrapperStr 
        with open(os.path.join(self.__rootDir, "JavaHelics.java"), "w") as helicsJavaFile:
            helicsJavaFile.write(helicsJAVAStr)
        javaBindingGeneratorLogger.info("JAVA HELICS API successfully created!")