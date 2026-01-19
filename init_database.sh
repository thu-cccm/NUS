#!/bin/bash

# 数据库初始化脚本
# 使用方法: ./init_database.sh [mysql_root_password]

DB_PASSWORD=${1:-""}

if [ -z "$DB_PASSWORD" ]; then
    echo "=== MySQL数据库初始化 ==="
    echo "请输入MySQL root用户密码:"
    read -s DB_PASSWORD
    echo ""
fi

echo "=== 创建数据库 ==="
mysql -uroot -p${DB_PASSWORD} -e "CREATE DATABASE IF NOT EXISTS \`maple-boot\` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>&1 | grep -v "Warning"
if [ $? -eq 0 ]; then
    echo "✓ 数据库创建成功"
else
    echo "✗ 数据库创建失败，请检查密码是否正确"
    exit 1
fi

echo ""
echo "=== 导入基础管理表结构 ==="
mysql -uroot -p${DB_PASSWORD} maple-boot < schema/base-manage.sql 2>&1 | grep -v "Warning"
if [ $? -eq 0 ]; then
    echo "✓ 基础表结构导入成功"
else
    echo "✗ 基础表结构导入失败"
    exit 1
fi

echo ""
echo "=== 导入新农村业务表结构与演示数据 ==="
mysql -uroot -p${DB_PASSWORD} maple-boot < schema/vms_init.sql 2>&1 | grep -v "Warning"
if [ $? -eq 0 ]; then
    echo "✓ 业务表结构与演示数据导入成功"
else
    echo "✗ 业务表结构与演示数据导入失败"
    exit 1
fi

echo ""
echo "=== 验证数据库表 ==="
TABLE_COUNT=$(mysql -uroot -p${DB_PASSWORD} maple-boot -e "SHOW TABLES;" 2>&1 | grep -v "Warning" | wc -l | tr -d ' ')
echo "✓ 共创建 $((TABLE_COUNT - 1)) 张表"

echo ""
echo "=== 检查默认管理员账号 ==="
mysql -uroot -p${DB_PASSWORD} maple-boot -e "SELECT account, nick_name FROM usc_user WHERE account='admin';" 2>&1 | grep -v "Warning"

echo ""
echo "=== 数据库初始化完成 ==="
echo "请确保 application-dev.yml 中的数据库密码与您输入的密码一致"

