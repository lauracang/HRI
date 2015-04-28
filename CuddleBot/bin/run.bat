setlocal
set PATH=%PATH%;C:\Program Files (x86)\Arduino\
"C:\Program Files (x86)\Java\jdk1.7.0_75\jre\bin\javac" -cp "C:\Program Files (x86)\Arduino\lib\RXTXcomm.jar" SerialTest.java
"C:\Program Files (x86)\Java\jdk1.6.0_12\jre\bin\java" -cp "C:\Program Files (x86)\Arduino\lib\RXTXcomm.jar;." SerialTest