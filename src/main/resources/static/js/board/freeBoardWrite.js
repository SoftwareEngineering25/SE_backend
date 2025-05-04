const editor = new toastui.Editor({
    el: document.querySelector('#contsEditor'),
    previewStyle: 'vertical',
    initialEditType: 'markdown',
    height: '500px',
    placeholder: '내용을 입력해 주세요.',
    hooks: {
        addImageBlobHook: async (blob, callback) => {
            const formData = new FormData();
            formData.append('file', blob);

            try {
                const response = await fetch('/uploadImage', { // 수정된 엔드포인트
                    method: 'POST',
                    body: formData
                });

                if (!response.ok) {
                    throw new Error('이미지 업로드에 실패했습니다.');
                }

                const imageUrl = await response.text(); // 서버에서 반환된 이미지 URL
                callback(imageUrl, 'alt text'); // 에디터에 URL 삽입
            } catch (error) {
                console.error('이미지 업로드 오류:', error);
                alert('이미지 업로드에 실패했습니다. 다시 시도해주세요.');
            }
        }
    }
})



$(document).ready(function() {
    $('#submitButton').on('click', function(e) {
        e.preventDefault();

        const title = $('.freeboardwrite-titleInput').val().trim();
        const content = editor.getMarkdown().trim();

        if (title === '' || content === '') {
            alert("등록할 제목과 내용을 작성하세요.");
            return false;
        }

        if (confirm("등록하시겠습니까?")) {
            $.ajax({
                type: 'POST',
                url: '/board/freeBoardWrite',
                contentType: 'application/json',
                data: JSON.stringify({
                    boardTitle: title,
                    boardText: content  // 에디터에서 받은 내용 (이미지 URL 포함)
                }),
                success: function(response) {
                    alert("글이 성공적으로 등록되었습니다.");
                    window.location.href = '/board/freeBoardList';
                },
                error: function(error) {
                    alert("글 등록에 실패했습니다. 다시 시도해 주세요.");
                }
            });
        }
    });
});

