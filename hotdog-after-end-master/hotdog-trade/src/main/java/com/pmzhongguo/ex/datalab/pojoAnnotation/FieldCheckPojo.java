package com.pmzhongguo.ex.datalab.pojoAnnotation;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class FieldCheckPojo {
    private static final String gener_String = "class java.lang.String";
    private static final String gener_Integer = "class java.lang.Integer";

    public ObjResp validate() throws Exception {

        //通过反射机制获取加载class的属性nodes,包括父类节点
        List<Field> fields = this.getAllFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ParamValidate.class)) {
                ParamValidate paramValidate = field.getAnnotation(ParamValidate.class);
                if (paramValidate.requrie()) {
                    // 如果类型是String
                    if (field.getGenericType().toString().equals(gener_String)) { // 如果type是类类型，则前面包含"class "，后面跟类名
                        // 拿到该属性的gettet方法
                        /**
                         * 这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的
                         * 在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX）
                         * 如果出现NoSuchMethod异常 就说明它找不到那个gettet方法 需要做个规范
                         */
                        Method m = this.getClass().getMethod("get" + getMethodName(field.getName()));
                        String val = (String) m.invoke(this);// 调用getter方法获取属性值
                        if (StringUtils.isEmpty(val))

                            return new ObjResp(Resp.FAIL, paramValidate.errorCode(), null);
                    } else if (field.getGenericType().toString().equals(gener_Integer)) { // 如果type是类类型，则前面包含"class "，后面跟类名
                        Method m = this.getClass().getMethod("get" + getMethodName(field.getName()));
                        Integer val = (Integer) m.invoke(this);// 调用getter方法获取属性值
                        if (val != null) {
                            System.out.println("String type:" + val);
                        }
                    }
                }
            }
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    /**
     * 把一个字符串的第一个字母大写、效率是最高的、
     */
    private String getMethodName(String fildeName) throws Exception {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

    private List<Field> getAllFields() {
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = this.getClass();
        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
        }
        return fieldList;
    }

//    public static void main(String[] args) {
//
//        List<Field> fieldList = new ArrayList<>() ;
//        Class tempClass = VoucherDetailsVo.class;
//        while (tempClass != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
//            fieldList.addAll(Arrays.asList(tempClass .getDeclaredFields()));
//            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
//        }
//        for (Field f : fieldList) {
//            System.out.println("getFields---"+f.getName());
//        }
//}
}

