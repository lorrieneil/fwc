#!/bin/bash

# 生成签名密钥脚本
# 用于为Android应用生成keystore文件

KEYSTORE_FILE="app/keystore.jks"
KEY_ALIAS="pushup-key"
STORE_PASSWORD="android123"
KEY_PASSWORD="android123"

echo "正在生成Android签名密钥..."
echo "密钥存储位置: $KEYSTORE_FILE"

# 检查是否已存在keystore文件
if [ -f "$KEYSTORE_FILE" ]; then
    echo "警告: keystore文件已存在！"
    read -p "是否要删除现有文件并重新生成？(y/N) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        rm -f "$KEYSTORE_FILE"
        echo "已删除现有keystore文件"
    else
        echo "操作已取消"
        exit 1
    fi
fi

# 使用gradlew的Java环境
if [ -f "./gradlew" ]; then
    # 确保gradlew可执行
    chmod +x ./gradlew
    
    # 获取Java路径
    JAVA_HOME_PATH=$(./gradlew -q javaToolchains 2>/dev/null | grep "Java Home" | head -1 | awk -F': ' '{print $2}')
    
    if [ -n "$JAVA_HOME_PATH" ]; then
        export JAVA_HOME="$JAVA_HOME_PATH"
        KEYTOOL="$JAVA_HOME/bin/keytool"
    fi
fi

# 如果找不到keytool，尝试系统路径
if [ -z "$KEYTOOL" ] || [ ! -f "$KEYTOOL" ]; then
    KEYTOOL=$(which keytool 2>/dev/null)
fi

# 如果还是找不到，使用默认路径
if [ -z "$KEYTOOL" ] || [ ! -f "$KEYTOOL" ]; then
    echo "错误: 找不到keytool工具"
    echo "请确保已安装JDK，或者先运行 ./gradlew 让它下载JDK"
    exit 1
fi

echo "使用keytool: $KEYTOOL"

# 生成keystore
"$KEYTOOL" -genkey -v \
    -keystore "$KEYSTORE_FILE" \
    -keyalg RSA \
    -keysize 2048 \
    -validity 10000 \
    -alias "$KEY_ALIAS" \
    -storepass "$STORE_PASSWORD" \
    -keypass "$KEY_PASSWORD" \
    -dname "CN=PushUp Counter, OU=Development, O=PushUp, L=City, S=State, C=CN"

if [ $? -eq 0 ]; then
    echo "✓ 密钥生成成功！"
    echo ""
    echo "密钥信息:"
    echo "  文件: $KEYSTORE_FILE"
    echo "  别名: $KEY_ALIAS"
    echo "  密码: $STORE_PASSWORD"
    echo ""
    echo "注意: 这是开发/测试用密钥，不要用于生产环境！"
else
    echo "✗ 密钥生成失败"
    exit 1
fi
