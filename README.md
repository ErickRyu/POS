POS

ToDo<br>
1. Database schema 변경<br>
2. 잘못된 파일을 읽었을 경우 에러 처리 (모든 메뉴 제거, 이전 상태로 dB복귀)


<b>중간 회고</b>
* 2016_05_30<br>
  초반에 데이터베이스를 연동하는 것보다 메모리에 올리는 것이 효율적이라고 생각했다.<br>
  나중에 JDBC연동을 할 때 꼬이는 부분이 많이 생겼다.<br>
  최대한 단순한 구조로 클래스를 작성하는 것이 JDBC연동에 좋았을 것 같다.<br>

  아쉬웠던 것.<br>
  자꾸 발걸음을 크게 하는 문제점.<br>
  ex) 메뉴 등록을 고치겠다. 라고 요구사항을 설정하고 시작했는데<br>
  고객 등록, 직원 등록 까지 모두 손보기 시작.<br>
  메뉴 등록을 하는 중에 리팩토링을 하면서 코드가 돌아가지 않는 상태로 만들어버림.<br>


  궁금한 점.<br>
  execute query를 jdbc안에서 요청하다가 result set을 제대로 닫지 못하는 문제점.<br>
  일단은 더럽지만 jdbc를 컨트롤러에 모두 집어넣는 방법을 택했다.<br>
  고려했던 다른 사항으로 파라미터 리스트를 모두 건네주었다가 값들이 담긴 리스트를 받아오는 것.<br>
  하지만 구현이 살짝 복잡해질 것이 우려돼서 단순한 코드로 작성.<br>
  JDBC를 계속해서 사용해야하는데 어떻게 하면 좋은 코드가 될 수 있는 것인지 모르겠다.<br>
  무작정 JDBC를 모든 컨트롤에 넘기는 것이 맞는 것일지.<br>
  static을 지금처럼 남발해도 되는 것일지. 이게 정말 남발이 맞을지.<br>
