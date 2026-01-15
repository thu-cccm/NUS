#!/bin/bash
# 设置Git仓库并推送到GitHub

set -e

echo "=========================================="
echo "开始设置Git仓库并推送到GitHub"
echo "=========================================="
echo ""

# 1. 删除现有Git历史（如果存在）
if [ -d .git ]; then
    echo "✓ 删除现有Git历史..."
    rm -rf .git
fi

# 2. 初始化新的Git仓库
echo "✓ 初始化新的Git仓库..."
git init

# 3. 添加所有文件
echo "✓ 添加所有文件到暂存区..."
git add .

# 4. 创建初始提交
echo "✓ 创建初始提交..."
git commit -m "项目初始化"

# 5. 添加远程仓库
echo "✓ 配置远程仓库..."
git remote remove origin 2>/dev/null || true
git remote add origin https://github.com/thu-cccm/NUS.git

# 6. 重命名分支为main
echo "✓ 设置主分支为main..."
git branch -M main

# 7. 显示当前提交记录
echo ""
echo "=========================================="
echo "当前提交记录："
echo "=========================================="
git log --oneline

echo ""
echo "=========================================="
echo "准备推送到GitHub"
echo "=========================================="
echo ""
echo "⚠️  注意：由于远程仓库可能已有内容，需要使用强制推送来覆盖历史"
echo ""
echo "请执行以下命令来推送代码："
echo ""
echo "  git push -f origin main"
echo ""
echo "或者，如果您想先查看远程仓库状态："
echo ""
echo "  git fetch origin"
echo "  git push -f origin main"
echo ""
echo "=========================================="

