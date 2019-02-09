# Road Navigation System (RNS)
This code has been developed for the Distributed Programming II course held at the Politecnico di Torino.

## Serializer 
Using the JAXB framework it is developed the Java application for serialization called RnsInfoSerializer (in package it.polito.dp2.RNS.sol1) that serializes the data about the DP2-RNS system into an XML file valid against the schema in the xsd folder. The output XML file include all the information that can be retrieved by accessing the interfaces defined in package it.polito.dp2.RNS, and the implementation of the interfaces to be used as data source are selected using the “abstract factory” pattern: the RnsInfoSerializer creates the data source by instantiating it.polito.dp2.RNS.RnsReaderFactory by means of its static method newInstance(). The application receive the name of the output XML file on the command line as its first and only argument.

The ant script provided can also be used to generate the bindings, compile and run the developed application.
The command for generation of the bindings from your schema is:
```
$ ant generate-bindings
```
The files will be generated into the gen directory. The command for compilation is:
```
$ ant build
```
whereas the command for execution is:
```
$ ant -Doutput=file.xml RnsInfoSerializer
```
This command also calls the targets for binding generation and compilation. Of course, file.xml is the selected output file name. When running the developed application in this way, the pseudo-random data source is used, and the seed and testcase (0,1,2,3) parameters can be set as here:
```
$ ant -Doutput=file.xml -Dtestcase=1 -Dseed=291293 RnsInfoSerializer
```

## Java Library 

