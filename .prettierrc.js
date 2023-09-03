//https://velog.io/@_jouz_ryul/ESLint-Prettier-Airbnb-Style-Guide%EB%A1%9C-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0
module.exports = {
    singleQuote: true, // 문자열 따옴표 설정
    semi: true, // 코드 마지막에 콜론 지정 여부
    useTabs: false, // 탭 사용을 금지하고 스페이스바 사용으로 대체
    tabWidth: 4, // 들여쓰기 너비
    trailingComma: 'all',
    printWidth: 160, // 코드 한줄 maximum 80
    arrowParens: 'avoid', // 화살표 함수가 하나의 매개변수를 받을 때 괄호를 생략
};
