# GitHub Actions 快速开始

本文档提供使用 GitHub Actions 自动构建和发布 APK 的快速指南。

## 🚀 5分钟快速发布

### 第一步：推送代码到 GitHub

如果你还没有推送代码到 GitHub：

```bash
# 初始化 Git（如果还没有）
git init

# 添加远程仓库
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO.git

# 添加所有文件
git add .

# 提交
git commit -m "Initial commit with GitHub Actions"

# 推送到 main 分支
git push -u origin main
```

### 第二步：触发自动构建

**方式 A：推送代码触发自动构建**

```bash
# 修改一些代码后
git add .
git commit -m "Update features"
git push origin main
```

GitHub Actions 会自动开始构建，你可以在 **Actions** 页面查看进度。

**方式 B：创建标签发布版本**

```bash
# 创建版本标签
git tag -a v1.0.0 -m "First release"

# 推送标签
git push origin v1.0.0
```

GitHub Actions 会自动构建并创建 Release！

### 第三步：下载 APK

#### 从 Releases 下载（推荐）

1. 访问：`https://github.com/YOUR_USERNAME/YOUR_REPO/releases`
2. 找到 `v1.0.0` 版本
3. 下载 `PushUpCounter-Release.apk`

#### 从 Actions 下载

1. 访问：`https://github.com/YOUR_USERNAME/YOUR_REPO/actions`
2. 点击最新的成功运行（绿色勾号）
3. 滚动到底部下载 Artifacts

### 第四步：安装 APK

1. 将 APK 传输到手机
2. 点击安装
3. 完成！🎉

## 📋 常用命令

### 发布新版本

```bash
# 主版本更新 (1.0.0 -> 2.0.0)
git tag -a v2.0.0 -m "Major update"
git push origin v2.0.0

# 次版本更新 (1.0.0 -> 1.1.0)
git tag -a v1.1.0 -m "Add new features"
git push origin v1.1.0

# 补丁版本 (1.0.0 -> 1.0.1)
git tag -a v1.0.1 -m "Bug fixes"
git push origin v1.0.1
```

### 查看标签

```bash
# 列出所有标签
git tag

# 查看标签详情
git show v1.0.0

# 删除标签（如果需要）
git tag -d v1.0.0                  # 删除本地
git push origin :refs/tags/v1.0.0  # 删除远程
```

### 触发手动构建

无需命令行，直接在 GitHub 上操作：

1. 进入 **Actions** 页面
2. 选择 **Build and Release APK**
3. 点击 **Run workflow**
4. 输入版本号并确认

## 🎯 使用场景

### 场景 1：日常开发

每天的工作流程：

```bash
# 1. 修改代码
vim app/src/main/java/com/pushup/counter/MainActivity.kt

# 2. 提交并推送
git add .
git commit -m "Improve UI"
git push origin main

# 3. 等待自动构建（3-5分钟）
# 4. 在 Actions 页面下载测试
```

### 场景 2：版本发布

准备发布时：

```bash
# 1. 更新版本号
# 编辑 app/build.gradle.kts:
#   versionCode = 2
#   versionName = "1.1.0"

# 2. 更新 CHANGELOG
echo "## [1.1.0] - 2024-01-15
- 新增功能A
- 优化功能B
- 修复Bug C" >> CHANGELOG.md

# 3. 提交更改
git add .
git commit -m "Bump version to 1.1.0"
git push origin main

# 4. 创建发布标签
git tag -a v1.1.0 -m "Release 1.1.0"
git push origin v1.1.0

# 5. 等待自动构建和发布
# 6. 分享 Release 页面链接给用户
```

### 场景 3：热修复

紧急修复 bug：

```bash
# 1. 创建修复分支
git checkout -b hotfix/fix-crash

# 2. 修复 bug
vim app/src/main/java/com/pushup/counter/PushUpDetector.kt

# 3. 测试并提交
git add .
git commit -m "Fix crash on video load"

# 4. 合并到主分支
git checkout main
git merge hotfix/fix-crash
git push origin main

# 5. 创建补丁版本
git tag -a v1.0.1 -m "Hotfix: Fix crash"
git push origin v1.0.1
```

## ✅ 检查清单

发布前确认：

- [ ] 代码已全部提交并推送
- [ ] 更新了版本号（versionCode 和 versionName）
- [ ] 更新了 CHANGELOG.md
- [ ] 本地测试通过
- [ ] 标签命名正确（v开头，如 v1.0.0）
- [ ] 标签消息清晰描述更新内容

## 🔍 故障排查

### 构建失败

**问题**：Actions 显示红色 ❌

**解决**：
1. 点击失败的运行
2. 查看详细日志
3. 搜索 "error" 或 "failed"
4. 根据错误信息修复代码
5. 重新推送

常见错误：
- 编译错误：修复 Kotlin/Java 代码
- 依赖问题：检查 build.gradle.kts
- 内存不足：已配置自动处理

### 找不到 APK

**问题**：构建成功但找不到 APK

**解决**：
- **Artifacts**：在工作流运行页面底部查找
- **Releases**：只有推送标签才会创建
- **保留期**：Artifacts 保留 7 天，Releases 永久

### 无法安装 APK

**问题**：下载的 APK 无法安装

**解决**：
1. 检查 Android 版本（需要 >= 7.0）
2. 启用"未知来源"安装
3. 卸载旧版本
4. 使用 Release 版本（不是 Debug）

## 📊 构建状态

### 添加徽章到 README

显示实时构建状态：

```markdown
![Build APK](https://github.com/YOUR_USERNAME/YOUR_REPO/workflows/Build%20APK/badge.svg)
![Release](https://github.com/YOUR_USERNAME/YOUR_REPO/workflows/Build%20and%20Release%20APK/badge.svg)
```

### 监控通知

GitHub 会在以下情况发送邮件：
- ✅ 首次构建成功
- ❌ 构建失败
- 🔧 需要手动干预

## 💡 最佳实践

### 1. 版本管理

```bash
# 开发版本：频繁推送到 main
git push origin main

# 预发布版本：使用 beta 标签
git tag -a v1.0.0-beta1 -m "Beta release"
git push origin v1.0.0-beta1

# 正式版本：经过充分测试
git tag -a v1.0.0 -m "Official release"
git push origin v1.0.0
```

### 2. 分支策略

```bash
# 主分支：稳定代码
main

# 开发分支：日常开发
develop

# 功能分支：新功能
feature/new-camera-mode

# 修复分支：bug 修复
hotfix/fix-counter
```

### 3. 提交信息

使用清晰的提交信息：

```bash
# 好的提交信息
git commit -m "Add video analysis feature"
git commit -m "Fix crash when loading large videos"
git commit -m "Improve pose detection accuracy"

# 不好的提交信息
git commit -m "update"
git commit -m "fix bug"
git commit -m "changes"
```

### 4. 标签信息

提供详细的发布说明：

```bash
git tag -a v1.1.0 -m "Release 1.1.0

New Features:
- Real-time camera mode
- Historical statistics
- Export workout data

Improvements:
- Faster pose detection
- Better UI responsiveness

Bug Fixes:
- Fixed video playback crash
- Resolved permission issues
"
```

## 🔗 相关链接

- **详细指南**：[GITHUB_ACTIONS_GUIDE.md](GITHUB_ACTIONS_GUIDE.md)
- **工作流说明**：[.github/workflows/README.md](.github/workflows/README.md)
- **构建指南**：[BUILD_APK.md](BUILD_APK.md)
- **项目文档**：[README.md](README.md)

## 🎓 下一步

现在你已经掌握了基础，可以：

1. ⭐ 为项目添加 Star
2. 🔀 Fork 并修改工作流
3. 📝 完善 Release 说明
4. 🚀 分享你的 APK

---

**准备好了？让我们开始！**

```bash
git tag -a v1.0.0 -m "My first release"
git push origin v1.0.0
```

然后访问 Releases 页面，你的 APK 就在那里！🎉
