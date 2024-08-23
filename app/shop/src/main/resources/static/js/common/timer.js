let timeLeft = 180;
let timer = null;

function startTimer() {
	timer = setInterval(() => {
		const min = Math.floor(timeLeft / 60);
		const sec = timeLeft % 60;

		document.getElementById('timeLeft').innerText =
			`${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;

		timeLeft--;

		// 제한 시간 모두 경과
		if (timeLeft < 0) {
			handleTimeUp();
		}
	}, 1000);
}

function stopTimer() {
	clearInterval(timer);
}

function resetTimer() {
	timeLeft = 180;
	updateTimerDisplay(3, 0);
}

function updateTimerDisplay(min, sec) {
	const displayElement = document.getElementById('timeLeft');
	displayElement.innerText = `${min.toString().padStart(2, '0')}:${sec.toString().padStart(2, '0')}`;
}

function handleTimeUp() {
	stopTimer();
	resetTimer();
	showMessage('이메일 인증 시간이 모두 경과하였습니다. 다시 시도해주세요.');
}
