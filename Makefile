
build:
	javac source/RAM.java
	javac source/CPU.java
	javac source/Controler.java
	javac source/Simulation.java
	javac source/Byte.java
	javac source/Register.java
	javac source/RegisterBank.java
	javac source/Bus.java

run:
	java Simulation

clean:
	rm -f *.class