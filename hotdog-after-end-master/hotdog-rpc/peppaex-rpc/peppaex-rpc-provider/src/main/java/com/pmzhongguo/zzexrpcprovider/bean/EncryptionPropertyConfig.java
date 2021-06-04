package com.pmzhongguo.zzexrpcprovider.bean;

import com.pmzhongguo.zzextool.utils.DESEncodeUtil;
import com.pmzhongguo.zzextool.utils.StringNetherSwap;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * springboot读取加密配置文件
 *
 * @author jary
 * @creatTime 2020/5/15 5:45 PM
 */

@Configuration
public class EncryptionPropertyConfig {

    @Bean(name = "encryptablePropertyResolver")
    public EncryptablePropertyResolver encryptablePropertyResolver() {
        return new EncryptionPropertyResolver();
    }

    class EncryptionPropertyResolver implements EncryptablePropertyResolver {

        @Override
        public String resolvePropertyValue(String value) {
            if (StringUtils.isBlank(value)) {
                return value;
            }
            //值以Diff@开头的均为DES加密,需要解密
            if (value.startsWith("Diff@")) {
                value = value.replace("Diff@", "");
//                System.out.println("截取后："+value);
                value = DESEncodeUtil.decrypt(value);
                value = StringNetherSwap.swap(value);
//                System.out.println("解密后："+value);
                return value;
            }
            // 不需要解密的值直接返回
            return value;
        }
    }
}
