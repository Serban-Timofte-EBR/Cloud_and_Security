package eu.learn.ro;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// Reflection allows a program to inspect and modify its behavior at runtime
public class ReflectionExample {
    public static void main(String[] args) throws Exception {
        // Create instance of a class
        Class<?> clazz = Class.forName("eu.learn.ro.model.Task");
        Object task = clazz.getDeclaredConstructor().newInstance();

        // Access fields via reflection
        Field nameField = clazz.getDeclaredField("name");
        nameField.setAccessible(true); // Bypass private access
        nameField.set(task, "Learn Reflection");

        // Access methods via reflection
        Method getNameMethod = clazz.getDeclaredMethod("getName");
        String name = (String) getNameMethod.invoke(task);

        System.out.println("Task Name: " + name);
    }
}
