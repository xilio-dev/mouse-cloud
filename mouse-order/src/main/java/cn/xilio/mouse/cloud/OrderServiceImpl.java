
package cn.xilio.mouse.cloud;

import org.apache.seata.core.context.RootContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.Objects;

/**
 * The type Order service.
 *
 * @author jimin.jm @alibaba-inc.com
 */
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    @Resource
    private AccountClient accountClient;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void create(String userId, String commodityCode, int orderCount) {
        LOGGER.info("Order Service Begin ... xid: " + RootContext.getXID());

        // 计算订单金额
        int orderMoney = calculate(commodityCode, orderCount);

        // 从账户余额扣款
        accountClient.debit(userId, orderMoney);


        KeyHolder keyHolder = new GeneratedKeyHolder();

        LOGGER.info(
                "Order Service SQL: insert into order_tbl (user_id, commodity_code, count, money) values ({}, {}, {}, {})",
                userId, commodityCode, orderCount, orderMoney);

        jdbcTemplate.update(con -> {
            PreparedStatement pst = con.prepareStatement(
                    "insert into order_tbl (user_id, commodity_code, count, money) values (?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setObject(1, userId);
            pst.setObject(2, commodityCode);
            pst.setObject(3, orderCount);
            pst.setObject(4, orderMoney);
            return pst;
        }, keyHolder);


        LOGGER.info("Order Service End ... Created " + Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    private int calculate(String commodityId, int orderCount) {
        return 200 * orderCount;
    }

}
