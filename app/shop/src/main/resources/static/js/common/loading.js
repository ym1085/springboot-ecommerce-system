function showLoadingMask() {
	//화면의 높이와 너비를 구합니다.
	let maskHeight = $(document).height();
	let maskWidth = window.document.body.clientWidth;

	//출력할 마스크를 설정해준다.
	let mask =
		"<div id='mask' style='position:absolute; z-index:9000; background-color:#000000; display:none; left:0; top:0;'></div>";
	// 로딩 이미지 주소 및 옵션
	let loadingImg = '';
	loadingImg +=
		"<div id='loadingImg' style='position:absolute; top: calc(50% - (200px / 2)); width:100%; z-index:99999999;'>";
	loadingImg +=
		" <img src='/bootstrap/images/loading/Spinner.gif' style='position: relative; display: block; margin: 0px auto;'/>";
	loadingImg += '</div>';
	//레이어 추가
	$('body').append(mask).append(loadingImg);
	//마스크의 높이와 너비로 전체 화면을 채운다.
	$('#mask').css({
		width: '100%',
		height: '100%',
		opacity: '0.1',
	});
	//마스크 표시
	$('#mask').show();
	//로딩 이미지 표시
	$('#loadingImg').show();
}

function closeLoadingWithMask() {
	$('#mask, #loadingImg').hide();
	$('#mask, #loadingImg').remove();
}
