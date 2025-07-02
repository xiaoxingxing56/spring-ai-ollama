# Spring AI Ollama 聊天应用

## 项目简介
这是一个基于Spring AI框架开发的聊天应用，可以与本地Ollama中部署的qwen3:4b大模型进行交互。用户可以通过Web界面与AI进行实时对话。

## 功能特性
- 🤖 与本地Ollama qwen3:4b模型实时对话
- 💬 支持多轮对话，保持上下文
- 🌐 现代化Web界面，支持实时聊天
- ⚡ 基于Spring WebFlux的响应式架构
- 🔧 可配置的模型参数（温度、重试次数等）

## 技术栈
- **后端**: Spring Boot 3.5.3 + Spring AI 1.0.0 + Spring WebFlux
- **前端**: HTML5 + CSS3 + JavaScript (原生)
- **AI模型**: Ollama qwen3:4b
- **构建工具**: Maven

## 环境要求
- Java 17+
- Maven 3.6+
- Ollama (已安装并运行qwen3:4b模型)
- 本地Ollama服务运行在 http://localhost:11434

## 快速开始

### 1. 启动Ollama服务
确保你的Ollama服务正在运行，并且已经下载了qwen3:4b模型：
```bash
ollama run qwen3:4b
```

### 2. 启动Spring Boot应用
```bash
mvn spring-boot:run
```

### 3. 访问聊天界面
打开浏览器访问：http://localhost:8080

## 项目结构
```
spring-ai-ollama/
├── src/main/java/com/hongzhang/
│   ├── config/          # 配置类
│   │   └── ModelConfig.java
│   ├── controller/      # 控制器
│   │   ├── ChatController.java
│   │   └── UserController.java
│   ├── dto/            # 数据传输对象
│   │   ├── ChatRequest.java
│   │   ├── ChatResponse.java
│   │   └── User.java
│   ├── service/        # 服务层
│   │   ├── ChatService.java
│   │   └── UserService.java
│   └── MainApp.java    # 主启动类
├── src/main/resources/
│   ├── static/         # 静态资源（前端页面）
│   │   └── index.html
│   └── application.yml # 配置文件
└── pom.xml            # Maven配置
```

## API接口说明

### 聊天接口
- **URL**: `/api/chat`
- **方法**: POST
- **请求体**:
```json
{
  "message": "用户输入的消息",
  "conversationId": "对话ID（可选）"
}
```
- **响应**:
```json
{
  "response": "AI回复的消息",
  "conversationId": "对话ID",
  "timestamp": "时间戳",
  "success": true,
  "errorMessage": null
}
```

### 清除对话接口
- **URL**: `/api/chat/clear`
- **方法**: POST
- **功能**: 清除当前对话历史

### 健康检查接口
- **URL**: `/api/chat/health`
- **方法**: GET
- **响应**:
```json
{
  "status": "UP",
  "service": "Spring AI Ollama Chat",
  "timestamp": "2025-07-02T07:47:31.840436800"
}
```

### 状态查询接口
- **URL**: `/api/chat/status`
- **方法**: GET
- **功能**: 获取系统状态和活跃对话数量

## 配置说明

### application.yml 主要配置项
- `server.port`: 服务端口（默认8080）
- `spring.ai.ollama.base-url`: Ollama服务地址
- `spring.ai.ollama.chat.options.model`: 使用的模型名称
- `spring.ai.ollama.chat.options.temperature`: 模型温度参数（0.0-1.0）

## 使用说明
1. 在聊天界面输入框中输入你的问题
2. 点击发送按钮或按回车键发送消息
3. AI会实时回复你的问题
4. 对话会保持上下文，支持多轮对话
5. 可以点击"清除对话"按钮开始新的对话

## 项目完成情况

### ✅ 已完成功能
1. **后端架构搭建**
   - Spring Boot 3.5.3 + Spring WebFlux 响应式架构
   - Spring AI 1.0.0 集成
   - 完整的MVC架构（Controller、Service、DTO）

2. **聊天功能实现**
   - 与Ollama API直接集成
   - 支持多轮对话，保持上下文
   - 对话历史管理（内存缓存）
   - 错误处理和重试机制

3. **前端界面开发**
   - 现代化响应式设计
   - 实时聊天界面
   - 支持移动端适配
   - 优雅的动画效果

4. **API接口设计**
   - RESTful API设计
   - 完整的错误处理
   - 健康检查和状态监控
   - 跨域支持

### 🧪 测试结果
- ✅ 应用启动成功
- ✅ 健康检查接口正常
- ✅ 前端页面可正常访问
- ✅ 聊天API接口响应正常
- ✅ 与Ollama模型连接成功

### 📝 技术实现亮点
1. **响应式编程**: 使用Spring WebFlux和Reactor实现非阻塞I/O
2. **直接API集成**: 绕过Spring AI的复杂配置，直接调用Ollama REST API
3. **内存管理**: 智能的对话历史管理，防止内存溢出
4. **错误处理**: 完善的异常处理和用户友好的错误提示
5. **现代化UI**: 使用CSS3和原生JavaScript实现流畅的用户体验

## 故障排除
- 确保Ollama服务正在运行
- 检查qwen3:4b模型是否已下载
- 查看应用日志了解详细错误信息
- 确认网络连接正常

## 开发说明
- 项目使用Spring AI 1.0.0版本
- 采用响应式编程模型（WebFlux）
- 支持Lombok简化代码
- 包含完整的错误处理和日志记录

## 后续优化建议
1. **持久化存储**: 使用Redis或数据库存储对话历史
2. **用户认证**: 添加用户登录和权限管理
3. **流式响应**: 实现AI回复的流式输出
4. **多模型支持**: 支持切换不同的AI模型
5. **性能监控**: 添加更详细的性能指标监控
6. **国际化**: 支持多语言界面 