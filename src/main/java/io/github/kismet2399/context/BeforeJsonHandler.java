package io.github.kismet2399.context;

/**
 * BeforeJsonHandler在mybatis中可以类似这样使用
 *
 * @author kismet
 */
public interface BeforeJsonHandler {

    /**
     * 获取beforeJson
     *
     * @param beanName 处理的beanName
     * @param bizNo    业务ID
     * @return beforeJson
     */
    String beforeJson(String beanName, String bizNo);
}
