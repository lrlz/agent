package com.timevale.agent;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Set;

public class PerformMonitorTransformer implements ClassFileTransformer {

    private static final Set<String> classNameSet = new HashSet<>();
    static {
        classNameSet.add("com.timevale.agent.AgentTest");
    }

    @Override
    public byte[] transform( ClassLoader loader, String className,
                             Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                             byte[] classfileBuffer ) throws IllegalClassFormatException {
        String currentClassName = className.replaceAll("/", ".");

        if (!classNameSet.contains(className)) {
            return null;
        }
        System.out.println("transform:["+ currentClassName +"]");

        try {
            CtClass ctClass = ClassPool.getDefault().get(currentClassName);
            CtBehavior[] methods = ctClass.getDeclaredBehaviors();
            for(CtBehavior method : methods) {
                enhanceMethod(method);
            }
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void enhanceMethod(CtBehavior method) throws Exception {
        if (method.isEmpty()) {
            return;
        }
        String methodName = method.getName();
        if (methodName.equalsIgnoreCase("main")) {
            return;
        }

        final StringBuilder source = new StringBuilder();

        source.append("{")
                .append("long start = System.nanoTime();\n") // 前置增强: 打入时间戳
                .append("$_ = $proceed($$);\n") // 保留原有的代码处理逻辑
                .append("System.out.print(\"method:[" + methodName + "]\");").append("\n")
                .append("System.out.println(\" cost:[\" +(System.nanoTime() -start)+ \"ns]\");") // 后置增强
                .append("}");

        ExprEditor editor = new ExprEditor(){
            @Override
            public void edit( MethodCall m ) throws CannotCompileException {
                m.replace(source.toString());
            }
        };

        method.instrument(editor);

    }

}
