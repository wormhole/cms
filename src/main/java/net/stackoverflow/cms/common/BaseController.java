package net.stackoverflow.cms.common;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.security.CmsUserDetails;
import net.stackoverflow.cms.util.JsonUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Controller基类
 *
 * @author 凉衫薄
 */
@Slf4j
public class BaseController {

    /**
     * 获取UserDetails
     *
     * @return
     */
    protected CmsUserDetails getUserDetails() {
        return (CmsUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * vo转dto
     *
     * @param clazzMap
     * @param vo
     * @return
     */
    protected Map<String, List<Object>> vo2dto(Map<String, Class> clazzMap, BaseVO vo) {

        log.info(JsonUtils.bean2json(vo));

        Map<String, List<Object>> result = new HashMap<>(16);
        Set<String> key = clazzMap.keySet();
        Map<String, Map<String, Object>[]> data = vo.getData();
        Iterator<String> it = key.iterator();

        while (it.hasNext()) {
            String name = it.next();

            if (data.containsKey(name)) {
                Field[] fields = clazzMap.get(name).getDeclaredFields();
                List<Object> dtos = new ArrayList<>();

                for (int i = 0; i < data.get(name).length; i++) {
                    try {
                        Object dto = Class.forName(clazzMap.get(name).getName()).newInstance();
                        for (Field field : fields) {
                            String attr = field.getName();
                            String upName = attr.substring(0, 1).toUpperCase() + attr.substring(1);
                            Method method = clazzMap.get(name).getMethod("set" + upName, field.getType());
                            method.invoke(dto, data.get(name)[i].get(attr));
                        }
                        dtos.add(dto);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        throw new BusinessException("vo2dto转换错误");
                    }
                }
                result.put(name, dtos);
            }
        }

        return result;
    }

    /**
     * vo转dto
     *
     * @param name
     * @param clazz
     * @param vo
     * @return
     */
    protected List<Object> vo2dto(String name, Class clazz, BaseVO vo) {
        Map<String, Class> classMap = new HashMap<String, Class>(16) {{
            put(name, clazz);
        }};
        Map<String, List<Object>> voMap = vo2dto(classMap, vo);
        return voMap.get(name);
    }
}
