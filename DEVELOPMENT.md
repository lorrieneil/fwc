# 开发指南

## 开发环境设置

### 必需工具
1. **Android Studio** - Hedgehog (2023.1.1) 或更新版本
   - 下载地址：https://developer.android.com/studio

2. **JDK** - Java Development Kit 8 或更高版本
   - 推荐使用 JDK 11 或 JDK 17

3. **Android SDK**
   - SDK Platform 34
   - Android SDK Build-Tools
   - Android SDK Platform-Tools

4. **Gradle** - 8.2（通过gradle wrapper自动下载）

### 项目设置

#### 1. 克隆项目
```bash
git clone <repository-url>
cd PushUpCounter
```

#### 2. 配置环境变量
确保设置了 `ANDROID_HOME` 环境变量：

**Linux/Mac:**
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

**Windows:**
```
setx ANDROID_HOME "%LOCALAPPDATA%\Android\Sdk"
```

#### 3. 同步项目
在Android Studio中：
1. 打开项目
2. 等待Gradle同步完成
3. 如果有错误，点击 "Sync Now"

#### 4. 配置设备
- **真机调试**：
  - 启用开发者选项
  - 启用USB调试
  - 连接设备
  
- **模拟器**：
  - Tools → Device Manager
  - 创建新的虚拟设备（推荐 Pixel 4 或更新）
  - API Level 24 或更高

## 项目结构

```
PushUpCounter/
├── app/                          # 主应用模块
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/pushup/counter/
│   │   │   │   ├── MainActivity.kt              # 主界面
│   │   │   │   ├── VideoAnalysisActivity.kt     # 视频分析
│   │   │   │   ├── PushUpDetector.kt           # 检测算法
│   │   │   │   └── PoseOverlayView.kt          # 姿态覆盖层
│   │   │   ├── res/
│   │   │   │   ├── layout/                     # 布局文件
│   │   │   │   ├── values/                     # 资源值
│   │   │   │   ├── drawable/                   # 图标资源
│   │   │   │   └── xml/                        # XML配置
│   │   │   └── AndroidManifest.xml             # 应用清单
│   │   └── test/                               # 测试代码
│   ├── build.gradle.kts                        # 应用级构建配置
│   └── proguard-rules.pro                      # ProGuard规则
├── gradle/                                     # Gradle配置
├── build.gradle.kts                            # 项目级构建配置
├── settings.gradle.kts                         # Gradle设置
├── gradle.properties                           # Gradle属性
└── README.md                                   # 项目说明

```

## 代码规范

### Kotlin代码风格
遵循 [Kotlin官方代码风格指南](https://kotlinlang.org/docs/coding-conventions.html)

- **命名约定**：
  - 类名：PascalCase（`MainActivity`）
  - 函数和变量：camelCase（`detectPushUp`）
  - 常量：UPPER_SNAKE_CASE（`FRAME_INTERVAL_MS`）
  - 资源ID：snake_case（`btn_select_video`）

- **代码组织**：
  - 按功能分组成员
  - 先声明属性，后声明函数
  - 公开函数在前，私有函数在后

### 注释规范
- 使用KDoc为公共API添加文档注释
- 复杂逻辑添加行内注释
- 避免显而易见的注释

示例：
```kotlin
/**
 * 检测视频帧中的俯卧撑动作
 * 
 * @param pose ML Kit检测到的人体姿态
 * @return 包含计数和状态的检测结果
 */
fun detectPushUp(pose: Pose): PushUpResult {
    // 实现代码...
}
```

## 构建和测试

### 常用Gradle任务

```bash
# 清理构建
./gradlew clean

# 构建Debug版本
./gradlew assembleDebug

# 构建Release版本
./gradlew assembleRelease

# 安装Debug到设备
./gradlew installDebug

# 运行单元测试
./gradlew test

# 运行Android仪器测试
./gradlew connectedAndroidTest

# 代码检查
./gradlew lint
```

### 调试技巧

#### 1. 日志输出
```kotlin
import android.util.Log

private val TAG = "MainActivity"

Log.d(TAG, "Debug message")
Log.i(TAG, "Info message")
Log.w(TAG, "Warning message")
Log.e(TAG, "Error message")
```

#### 2. 断点调试
- 在代码行号左侧点击设置断点
- 以Debug模式运行应用
- 使用调试工具栏控制执行

#### 3. Layout Inspector
- Tools → Layout Inspector
- 查看实时UI层级和属性

#### 4. Profiler
- View → Tool Windows → Profiler
- 监控CPU、内存、网络使用

## ML Kit集成

### Pose Detection使用

```kotlin
// 初始化检测器
val options = AccuratePoseDetectorOptions.Builder()
    .setDetectorMode(AccuratePoseDetectorOptions.SINGLE_IMAGE_MODE)
    .build()
val poseDetector = PoseDetection.getClient(options)

// 处理图像
val inputImage = InputImage.fromBitmap(bitmap, 0)
poseDetector.process(inputImage)
    .addOnSuccessListener { pose ->
        // 处理检测结果
    }
    .addOnFailureListener { e ->
        // 处理错误
    }
```

### 关键身体部位

```kotlin
// 获取关键点
val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)

// 获取位置和置信度
leftElbow?.let {
    val x = it.position.x
    val y = it.position.y
    val z = it.position.z
    val confidence = it.inFrameLikelihood
}
```

## 性能优化

### 1. 内存管理
- 及时回收Bitmap：`bitmap.recycle()`
- 使用内存缓存避免重复加载
- 监控内存泄漏

### 2. 异步处理
- 使用协程处理耗时操作
- 避免在主线程进行重计算
- 合理使用 `Dispatchers.IO` 和 `Dispatchers.Main`

### 3. 视频处理优化
- 调整帧采样率（默认100ms）
- 使用合适的视频分辨率
- 考虑使用MediaCodec进行硬件加速

## 贡献指南

### 提交代码

1. **创建分支**
```bash
git checkout -b feature/your-feature-name
```

2. **提交更改**
```bash
git add .
git commit -m "描述你的更改"
```

3. **推送分支**
```bash
git push origin feature/your-feature-name
```

4. **创建Pull Request**
- 在GitHub上创建PR
- 填写详细的描述
- 等待代码审查

### Commit信息规范

使用约定式提交：
```
类型(范围): 简短描述

详细描述

相关Issue: #123
```

类型：
- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 重构
- `test`: 测试相关
- `chore`: 构建/工具相关

示例：
```
feat(detector): 添加姿态对齐检测

实现了基于肩膀和髋部位置的身体对齐检测，
可以判断俯卧撑动作是否规范。

相关Issue: #15
```

## 发布流程

### 1. 准备发布
- 更新版本号（build.gradle.kts）
- 更新CHANGELOG.md
- 运行完整测试

### 2. 构建Release版本
```bash
./gradlew assembleRelease
```

### 3. 签名APK
配置签名密钥：
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("keystore.jks")
            storePassword = "password"
            keyAlias = "key-alias"
            keyPassword = "password"
        }
    }
}
```

### 4. 发布到Google Play
- 登录Google Play Console
- 创建新版本
- 上传APK或AAB
- 填写版本说明
- 提交审核

## 常见问题

### Q: Gradle同步失败
A: 
- 检查网络连接
- 清理Gradle缓存：`./gradlew clean`
- 删除 `.gradle` 目录
- Invalidate Caches / Restart

### Q: ML Kit依赖冲突
A:
- 确保使用兼容的版本
- 检查 `gradle.properties` 配置
- 使用 `./gradlew dependencies` 查看依赖树

### Q: 视频播放失败
A:
- 检查视频编码格式
- 验证文件权限
- 查看Logcat错误信息

## 资源链接

- [Android开发者文档](https://developer.android.com)
- [Kotlin文档](https://kotlinlang.org/docs/home.html)
- [ML Kit文档](https://developers.google.com/ml-kit)
- [ExoPlayer文档](https://exoplayer.dev)
- [Material Design指南](https://material.io/design)
