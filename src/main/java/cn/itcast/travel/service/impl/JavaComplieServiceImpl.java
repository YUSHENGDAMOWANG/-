package cn.itcast.travel.service.impl;

import cn.itcast.travel.config.Constans;
import cn.itcast.travel.enums.ResultTypeEnum;
import cn.itcast.travel.exception.ComplieException;
import cn.itcast.travel.service.JavaComplieService;
import cn.itcast.travel.util.ClassClassLoader;
import cn.itcast.travel.vo.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;


import static cn.itcast.travel.config.Constans.clasName;
import static cn.itcast.travel.config.Constans.classPath;



public class JavaComplieServiceImpl implements JavaComplieService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Class complie(String javaSource) throws Exception {

        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager standardFileManager = javaCompiler.getStandardFileManager(null, null, null);
        StringObject so = new StringObject(clasName, javaSource);
        JavaFileObject file = so;
        Iterable options = Arrays.asList("-d", classPath);
        Iterable<? extends JavaFileObject> files = Arrays.asList(file);

        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, standardFileManager, null, options, null, files);
        Boolean result = task.call();
        if (result) {
            logger.info("complie java source success!");
        } else {
            logger.info("complie java scoure fail!");
            throw new ComplieException("complie java scoure fail exception");
        }
        return loadClass(clasName);
    }

    /**
     * 加载CLASS
     *
     * @param clasName 类名
     * @return class文件
     */
    public Class loadClass(String clasName) throws Exception {
//        Class<?> clazz = Class.forName("Main");
        //用自定义classLoader加载这个class
        ClassClassLoader classClassLoader = new ClassClassLoader(getClass().getClassLoader());
        Class<?> clazz = classClassLoader.loadClass(clasName);
        return clazz;
    }


    @Override
    public ResultResponse excuteMainMethod(Class clazz) throws Exception {
        return excuteMainMethodWithClass(clazz);
    }


    private ResultResponse excuteMainMethodWithClass(Class clazz) throws Exception {

        ByteArrayOutputStream baoStream = new ByteArrayOutputStream(1024);
        PrintStream cacheStream = new PrintStream(baoStream);
        PrintStream oldStream = System.out;
        System.setOut(cacheStream);//将输出结果保持到baoStream中，以便后面用
        //执行Main方法
        Method method = clazz.getMethod(Constans.excuteMainMethodName, String[].class);

        Long startTime = System.currentTimeMillis();//开始时间

        method.invoke(null, (Object) new String[]{});

        Long endTime = System.currentTimeMillis();//结束时间

        System.setOut(oldStream);//将输出打印到控制台
        String reusltInfo = baoStream.toString();
        ResultResponse resultResponse = new ResultResponse();
        resultResponse.setExcuteResult(reusltInfo);
        resultResponse.setExcuteDurationTime(endTime - startTime);
        resultResponse.setResultTypeEnum(ResultTypeEnum.ok);
        resultResponse.setMessage("ok");
        return resultResponse;
    }

    /**
     * 将args参数设为程序运行时的参数
     *
     * @param args 参数数组
     */
    private void setInputArgs(String[] args) {
        StringBuffer argSb = new StringBuffer();
        for (String argItem : args) {
            argSb.append(argItem);
            argSb.append(" ");
        }
        BufferedInputStream argInputStrem = new BufferedInputStream(new ByteArrayInputStream(argSb.toString().getBytes()));
        System.setIn(argInputStrem);
    }


    private class StringObject extends SimpleJavaFileObject {
        private String contents = null;

        public StringObject(String clasName, String contents) throws Exception {
            super(URI.create("String:///" + clasName + Kind.SOURCE.extension), Kind.SOURCE);
            this.contents = contents;
        }

        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return contents;
        }
    }


    public static void main(String[] args) {
        while (true) {
            String code = "public class Main {\n" +
                    "\tpublic static void main(String[] args){\n" +
                    "\t\tSystem.out.println(\"hello\t\t\t\t\tworld\");\n" +
                    "\t}\n" +
                    "}";
            System.out.println(code);
            JavaComplieServiceImpl javaComplieService = new JavaComplieServiceImpl();
            try {
                Class clazz = javaComplieService.complie(code);
                ResultResponse resultResponse = javaComplieService.excuteMainMethod(clazz);
                System.out.println("--------->" + resultResponse.getExcuteResult());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
