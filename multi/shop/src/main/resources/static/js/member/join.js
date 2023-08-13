/**
 * @since           :       2023-08-13
 * @author          :       youngmin
 * @version         :       1.0.0
 * @description     :       회원 가입 시 기본적인 Validation과 회원 가입 요청을 위해 사용
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-08-13       youngmin           최초 생성
 **/

/**
 * 맴버의 회원 가입 정보를 저장하여 obj 형태로 반환
 */
function setMemberJoinInfoObj() {
    console.debug(`setMemberJoinInfoObj...`);
    return {
        username: document.getElementById("name"),
        account: document.getElementById("account"),
        password1: document.getElementById("password1"),
        password2: document.getElementById("password2"),
        email: document.getElementById("email"),
        phonePrefix: document.getElementById("phonePrefix"),
        phoneMiddle: document.getElementById("phoneMiddle"),
        phoneLast: document.getElementById("phoneLast"),
        gender: document.getElementById("gender"),
        birthDate: document.getElementById("birthDate")
    };
}

/**
 * 맴버의 회원 가입 정보를 서버에 전달 하기전에 1차 검증
 */
function validateMemberJoinInfoObj(memberJoinInfoObj) {
    console.debug(`validateMemberJoinInfoObj...`);

    // TODO: 20230814 validation 진행 -> key, value 형태로 뽑아서 공백 체크 진행 하려고 했음
}

const main = {
    init: function () {
        console.debug(`initializing join func... this => ${this}`);
        let _this = this;
        $('#btn-join').on('click', function () {
            alert('click btn-save button...');
            _this.validate();
            _this.join();
        });
        console.debug(`finished init func...`);
    },
    validate: function() {
        // validate join form
        console.debug(`starting validate func...`);

        // 값 세팅
        let memberJoinInfoObj = setMemberJoinInfoObj();
        console.debug(`memberJoinInfoObj => ${JSON.stringify(memberJoinInfoObj)}`);

        // 유효성 검증
        validateMemberJoinInfoObj(memberJoinInfoObj);

    },
    join: function () {
        // start join for basic member
        console.log(`starting join func...`);

    }
};

// initialized
main.init();