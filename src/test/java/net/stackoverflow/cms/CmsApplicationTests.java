package net.stackoverflow.cms;

import net.stackoverflow.cms.security.CmsMd5PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CmsApplicationTests {

    @Test
    void contextLoads() {
        CmsMd5PasswordEncoder encoder = new CmsMd5PasswordEncoder();
        System.out.println(encoder.encode("admin"));
    }

}
