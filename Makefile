# Makefile for wqueue

JC=javac
JFLAGS=

wqueue:
	$(JC) $(JFLAGS) *.java

clean:
	rm *.class
