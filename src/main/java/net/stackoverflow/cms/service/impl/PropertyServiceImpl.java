package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.PropertyDAO;
import net.stackoverflow.cms.model.dto.PropertyDTO;
import net.stackoverflow.cms.model.entity.Property;
import net.stackoverflow.cms.service.PropertyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 属性配置服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyDAO propertyDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Property findByKey(String key) {
        Property property = null;
        if (!StringUtils.isEmpty(key)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.eq("key", key);
            List<Property> properties = propertyDAO.selectByCondition(builder.build());
            if (!CollectionUtils.isEmpty(properties)) {
                property = properties.get(0);
            }
        }
        return property;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PropertyDTO> findAll() {
        List<Property> properties = propertyDAO.selectByCondition(new QueryWrapper());
        List<PropertyDTO> propertyDTOS = new ArrayList<>();
        for (Property property : properties) {
            PropertyDTO propertyDTO = new PropertyDTO();
            BeanUtils.copyProperties(property, propertyDTO);
            propertyDTOS.add(propertyDTO);
        }
        return propertyDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(List<PropertyDTO> propertyDTOS) {
        for (PropertyDTO propertyDTO : propertyDTOS) {
            updateByKey(propertyDTO.getKey(), propertyDTO.getValue());
        }
    }

    @Override
    @Transactional
    public void updateByKey(String key, String value) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.update("value", value);
        builder.update("ts", new Date());
        builder.eq("key", key);
        propertyDAO.updateByCondition(builder.build());
    }


}
