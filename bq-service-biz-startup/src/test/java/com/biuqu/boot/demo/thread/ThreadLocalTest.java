package com.biuqu.boot.demo.thread;

import com.biuqu.utils.JsonUtil;
import com.biuqu.utils.RandomUtil;
import lombok.Data;

import java.security.SecureRandom;

/**
 * ThreadLocal验证类
 * <p>
 * 参考：
 * https://pdai.tech/md/java/thread/java-thread-x-threadlocal.html
 *
 * @author BiuQu
 * @date 2023/2/26 21:40
 */
public class ThreadLocalTest implements Runnable
{

    private static final ThreadLocal<Student> threadLocal = new ThreadLocal<>();

    @Override
    public void run()
    {
        String currentThreadName = Thread.currentThread().getName();
        Student student = getStudent();
        System.out.println(currentThreadName + " is running...,age=" + student.getAge());
        SecureRandom random = RandomUtil.create();
        int age = random.nextInt(100);
        System.out.println(currentThreadName + " is set age: " + age);
        student.setAge(age);
        System.out.println(currentThreadName + " is first get age: " + student.getAge());
        try
        {
            Thread.sleep(500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println(currentThreadName + " is second get age: " + student.getAge());
    }

    private static Student getStudent()
    {
        Student student = threadLocal.get();
        if (null == student)
        {
            student = new Student();
            threadLocal.set(student);
        }
        return student;
    }

    public static void main(String[] args)
    {
        Student student = getStudent();
        student.setName("zhang3");
        student.setAge(30);
        ThreadLocalTest t = new ThreadLocalTest();
        Thread t1 = new Thread(new ThreadLocalTest()
        {
            @Override
            public void run()
            {
                ThreadLocalTest.getStudent().setAge(30);
                super.run();
            }
        }, "Thread-1");
        Thread t2 = new Thread(t, "Thread-2");
        t1.start();
        t2.start();
        System.out.println("zhang3[5]:" + JsonUtil.toJson(student));
    }

}

@Data
class Student
{
    private int age;
    private String name;
}