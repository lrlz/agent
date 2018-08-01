package com.timevale.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class MyAgent {

    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("enter premain method...");
        ClassFileTransformer transformer = new PerformMonitorTransformer();
        instrumentation.addTransformer(transformer);
    }

}
