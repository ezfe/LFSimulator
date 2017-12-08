compile:
	rm -rf build
	mkdir build
	javac -d build source/*.java

run:
	java -classpath build Simulation

javadoc:
	rm -rf docs
	mkdir docs
	javadoc -d docs/ source/*.java