package clickme.nocubes.asm;

import java.util.Iterator;
import java.util.ListIterator;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class BlockTweakInjector implements IClassTransformer {
   public byte[] transform(String name, String transformedName, byte[] bytes) {
      if(bytes == null) {
         return null;
      } else if(!"net.minecraft.block.Block".equals(name)) {
         return bytes;
      } else {
         ClassNode classNode = new ClassNode();
         ClassReader classReader = new ClassReader(bytes);
         classReader.accept(classNode, 8);
         MethodNode targetMethod = null;
         Iterator injectedMethod = classNode.methods.iterator();

         while(injectedMethod.hasNext()) {
            MethodNode iterator = (MethodNode)injectedMethod.next();
            if(iterator.name.equals("shouldSideBeRendered") && iterator.desc.equals("(Lnet/minecraft/world/IBlockAccess;IIII)Z")) {
               targetMethod = iterator;
               break;
            }
         }

         if(targetMethod == null) {
            return bytes;
         } else {
            System.out.println("Inside the Block class: " + name);
            MethodNode var12 = new MethodNode();
            ListIterator var13 = targetMethod.instructions.iterator();
            int varCount = 0;

            while(var13.hasNext()) {
               AbstractInsnNode writer = (AbstractInsnNode)var13.next();
               if(writer.getOpcode() == 165) {
                  JumpInsnNode varInsnNode = (JumpInsnNode)writer;
                  targetMethod.instructions.insert(writer, new JumpInsnNode(154, varInsnNode.label));
                  targetMethod.instructions.insert(writer, new MethodInsnNode(184, "clickme/nocubes/NoCubes", "isBlockNatural", "(Lnet/minecraft/block/Block;)Z"));
                  targetMethod.instructions.insert(writer, new VarInsnNode(25, 24));
                  System.out.println("Inserted instructions extra check");
               }

               if(writer.getOpcode() == 21) {
                  VarInsnNode var15 = (VarInsnNode)writer;
                  if(var15.var == 19) {
                     ++varCount;
                     if(varCount == 2) {
                        targetMethod.instructions.insertBefore(writer, var12.instructions);
                        System.out.println("Inserted instructions render hook");
                     }
                  }
               }
            }

            ClassWriter var14 = new ClassWriter(3);
            classNode.accept(var14);
            return var14.toByteArray();
         }
      }
   }
}
