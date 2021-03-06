package com.tubugs.springboot.frame.aop;

import com.alibaba.fastjson.JSONObject;
import com.tubugs.springboot.frame.ResponseStatus;
import com.tubugs.springboot.frame.ResponseVo;
import com.tubugs.springboot.frame.SessionManager;
import com.tubugs.springboot.frame.ex.CsrfException;
import com.tubugs.springboot.frame.ex.KickException;
import com.tubugs.springboot.frame.validator.ParamError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by xuzhang2 on 2017/1/18.
 */
@ControllerAdvice
public class HandlerException {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    //运行时异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseVo ExceptionHandler(Exception ex) {
        HttpServletRequest r = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String request = String.format(
                "用户:%s,会话:%s\r\n请求:%s;",
                SessionManager.getUserAccount(),
                SessionManager.getSessionID(),
                String.format("%s %s %s", r.getMethod(), r.getRequestURI(), JSONObject.toJSONString(r.getParameterMap()))
        );

        if (ex instanceof CsrfException) {
            //CSRF攻击
            return new ResponseVo(ResponseStatus.RESPONSE_CSRF);
        } else if (ex instanceof KickException) {
            //禁止用户同时登录
            return new ResponseVo(ResponseStatus.RESPONSE_KICK);
        } else if (ex instanceof ParamError) {
            //参数错误
            return new ResponseVo(ResponseStatus.RESPONSE_ERROR_PARAMS, ex.getMessage());
        }

        //内部异常
        logger.error("Exception :" + request, ex);
        return new ResponseVo(ResponseStatus.RESPONSE_INNER_ERROR);
    }
}
