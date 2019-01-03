package com.shangsc.secretive;

import java.lang.reflect.Method;

/**
 * @Author ssc
 * @Date 2019/1/3 9:32
 * @Desc 用途：动态加载类
 */
public class EdClassLoader extends ClassLoader {

    private String toolFile = "com.pub.Secretive";

    private EdCipher edCipher = new EdCipher();

    public EdClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        EdCipher edCipher = this.edCipher == null ? new EdCipher() : this.edCipher;
        byte[] data = edCipher.decryptClass(name);
        int length = data == null ? 0 : data.length;
        return defineClass(toolFile, data, 0, length);
    }

    @Override
    public Class<?> loadClass(String s) throws ClassNotFoundException {
        if (!s.contains("Checker")) {
            Class loadedClass = findLoadedClass(s);
            if (loadedClass == null) {
                loadedClass = getParent().loadClass(s);
                return loadedClass;
            } else {
                return loadedClass;
            }
        }
        return findClass(s);
    }

    /**
     * 获取需要解密的类
     */
    public String getToolfile() {
        return toolFile;
    }

    //在动态加载class文件后，需要通过反射才能调用其中方法
    public static void main(String[] args) throws ClassNotFoundException {
        EdClassLoader edClassLoader = new EdClassLoader();
        Object result = null;
        try {
            Class myClass = edClassLoader.loadClass(edClassLoader.getToolfile());
            // method1 就是方法名
            Method method = myClass.getDeclaredMethod("method1");
            Object obj = myClass.newInstance();
            // result 就是方法返回值
            result = method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
    }
}