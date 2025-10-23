# 快速开始指南

## 5分钟快速上手

### 第一步：克隆和设置
```bash
git clone <repository-url>
cd PushUpCounter
./setup.sh  # 可选，自动设置环境
```

### 第二步：打开项目
- 启动 Android Studio
- 选择 "Open an existing project"
- 导航到项目目录并打开

### 第三步：同步和构建
- 等待 Gradle 自动同步（首次需要几分钟）
- 如果有提示，点击 "Sync Now"

### 第四步：运行应用
1. 连接 Android 设备或启动模拟器
2. 点击绿色运行按钮（▶️）
3. 等待应用安装完成

### 第五步：使用应用
1. 授予相机和存储权限
2. 点击"选择视频"
3. 选择一个俯卧撑视频
4. 观看分析结果！

## 命令行快速构建

```bash
# 构建 Debug 版本
./gradlew assembleDebug

# 安装到设备
./gradlew installDebug

# 运行测试
./gradlew test

# 查看所有任务
./gradlew tasks
```

## 系统要求

### 开发环境
- ✅ Android Studio Hedgehog (2023.1.1) 或更新
- ✅ JDK 8+（推荐 JDK 11 或 JDK 17）
- ✅ Android SDK 34
- ✅ 至少 8GB RAM
- ✅ 10GB 可用磁盘空间

### 运行设备
- ✅ Android 7.0 (API 24) 或更高
- ✅ 至少 2GB RAM
- ✅ 200MB 可用存储空间
- ✅ 相机和存储权限

## 常见问题

### Q: Gradle 同步失败？
```bash
# 清理并重试
./gradlew clean
# 在 Android Studio: File > Invalidate Caches / Restart
```

### Q: 找不到 Android SDK？
设置 ANDROID_HOME 环境变量：
```bash
# Linux/Mac
export ANDROID_HOME=$HOME/Android/Sdk

# Windows
setx ANDROID_HOME "%LOCALAPPDATA%\Android\Sdk"
```

### Q: 应用闪退？
检查：
1. Android 版本是否 >= 7.0
2. 是否授予了必要权限
3. 查看 Logcat 错误日志

### Q: 视频分析不准确？
提示：
1. 使用侧面拍摄角度
2. 确保光线充足
3. 保持身体完全在画面中
4. 使用标准俯卧撑姿势

## 项目结构速览

```
PushUpCounter/
├── app/                        # 主应用代码
│   ├── src/main/
│   │   ├── java/              # Kotlin 源文件
│   │   └── res/               # 资源文件
│   └── build.gradle.kts       # 应用配置
├── gradle/                    # Gradle 配置
├── *.md                       # 文档文件
└── gradlew                    # Gradle 脚本
```

## 核心文件说明

| 文件 | 作用 |
|------|------|
| `MainActivity.kt` | 主界面，处理视频选择 |
| `VideoAnalysisActivity.kt` | 视频分析和播放 |
| `PushUpDetector.kt` | 俯卧撑检测算法 |
| `activity_main.xml` | 主界面布局 |
| `activity_video_analysis.xml` | 分析界面布局 |
| `AndroidManifest.xml` | 应用配置和权限 |

## 调试技巧

### 查看日志
```kotlin
// 在代码中添加日志
import android.util.Log
Log.d("TAG", "调试信息")
```

在 Android Studio 中查看：
- View > Tool Windows > Logcat

### 断点调试
1. 在代码行号左侧点击设置断点
2. 点击 Debug 按钮（🐞）
3. 应用暂停时查看变量值

### 性能分析
- View > Tool Windows > Profiler
- 监控 CPU、内存、网络使用

## 下一步

- 📖 阅读完整 [README](README.md)
- 📱 查看 [使用指南](USAGE.md)
- 💻 参考 [开发文档](DEVELOPMENT.md)
- 🤝 了解如何 [贡献](CONTRIBUTING.md)

## 获取帮助

- 🐛 [报告 Bug](../../issues)
- 💡 [提出功能建议](../../issues)
- 📚 [查看文档](README.md)

---

**祝你使用愉快！** 🎉
