package com.timevale.agent;

import java.util.ArrayList;
import java.util.List;

public class AgentTest {

    private void fun1() throws Exception {
        System.out.println("this is fun 1.");
        Thread.sleep(500);
    }

    private void fun2() throws Exception{
        System.out.println("this is fun 2.");
        Thread.sleep(1000);
    }


    public static void main(String[] args) throws Exception {
        AgentTest test = new AgentTest();
        System.out.println("this is main");
        test.fun1();
        test.fun2();
        while (true) {
            List<Object> list = new ArrayList<Object>();
            list.add("hello world");
        }
    }
}
