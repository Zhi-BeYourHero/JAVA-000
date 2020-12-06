学习笔记
```mysql
-- 用户表
CREATE TABLE user
(
id bigint(20) NOT NULL COMMENT '用户ID,手机号码',
nickname varchar(255) NOT NULL,
password varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文 固定salt) salt)',
salt varchar(10) DEFAULT NULL,
head varchar(128) DEFAULT NULL COMMENT '头像,云存储的ID',
register_date datetime DEFAULT NULL COMMENT '注册时间',
last_login_date datetime DEFAULT NULL COMMENT '上次登录时间',
login_count int(11) DEFAULT 0 COMMENT '登录次数',
PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品表
CREATE TABLE goods
(
    id bigint(20) NOT NULL AUTO_INCREMENT COMMENT'商品ID',
    goods_name varchar(16) DEFAULT NULL COMMENT '商品名称',
    goods_title varchar(64) DEFAULT NULL COMMENT '商品标题',
    goods_img varchar(64) DEFAULT NULL COMMENT'商品的图片',
    goods_detail longtext COMMENT '商品的详情介绍',
    goods_price decimal(10,2) DEFAULT 0.00 COMMENT '商品单价',
    goods_stock int(11) DEFAULT 0 COMMENT '商品库存,-1表示没有限制',
    PRIMARY KEY (id)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='商品表';
 
 -- 订单表
CREATE TABLE order_info
(
    id bigint(20) NOT NULL AUTO_INCREMENT,
    user_id bigint(20) DEFAULT NULL COMMENT '用户ID',
    goods_id bigint(20) DEFAULT NULL COMMENT '商品ID',
    delivery_addr_id bigint(20) DEFAULT NULL COMMENT'收获地址ID',
    goods_name varchar(16) DEFAULT NULL COMMENT '冗余过来的商品名称',
    goods_count int(11) DEFAULT 0 COMMENT '商品数量',
    goods_price decimal(10,2) DEFAULT 0.00 COMMENT'商品单价',
    order_channel tinyint(4) DEFAULT 0 COMMENT '渠道 pc, 2android, 3ios',
    status tinyint(4) DEFAULT '0' COMMENT '订单状态,0新建未支付, 1已支付,2已发货, 3已收货, 4已退款,5已完成',
    create_date datetime DEFAULT NULL COMMENT '订单的创建时间',
    pay_date datetime DEFAULT NULL COMMENT '支付时间',
    PRIMARY KEY (id)
)ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';


```

自己的不太完善，借鉴大佬的加以修改
订单表
```mysql
CREATE TABLE IF NOT EXISTS `mydb`.`t_order_master` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `order_sn` VARCHAR(45) NOT NULL COMMENT '订单编号',
    `customer_id` INT NOT NULL COMMENT '下单人 ID',
    `order_status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '支付方式',
    `create_time` TIMESTAMP NOT NULL COMMENT '下单时间',
    `ship_time` TIMESTAMP NULL COMMENT '发货时间',
    `pay_time` TIMESTAMP NULL COMMENT '支付时间',
    `receive_time` TIMESTAMP NULL COMMENT '收货时间',
    `discount_money` DECIMAL(8,2) NOT NULL COMMENT '优惠金额',
    `ship_money` DECIMAL(8,2) NOT NULL COMMENT '运费金额',
    `pay_money` DECIMAL(8,2) NOT NULL COMMENT '支付金额',
    `pay_method` TINYINT(1) NULL COMMENT '支付方式',
    `address` VARCHAR(200) NOT NULL COMMENT '收货地址',
    `receice_user` VARCHAR(10) NOT NULL COMMENT '收货人',
    `ship_sn` VARCHAR(45) CHARACTER SET 'ascii' NULL COMMENT '快递单号',
    `ship_company_name` VARCHAR(10) NULL COMMENT '快递公司名称',
PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单主表';

CREATE TABLE IF NOT EXISTS `mydb`.`t_order_detail` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `order_id` INT UNSIGNED NOT NULL COMMENT '订单 ID',
    `product_id` INT UNSIGNED NOT NULL COMMENT '商品 ID ',
    `product_name` VARCHAR(45) NOT NULL COMMENT '商品名称',
    `product_count` INT UNSIGNED NOT NULL COMMENT '商品数量',
    `product_price` DECIMAL(8,2) NOT NULL COMMENT '商品单价',
    `product_weight` DECIMAL(8,2) NULL COMMENT '商品量',
    PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '订单明细表';
```

商品表
```mysql
CREATE TABLE IF NOT EXISTS `mydb`.`t_product_info` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `product_code` VARCHAR(45) NOT NULL COMMENT '商品编码',
    `product_name` VARCHAR(45) NOT NULL COMMENT '商品名称',
    `product_status` TINYINT(1) NOT NULL COMMENT '商品状态',
    `price` DECIMAL(8,2) NOT NULL COMMENT '商品价格',
    `weight` FLOAT NULL COMMENT '商品重量',
    `length` FLOAT NULL COMMENT '商品长度',
    `width` FLOAT NULL COMMENT '商品宽度',
    `height` FLOAT NULL COMMENT '商品高度',
    `production_date` DATETIME NULL COMMENT '生产日期',
    `shelf_life` VARCHAR(45) NOT NULL COMMENT '有效期',
    `description` VARCHAR(45) NULL COMMENT '商品描述',
    PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '商品信息表';

CREATE TABLE IF NOT EXISTS `mydb`.`t_product_comment` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `product_id` INT UNSIGNED NOT NULL COMMENT '商品 ID ',
    `order_id` INT UNSIGNED NOT NULL COMMENT '订单 ID ',
    `customer_id` INT UNSIGNED NOT NULL COMMENT '用户 ID',
    `title` VARCHAR(45) NOT NULL COMMENT '评论标题',
    `content` VARCHAR(300) NOT NULL COMMENT '评论内容',
    `audit_status` TINYINT(1) NOT NULL COMMENT '评论审核状态',
    `audit_time` DATETIME NOT NULL COMMENT '评论时间',
    PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '商品评论表';
```

用户表
```mysql
CREATE TABLE IF NOT EXISTS `mydb`.`t_customer_info` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `gender` CHAR(1) NULL COMMENT '用户性别',
    `customer_name` VARCHAR(20) NOT NULL COMMENT '用户姓名',
    `identity_card_no` VARCHAR(20) NOT NULL COMMENT '用户证件号码',
    `identity_card_type` TINYINT(1) NOT NULL COMMENT '用户证件类型',
    `phone_number` INT UNSIGNED NOT NULL COMMENT '联系电话',
    PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '用户信息表';

CREATE TABLE IF NOT EXISTS `mydb`.`t_customer_address` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
    `province` SMALLINT NOT NULL COMMENT '省',
    `city` SMALLINT NOT NULL COMMENT '市',
    `county` SMALLINT NOT NULL COMMENT '县或区',
    `address` VARCHAR(200) NOT NULL COMMENT '详细地址',
    `is_default` TINYINT(1) NOT NULL COMMENT '是否默认，0-否，1-是',
    PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '用户地址信息';
```