# 예약 서비스

## Swagger

<details>
    <summary></summary>

![swagger](docs/swagger.png)

</details>

## 프로젝트 MileStone, API명세서

[구글 Docs 문서](https://docs.google.com/spreadsheets/d/1S2TsgAATEp7fZUqq_fYa20B5rVjvJuVm4wjqANgiSU4/edit?usp=sharing)



## 시퀀스 다이어그램

<details>
    <summary></summary>

![시퀀스다이어그램](docs/시퀀스다이어그램.png)

</details>

## ERD

<details>
    <summary></summary>

![concertERD](https://github.com/Ssantoo/booking_service/assets/112769188/28e294ad-7a17-4902-8e69-d72242b03150)

</details>


## MOCK Test Code

<details>
    <summary></summary>

[Mock TEST CODE](https://github.com/Ssantoo/booking_service/pull/1)
</details>

-> restdocs-api 작성 시도 중

## API Specs

<details>
    <summary></summary>

1️⃣ **`주요` 유저 대기열 토큰 기능    post**

- 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
- 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
- 이후 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.

> 기본적으로 폴링으로 본인의 대기열을 확인한다고 가정하며, 다른 방안 또한 고려해보고 구현해 볼 수 있습니다.
> 

2️⃣ `기본` 예약 가능 날짜 / 좌석 API  get**

- 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 각각 작성합니다.
- 예약 가능한 날짜 목록을 조회할 수 있습니다.

- 날짜 정보를 입력받아 예약가능한 좌석정보를 조회할 수 있습니다.

> 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됩니다.
> 

3️⃣ **`주요` 좌석 예약 요청 API   post**

- 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 5분간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 하며 다른 사용자는 예약할 수 없어야 한다.

4️⃣ **`기본`**  **잔액 충전 / 조회 API   post / get**

- 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.

5️⃣ **`주요` 결제 API   post**

- 결제 처리하고 결제 내역을 생성하는 API 를 작성합니다.
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료시킵니다.

#### 고민중
- 유저는 한명당 하나의 좌석만 예약할 수 있다.
- 좌석별로 가격이 다르다
- 포인트 충전 좌석가격에 비해 부족하면 충전
- 콘서트 예약 날짜 당일 취소시 취소 수수료 발생
- 좌석 상태 변경 ( 빈자리, 예약중, 결제중?, 예약됨)
- 좌석 토큰에 날짜 등 , 정보포함
- 해당 날짜부터 콘서트 시작날짜까지 선택가능
- 콘서트예약 오픈날짜
- 콘서트날짜
  
</details>



