package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.dto.PropertyDTO;

import java.util.List;

/**
 * 属性配置服务类接口
 *
 * @author 凉衫薄
 */
public interface PropertyService {

    PropertyDTO findByKey(String key);

    List<PropertyDTO> findByKeys(List<String> keys);

    void batchUpdateByKey(List<PropertyDTO> propertyDTOS);

    void updateByKey(String key, String value);
}
