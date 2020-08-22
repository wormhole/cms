package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.dto.PropertyDTO;

import java.util.List;

/**
 * 属性配置服务类接口
 *
 * @author 凉衫薄
 */
public interface PropertyService {

    /**
     * 根据key查询
     *
     * @param key
     * @return
     */
    PropertyDTO findByKey(String key);

    /**
     * 根据key批量查询
     *
     * @param keys
     * @return
     */
    List<PropertyDTO> findByKeys(List<String> keys);

    /**
     * 批量更新
     *
     * @param propertyDTOS
     */
    void batchUpdateByKey(List<PropertyDTO> propertyDTOS);

    /**
     * 更细
     *
     * @param key
     * @param value
     */
    void updateByKey(String key, String value);
}
