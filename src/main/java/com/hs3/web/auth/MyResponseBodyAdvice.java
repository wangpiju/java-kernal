//package com.hs3.web.auth;
//
//import org.springframework.core.MethodParameter;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//
//import javax.annotation.Resource;
//
///**
// * program: java-kernal
// * des:
// * author: Terra
// * create: 2018-07-15 17:47
// **/
//@ControllerAdvice
//public class MyResponseBodyAdvice implements ResponseBodyAdvice {
//
//    @Resource
//
//    @Override
//    public Object  beforeBodyWrite(Object  result, MethodParameter returnType, MediaType selectedContentType,
//                                   Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        //获取在ApiInterceptor对指定请求参数放如到线程局部变量的对象
//        RequestModel model = RequestModel.getRequestModel();
//        //移除线程局部变量,释放内存
//        RequestModel.removeRequestModel();
//        //保存日志
//        operateLogService.saveLog(model, result);
//        return result;
//    }
//
//    @Override
//    public boolean supports(MethodParameter arg0, Class<? extends HttpMessageConverter<?>> arg1) {
//        //Do Nothing.
//        return true;
//    }
//}
