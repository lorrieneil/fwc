# GitHub Actions 自动构建指南

本文档介绍如何使用 GitHub Actions 自动构建和发布 APK 文件。

## 概述

项目配置了两个自动化工作流：

1. **build-apk.yml** - 持续构建
   - 每次推送到 main/master/develop 分支时自动构建
   - 适合日常开发和测试
   - APK 保存为 Artifacts，保留 7 天

2. **build-and-release-apk.yml** - 发布构建
   - 推送 Git 标签时触发（如 v1.0.0）
   - 自动创建 GitHub Release
   - 上传 APK 到 Releases，永久保存
   - 生成详细的发布说明

## 使用场景

### 场景一：日常开发和测试

当你推送代码到 main 分支时，GitHub Actions 会自动构建 APK：

```bash
git add .
git commit -m "Add new feature"
git push origin main
```

**查看和下载构建产物：**

1. 访问仓库的 **Actions** 页面
2. 点击最新的工作流运行
3. 滚动到底部的 **Artifacts** 区域
4. 下载 `PushUpCounter-Debug` 或 `PushUpCounter-Release`

### 场景二：正式发布版本

当你准备好发布新版本时，使用 Git 标签触发发布流程：

```bash
# 1. 确保所有更改已提交
git add .
git commit -m "Prepare for v1.0.0 release"
git push origin main

# 2. 创建版本标签
git tag -a v1.0.0 -m "Release version 1.0.0
- 新功能：实时视频分析
- 优化：提升检测准确度
- 修复：修复已知问题"

# 3. 推送标签到 GitHub
git push origin v1.0.0
```

**结果：**
- GitHub Actions 自动开始构建
- 构建成功后，自动创建名为 `v1.0.0` 的 Release
- APK 文件自动上传到 Release
- 用户可以在 Releases 页面直接下载

### 场景三：手动触发构建

如果需要手动触发构建（例如重新构建某个版本）：

1. 访问仓库的 **Actions** 页面
2. 选择 **Build and Release APK** 工作流
3. 点击右上角的 **Run workflow** 按钮
4. 选择分支并输入版本号（如 v1.0.1）
5. 点击绿色的 **Run workflow** 按钮

## 版本号规范

建议遵循 [语义化版本](https://semver.org/lang/zh-CN/) 规范：

**格式：** `v主版本号.次版本号.修订号`

- **主版本号**：重大更新或不向后兼容的修改（v1.0.0 → v2.0.0）
- **次版本号**：新增功能，向后兼容（v1.0.0 → v1.1.0）
- **修订号**：Bug 修复，向后兼容（v1.0.0 → v1.0.1）

**示例：**
```bash
# 首次正式发布
git tag -a v1.0.0 -m "首次正式发布"

# 添加新功能
git tag -a v1.1.0 -m "添加历史记录功能"

# 修复 Bug
git tag -a v1.1.1 -m "修复计数错误问题"

# 重大更新
git tag -a v2.0.0 -m "全新 UI 设计"
```

## 工作流详解

### 构建流程

1. **检出代码** - 从 Git 仓库获取最新代码
2. **设置 Java 环境** - 安装 JDK 17
3. **配置 Gradle 缓存** - 加速后续构建
4. **生成签名密钥** - 自动创建开发用密钥
5. **构建 APK** - 同时构建 Debug 和 Release 版本
6. **重命名文件** - 使用友好的文件名
7. **上传 APK** - 上传到 Artifacts 或 Release

### 构建时间

- 首次构建：约 5-8 分钟（需要下载依赖）
- 后续构建：约 3-5 分钟（使用缓存）

### 构建产物

每次构建生成两个 APK 文件：

1. **PushUpCounter-Debug.apk**
   - 包含调试信息
   - 体积较大（约 50-60 MB）
   - 适合开发测试

2. **PushUpCounter-Release.apk** ⭐ 推荐
   - 代码已优化和混淆
   - 体积较小（约 30-40 MB）
   - 性能更好
   - 适合日常使用

## 下载和安装 APK

### 方法一：从 Releases 下载

1. 访问仓库首页
2. 点击右侧的 **Releases** 链接
3. 选择想要的版本
4. 在 **Assets** 区域下载 APK
5. 推荐下载 `PushUpCounter-Release.apk`

### 方法二：从 Artifacts 下载

1. 访问 **Actions** 页面
2. 选择一个成功的工作流运行（绿色勾号）
3. 滚动到底部的 **Artifacts** 区域
4. 点击下载（需要登录 GitHub）

### 安装 APK

1. 将下载的 APK 传输到 Android 设备
2. 在设备上找到 APK 文件
3. 点击文件开始安装
4. 如果提示"未知来源"：
   - 进入**设置** → **安全**
   - 允许从该来源安装应用

## 查看构建状态

### 在 README 中显示构建徽章

可以在 README.md 中添加构建状态徽章：

```markdown
![Build APK](https://github.com/YOUR_USERNAME/YOUR_REPO/workflows/Build%20APK/badge.svg)
```

将 `YOUR_USERNAME` 和 `YOUR_REPO` 替换为实际的用户名和仓库名。

### 监控构建

- **Actions 页面**：查看所有工作流运行历史
- **邮件通知**：构建失败时 GitHub 会发送邮件
- **Commits 页面**：每个提交旁边会显示构建状态

## 常见问题

### 1. 构建失败："Execution failed for task ':app:lintVitalRelease'"

这通常是代码质量检查问题。可以：
- 修复代码中的警告和错误
- 或在 `app/build.gradle.kts` 中临时禁用：
  ```kotlin
  lintOptions {
      isAbortOnError = false
  }
  ```

### 2. 无法创建 Release

**原因：**
- 标签名称格式不正确（必须以 `v` 开头）
- 该版本的 Release 已存在

**解决方法：**
```bash
# 删除本地标签
git tag -d v1.0.0

# 删除远程标签
git push origin :refs/tags/v1.0.0

# 重新创建并推送
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

### 3. APK 无法下载

**从 Artifacts 下载：**
- 需要登录 GitHub 账号
- Artifacts 保留 7 天后自动删除

**从 Releases 下载：**
- 无需登录
- 永久保存
- 任何人都可以下载

### 4. 构建速度慢

优化方法：
- Gradle 缓存已自动启用
- 避免频繁清理缓存
- 合并多个小提交后再推送

### 5. 密钥安全问题

**当前配置：**
- 使用硬编码的开发密钥
- 密码：android123
- 仅适合开发和测试

**生产环境：**
如需发布到 Google Play，应该：

1. 生成生产密钥：
   ```bash
   keytool -genkey -v -keystore release.jks \
     -keyalg RSA -keysize 2048 -validity 10000 \
     -alias release-key
   ```

2. 将密钥信息保存到 GitHub Secrets：
   - 进入仓库 **Settings** → **Secrets and variables** → **Actions**
   - 添加以下 Secrets：
     - `KEYSTORE_FILE`（Base64 编码的密钥文件）
     - `KEYSTORE_PASSWORD`
     - `KEY_ALIAS`
     - `KEY_PASSWORD`

3. 修改工作流使用 Secrets

## 进阶配置

### 自定义构建

如需修改构建配置，编辑 `.github/workflows/build-and-release-apk.yml`：

```yaml
# 修改触发条件
on:
  push:
    tags:
      - 'v*'          # 仅 v 开头的标签
      - 'release-*'   # 也支持 release- 开头

# 修改 JDK 版本
- name: Set up JDK 17
  uses: actions/setup-java@v4
  with:
    distribution: 'temurin'
    java-version: '17'

# 修改保留时间
- name: Upload APK
  uses: actions/upload-artifact@v4
  with:
    retention-days: 30  # 改为 30 天
```

### 添加自动化测试

在构建 APK 之前运行测试：

```yaml
- name: Run Tests
  run: ./gradlew test

- name: Run Lint
  run: ./gradlew lint
```

### 发布到 Google Play

可以添加自动发布到 Google Play 的步骤，但需要：
1. Google Play Console 账号
2. 服务账号密钥
3. 使用 [r0adkll/upload-google-play](https://github.com/r0adkll/upload-google-play) action

## 相关资源

- [GitHub Actions 文档](https://docs.github.com/cn/actions)
- [语义化版本规范](https://semver.org/lang/zh-CN/)
- [Android 签名配置](https://developer.android.com/studio/publish/app-signing)
- [项目构建指南](BUILD_APK.md)
- [部署指南](DEPLOYMENT.md)

## 技术支持

如有问题：
1. 查看 Actions 页面的构建日志
2. 阅读错误信息和堆栈跟踪
3. 搜索类似问题
4. 提交 Issue 寻求帮助

## 总结

使用 GitHub Actions 自动构建的优势：

✅ **自动化** - 推送代码自动构建，无需手动操作
✅ **可靠** - 在干净的环境中构建，避免本地环境问题
✅ **可访问** - 团队成员和用户可直接下载 APK
✅ **可追溯** - 每个版本都有完整的构建日志
✅ **省时省力** - 无需配置本地 Android 开发环境

开始使用：
```bash
git tag -a v1.0.0 -m "First release"
git push origin v1.0.0
```

然后访问 Releases 页面查看你的第一个自动构建的发布版本！ 🎉
