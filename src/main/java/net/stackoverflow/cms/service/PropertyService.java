package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.dto.PropertyDTO;
import net.stackoverflow.cms.model.entity.Property;

import java.util.List;

/**
 * 属性配置服务类接口
 *
 * @author 凉衫薄
 */
public interface PropertyService {

    Property findByKey(String key);

    List<PropertyDTO> findAll();

    void update(List<PropertyDTO> propertyDTOS);

    void updateByKey(String key, String value);
}
