package clickme.nocubes.asm;

import java.util.Iterator;
import java.util.ListIterator;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class WorldRenderInjector implements IClassTransformer {
   public byte[] transform(String name, String transformedName, byte[] bytes) {
      if(bytes == null) {
         return null;
      } else if(!"blo".equals(name)) {
         return bytes;
      } else {
         ClassNode classNode = new ClassNode();
         ClassReader classReader = new ClassReader(bytes);
         classReader.accept(classNode, 8);
         MethodNode targetMethod = null;
         Iterator injectedMethod = classNode.methods.iterator();

         while(injectedMethod.hasNext()) {
            MethodNode label0 = (MethodNode)injectedMethod.next();
            if(label0.name.equals("a") && label0.desc.equals("(Lsv;)V")) {
               targetMethod = label0;
               break;
            }
         }

         if(targetMethod == null) {
            return bytes;
         } else {
            System.out.println("Inside the WorldRenderer class: " + name);
            MethodNode var16 = new MethodNode();
            Label var17 = new Label();
            var16.visitLabel(var17);
            var16.visitVarInsn(21, 20);
            Label label1 = new Label();
            var16.visitJumpInsn(154, label1);
            Label label2 = new Label();
            var16.visitLabel(label2);
            var16.visitInsn(4);
            var16.visitVarInsn(54, 20);
            Label label3 = new Label();
            var16.visitLabel(label3);
            var16.visitVarInsn(25, 0);
            var16.visitVarInsn(21, 17);
            var16.visitMethodInsn(183, "blo", "b", "(I)V");
            var16.visitLabel(label1);
            var16.visitFrame(2, 1, (Object[])null, 0, (Object[])null);
            var16.visitVarInsn(21, 19);
            var16.visitVarInsn(21, 17);
            var16.visitVarInsn(21, 2);
            var16.visitVarInsn(21, 3);
            var16.visitVarInsn(21, 4);
            var16.visitVarInsn(25, 15);
            var16.visitVarInsn(25, 16);
            var16.visitMethodInsn(184, "clickme/nocubes/renderer/SurfaceNets", "renderChunk", "(IIIILahl;Lblm;)Z");
            var16.visitInsn(128);
            var16.visitVarInsn(54, 19);
            ListIterator iterator = targetMethod.instructions.iterator();
            int varCount = 0;

            while(iterator.hasNext()) {
               AbstractInsnNode writer = (AbstractInsnNode)iterator.next();
               if(writer.getOpcode() == 165) {
                  JumpInsnNode varInsnNode = (JumpInsnNode)writer;
                  targetMethod.instructions.insert(writer, new JumpInsnNode(154, varInsnNode.label));
                  targetMethod.instructions.insert(writer, new MethodInsnNode(184, "clickme/nocubes/NoCubes", "isBlockNatural", "(Laji;)Z"));
                  targetMethod.instructions.insert(writer, new VarInsnNode(25, 24));
                  System.out.println("Inserted instructions extra check");
               }

               if(writer.getOpcode() == 21) {
                  VarInsnNode var19 = (VarInsnNode)writer;
                  if(var19.var == 19) {
                     ++varCount;
                     if(varCount == 2) {
                        targetMethod.instructions.insertBefore(writer, var16.instructions);
                        System.out.println("Inserted instructions render hook");
                     }
                  }
               }
            }

            ClassWriter var18 = new ClassWriter(3);
            classNode.accept(var18);
            return var18.toByteArray();
         }
      }
   }
}
