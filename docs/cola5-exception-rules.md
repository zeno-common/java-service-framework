在 COLA（Clean Object-Oriented and Layered Architecture）5 架构中，异常处理是保持代码整洁、层次分明、职责单一的关键环节。COLA 强调**依赖倒置**和**领域驱动**，因此异常的设计需要体现业务语义，并与架构层级严格对应。

以下是为 COLA 5 分层架构设计的异常类型、抛出规则和捕获规则：
  
---  

### 一、 异常类型设计

异常类型应结合业务语义和技术语义进行分类。建议设计一个统一的异常体系，以 `BaseException` 为基类，派生出业务异常和系统异常。

#### 1. 异常类层级结构

```java  

// 异常类型枚举，用于标识异常的来源分类
public enum ExceptionType {
  /** 未定义异常 */
  UNDEFINED,
  /** 业务异常 (Application/Domain层使用)  */
  BIZ,
  /** 系统异常 (Infrastructure层使用)   */
  SYS,
  /** 参数校验异常 (Adapter层使用)   */
  PARAM
}

// 1. 基础异常抽象类 (定义通用属性：errorCode, errorMessage)  
public abstract class BaseException extends RuntimeException {

  private final String code;
  public ExceptionType type() { return  ExceptionType.UNKNOWN; }
  // 构造函数...  
}  
  
// 2. 业务异常 (Application/Domain层使用)  
public class BizException extends BaseException {
  public ExceptionType type() { return  ExceptionType.BIZ; }
    // 构造函数...  
}  
  
// 3. 系统异常 (Infrastructure层使用)  
public class SysException extends BaseException {  
  public ExceptionType type() { return  ExceptionType.SYS; }
    // 构造函数...  
}  
  
// 4. 参数校验异常 (Adapter层使用)  
public class ParamException extends BaseException {  
  public ExceptionType type() { return  ExceptionType.PARAM; }
    // 构造函数...  
}  
```  

#### 2. 错误码`code`设计
错误码应作为枚举按领域划分独立管理，建议分为两部分：`BizCode`（业务级） + `错误编码`）。
*   **格式建议**：`[业务]-[编码]`，例如 `UC-ACCOUNT-EXSITED`。
*   **领域错误码**：在 Domain 层定义，因为它们表达了领域规则；**系统错误码**在 Infrastructure 层或基础模块定义。

---  

### 二、 抛出异常的规则（按层级）

**核心原则：尽早抛出，抛出带有明确语义的异常。**

#### 1. Adapter 层（适配器层 / Web/MQ）
*   **职责**：协议转换、参数校验。
*   **抛出规则**：
  *   进行入参的基本校验（非空、格式）。如果校验失败，抛出 `ParamException` 或 `ValidationException`。
  *   **不处理**业务逻辑，不抛出底层技术异常（如 `SQLException`）。

#### 2. App 层（应用层）
*   **职责**：用例编排、事务控制、防腐层转换。
*   **抛出规则**：
  *   不抛出技术异常。
  *   如果编排过程中发现状态不满足（例如：步骤A的前置条件不满足），抛出 `BizException`。
  *   调用 Domain 层或 Infrastructure 层时，如果底层抛出异常，App 层通常**不捕获不抛出**，让其自然向上冒泡（交由全局处理器处理），或者在需要时将其**包装为更高级别的业务异常**重新抛出。

#### 3. Domain 层（领域层）
*   **职责**：核心业务逻辑、领域规则计算。
*   **抛出规则**：
  *   **纯粹的业务校验**：当违反领域不变量时，必须抛出 `BizException`。例如：`if (balance < amount) throw new BizException("BIZ-ACCT-001", "余额不足");`
  *   **不依赖任何技术框架**：绝不抛出 `SQLException`, `NullPointerException` 等技术异常。
  *   **返回值 vs 异常**：如果是一个常见的预期检查（如检查用户是否存在），可以返回 `boolean` 或 `Optional`；如果是阻断流程的错误，抛出异常。

#### 4. Infrastructure 层（基础设施层）
*   **职责**：技术实现、数据库访问、外部API调用。
*   **抛出规则**：
  *   捕获所有底层技术异常（如 `SQLException`, `RedisConnectionException`, `HttpClientException`）。
  *   将技术异常**包装转换**为 `SysException` 抛出。例如：`catch (SQLException e) { throw new SysException("SYS-DB-001", "数据库访问失败", e); }`
  *   如果是调用外部接口返回的业务错误（如第三方返回“账户冻结”），应转换为当前领域的 `BizException` 抛出。

---  

### 三、 捕获异常的规则（按层级）

**核心原则：按需捕获，统一兜底，禁止吞掉异常。**

#### 1. Infrastructure 层
*   **捕获规则**：**必须捕获**。捕获所有第三方库和中间件抛出的技术异常，转换为 `SysException` 或特定领域的 `BizException` 后向上抛出。底层不能泄露技术细节到上层。

#### 2. Domain 层
*   **捕获规则**：**通常不捕获**。让业务异常自然向上冒泡到 App 层。
*   **例外**：如果在执行一个领域操作时，需要同时执行A和B，且B失败需要回滚A（在领域内部），则需要捕获B的异常，执行回滚逻辑，然后重新抛出。

#### 3. App 层
*   **捕获规则**：
  *   **事务控制点**：这是声明式事务（`@Transactional`）通常放置的地方。Spring 事务默认只在遇到 `RuntimeException` 时回滚。因为我们的 `BaseException` 继承自 `RuntimeException`，所以不需要在 App 层为了事务去 `catch` 异常。
  *   **异常转换**：如果调用了不同的 Domain 服务，希望将底层细分的异常转换为对调用方（Adapter）更友好的粗粒度异常，可以在这里 `catch` 并重新 `throw`。
  *   **不要吞掉异常**：严禁 `catch (Exception e) {}`。

#### 4. Adapter 层 & 全局兜底
*   **捕获规则**：
  *   在 Adapter 层不写 `try-catch` 块处理业务/系统异常。
  *   **必须使用全局异常处理器**（如 Spring 的 `@RestControllerAdvice`）进行统一捕获和转换。

**全局异常处理器的设计示例：**

```java  
@RestControllerAdvice  
public class GlobalExceptionHandler {  
  
    // 1. 处理参数异常  
    @ExceptionHandler(ParamException.class)  
    public ResponseEntity<Result> handleParam(ParamException e) {  
        log.warn("参数校验失败: {}", e.getMessage());  
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)                .body(Result.failure(e.getErrCode(), e.getErrMessage()));    }  
    // 2. 处理业务异常  
    @ExceptionHandler(BizException.class)  
    public ResponseEntity<Result> handleBiz(BizException e) {  
        log.warn("业务处理失败: {}", e.getMessage());  
        // 业务异常通常返回 200 OK，但在 Body 中携带错误码 (RESTful 实践中也有用 422 的)  
        return ResponseEntity.status(HttpStatus.OK)                .body(Result.failure(e.getErrCode(), e.getErrMessage()));    }  
    // 3. 处理系统异常  
    @ExceptionHandler(SysException.class)  
    public ResponseEntity<Result> handleSys(SysException e) {  
        log.error("系统异常: ", e);  
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)                .body(Result.failure("SYSTEM_ERROR", "系统繁忙，请稍后再试")); // 不向前端暴露底层细节  
    }  
    // 4. 兜底处理 (处理所有未预料到的异常，如 NPE)    @ExceptionHandler(Exception.class)  
    public ResponseEntity<Result> handleUnknown(Exception e) {  
        log.error("未知系统异常: ", e);  
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)                .body(Result.failure("UNKNOWN_ERROR", "系统内部错误"));  
    }}  
```  
  
---  

### 四、 总结矩阵表

| 层级 | 抛出什么异常？ | 捕获处理规则？ | 典型场景 |  
| :--- | :--- | :--- | :--- |  
| **Adapter** | `ParamException` | 不捕获业务/系统异常，交由全局处理器兜底。 | 入参非空、格式校验失败。 |  
| **App** | `BizException` | 通常不捕获，让其冒泡触发事务回滚；必要时做异常翻译。 | 编排流程时前置条件不满足。 |  
| **Domain** | `BizException` | 不捕获，直接抛出。绝不抛出技术异常。 | 余额不足、状态机流转非法。 |  
| **Infrastructure**| `SysException` / `BizException` | **必须捕获**技术异常，包装为 SysException 或 BizException。 | 数据库连接超时转为 SysException。 |  
| **Global Handler**| N/A | **统一捕获**所有异常，转换为标准 API 响应格式。 | 统一封装错误码和提示信息给前端。 |  

### 五、 最佳实践建议
1. **防腐败层（ACL）的异常转换**：在 App 层或 Infrastructure 层调用外部服务时，外部服务的异常体系可能会污染我们的系统。必须在外部调用的边界处（通常在 Infrastructure 的实现类中）`catch` 外部异常，转换为我们内部的 `SysException` 或 `BizException`。
2. **日志打印规范**：
  * `BizException` 和 `ParamException`：记录 `WARN` 级别即可，不需要打印完整堆栈（因为是业务预期内的错误）。
  * `SysException` 和 `Exception`：必须记录 `ERROR` 级别，并打印完整堆栈。
1. **事务回滚**：确保自定义异常继承自 `RuntimeException`，否则 Spring 的 `@Transactional` 默认不会回滚。