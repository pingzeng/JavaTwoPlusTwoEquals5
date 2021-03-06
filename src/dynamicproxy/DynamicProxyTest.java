package dynamicproxy;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

interface HelloWorld
{
    void print();
}

class HelloWorldImpl implements HelloWorld
{
    public void print()
    {
        System.out.println("Hello World");
    }
}


public class DynamicProxyTest implements Serializable
{
    private static final long serialVersionUID = 1L;
 
    private static void test() throws Exception{
    	long start = System.currentTimeMillis();
        HelloWorld helloWorld = (HelloWorld) newProxyInstance();
        System.out.println("动态生成代理耗时：" + (System.currentTimeMillis() - start) + "ms");
        helloWorld.print();
        System.out.println();
    }
    
    public static void main(String[] arg) throws Exception{
    	test();
    }
    public static Object newProxyInstance() throws Exception
    {
        String src = "package dynamicproxy;\n\n" + 
                     "public class StaticProxy implements HelloWorld\n" + 
                     "{\n" + 
                     "\tHelloWorld helloWorld;\n\n" + 
                     "\tpublic StaticProxy(HelloWorld helloWorld)\n" + 
                     "\t{\n" + 
                     "\t\tthis.helloWorld = helloWorld;\n" + 
                     "\t}\n\n" + 
                     "\tpublic void print()\n" + 
                     "\t{\n" + 
                     "\t\tSystem.out.println(\"Before Hello World!\");\n" + 
                     "\t\thelloWorld.print();\n" + 
                     "\t\tSystem.out.println(\"After Hello World!\");\n" + 
                     "\t}\n" + 
                     "}";
 
        /** 生成一段Java代码 */
        String fileDir = System.getProperty("user.dir");
        String fileName = "C:\\Users\\i042416\\git\\JavaTwoPlusTwoEquals5\\src\\dynamicproxy\\StaticProxyDemo.java";
        File javaFile = new File(fileName);
        Writer writer = new FileWriter(javaFile);
        writer.write(src);
        writer.close();
 
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager sjfm = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> iter = sjfm.getJavaFileObjects(fileName);
        CompilationTask ct = compiler.getTask(null, sjfm, null, null, null, iter);
        ct.call();
        sjfm.close();
 
        URL[] urls = new URL[] {(new URL("file:\\" + "C:\\Users\\i042416\\git\\JavaTwoPlusTwoEquals5\\dynamicproxy\\src"))};
        URLClassLoader ul = new URLClassLoader(urls);
        System.out.println("class loader for url");
        Class<?> c = ul.loadClass("dynamicproxy.StaticProxy");
 
        Constructor<?> constructor = c.getConstructor(HelloWorld.class);
        HelloWorld helloWorldImpl = new HelloWorldImpl();
        HelloWorld helloWorld = (HelloWorld)constructor.newInstance(helloWorldImpl);
 
        File classFile = new File(fileDir + "\\src\\com\\xrq\\proxy\\StaticProxy.class");
        javaFile.delete();
        classFile.delete();
 
        return helloWorld;
    }
}