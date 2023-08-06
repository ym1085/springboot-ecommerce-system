/**
 * Functions used when registering a text
 * @since 2023-08-01
 * @author ymkim
 */
const main = {
    init: function () {
        console.log(`initializing join func... this => ${this}`);
        let _this = this;
        $('#btn-join').on('click', function () {
            alert('click btn-save button...');
            _this.validate();
            _this.join();
        });
        console.log(`finished init func...`);
    },
    join: function () {
        // start join for basic member
        console.log(`starting join func...`);

    },
    validate: function() {
        // validate join form
        console.log(`starting validate func...`);

        /**
         * 유효성 검증
         * name, account, password1, password2, email,
         * phonePrefix, phoneMiddle, phoneLast, gender, birthDate,
         *
         * 유효성 검증의 경우 CommonFunc 함수에 다양한 함수 넣어두고 해당 함수 호출해서 하는걸로 진행
         */
    },
};

// initialized
main.init();