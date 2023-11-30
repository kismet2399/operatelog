package com.kismet.operatelog.starter.context;

/**
 * BeforeJsonHandler在mybatis中可以类似这样使用
 * @Component
 * public class MyBeforeJsonHandler implements BeforeJsonHandler {
 *     @Resource
 *     private Map<String, BaseMapper<?>> maps;
 *
 *     @Override
 *     public String beforeJson(String beanName, String bizNo) {
 *         if (StringUtils.isEmpty(bizNo)) {
 *             return StringUtils.EMPTY;
 *         }
 *         String[] split = bizNo.split(StrPool.COMMA);
 *         BaseMapper<?> mapper = maps.get(beanName);
 *         Object object;
 *         if (split.length == 1) {
 *             object = mapper.selectById(bizNo);
 *         } else {
 *             object = Splitter.of(Arrays.asList(split)).flatMap(mapper::selectBatchIds);
 *         }
 *         return JSON.toJSONString(object);
 *     }
 * }
 *
 * @author kismet
 */
public interface BeforeJsonHandler {

    /**
     * 获取beforeJson
     *
     * @param beanName 处理的beanName
     * @param bizNo 业务ID
     * @return
     */
    String beforeJson(String beanName, String bizNo);
}
