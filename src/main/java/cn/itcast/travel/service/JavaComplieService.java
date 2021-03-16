package cn.itcast.travel.service;

import cn.itcast.travel.vo.ResultResponse;

/**
 * author: haiyangp
 * date:  2017/9/22
 * desc: JAVA编译器service接口
 */
public interface JavaComplieService {

    /**
     * 编译，并获取编译后的class类
     *
     * @param javaSource JAVA代码
     * @return 编译后的CLASS
     */
    Class complie(String javaSource) throws Exception;

    /**
     * 执行MAIN方法
     *
     * @param clazz 编译后的CLASS
     * @return 执行结果
     */
    ResultResponse excuteMainMethod(Class clazz) throws Exception;


}
