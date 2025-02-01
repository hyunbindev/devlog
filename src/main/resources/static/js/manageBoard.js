let board = [];

const renderBoard = () => {
    const boarderList = document.getElementById('boarder_list');
    boarderList.innerHTML = '';

    board.forEach(element => {
        const newBoarderElement = document.createElement('div');
        newBoarderElement.classList.add('boarder_element');

        const newBoarderElementTitle = document.createElement('div');
        newBoarderElementTitle.classList.add('boarder_title');
        newBoarderElementTitle.textContent = element.title;
		
		const newBoadPostCount = document.createElement('div');
		newBoadPostCount.textContent = element.postCount;
		
		const newBoarderTitleGroup = document.createElement('div');
		newBoarderTitleGroup.appendChild(newBoarderElementTitle);
		newBoarderTitleGroup.appendChild(newBoadPostCount);
		newBoarderTitleGroup.classList.add('boarder_title_group');
		



        const newBoarderElementBtnGroup = document.createElement('div');
        newBoarderElementBtnGroup.classList.add('boarder_btn');

        const modifyBtn = document.createElement('button');
        modifyBtn.classList.add('btn');
        modifyBtn.textContent = "수정";

        const deleteBtn = document.createElement('button');
        deleteBtn.classList.add('btn');
        deleteBtn.textContent = "삭제";
        if(element.postCount !== 0){
            console.log(element.postCount)
            deleteBtn.setAttribute('disabled', 'true');
        }
        deleteBtn.addEventListener('click', function () {
            deleteBoard(element.id);
        });

        newBoarderElementBtnGroup.appendChild(modifyBtn);
        newBoarderElementBtnGroup.appendChild(deleteBtn);

        newBoarderElement.appendChild(newBoarderTitleGroup);
        newBoarderElement.appendChild(newBoarderElementBtnGroup);
        boarderList.appendChild(newBoarderElement);
    });
};

const addNewBoarder = () => {
    const newBoarderTitle = document.getElementById('new_boarder_title').value;
    if (!newBoarderTitle.trim()) {
        alert("제목을 입력하세요.");
        return;
    }

    axios.post('/api/board', { title: newBoarderTitle })
        .then(response => {
            board = response.data;
            renderBoard();
            document.getElementById('new_boarder_title').value = '';
        })
        .catch(err => console.error(err));
};

const deleteBoard = (boardId) => {
    axios.post('/api/board/delete', { id: boardId })
        .then(response => {
            board = response.data;
            renderBoard();
        })
        .catch(err => console.error(err));
};

document.addEventListener('DOMContentLoaded', function () {
    axios.get('/api/board')
        .then(response => {
            board = response.data;
            renderBoard();
        })
        .catch(error => console.error(error));
});
