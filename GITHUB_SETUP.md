# GitHub 仓库设置指南

## 目标
将代码上传到 https://github.com/thu-cccm/NUS 仓库，并删除所有历史提交记录，只保留一次"项目初始化"的提交。

## 操作步骤

### 方法一：使用脚本（推荐）

直接运行脚本：
```bash
bash setup_github.sh
```

然后执行推送命令：
```bash
git push -f origin main
```

### 方法二：手动执行

#### 1. 删除现有Git历史（如果存在）
```bash
rm -rf .git
```

#### 2. 初始化新的Git仓库
```bash
git init
```

#### 3. 添加所有文件
```bash
git add .
```

#### 4. 创建初始提交
```bash
git commit -m "项目初始化"
```

#### 5. 添加远程仓库
```bash
git remote add origin https://github.com/thu-cccm/NUS.git
```

如果远程仓库已存在，先删除再添加：
```bash
git remote remove origin
git remote add origin https://github.com/thu-cccm/NUS.git
```

#### 6. 设置主分支为main
```bash
git branch -M main
```

#### 7. 强制推送到GitHub（覆盖远程历史）
```bash
git push -f origin main
```

⚠️ **重要提示**：
- `-f` 参数会强制推送，覆盖远程仓库的所有历史记录
- 确保您有该仓库的写入权限
- 如果仓库是空的，也可以使用 `git push -u origin main`

## 验证

推送完成后，可以验证：
```bash
# 查看提交记录（应该只有一条"项目初始化"）
git log --oneline

# 查看远程仓库配置
git remote -v
```

## 后续操作

设置完成后，您可以使用以下命令进行日常开发：

```bash
# 查看状态
git status

# 添加文件
git add .

# 提交更改
git commit -m "您的提交信息"

# 推送到远程
git push origin main
```

