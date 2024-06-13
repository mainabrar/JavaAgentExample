package abr.tas.javaagentexample;



public class TestTransformedClass {
    public static void main(String[] args) {
        System.out.println("Hello world! You won't see this message.");
    }
    /*
         0: getstatic     #7                  // Field java/lang/System.out:Ljava/io/PrintStream;
         3: ldc           #13                 // String Hello world! You won\'t see this message.
         5: invokevirtual #15                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
         8: return
     */
}