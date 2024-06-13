package abr.tas.javaagentexample;



import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;



public class JavaAgentExample {
    public static void premain(String premainArgs, Instrumentation inst) { // This will get loaded before the main program is loaded
        System.out.println("Java agent example: premain");
        System.out.println("Premain arguments:");
        System.out.println(premainArgs);
        ClassFileTransformer testTransformer = new TestTransformer();
        inst.addTransformer(testTransformer);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) { // This will get loaded idk when
        System.out.println("Java agent example: agentmain");
        System.out.println("Agent arguments:");
        System.out.println(agentArgs);
        // Assuming that class file transformers do not work in agentmain
    }
}
