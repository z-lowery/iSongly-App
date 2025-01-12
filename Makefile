default: runApp

runApp: 
	@javac -cp .:../junit5.jar *.java
	@java App

runTests: ../junit5.jar
	@javac -cp .:../junit5.jar *Tests.java	
	@java -cp .:../junit5.jar org.junit.platform.console.ConsoleLauncher --select-class BackendTests
	@java -cp .:../junit5.jar org.junit.platform.console.ConsoleLauncher --select-class FrontendTests

clean: 
	@rm *.class