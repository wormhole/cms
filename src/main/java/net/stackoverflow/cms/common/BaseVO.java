package net.stackoverflow.cms.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseVO {

    private Page page;

    private Map<String, Map<String, Object>[]> data;
}
