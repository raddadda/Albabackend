<h1> ▶️ AlbaOn </h1>
<div align=center>
<p style="font-weight: 300;">2023-11 PROJECT</p>
<a href="#getting-started">Getting Started</a> •
<a href="#Introduction">Introduction</a> •
<a href="#function">functions</a> •
<a href="#stack">Stack</a> •
<a href="#contributors">Contributors</a> •
<a href="#backend-repo">Backend repo</a> •
</div>
<br>
> 소규모 사업장 관리 서비스 <br>
> 개발기간: 2023.11.09~2023.12.09
<br>
<br>
<div id="getting-started">
<h2>⏸️ 시작하기</h2>
</div>
<div align="center">
<h3>
<a href="http://ec2-3-35-58-201.ap-northeast-2.compute.amazonaws.com/" target="_blank">ALBAON바로가기</a><br><br>
<a href="http://ec2-3-39-203-178.ap-northeast-2.compute.amazonaws.com/swagger-ui" target="_blank">SWAGGER UI 바로가기</a>
</h3>
모두를 위한 알바온
</div>
<br>
<div id ="Introduction">
<h3>👊🏻 프로젝트소개</h3> 
</div>
<div align="center">
<br>
<p>
1. [포스코X코딩온] 사업장 내 근로자들의 스케쥴을 조정합니다..
</p>
<p>
2. 근로자는 자신의 출퇴근을 요청하고 그에 따른 급여를 확인할 수 있습니다.<br>
사업자는 근로자들 개인의 근퇴내역과 급여내역을 확인할 수 있습니다.!
</p>
<br>
<br>
</div>
<div id="function">
<h3> 💡 진행 및 기능 소개</h3>
<div align="center">
플로우차트
<br>
<br>
<img width="1153" alt="Alllogic1" src="https://github.com/kangseokjooo/Albabackend/assets/138436288/03e311f1-18cf-48a3-ac35-69f3527e16a8">
<br>
와이어프레임
<br>
<br>
<img width="3047" alt="Section 2" src="https://github.com/TeamOHJO/Frontend/assets/38286505/0d53b656-ad7c-4d91-b858-df9bd9e5ecb5">
</div>
<br>
<br>
<div align="center">
[영상]
</div>
<br>
<br>
<br>
<div id="stack">
<h2> 🛠️ 기술 스택 </h2>
</div>
 <div align="center">
 <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"/>
 <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"/>
 <img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=Swagger&logoColor=white"/>
 <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"/>
 </div>
 <div align="center">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>
  <img src="https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white"/>
  <img src="https://img.shields.io/badge/S3-569A31?style=for-the-badge&logo=AmazonS3&logoColor=white"/>
  <img src="https://img.shields.io/badge/hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white"/>
 </div>
<br>
<h2>🔥 필수 구현 사항</h2>
:흰색_확인_표시: 회원가입 가능<br> :흰색_확인_표시:기본 정보는 이메일 주소와, 비밀번호 이름.<br><br> :흰색_확인_표시:이메일과 비밀번호로 로그인 가능. <br> :흰색_확인_표시:회원 정보를 저장해둔 데이터베이스를 검색하여 해당 사용자가 유효한 사용자 인지 판단.<br>:흰색_확인_표시:상품 조회(전체, 개별), 회원 가입은 로그인 없이 사용 가능.<br> :흰색_확인_표시:이 외 기능은 로그인이 필요.<br><br> :흰색_확인_표시:데이터베이스에서 전체 상품 목록을 GET.<br> :흰색_확인_표시:이미지, 상품명, 상품가격을 기본으로 출력.<br> :흰색_확인_표시:재고에 따라 품절일 경우, 버튼 비활성화를 통해 품절 여부를 출력.<br> :흰색_확인_표시:옵션 카테고리를 분류하여, 상품을 출력<br> :흰색_확인_표시:한 페이지에 출력되는 상품 개수는 팀별로 정하여, 페이징.<br><br> :흰색_확인_표시:상세 정보를 상품에 저장해 둔 데이터베이스에서 GET.<br> :흰색_확인_표시:이미지, 상품명, 상품가격, 상품 상세 소개 (1줄 이상)을 기본으로 출력.<br> :흰색_확인_표시:재고에 따라 품절일 경우 예약하기 버튼을 비활성화.<br><br> :흰색_확인_표시:상품 상세 소개 페이지에서 상품 옵션(날짜, 숙박, 인원)을 선택.<br> :흰색_확인_표시:이메일과 비밀번호로 로그인할 수 있습니다.<br> :흰색_확인_표시:이메일과 비밀번호로 로그인할 수 있습니다.<br> :흰색_확인_표시:이메일과 비밀번호로 로그인할 수 있습니다.<br> :흰색_확인_표시:룸 형태의 상품들로 구성하여 상품을 제공.<br><br> :흰색_확인_표시:장바구니 담기 버튼을 클릭하면 선택한 상품이 장바구니에 담김.<br><br> :흰색_확인_표시:장바구니에 담긴 상품 데이터 (이미지, 상품명, 옵션 등)에 따른 상품별 구매 금액, 전체
주문 합계 금액 등을 화면에 출력.<br> :흰색_확인_표시:체크 박스를 통해 결제할 상품을 선택/제외.<br> :흰색_확인_표시:주문하기 버튼을 통해 주문/결제 화면으로 이동.<br><br>:흰색_확인_표시:장바구니에서 주문하기 버튼 또는 개별 상품 조회 페이지에서 주문.<br> :흰색_확인_표시:만 14세 이상 이용 동의를 체크 박스로 입력 받으면, 화면 최하단에 결제하기 버튼이 활성화.<br><br> :흰색_확인_표시:결제하기 버튼을 클릭 —> 상품을 바로 주문.<br> :흰색_확인_표시:주문을 저장하는 데이터베이스에 주문 정보를 저장.<br><br> :흰색_확인_표시:결제를 성공적으로 처리하면, 주문한 상품(들)에 대한 주문 결과를 출력.<br><br> :흰색_확인_표시:별도 주문 내역 페이지에 여태 주문한 모든 이력을 출력.<br>
<br>
## :파란색_책:Convention
- [**commit convention**](https://www.notion.so/Commit-Convention-6f8f2c9f19594d3f8938cc55d9e64d94?pvs=4)
- [pull request](https://www.notion.so/pr-04666827e78e429d8bf4a2a72ef62d08?pvs=4)
- [branch](https://www.notion.so/branch-b2d7151b35d24c93b88ba4aa1d22b17c?pvs=4)
- [issue](https://www.notion.so/issue-86653fb009e24ae9a706605c0c8c9bff?pvs=4)
- [stack](https://www.notion.so/Stack-879d61903f624a1ca50dd39ccdbc361d?pvs=4)
<br>
<div id=contributors>
## :손을_든_여성:Contributors
</div>
| <img src="https://avatars.githubusercontent.com/u/83493231?v=4" width="150px" /> | <img src="https://avatars.githubusercontent.com/u/38286505?v=4" width="150px" /> | <img src="https://avatars.githubusercontent.com/u/57976371?v=4" width="150px" /> | <img src="https://avatars.githubusercontent.com/u/139190686?v=4" width="150px" /> | <img src="https://avatars.githubusercontent.com/u/139193612?v=4" width="150px" /> |
| :------------------------------------------------------------------------------: | :------------------------------------------------------------------------------: | :------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------: |
|                   FE: [김특희](https://github.com/turkey-kim)                    |                   FE: [최재훈](https://github.com/zoeyourlife)                   |                   FE: [진정민](https://github.com/JeongMin83)                    |                   FE: [김다빈](https://github.com/dabin-Hailey)                   |                    FE: [신하연](https://github.com/gkdus2217)                     |
<br/>
<br />
<br />
<div id="backend-repo">
## 백엔드 REPOSITORY
</div>
**[`OHNOLZA-BACKEND 레포지토리`](https://github.com/TeamOHJO/yanoljaProject-Backend)**
OH! 놀자의 백엔드 레포지토리 입니다.
<br />
## 네이밍 규칙
### :흰색_확인_표시: Commit log
```
feat : 새로운 기능 추가
env : 개발 환경 관련 설정
fix : 버그 수정
style : 코드 스타일 수정 (세미 콜론, 인덴트 등의 스타일적인 부분만)
refactor : 코드 리팩토링 (더 효율적인 코드로 변경 등)
design : CSS 등 디자인 추가/수정
comment : 주석 추가/수정
docs : 내부 문서 추가/수정
test : 테스트 추가/수정
chore : 빌드 관련 코드 수정
rename : 파일 및 폴더명 수정
remove : 파일 삭제
```
### :흰색_확인_표시: Branch Naming
```
feat/#이슈번호
```
### :흰색_확인_표시: Issue Naming
```
[커밋 로그 명] 주요 기능 명
ex) [feat] ~~기능 추가
```
### :흰색_확인_표시: Requests Naming
```
[#이슈넘버] 주요 기능 명
ex) [#4] ~~기능 추가
```
## 폴더 구조
```
:열린_파일_폴더: src
┣ :열린_파일_폴더: api
┣ :열린_파일_폴더: assets                   # 폰트, 이미지 ,아이콘
┃  ┣ :열린_파일_폴더: images
┃  ┣ :열린_파일_폴더: fonts
┣ :열린_파일_폴더: components               # 공용 컴포넌트
┃  ┣ :열린_파일_폴더: Modal
┃  ┣ :열린_파일_폴더: SideBar
┃  ┣ ...
┣ :열린_파일_폴더: constant
┣ :열린_파일_폴더: hooks                    # 커스텀훅
┣ :열린_파일_폴더: pages                    # 페이지 컴포넌트
┃  ┣ :열린_파일_폴더: Home
┃  ┣ :열린_파일_폴더: GroupChatList
┃  ┣ :열린_파일_폴더: Login
┃  ┃  ┣ LoginBox.tsx
┃  ┃  ┣ index.tsx
┃  ┣ ...
┣ :열린_파일_폴더: routes
┣ :열린_파일_폴더: utils
┣ :열린_파일_폴더: states                   # 전역상태
┣ :열린_파일_폴더: styles                   # 스타일테마
┣ :열린_파일_폴더: @types                   # 타입스크립트 공용 인터페이스
┣ App.tsx
┣ index.tsx
```
## 기타 규칙
```
- 컴포넌트들은 기본적으로 PascalCase(대문자 시작) 제작
- 상위 폴더는 기본적으로 camelCase(소문자 시작) 시작
- 상수화 파일 제외 모든 변수, 함수명은 camelCase로 시작
- 스타일 컴포넌트는 같은 파일 내에 기재
</div>
