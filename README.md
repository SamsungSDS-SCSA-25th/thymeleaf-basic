# thymeleaf-basic

# Thymeleaf 핵심 정리

## 1. Thymeleaf 소개

### 주요 특징
- **SSR (Server-Side Rendering)**: 서버에서 HTML을 렌더링
- **Natural Template**: 브라우저에서 직접 열어도 읽을 수 있는 구조
- **Spring 통합**: Spring MVC와 자연스러운 연동

### 기본 선언
```html
<html xmlns:th="http://www.thymeleaf.org">
```

---

## 2. 기본 표현식

| 표현식 | 용도 | 예시 |
|--------|------|------|
| `${...}` | 변수 표현식 | `${user.name}` |
| `*{...}` | 선택 변수 표현식 | `*{name}` |
| `#{...}` | 메시지 표현식 | `#{msg.title}` |
| `@{...}` | 링크 URL 표현식 | `@{/user/list}` |
| `~{...}` | 조각 표현식 | `~{footer :: copy}` |

---

## 3. 텍스트 출력

### 이스케이프 `th:text` vs 언이스케이프 `th:utext` 비교

| 구분 | 이스케이프 (Escape) | 언이스케이프 (Unescape) |
|------|-------------------|----------------------|
| **처리 방식** | HTML 특수문자를 텍스트로 변환 | HTML을 그대로 렌더링 |
| **변환 예시** | `<` → `&lt;`, `>` → `&gt;` | 변환 없음 |
| **입력 예시** | `<script>alert()</script>` | `<script>alert()</script>` |
| **출력 결과** | 텍스트 그대로 보임 | 스크립트 실행됨 |
| **보안** | 안전 ✅ | 위험 ⚠️ |
| **Thymeleaf 문법** | `[[${data}]]` 또는 `th:text` | `[(${data})]` 또는 `th:utext` |
| **사용 시기** | 사용자 입력 (기본) | 신뢰할 수 있는 HTML만 |

```html
<!-- 이스케이프 (안전) -->
<p>[[${userInput}]]</p>
<p th:text="${userInput}">텍스트</p>

<!-- 언이스케이프 (주의 필요) -->
<p>[(${adminContent})]</p>
<p th:utext="${adminContent}">HTML</p>
```

> **기본은 이스케이프, 신뢰할 수 있는 HTML만 언이스케이프!**

---

## 4. 변수 & SpringEL

### 지역 변수 선언
```html
<div th:with="first=${users[0]}">
  <span th:text="${first.username}"></span>
</div>
```

### 객체/리스트/맵 접근
```html
${user.name}
${users[0].name}
${userMap['key'].name}
```

---

## 5. 기본 객체

```html
<!-- 요청 파라미터 -->
<p th:text="${param.q}"></p>

<!-- 세션 -->
<p th:text="${session.userId}"></p>

<!-- 스프링 빈 -->
<p th:text="${@helloBean.hello('Spring')}"></p>

<!-- 로케일 -->
<p th:text="${#locale}"></p>
```

---

## 6. 날짜/시간 유틸리티

```html
<span th:text="${#temporals.format(now, 'yyyy-MM-dd HH:mm:ss')}"></span>
<span th:text="${#temporals.day(now)}"></span>
<span th:text="${#temporals.month(now)}"></span>
```

---

## 7. URL 링크

```html
<!-- 기본 URL -->
<a th:href="@{/user/list}">목록</a>

<!-- 경로 변수 -->
<a th:href="@{/user/{id}(id=${user.id})}">상세</a>

<!-- 경로 변수 + 쿼리 파라미터 -->
<a th:href="@{/items/{id}(id=${item.id}, sort=${sort})}">정렬</a>
```

---

## 8. 리터럴

### 문자열 리터럴
```html
<span th:text="'Hello'"></span>
```

### 리터럴 대체 (Literal Substitution)
```html
<!-- 간편한 문자열 조합 -->
<span th:text="|Hello ${name}!|"></span>
```

---

## 9. 연산자

### 비교 연산자
- `gt` (>), `lt` (<), `ge` (≥), `le` (≤)
- `eq` (==), `ne` (!=)

### 조건 연산자
```html
<!-- 삼항 연산자 -->
<span th:text="${age ge 20} ? '성인' : '미성년'"></span>

<!-- Elvis 연산자 -->
<span th:text="${maybe} ?: '기본값'"></span>

<!-- No-Operation (_) -->
<span th:text="${data} ?: _">기존 텍스트</span>
```

---

## 10. 속성 설정

### 속성 대체
```html
<input type="text" th:name="${user.username}"/>
<input type="text" th:value="${user.age}"/>
```

### 속성 추가
```html
<!-- 클래스 추가 -->
<div class="text" th:classappend="' large'"></div>

<!-- 속성 앞/뒤 추가 -->
<input th:attrappend="class=' large'"/>
<input th:attrprepend="class='prefix '"/>
```

---

## 11. 반복문 (`th:each`)

```html
<tr th:each="user : ${users}">
  <td th:text="${user.username}"></td>
</tr>

<!-- 상태 객체 사용 -->
<tr th:each="user, stat : ${users}">
  <td th:text="${stat.count}"></td>      <!-- 1부터 시작 -->
  <td th:text="${stat.index}"></td>      <!-- 0부터 시작 -->
  <td th:text="${stat.first}"></td>      <!-- 첫 번째 여부 -->
  <td th:text="${stat.last}"></td>       <!-- 마지막 여부 -->
  <td th:text="${user.username}"></td>
</tr>
```

---

## 12. 조건문

### if / unless
```html
<!-- 조건이 true일 때만 렌더링 -->
<span th:if="${user.age lt 20}">미성년자</span>

<!-- 조건이 false일 때만 렌더링 -->
<span th:unless="${user.age ge 20}">미성년자</span>
```

### switch
```html
<td th:switch="${user.role}">
  <span th:case="'ADMIN'">관리자</span>
  <span th:case="'USER'">일반 사용자</span>
  <span th:case="*">기타</span>
</td>
```

---

## 13. 주석

```html
<!-- 표준 HTML 주석: 렌더링되어 브라우저에 전달됨 -->

<!--/* 파서 주석: 렌더링 시 완전히 제거됨 */-->
<!--/* [[${data}]] */-->

<!--/*/
  프로토타입 주석: 개발 시 보이고, 렌더링 시만 활성화
/*/-->
```

---

## 14. 블록 태그 (`th:block`)

렌더링 시 제거되는 그룹핑 태그

```html
<th:block th:each="item : ${cart}">
  <li th:text="${item.name}"></li>
  <li th:text="${item.price}"></li>
</th:block>
```

---

## 15. JavaScript 인라인

```html
<script th:inline="javascript">
  // 문자열 자동 이스케이프
  const username = [[${user.username}]];
  
  // 객체는 자동으로 JSON 변환
  const user = [[${user}]]; 
  // 결과: {"username":"홍길동","age":25}
  
  // 인라인 each
  [# th:each="item : ${items}"]
  console.log([[${item}]]);
  [/]
</script>
```

---

## 16. 템플릿 조각 (Fragment)

### 조각 정의
```html
<!-- footer.html -->
<footer th:fragment="copy">
  © 2025 Company
</footer>
```

### 조각 사용
```html
<!-- insert: 태그 내부에 삽입 -->
<div th:insert="~{footer :: copy}"></div>

<!-- replace: 태그를 완전히 대체 -->
<div th:replace="~{footer :: copy}"></div>
```

---

## 17. 레이아웃 - Head 공통화

### 공통 헤더 정의
```html
<!-- layout/base.html -->
<head th:fragment="common_header(title, links)">
  <title th:replace="${title}">기본 타이틀</title>
  <link rel="stylesheet" th:href="@{/css/common.css}">
  <th:block th:replace="${links}"/>
</head>
```

### 페이지에서 사용
```html
<head th:replace="~{layout/base :: common_header(~{::title}, ~{::link})}">
  <title>메인 페이지</title>
  <link rel="stylesheet" th:href="@{/css/main.css}">
</head>
```

---

## 18. 레이아웃 - 전체 프레임

### 레이아웃 템플릿
```html
<!-- layout/layoutFile.html -->
<html th:fragment="layout(title, content)">
<head>
  <title th:replace="${title}">레이아웃 타이틀</title>
</head>
<body>
  <header>공통 헤더</header>
  <div th:replace="${content}">
    <p>기본 컨텐츠</p>
  </div>
  <footer>공통 푸터</footer>
</body>
</html>
```

### 페이지에서 사용
```html
<html th:replace="~{layout/layoutFile :: layout(~{::title}, ~{::section})}">
<head>
  <title>실제 페이지 제목</title>
</head>
<body>
  <section>
    <h1>실제 컨텐츠</h1>
    <p>여기에 내용이 들어갑니다.</p>
  </section>
</body>
</html>
```

---

## 📝 요약 체크리스트

- ✅ 텍스트 출력: `th:text` (escape), `th:utext` (unescape)
- ✅ 반복: `th:each` + 상태 객체
- ✅ 조건: `th:if`, `th:unless`, `th:switch`
- ✅ URL: `@{...}` 경로변수, 쿼리파라미터
- ✅ JavaScript: `th:inline="javascript"`로 안전한 변수 전달
- ✅ 조각/레이아웃: `th:fragment`, `th:insert/replace`로 재사용