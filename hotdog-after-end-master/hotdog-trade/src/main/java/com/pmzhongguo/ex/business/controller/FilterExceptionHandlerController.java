package com.pmzhongguo.ex.business.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 过滤器中的错误提示
 * @date: 2019-01-29 16:41
 * @author: 十一
 */
@ApiIgnore
@Controller
@RequestMapping("/e")
public class FilterExceptionHandlerController extends TopController {

    /**
     * 文章编辑敏感词提示
     * @return
     */
    @RequestMapping("/cms-article-sensitive/{sensitiveWord}")
    @ResponseBody
    public Resp cmsArticleHandler(@PathVariable String sensitiveWord) {
        return new Resp(Resp.FAIL, ErrorInfoEnum.INCLUDE_SENSITIVE_WORDS.getErrorCNMsg() + ": " + sensitiveWord);
    }
}
