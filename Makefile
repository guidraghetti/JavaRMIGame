all:			Client.class JogadorInterface.class \
			Server.class JogoInterface.class

Client.class:		Client.java
			@javac Client.java

Server.class:		Server.java
			@javac Server.java

JogoInterface.class:	JogoInterface.java
			@javac JogoInterface.java

JogadorInterface.class:		JogadorInterface.java
			@javac JogadorInterface.java
clean:
			@rm -f *.class *~
