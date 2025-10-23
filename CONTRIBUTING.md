# 贡献指南

感谢你对俯卧撑计数器项目的兴趣！我们欢迎各种形式的贡献。

## 如何贡献

### 报告Bug

如果你发现了Bug，请创建一个Issue并包含以下信息：

1. **Bug描述**：清晰简洁地描述问题
2. **重现步骤**：
   - 第一步...
   - 第二步...
   - 看到的错误...
3. **预期行为**：描述你期望发生什么
4. **实际行为**：描述实际发生了什么
5. **截图**：如果可能，添加截图帮助说明问题
6. **环境信息**：
   - 设备型号
   - Android版本
   - 应用版本

### 提出新功能

如果你有好的想法，请创建一个Issue并说明：

1. **功能描述**：你想要什么功能
2. **使用场景**：为什么需要这个功能
3. **建议方案**：你认为应该如何实现（可选）

### 提交代码

#### 准备工作

1. Fork本仓库
2. 克隆你的Fork：
   ```bash
   git clone https://github.com/your-username/PushUpCounter.git
   ```
3. 添加上游仓库：
   ```bash
   git remote add upstream https://github.com/original/PushUpCounter.git
   ```

#### 开发流程

1. **创建特性分支**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **进行开发**
   - 遵循代码规范
   - 编写清晰的代码
   - 添加必要的注释
   - 编写测试（如果适用）

3. **提交更改**
   ```bash
   git add .
   git commit -m "feat: 添加新功能描述"
   ```

4. **保持同步**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

5. **推送到你的Fork**
   ```bash
   git push origin feature/your-feature-name
   ```

6. **创建Pull Request**
   - 访问GitHub上的你的Fork
   - 点击"New Pull Request"
   - 填写PR描述
   - 等待审查

#### Commit信息规范

使用约定式提交格式：

```
<类型>(<范围>): <简短描述>

<详细描述>

<footer>
```

**类型**：
- `feat`: 新功能
- `fix`: Bug修复
- `docs`: 文档更新
- `style`: 代码格式调整
- `refactor`: 代码重构
- `test`: 测试相关
- `chore`: 构建/工具相关
- `perf`: 性能优化

**示例**：
```
feat(detector): 添加动作质量评分功能

实现了基于姿态数据的动作质量评分系统，
可以为每个俯卧撑动作打分。

Closes #42
```

#### 代码审查

你的PR将会被审查，可能会收到反馈要求修改。这是正常的过程，不要气馁！

- 及时响应审查意见
- 根据反馈进行修改
- 推送更新到同一分支

### 代码规范

#### Kotlin风格

遵循 [Kotlin官方代码风格指南](https://kotlinlang.org/docs/coding-conventions.html)

- 使用4个空格缩进
- 每行最多120个字符
- 使用有意义的变量名
- 函数应该简短且专注单一职责

#### 命名约定

```kotlin
// 类名：PascalCase
class PushUpDetector

// 函数和变量：camelCase
fun detectPushUp(pose: Pose)
val pushUpCount = 0

// 常量：UPPER_SNAKE_CASE
const val MAX_ANGLE = 180f

// 资源ID：snake_case
R.id.btn_select_video
```

#### 注释

```kotlin
/**
 * KDoc注释用于公共API
 * 
 * @param pose 输入的姿态数据
 * @return 检测结果
 */
fun detectPushUp(pose: Pose): Result {
    // 行内注释解释复杂逻辑
    val angle = calculateAngle(...)
    return Result(angle)
}
```

### 测试

如果你添加了新功能，请考虑添加测试：

#### 单元测试
```kotlin
@Test
fun testPushUpDetection() {
    val detector = PushUpDetector()
    val result = detector.detectPushUp(mockPose)
    assertEquals(1, result.count)
}
```

#### Android仪器测试
```kotlin
@Test
fun testVideoPlayback() {
    // 测试代码
}
```

### 文档

如果你的更改影响了用户如何使用应用，请更新相关文档：

- README.md
- USAGE.md
- FEATURES.md
- DEVELOPMENT.md

## 开发环境

### 必需工具
- Android Studio Hedgehog (2023.1.1) 或更新
- JDK 8+
- Android SDK 34
- Git

### 设置
参考 [DEVELOPMENT.md](DEVELOPMENT.md) 获取详细的开发环境设置指南。

## 社区准则

### 行为准则

我们致力于提供一个友好、安全和欢迎所有人的环境。

**我们的承诺**：
- 使用友好和包容的语言
- 尊重不同的观点和经验
- 优雅地接受建设性批评
- 关注对社区最有利的事情
- 对其他社区成员表示同理心

**不可接受的行为**：
- 使用性化的语言或图像
- 侮辱/贬损的评论
- 公开或私下的骚扰
- 未经许可发布他人的私人信息
- 其他不道德或不专业的行为

### 沟通渠道

- **Issues**：报告Bug和提出功能请求
- **Pull Requests**：贡献代码
- **Discussions**：一般性讨论和问答

## 许可证

通过贡献，你同意你的贡献将使用与本项目相同的 MIT 许可证。

## 问题？

如果你有任何问题，可以：
1. 查看现有的Issues
2. 阅读文档
3. 创建新的Issue询问

感谢你的贡献！🎉
