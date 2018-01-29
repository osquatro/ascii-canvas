Ascii Canvas implementation
How to run:

java -DprintStackTrace=true -cp canvas-1.0-SNAPSHOT.jar org.creditsuisse.Main

CommandLineManager allows to use different output streams for commandLine and canvas. For example
user can use computer command line and canvas can be printed into hardware terminal or file.
Input stream for user input also can be replaced .

Parameter -DprintStackTrace=true used to print stack trace into output for debug purposes.


Sample for printing canvas into file:
PrintStream fileWriter = new PrintStream(new FileOutputStream("out.dat"));
new CommandLineManager(System.out, System.in, fileWriter).consume();
