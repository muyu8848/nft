/*
 Navicat Premium Data Transfer

 Source Server         : local_mysql
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : localhost:3306
 Source Schema         : open_nft

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 14/11/2022 23:21:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account_role
-- ----------------------------
DROP TABLE IF EXISTS `account_role`;
CREATE TABLE `account_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `account_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for air_drop_record
-- ----------------------------
DROP TABLE IF EXISTS `air_drop_record`;
CREATE TABLE `air_drop_record`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `air_drop_time` datetime(0) DEFAULT NULL,
  `collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `issued_collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for background_account
-- ----------------------------
DROP TABLE IF EXISTS `background_account`;
CREATE TABLE `background_account`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `google_auth_bind_time` datetime(0) DEFAULT NULL,
  `google_secret_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lately_login_time` datetime(0) DEFAULT NULL,
  `login_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `registered_time` datetime(0) DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `super_admin_flag` bit(1) DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Records of background_account
-- ----------------------------
BEGIN;
INSERT INTO `background_account` VALUES ('1', b'0', NULL, NULL, NULL, '2022-11-14 22:12:03', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', '2022-02-16 17:44:03', '1', b'1', 'admin', 183);
COMMIT;

-- ----------------------------
-- Table structure for chain_setting
-- ----------------------------
DROP TABLE IF EXISTS `chain_setting`;
CREATE TABLE `chain_setting`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `current_in_use_chain` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lately_update_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Records of chain_setting
-- ----------------------------
BEGIN;
INSERT INTO `chain_setting` VALUES ('1557267781324374016', 'noneChain', '2022-11-14 22:22:10');
COMMIT;

-- ----------------------------
-- Table structure for collection
-- ----------------------------
DROP TABLE IF EXISTS `collection`;
CREATE TABLE `collection`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `collection_hash` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `commodity_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `cover` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `creator_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `external_sale_flag` bit(1) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `price` double DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `sale_time` datetime(0) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `sync_chain_time` datetime(0) DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Records of collection
-- ----------------------------
BEGIN;
INSERT INTO `collection` VALUES ('1592161519041249280', '57cb96609df74da99161978370b07ec4', '1', 'https://img2.baidu.com/it/u=1081635636,3784235723&amp;fm=253&amp;fmt=auto&amp;app=138&amp;f=JPEG?w=628&amp;h=324', '2022-11-14 22:24:22', '1592161297795907584', b'0', NULL, b'1', '小鸡吃米图', 188, 1888, '2022-11-16 14:00:00', 1888, '2022-11-14 22:24:24', 1);
COMMIT;

-- ----------------------------
-- Table structure for collection_give_record
-- ----------------------------
DROP TABLE IF EXISTS `collection_give_record`;
CREATE TABLE `collection_give_record`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `give_from_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `give_time` datetime(0) DEFAULT NULL,
  `give_to_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `hold_collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for collection_story
-- ----------------------------
DROP TABLE IF EXISTS `collection_story`;
CREATE TABLE `collection_story`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `order_no` double DEFAULT NULL,
  `pic_link` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for compose_activity
-- ----------------------------
DROP TABLE IF EXISTS `compose_activity`;
CREATE TABLE `compose_activity`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `activity_time_end` datetime(0) DEFAULT NULL,
  `activity_time_start` datetime(0) DEFAULT NULL,
  `collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for compose_material
-- ----------------------------
DROP TABLE IF EXISTS `compose_material`;
CREATE TABLE `compose_material`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `activity_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `material_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for compose_record
-- ----------------------------
DROP TABLE IF EXISTS `compose_record`;
CREATE TABLE `compose_record`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `compose_activity_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `compose_time` datetime(0) DEFAULT NULL,
  `issued_collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for creator
-- ----------------------------
DROP TABLE IF EXISTS `creator`;
CREATE TABLE `creator`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `last_modify_time` datetime(0) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Records of creator
-- ----------------------------
BEGIN;
INSERT INTO `creator` VALUES ('1592161297795907584', 'https://img0.baidu.com/it/u=3344918055,1936247255&amp;fm=253&amp;fmt=auto&amp;app=120&amp;f=JPEG?w=640&amp;h=439', '2022-11-14 22:23:29', b'0', NULL, '2022-11-14 22:23:29', '唐伯虎', 0);
COMMIT;

-- ----------------------------
-- Table structure for dict_item
-- ----------------------------
DROP TABLE IF EXISTS `dict_item`;
CREATE TABLE `dict_item`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `dict_item_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `dict_item_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `dict_type_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `order_no` double DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Records of dict_item
-- ----------------------------
BEGIN;
INSERT INTO `dict_item` VALUES ('1496402213579259904', '1', '成功', '1496402134919282688', 1, 0), ('1496402213583454208', '0', '失败', '1496402134919282688', 2, 0), ('1503357286762938368', '1', '启用', '1499723556546674688', 1, 0), ('1503357286767132672', '0', '禁用', '1499723556546674688', 2, 0), ('1505122214314246144', '1', '未发送', '1505122154398613504', 1, 0), ('1505122214314246145', '2', '发送成功', '1505122154398613504', 2, 0), ('1505122214318440448', '3', '发送失败', '1505122154398613504', 3, 0), ('1544618189466370048', '1', '待付款', '1544618053960990720', 1, 0), ('1544618189470564352', '2', '已付款', '1544618053960990720', 2, 0), ('1544618189470564353', '3', '已取消', '1544618053960990720', 3, 0), ('1544697294505377792', 'member', '会员端', '1501914563405152256', 1, 0), ('1544697294513766400', 'admin', '运营平台', '1501914563405152256', 2, 0), ('1545422722631204864', 'login', '验证码_登录', '1505114508769624064', 1, 0), ('1545422722631204865', 'modifyPayPwd', '验证码_修改支付密码', '1505114508769624064', 2, 0), ('1545713642425548800', '1', '已发布', '1545713552667443200', 1, 0), ('1545713642425548801', '2', '已卖出', '1545713552667443200', 2, 0), ('1545713642425548802', '3', '已取消', '1545713552667443200', 3, 0), ('1547114627677028352', '1', '藏品', '1547114589852794880', 1, 0), ('1547114627677028353', '2', '盲盒', '1547114589852794880', 2, 0), ('1547115345276305408', '1', '平台自营', '1547113430270345216', 1, 0), ('1547115345276305409', '2', '二级市场', '1547113430270345216', 2, 0), ('1550124293239603200', '1', '上新', '1550123416126750720', 1, 0), ('1550124293239603201', '2', '合成', '1550123416126750720', 2, 0), ('1550124293243797504', '3', '空投', '1550123416126750720', 3, 0), ('1550124293243797505', '4', '活动', '1550123416126750720', 4, 0), ('1553392775569866752', 'bankCard', '银行卡', '1553392651200364544', 1, 0), ('1553392775578255360', 'alipay', '支付宝', '1553392651200364544', 2, 0), ('1553392775578255361', 'wechat', '微信', '1553392651200364544', 3, 0), ('1553403259870248960', '1', '系统', '1499675313855004672', 1, 0), ('1553403259899609088', '2', '购买藏品', '1499675313855004672', 2, 0), ('1553403259899609089', '3', '购买转售的藏品', '1499675313855004672', 3, 0), ('1553403259903803392', '4', '出售藏品', '1499675313855004672', 4, 0), ('1553403259903803393', '5', '提现', '1499675313855004672', 5, 0), ('1553403259903803394', '6', '提现驳回', '1499675313855004672', 6, 0), ('1553410466036842496', '1', '审核中', '1553410390413541376', 1, 0), ('1553410466036842497', '2', '已提现', '1553410390413541376', 2, 0), ('1553410466036842498', '3', '已驳回', '1553410390413541376', 3, 0), ('1559016018716655616', '1', '购买', '1541685676322324480', 1, 0), ('1559016018720849920', '2', '赠送', '1541685676322324480', 2, 0), ('1559016018725044224', '3', '二级市场', '1541685676322324480', 3, 0), ('1559016018725044225', '4', '盲盒', '1541685676322324480', 4, 0), ('1559016018725044226', '5', '合成', '1541685676322324480', 5, 0), ('1559016018725044227', '6', '空投', '1541685676322324480', 6, 0), ('1559016018725044228', '7', '兑换码', '1541685676322324480', 7, 0), ('1592161943660003328', '1', '持有中', '1545703747856891904', 1, 0), ('1592161943664197632', '2', '已转赠', '1545703747856891904', 2, 0), ('1592161943664197633', '3', '转售中', '1545703747856891904', 3, 0), ('1592161943668391936', '4', '已卖出', '1545703747856891904', 4, 0), ('1592161943668391937', '5', '开盲盒销毁', '1545703747856891904', 5, 0), ('1592161943668391938', '6', '合成销毁', '1545703747856891904', 6, 0), ('1592162063357050880', '1', '未使用', '1552688179037863936', 1, 0), ('1592162063357050881', '2', '已使用', '1552688179037863936', 2, 0), ('1592162063357050882', '3', '已作废', '1552688179037863936', 3, 0);
COMMIT;

-- ----------------------------
-- Table structure for dict_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE `dict_type`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `dict_type_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `dict_type_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `last_modify_time` datetime(0) DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Records of dict_type
-- ----------------------------
BEGIN;
INSERT INTO `dict_type` VALUES ('1496402134919282688', 'loginState', '登录状态', '2022-02-23 16:31:04', '', 1), ('1499675313855004672', 'memberBalanceChangeType', '会员余额变动类型', '2022-07-30 23:32:53', '', 9), ('1499723556546674688', 'functionState', '功能状态', '2022-03-14 21:08:03', '', 4), ('1501914563405152256', 'subSystem', '子系统', '2022-07-06 22:58:29', '', 4), ('1505114508769624064', 'smsType', '短信类型', '2022-07-08 23:01:04', '', 2), ('1505122154398613504', 'smsSendState', '短信发送状态', '2022-03-19 18:01:14', '', 1), ('1541685676322324480', 'collectionGainWay', '藏品获取方式', '2022-08-15 11:15:59', '', 4), ('1544618053960990720', 'payOrderState', '支付订单状态', '2022-07-06 17:44:09', '', 1), ('1545703747856891904', 'memberHoldCollectionState', '用户持有藏品状态', '2022-11-14 22:26:03', '', 6), ('1545713552667443200', 'memberResaleCollectionState', '会员转售藏品状态', '2022-07-09 18:17:05', '', 1), ('1547113430270345216', 'payOrderBizMode', '支付订单业务模式', '2022-07-13 15:06:57', '', 1), ('1547114589852794880', 'commodityType', '商品类型', '2022-07-13 15:04:06', '', 1), ('1550123416126750720', 'noticeType', '公告类型', '2022-07-21 22:23:26', '', 1), ('1552688179037863936', 'exchangeCodeState', '兑换码状态', '2022-11-14 22:26:32', '', 4), ('1553392651200364544', 'settlementAccountType', '结算账户类型', '2022-07-30 22:51:13', '', 1), ('1553410390413541376', 'withdrawRecordState', '提现记录状态', '2022-07-31 00:01:31', '', 1);
COMMIT;

-- ----------------------------
-- Table structure for exchange_code
-- ----------------------------
DROP TABLE IF EXISTS `exchange_code`;
CREATE TABLE `exchange_code`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `exchange_time` datetime(0) DEFAULT NULL,
  `issued_collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for issued_collection
-- ----------------------------
DROP TABLE IF EXISTS `issued_collection`;
CREATE TABLE `issued_collection`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `collection_serial_number` int(11) DEFAULT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `issue_time` datetime(0) DEFAULT NULL,
  `sync_chain_time` datetime(0) DEFAULT NULL,
  `unique_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for issued_collection_action_log
-- ----------------------------
DROP TABLE IF EXISTS `issued_collection_action_log`;
CREATE TABLE `issued_collection_action_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `action_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `action_time` datetime(0) DEFAULT NULL,
  `issued_collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `browser` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `ip_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `login_time` datetime(0) DEFAULT NULL,
  `msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `os` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `sub_system` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `balance` double DEFAULT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `keep_login_duration` int(11) DEFAULT NULL,
  `lately_login_time` datetime(0) DEFAULT NULL,
  `login_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `pay_pwd` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `registered_time` datetime(0) DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `push_client_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `block_chain_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `bind_real_name_time` datetime(0) DEFAULT NULL,
  `identity_card` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `account_level` int(11) DEFAULT NULL,
  `account_level_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `invite_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `inviter_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `bought_flag` bit(1) DEFAULT NULL,
  `sync_chain_time` datetime(0) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Records of member
-- ----------------------------
BEGIN;
INSERT INTO `member` VALUES ('1515361329685200896', 97583, b'0', NULL, 12, '2022-11-08 00:00:28', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', '13533333333', '爷爷在此', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', '张三', '2022-04-17 00:07:50', '1', 252, '982f02f06cb1965acbabcdc03049768d', 'iaa15s2259ke9rgcv4v2ukjr8kz0nc768k8ld0uw4q', '', '2022-08-09 21:41:30', '44544793492893728493', 1, '1515361329685200896', 'ih50g53', NULL, b'1', '2022-08-09 21:41:32'), ('1557027532983762944', 4180, b'0', NULL, 12, '2022-11-06 11:02:28', NULL, '18888888888', '藏家_patnh22mk', NULL, '郭炳全', '2022-08-09 23:34:27', '1', 11, NULL, 'iaa18790p83u40vknqu3k04knk75sd9txwn57ua099', NULL, '2022-08-09 23:34:42', '4343894374989385534', 1, '1557027532983762944', 'leymo5e', NULL, b'1', '2022-08-09 23:34:44'), ('1579753321445457920', 5000, b'0', NULL, 12, '2022-10-11 16:38:37', NULL, '13544444444', '藏家_qeakn0sw3', NULL, '测试吧', '2022-10-11 16:38:37', '1', 4, NULL, 'iaa1lahfncpr0ljr0tndqrp24m76df883tcfdxzyzh', NULL, '2022-10-11 16:38:45', '54655475665', 2, '1515361329685200896.1579753321445457920', 'qn4e87i', '1515361329685200896', b'0', '2022-10-11 16:38:47');
COMMIT;

-- ----------------------------
-- Table structure for member_balance_change_log
-- ----------------------------
DROP TABLE IF EXISTS `member_balance_change_log`;
CREATE TABLE `member_balance_change_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `balance_after` double DEFAULT NULL,
  `balance_before` double DEFAULT NULL,
  `balance_change` double DEFAULT NULL,
  `biz_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `change_time` datetime(0) DEFAULT NULL,
  `change_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for member_hold_collection
-- ----------------------------
DROP TABLE IF EXISTS `member_hold_collection`;
CREATE TABLE `member_hold_collection`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `gain_way` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `hold_time` datetime(0) DEFAULT NULL,
  `issued_collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lose_time` datetime(0) DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `pre_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `price` double DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `sync_chain_time` datetime(0) DEFAULT NULL,
  `transaction_hash` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for member_resale_collection
-- ----------------------------
DROP TABLE IF EXISTS `member_resale_collection`;
CREATE TABLE `member_resale_collection`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `cancel_time` datetime(0) DEFAULT NULL,
  `collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `issued_collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lock_pay_member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `member_hold_collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `resale_price` double DEFAULT NULL,
  `resale_time` datetime(0) DEFAULT NULL,
  `sold_time` datetime(0) DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `order_no` double DEFAULT NULL,
  `parent_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Records of menu
-- ----------------------------
BEGIN;
INSERT INTO `menu` VALUES ('1240880886262530048', b'0', NULL, '系统日志数据', 11, '', 'menu_1', ''), ('1240881072485433344', b'0', NULL, '登录日志', 2, '1240880886262530048', 'menu_2', '/page/login-log'), ('1240881126407405568', b'0', NULL, '操作日志', 2, '1240880886262530048', 'menu_2', '/page/oper-log'), ('1240881494369501184', b'0', NULL, '后台管理', 13, '', 'menu_1', ''), ('1240881668613472256', b'0', NULL, '后台菜单', 3, '1240881494369501184', 'menu_2', '/page/menu-manage'), ('1241121324122767360', b'0', NULL, '后台角色', 2, '1240881494369501184', 'menu_2', '/page/role-manage'), ('1241121744119398400', b'0', NULL, '后台账号', 1, '1240881494369501184', 'menu_2', '/page/background-account'), ('1431995012924571648', b'0', NULL, '超级管理员', 0.5, '1240881494369501184', 'menu_2', '/page/super-admin'), ('1505120272338911232', b'0', NULL, '会员资金流水', 1, '1240880886262530048', 'menu_2', '/page/member-balance-change-log'), ('1505120569647955968', b'0', NULL, '短信发送情况', 1.3, '1240880886262530048', 'menu_2', '/page/sms-send-record'), ('1506198540496404480', b'0', NULL, '会员管理', 6, '', 'menu_1', ''), ('1506198610616778752', b'0', NULL, '会员账号', 1, '1506198540496404480', 'menu_2', '/page/member'), ('1506199470319075328', b'0', NULL, '系统设置', 12, '', 'menu_1', ''), ('1506199619699212288', b'0', NULL, '系统功能调控', 1, '1506199470319075328', 'menu_2', '/page/setting'), ('1506199685440733184', b'0', NULL, '站内公告', 2, '1506199619699212288', 'menu_2', '/page/notice'), ('1506200016312598528', b'0', NULL, '数据清理', 3, '1506199470319075328', 'menu_2', '/page/data-clean'), ('1541265744002547712', b'0', NULL, '数字艺术品', 1.2, '', 'menu_1', ''), ('1541265862017679360', b'0', NULL, '艺术品管理', 2, '1541265744002547712', 'menu_2', '/page/collection'), ('1541334362069401600', b'0', NULL, '创作者', 1, '1541265744002547712', 'menu_2', '/page/creator'), ('1541674334395826176', b'0', NULL, '持有艺术品', 3, '1541265744002547712', 'menu_2', '/page/member-hold-collection'), ('1541706271407734784', b'0', NULL, '艺术品转赠记录', 4, '1541265744002547712', 'menu_2', '/page/collection-give-record'), ('1542078939550187520', b'0', NULL, '二级市场艺术品', 5, '1541265744002547712', 'menu_2', '/page/member-resale-collection'), ('1547110744879792128', b'0', NULL, '支付订单', 6, '1541265744002547712', 'menu_2', '/page/pay-order'), ('1552517284013015040', b'0', NULL, '首页', 1, '', 'menu_1', '/page/statistic-data'), ('1553409313869922304', b'0', NULL, '提现记录', 2, '1506198540496404480', 'menu_2', '/page/withdraw-record'), ('1563717766635061248', b'0', NULL, '运营工具', 1.3, '', 'menu_1', ''), ('1563717917952966656', b'0', NULL, '合成活动', 1, '1563717766635061248', 'menu_2', '/page/compose-activity'), ('1581930219428642816', b'0', NULL, '内容管理', 7, '', 'menu_1', ''), ('1581930312483471360', b'0', NULL, '站内公告', 1, '1581930219428642816', 'menu_2', '/page/notice'), ('1581930933164965888', b'0', NULL, '数据字典', 2.2, '1506199470319075328', 'menu_2', '/page/dict-manage');
COMMIT;

-- ----------------------------
-- Table structure for mystery_box_commodity
-- ----------------------------
DROP TABLE IF EXISTS `mystery_box_commodity`;
CREATE TABLE `mystery_box_commodity`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `commodity_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `probability` double DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `last_modify_time` datetime(0) DEFAULT NULL,
  `publish_time` datetime(0) DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for oper_log
-- ----------------------------
DROP TABLE IF EXISTS `oper_log`;
CREATE TABLE `oper_log`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ip_addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `module` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `oper_account_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `oper_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `oper_time` datetime(0) DEFAULT NULL,
  `operate` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `request_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `request_param` longtext CHARACTER SET utf8 COLLATE utf8_bin,
  `request_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `sub_system` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `amount` double DEFAULT NULL,
  `biz_order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `biz_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `collection_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `order_deadline` datetime(0) DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `paid_time` datetime(0) DEFAULT NULL,
  `cancel_time` datetime(0) DEFAULT NULL,
  `biz_mode` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `menu_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for settlement_account
-- ----------------------------
DROP TABLE IF EXISTS `settlement_account`;
CREATE TABLE `settlement_account`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `activated` bit(1) DEFAULT NULL,
  `activated_time` datetime(0) DEFAULT NULL,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `card_number` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `deleted_flag` bit(1) DEFAULT NULL,
  `deleted_time` datetime(0) DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for sms_send_record
-- ----------------------------
DROP TABLE IF EXISTS `sms_send_record`;
CREATE TABLE `sms_send_record`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `create_time` datetime(0) DEFAULT NULL,
  `error_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `send_time` datetime(0) DEFAULT NULL,
  `sms_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `verification_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for storage
-- ----------------------------
DROP TABLE IF EXISTS `storage`;
CREATE TABLE `storage`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `associate_biz` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `associate_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `upload_time` datetime(0) DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Table structure for system_setting
-- ----------------------------
DROP TABLE IF EXISTS `system_setting`;
CREATE TABLE `system_setting`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_schema` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `app_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `app_version` double DEFAULT NULL,
  `customer_service_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `h5gateway` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `lately_update_time` datetime(0) DEFAULT NULL,
  `local_storage_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- Records of system_setting
-- ----------------------------
BEGIN;
INSERT INTO `system_setting` VALUES ('1592160584348991488', '1', '1', 1, '', '1', '2022-11-14 22:20:39', 'C:\\home');
COMMIT;

-- ----------------------------
-- Table structure for withdraw_record
-- ----------------------------
DROP TABLE IF EXISTS `withdraw_record`;
CREATE TABLE `withdraw_record`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `amount` double DEFAULT NULL,
  `deal_time` datetime(0) DEFAULT NULL,
  `handling_fee` double DEFAULT NULL,
  `member_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `note` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `settlement_account_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `submit_time` datetime(0) DEFAULT NULL,
  `to_the_account` double DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin;

-- ----------------------------
-- View structure for v_everyday_trade_data
-- ----------------------------
DROP VIEW IF EXISTS `v_everyday_trade_data`;
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`%` SQL SECURITY DEFINER VIEW `v_everyday_trade_data` AS select concat(convert(`a`.`everyday` using utf8),`a`.`biz_mode`) AS `id`,str_to_date(`a`.`everyday`,'%Y-%m-%d %H:%i:%s') AS `everyday`,`a`.`biz_mode` AS `biz_mode`,`a`.`success_amount` AS `success_amount`,`a`.`success_count` AS `success_count` from (select date_format(`open_nft`.`pay_order`.`create_time`,'%Y-%m-%d') AS `everyday`,`open_nft`.`pay_order`.`biz_mode` AS `biz_mode`,round(sum((case when (`open_nft`.`pay_order`.`state` = '2') then `open_nft`.`pay_order`.`amount` else 0 end)),2) AS `success_amount`,sum((case when (`open_nft`.`pay_order`.`state` = '2') then 1 else 0 end)) AS `success_count` from `open_nft`.`pay_order` group by `open_nft`.`pay_order`.`biz_mode`,date_format(`open_nft`.`pay_order`.`create_time`,'%Y-%m-%d')) `a`;

SET FOREIGN_KEY_CHECKS = 1;
