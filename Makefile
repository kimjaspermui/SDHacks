run : CarDetectorGUI.o
	java -cp ./Acme.jar:./objectdraw.jar:. CarDetectorGUI

CarDetectorGUI.o : CarDetectorGUI.java
	javac -cp ./Acme.jar:./objectdraw.jar:. CarDetectorGUI.java

clean : 
	rm *.class
