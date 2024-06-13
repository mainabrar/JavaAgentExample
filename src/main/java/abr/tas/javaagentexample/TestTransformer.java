package abr.tas.javaagentexample;



import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Objects;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;



public class TestTransformer implements ClassFileTransformer, Opcodes { // Implement Opcodes for unnamed references to opcodes
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined, // reference to the java.lang.Class
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer // the entire class is of bytes
    ) {
       if (!Objects.equals(className, "abr/tas/javaagentexample/TestTransformedClass")) return classfileBuffer; // Don't modify anything else
       // Using ObjectWeb ASM, there are similar libraries for editing bytecode like Javassist
       // Using node API
       System.out.println("Found transform class" + className);
       ClassReader cr = new ClassReader(classfileBuffer); // Read the class!
       ClassNode classNode = new ClassNode();
       cr.accept(classNode, 0); // Put the read class data in the classNode

       for (MethodNode method : classNode.methods) { // All methods in the class
           // In this case there is only one method named method
           // However for overloaded methods use the descriptor too
           // org.objectweb.asm.Type class has some methods for getting certain names such as descriptors
           if (method.name.equals("main") && method.desc.equals("([Ljava/lang/String;)V")) {
               System.out.println("Now injecting into main method");
               InsnList listBeforeStart = new InsnList();
               // Get (static) field "out" (a java.io.PrintStream) from System class
               FieldInsnNode getPrintStreamNode = new FieldInsnNode(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
               // Load constant (string)
               LdcInsnNode loadStringNode = new LdcInsnNode("This should run before the main method");
               // Invoke (virtual) method "println" (void) from PrintStream class
               MethodInsnNode invokePrintlnNode = new MethodInsnNode(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
               listBeforeStart.add(getPrintStreamNode);
               listBeforeStart.add(loadStringNode);
               listBeforeStart.add(invokePrintlnNode);
               // Insert this list into the list of normal instructions
               method.instructions.insertBefore(method.instructions.getFirst(), listBeforeStart);

               // Now onto some other injection
               method.instructions.forEach(insnNode -> {
                   // if the instruction is ldc and the constant is that hello world message
                   if (insnNode instanceof LdcInsnNode ldcNode && ldcNode.cst.equals("Hello world! You won't see this message.")) {
                       ldcNode.cst = "Hello world, this is transformed text!";
                   }
               });
           }
       }

       ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
       classNode.accept(cw);
       return cw.toByteArray();
    }
}
