package com.example.servicebase.exceptionhandler;


import com.example.commonutils.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    // 统一异常执行方法
    @ExceptionHandler(Exception.class)
    @ResponseBody // 为了返回数据
    public R error(Exception e){
        e.printStackTrace();
        return R.error().message("方法执行了全局异常处理！");
    }

    // 指定异常执行方法
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody // 为了返回数据
    public R error(ArithmeticException e){
        e.printStackTrace();
        return R.error().message("方法执行ArithmeticException异常！");
    }

    // 自定义异常执行方法
    @ExceptionHandler(CustomException.class)
    @ResponseBody // 为了返回数据
    public R error(CustomException e){
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error().code(e.getCode()).message(e.getMsg());
    }
}
