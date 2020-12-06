package com.zhi.test;

import com.zhi.entity.Order;
import com.zhi.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.infra.hint.HintManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @Description 单元测试的测试类一定要和启动类在同一个根目录下。
 * 参考<link href="https://blog.csdn.net/qq_31226223/article/details/110199271"/>
 * 参考<link href="https://github.com/lw1243925457/JAVA-000/tree/main/Week_07"/>
 * 参考<link href="https://github.com/ykthree/JAVA-000/tree/main/Week_07"/>
 * 参考<link href="https://shardingsphere.apache.org/document/legacy/3.x/document/en/manual/sharding-jdbc/usage/read-write-splitting/"/>
 * @Author WenZhiLuo
 * @Date 2020-12-06 16:20
 */
@SpringBootTest
@Slf4j
public class OrderServiceTest {
    @Autowired
    private IOrderService orderService;

    /**
     * 写操作测试
     */
    @Test
    public void testInsert() {
        int insert = orderService.insert(buildOrder());
        log.info("successful counts：{}",insert);
    }

    /**
     * 读操作测试
     * 读操作仅走读库，且负载均衡生效。
     */
    @Test
    public void testSelectById() {
        //连续进行10次读取 测试负载均衡
        for (int i = 0; i < 10; i++) {
            Order order = orderService.selectById(1);
            log.info("order信息 ：{}", order);
            assertNotNull(order);
            assertEquals(1, order.getId());
        }
    }

    /**
     * 强制主库读操作测试
     * TODO：并未生效，还是走从库
     */
    @Test
    public void readMasterTest() {
        //强制主库
        HintManager hintManager = HintManager.getInstance();
        hintManager.setPrimaryRouteOnly();
        //连续进行2次读取
        for (int i = 0; i < 2; i++) {
            Order orderInfo = orderService.selectById(1);
            log.info("读操作 {}", orderInfo);
        }
        //关闭
        hintManager.close();
    }

    /**
     * 混合操作测试
     * 前2次读操作走读库，中间写操作走主库（打开主库数据表查看），后2次读操作走读库。
     * 读操作负载均衡生效。
     */
    @Test
    public void readWriteTest() {
        //连续进行2次读取 测试负载均衡
        for (int i = 0; i < 2; i++) {
            Order order = orderService.selectById(1);
            log.info("order信息 ：{}", order);
            assertNotNull(order);
            assertEquals(1, order.getId());
        }

        int insert = orderService.insert(buildOrder());
        log.info("successful counts：{}",insert);

        for (int i = 0; i < 2; i++) {
            Order order = orderService.selectById(1);
            log.info("order信息 ：{}", order);
            assertNotNull(order);
            assertEquals(1, order.getId());
        }
    }

    /**
     * 事务内混合操作测试
     * TODO：报错：Failed to retrieve PlatformTransactionManager for @Transactional test
     * 如果正常的话，结果应该是开启了事务，只分配了一个连接（读库1），且在写操作之后，所有读操作都分配到了主库。
     * 这刚好和官方说的“同一线程且同一数据库连接内，如有写入操作，以后的读操作均从主库读取，用于保证数据一致性”一样。
     */
    @Test
    @Transactional(rollbackFor = Exception.class)
    public void readWriteTransactionTest() {
        //连续进行2次读取 测试负载均衡
        for (int i = 0; i < 2; i++) {
            Order order = orderService.selectById(1);
            log.info("order信息 ：{}", order);
            assertNotNull(order);
            assertEquals(1, order.getId());
        }

        int insert = orderService.insert(buildOrder());
        log.info("successful counts：{}",insert);

        for (int i = 0; i < 2; i++) {
            Order order = orderService.selectById(1);
            log.info("order信息 ：{}", order);
            assertNotNull(order);
            assertEquals(1, order.getId());
        }
    }

    private Order buildOrder() {
        Order order = new Order();
        order.setOrderSn("AAA");
        order.setCustomerId(1);
        order.setOrderStatus((short) 1);
        order.setCreateTime(new Date());
        order.setPayTime(new Date());
        order.setShipTime(new Date());
        order.setReceiveTime(new Date());
        order.setDiscountMoney(new BigDecimal("30"));
        order.setShipMoney(new BigDecimal("0"));
        order.setPayMoney(new BigDecimal("99"));
        order.setPayMethod((short) 1);
        order.setAddress("CHANGSHA");
        order.setReceiveUser("Bob");
        order.setShipSn("BBB");
        order.setShipCompanyName("CCC");
        return order;
    }
}
