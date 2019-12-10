package net.stackoverflow.cms.security;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 拒绝访问handler
 *
 * @author 凉衫薄
 */
@Slf4j
public class CmsAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        HttpSession session = request.getSession();
        UserDetails userDetails = (UserDetails) session.getAttribute("user");
        log.error("权限不足:" + userDetails.getUsername() + "->" + request.getRequestURI());

        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            response.setStatus(403);
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            Result result = new Result();
            result.setStatus(Result.Status.FAILURE);
            result.setMessage("权限不足");
            out.write(JsonUtils.bean2json(result));
            out.flush();
            out.close();
        } else {
            response.sendRedirect("/");
        }
    }
}
